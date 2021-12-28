package com.vaultchallenge.fundloader.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.vaultchallenge.fundloader.models.Customer;
import com.vaultchallenge.fundloader.models.FundPayload;
import org.joda.money.Money;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

@Service
public class FundsService {
    private HashMap<String, List<FundPayload>> processedRequests = new HashMap<>();

    Customer customer = new Customer();


    public String loadFunds(Queue<Map<String, String>> loadQueue) {
        while(!loadQueue.isEmpty()) {
            var request = loadQueue.remove();

            FundPayload payload = new FundPayload();
            buildFundPayload(payload, request);

            addToProcessedRequests(request, payload);
        }


        return null;
    }


    private void addToProcessedRequests(Map<String, String> request, FundPayload payload) {
        if(processedRequests.containsKey(request.get("customer_id"))) {
            var payloadList = processedRequests.get(request.get("customer_id"));
            payloadList.add(payload);
            processedRequests.put(request.get("customer_id"), payloadList);
        } else {
            List<FundPayload> newPayloadList = new ArrayList<>();
            newPayloadList.add(payload);
            processedRequests.put(request.get("customer_id"), newPayloadList);
        }
    }

    private void updateCustomerData(Customer customer, Map<String, String> request) {
//        var amount = Money.parse(request.get("load_amount").replace("$", "USD "));
//
//        customer.setId(request.get("customer_id"));
//        customer.setFundsLoadedToday(amount);
//        customer.setFundsLoadedThisWeek(amount);
//        customer.setNumberOfLoadsToday(customer.getNumberOfLoadsToday() + 1);

    }

    private void buildFundPayload(FundPayload fundPayload, Map<String, String> request) {
        var amount = Money.parse(request.get("load_amount").replace("$", "USD "));
        var time = Instant.parse(request.get("time"));

        fundPayload.setId(request.get("id"));
        fundPayload.setCustomerId(request.get("customer_id"));
        fundPayload.setLoadAmount(amount);
        fundPayload.setRequestedAtUTC(time);

    }
}
