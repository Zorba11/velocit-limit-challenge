package com.vaultchallenge.fundloader.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vaultchallenge.fundloader.models.FundPayload;
import com.vaultchallenge.fundloader.models.LoadResult;
import com.vaultchallenge.fundloader.utils.QueueBuilder;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.stereotype.Service;
import java.io.*;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FundsService {
    private HashMap<String, Set<FundPayload>> processedRequests = new HashMap<>();
    private static final Money DAILY_LIMIT = Money.parse("USD 5000");
    private static final Money WEEKLY_LIMIT = Money.parse("USD 20000");
    private static final Integer DAILY_LOAD_FREQUENCY_LIMIT = 3;

    private String fundInputsURI = "src/inputs/input.txt";

    public void processLoadRequests() throws IOException {
        var loadQueue = buildQueue();

        while(!loadQueue.isEmpty()) {
            var request = loadQueue.remove();
            FundPayload payload = new FundPayload();
            buildFundPayload(payload, request);

            log.info("loadId: " + payload.getId() + " status: processing" );

            if(!processedRequests.isEmpty())
                if(duplicateLoad(payload)) {
                    log.info("loadId: " + payload.getId() + " status: ignoring, message: duplicate found");
                    continue;
                }

            var loadResult = loadFunds(request, payload);

            log.info("loadId: " + payload.getId() + " status: processed, message: " + loadResult.getAccepted());

            if(loadResult.getAccepted())
                addToProcessedRequests(request, payload);

            log.info("loadId: " + payload.getId() + " status: added to processed queue");

            var loadResultJson = buildJsonString(loadResult);

            BufferedWriter bw = new BufferedWriter(new FileWriter("src/outputs/output.txt", true));
            bw.write(loadResultJson + "\n");
            bw.close();

            log.info("loadId: " + payload.getId() + " status: output written, message: output written to file");

        }
    }

    private LoadResult loadFunds(Map<String, String> request, FundPayload payload) {
        var loadResult = new LoadResult();

            var requestIsValid = validateLoadRequest(payload);
            loadResult.setId(payload.getId());
            loadResult.setCustomer_id(payload.getCustomerId());

            if(requestIsValid)
                loadResult.setAccepted(true);
             else
                loadResult.setAccepted(false);

        return loadResult;
    }

    private Boolean validateLoadRequest(FundPayload fundPayload) {
        var loadResult = new LoadResult();
        Boolean loadAccepted = false;
        var customerPayloads = processedRequests.get(fundPayload.getCustomerId());

        if(customerPayloads == null) {
            customerPayloads = new HashSet<FundPayload>();
        }

            if (dailyLoadFrequencyReached(customerPayloads, fundPayload)) {
                loadAccepted = false;
                log.info("loadId: " + fundPayload.getId() + " status: load denied, message: daily load frequency reached");
            } else {
                if (dailyLoadLimitReached(customerPayloads, fundPayload)) {
                    loadAccepted = false;
                    log.info("loadId: " + fundPayload.getId() + " status: load denied, message: daily fund load limit reached");
                } else {
                    if (weeklyLoadLimitReached(customerPayloads, fundPayload)) {
                        loadAccepted = false;
                        log.info("loadId: " + fundPayload.getId() + " status: load denied, message: weekly fund load limit reached");
                    } else {
                        loadAccepted = true;
                        log.info("loadId: " + fundPayload.getId() + " status: load approved, message: load validated");
                    }
                }
            }

        return loadAccepted;
    }

    private Boolean weeklyLoadLimitReached(Set<FundPayload> customerPayloads, FundPayload newPayload) {

        var weeklyTotalFunds = Money.parse("USD 0");

        var dayOfWeek = newPayload.getDayOfWeek();
        var currentDateTime = newPayload.getRequestedAtUTC();
        var currentDayValue = dayOfWeek.getValue();

        var dateTimeOfWeekdays = findAllWeekDates(currentDayValue, currentDateTime, dayOfWeek);

        var allWeekPayloads = fetchAllWeekPayloads(customerPayloads, dateTimeOfWeekdays);

        for(var payload: allWeekPayloads) {
            var amount = payload.getLoadAmount();
            weeklyTotalFunds = weeklyTotalFunds.plus(amount);
        }

        return (weeklyTotalFunds.plus(newPayload.getLoadAmount()).isGreaterThan(WEEKLY_LIMIT)) ? true : false;
    }

    private Set<FundPayload> fetchAllWeekPayloads(Set<FundPayload> customerPayloads, ArrayList<ZonedDateTime> dateTimeOfWeekdays) {
        Set<FundPayload> allWeekPayloads = new HashSet<FundPayload>();

        for(var dateTime: dateTimeOfWeekdays) {
            var payloadYear = dateTime.getYear();
            var payloadMonth = dateTime.getMonth();
            var payloadDay = dateTime.getDayOfMonth();

            var allDayPayLoads = fetchAllDayPayLoads(customerPayloads, payloadYear, payloadMonth, payloadDay);
            allWeekPayloads.addAll(allDayPayLoads);
        }
        return allWeekPayloads;
    }

    private ArrayList<ZonedDateTime> findAllWeekDates(Integer currentDayValue, ZonedDateTime currentDateTime, DayOfWeek dayOfWeek) {
        var dateTimeOfWeekdays = new ArrayList<ZonedDateTime>();
        while(currentDayValue >= 1) {
            var previousDateTime= currentDateTime.minusDays(currentDayValue - 1);
            dateTimeOfWeekdays.add(previousDateTime);
            currentDayValue--;
        }

        currentDayValue = dayOfWeek.getValue();

        var j = 7 - currentDayValue;
        var k = 1;

        while(k <= j) {
            var nextDateTime= currentDateTime.plusDays(k);
            dateTimeOfWeekdays.add(nextDateTime);
            k++;
        }
        return dateTimeOfWeekdays;
    }


    private Boolean dailyLoadLimitReached(Set<FundPayload> customerPayloads, FundPayload newPayload) {
        var dailyTotalFunds = Money.parse("USD 0");

        var payloadYear = newPayload.getRequestedAtYear();
        var payloadMonth = newPayload.getRequestedOnMonth();
        var payloadDay = newPayload.getRequestedOnDay();

        var allDayPayLoads = fetchAllDayPayLoads(customerPayloads, payloadYear, payloadMonth, payloadDay);

        for(var payload: allDayPayLoads) {
            var amount = payload.getLoadAmount();
            dailyTotalFunds = dailyTotalFunds.plus(amount);
        }

        return (dailyTotalFunds.plus(newPayload.getLoadAmount()).isGreaterThan(DAILY_LIMIT)) ? true : false;
    }

    private Set<FundPayload> fetchAllDayPayLoads(Set<FundPayload> customerPayloads, Integer payloadYear, Month payloadMonth, Integer payloadDay) {
        return customerPayloads.stream().filter(payload -> (
                payload.getRequestedAtYear().equals(payloadYear) && payload.getRequestedOnMonth().equals(payloadMonth) && payload.getRequestedOnDay().equals(payloadDay)
        )).collect(Collectors.toSet());
    }

    private boolean dailyLoadFrequencyReached(Set<FundPayload> customerPayloads, FundPayload newPayload) {
        var year = newPayload.getRequestedAtYear();
        var month = newPayload.getRequestedOnMonth();
        var day = newPayload.getRequestedOnDay();

        var dailyLoadCount = customerPayloads.stream().filter(payload -> (
                payload.getRequestedAtYear().equals(year) && payload.getRequestedOnMonth().equals(month) && payload.getRequestedOnDay().equals(day)
                )).count();

       return (dailyLoadCount >= DAILY_LOAD_FREQUENCY_LIMIT) ? true : false;
    }

    private Boolean duplicateLoad(FundPayload fundPayload) {
        var previousLoads = processedRequests.get(fundPayload.getCustomerId());
        if(previousLoads == null)
            return false;
        else
        return previousLoads.stream()
                .filter(payload -> payload.getId().equals(fundPayload.getId()))
                .count() > 0;
    }



    private void addToProcessedRequests(Map<String, String> request, FundPayload payload) {
        if(processedRequests.containsKey(request.get("customer_id"))) {
            var payloadList = processedRequests.get(request.get("customer_id"));
            payloadList.add(payload);
            processedRequests.put(request.get("customer_id"), payloadList);
        } else {
            Set<FundPayload> newPayloadList = new HashSet<>();
            newPayloadList.add(payload);
            processedRequests.put(request.get("customer_id"), newPayloadList);
        }
    }

    private void buildFundPayload(FundPayload fundPayload, Map<String, String> request) {
        var amount = Money.parse(request.get("load_amount").replace("$", "USD "));
        var time = ZonedDateTime.parse(request.get("time"));

        fundPayload.setId(request.get("id"));
        fundPayload.setCustomerId(request.get("customer_id"));
        fundPayload.setLoadAmount(amount);
        fundPayload.setRequestedAtUTC(time);
        fundPayload.setRequestedAtYear(time.getYear());
        fundPayload.setRequestedOnMonth(time.getMonth());
        fundPayload.setRequestedOnDay(time.getDayOfMonth());
        fundPayload.setDayOfWeek(time.getDayOfWeek());
    }

    private String buildJsonString(LoadResult loadResult) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer();
        String loadResultJson = ow.writeValueAsString(loadResult);
        return loadResultJson;
    }

    private Queue<Map<String, String>> buildQueue() {
        var builder = new QueueBuilder();

        return builder.buildQueueFromFile(fundInputsURI);
    }
}
