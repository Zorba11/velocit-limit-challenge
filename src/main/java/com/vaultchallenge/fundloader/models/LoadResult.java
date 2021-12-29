package com.vaultchallenge.fundloader.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoadResult {

    private String id;
    private String customer_id;
    private Boolean accepted;
}
