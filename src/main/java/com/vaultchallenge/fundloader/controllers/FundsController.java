package com.vaultchallenge.fundloader.controllers;

import com.vaultchallenge.fundloader.services.FundsService;
import com.vaultchallenge.fundloader.utils.VerifyOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> loadFunds() throws IOException {

        var loadResult = fundsService.processLoadRequests();

        return new ResponseEntity<>(loadResult, HttpStatus.OK);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyOutput() {
        var result = verifyOutput.verifyOutputs();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
