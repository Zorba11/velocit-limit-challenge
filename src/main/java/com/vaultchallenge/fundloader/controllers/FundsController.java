package com.vaultchallenge.fundloader.controllers;

import com.vaultchallenge.fundloader.services.FundsService;
import com.vaultchallenge.fundloader.utils.VerifyOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/loadfunds")
public class FundsController {

    @Autowired
    private FundsService fundsService;

    VerifyOutput verifyOutput = new VerifyOutput();

    @GetMapping
    public String loadFunds() throws IOException {
        fundsService.processLoadRequests();

        return "Queue processed !";
    }

    @GetMapping("/verify")
    public String verifyOutput() {
        return verifyOutput.verifyOutputs();
    }
}
