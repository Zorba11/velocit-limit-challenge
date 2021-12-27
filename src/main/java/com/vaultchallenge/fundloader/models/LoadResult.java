package com.vaultchallenge.fundloader.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoadResult {

    private String id;
    private String customer_id;
    private Boolean accepted;
}
