package com.vaultchallenge.fundloader.models;

import lombok.*;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FundPayload {

    private String id;
    private String customerId;
    private Money loadAmount;
    private Instant requestedAtUTC;
}
