package com.vaultchallenge.fundloader.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.joda.money.Money;

import java.util.Currency;

@Getter
@Setter
@AllArgsConstructor
public class Customer {
    private String id;
//    private Customer
    private Money fundsLoadedToday;
    private Money fundsLoadedThisWeek;
    private Integer numberOfLoadsToday = 0;
}
