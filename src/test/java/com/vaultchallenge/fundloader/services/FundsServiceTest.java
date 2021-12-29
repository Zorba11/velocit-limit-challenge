package com.vaultchallenge.fundloader.services;

import com.vaultchallenge.fundloader.models.FundPayload;
import com.vaultchallenge.fundloader.models.LoadResult;
import com.vaultchallenge.fundloader.utils.QueueBuilder;
import org.joda.money.Money;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Running tests for FundsService")
public class FundsServiceTest {

/*
I didn't had the intention to write unit tests in the beginning so, due to the
specific design of the fundsService class - specifically the private variables in it
won't let me write unit tests within the few more hours left for this assigment :(

Instead I'm writing an endpoint that runs a script that will verify the outputs :)
The outputs given by you guys are stored in the `givenoutputs` folder.
The verification endpoint is GET api/v1/loadfunds/verify
 */


    FundsService fundsService;
    TestInfo testInfo;
    TestReporter testReporter;
    private HashMap<String, Set<FundPayload>> processedRequests = new HashMap<>();
    private String fundInputsURI = "../../inputs/testInput.txt";

    @BeforeEach
    void init() {
        fundsService = new FundsService();
        this.testInfo = testInfo;
        this.testReporter = testReporter;
        testReporter.publishEntry("Running " + testInfo.getDisplayName());
        buildQueue();
    }

    @Nested
    @DisplayName("loadFunds method")
    class LoadFunds {
        @Test
        @DisplayName("Testing for daily load limit")
        void testLoadFrequency() {
            Map<String, String> request = new HashMap<>();
            request.put("id", "27141");
            request.put("customer_id", "52");
            request.put("load_amount", "$5431.33");
            request.put("time", "2000-01-02T03:36:54Z");

            FundPayload payload = new FundPayload();
            buildFundPayload(payload, request);

            LoadResult expected = new LoadResult("27141", "52", false);

//            LoadResult actual = fundsService.loadFunds(request, payload);

//            assertEquals(expected, actual, "should return " + expected + " but returned " + actual);
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

    private Queue<Map<String, String>> buildQueue() {
        var builder = new QueueBuilder();

        return builder.buildQueueFromFile(fundInputsURI);
    }

}
