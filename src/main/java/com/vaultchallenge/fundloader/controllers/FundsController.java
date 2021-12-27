package com.vaultchallenge.fundloader.controllers;

import com.vaultchallenge.fundloader.services.FundsService;
import com.vaultchallenge.fundloader.utils.LoadReader;
import org.apache.tomcat.jni.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
@RequestMapping("api/v1/loadfunds")
public class FundsController {

    @Autowired
    private FundsService fundsService;

    @GetMapping
    public String loadFunds() {
        var reader = new LoadReader();
        var fundLoadQueue = reader.readInput();
        fundsService.loadFunds(fundLoadQueue);

        return "loaded";
    }
}
