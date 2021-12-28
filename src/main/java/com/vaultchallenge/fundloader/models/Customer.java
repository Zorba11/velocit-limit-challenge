package com.vaultchallenge.fundloader.models;

import lombok.*;
import org.joda.money.Money;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Customer {
    private String id;
//    private Customer
    private Money fundsLoadedToday;
    private Money fundsLoadedThisWeek;
    private Integer numberOfLoadsToday = 0;
    private List<FundPayload> requestsReceived = new ArrayList<>();

}
