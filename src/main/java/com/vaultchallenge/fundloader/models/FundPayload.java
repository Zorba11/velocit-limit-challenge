package com.vaultchallenge.fundloader.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class FundPayload {

    private String id;
    private String customerId;
    private Money loadAmount;
    private SimpleDateFormat requestAt;
}
