package com.vaultchallenge.fundloader.models;

import lombok.*;
import org.joda.money.Money;
import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FundPayload {

    private String id;
    private String customerId;
    private Money loadAmount;
    private ZonedDateTime requestedAtUTC;
    private Integer requestedAtYear;
    private Month requestedOnMonth;
    private Integer requestedOnDay;
    private DayOfWeek dayOfWeek;
}
