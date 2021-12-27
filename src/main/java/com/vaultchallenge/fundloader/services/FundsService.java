package com.vaultchallenge.fundloader.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.vaultchallenge.fundloader.models.Customer;
import com.vaultchallenge.fundloader.models.FundPayload;
import org.joda.money.Money;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

@Service
public class FundsService {
    private HashMap<Customer, List<FundPayload>> processedRequests = new HashMap<>();
    private Customer customer;
    private FundPayload fundPayload;

    public String loadFunds(Queue<JsonNode> loadQueue) {
        var request = loadQueue.remove();
        var requestJSON = request.toPrettyString();

        System.out.println(requestJSON);

        setCustomerValues(request);
        setFundPayloads(request);
//        if(!request.at("/id").isEmpty())



        return null;
    }

    private void setCustomerValues(JsonNode request) {
        var amount = Money.parse("USD 23.87");
        var customerId = String.valueOf(request.at("/customer_id"));

        System.out.println(amount);

        customer.setId(customerId);
        customer.setFundsLoadedToday(amount);
        customer.setFundsLoadedThisWeek(amount);
        customer.setNumberOfLoadsToday(customer.getNumberOfLoadsToday() + 1);

    }

    private void setFundPayloads(JsonNode request) {
//        var amount = Money.parse(String.valueOf(request.at("/load_amount")));
        var dateTime = String.valueOf(request.at("/time"));

        var formatedDateTime = new SimpleDateFormat("MM/dd/yyyy KK:mm:ss a Z").format(dateTime);

        System.out.println(dateTime);
        System.out.println(formatedDateTime);


        fundPayload.setId(String.valueOf(request.at("/id")));
        fundPayload.setCustomerId(String.valueOf(request.at("/customer_id")));
//        fundPayload.setLoadAmount(amount);
//        fundPayload.setRequestAt();

    }
}
