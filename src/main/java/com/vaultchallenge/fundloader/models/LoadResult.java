package com.vaultchallenge.fundloader.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoadResult {

    private String id;
    private String customerId;
    private Boolean accepted;
}
