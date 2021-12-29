package com.vaultchallenge.fundloader.utils;

import com.vaultchallenge.fundloader.models.LoadResult;

import java.util.Map;

public class VerifyOutput {
    private String outputURI = "src/outputs/output.txt";
    private String givenOutputURI = "src/outputs/given-output.txt";

    public String verifyOutputs() {
        var builder = new QueueBuilder();
        var outputResult = new LoadResult();
        var givenOutputResult = new LoadResult();

        Boolean isSame = false;

        var giveOutputQueue = builder.buildQueueFromFile(givenOutputURI);
        var outputQueue = builder.buildQueueFromFile(outputURI);

        while(!outputQueue.isEmpty() && !giveOutputQueue.isEmpty()) {
            var output = outputQueue.remove();
            var givenoutput = giveOutputQueue.remove();

            var outputObject = buildResult(outputResult, output);
            var givenOutputObject = buildResult(givenOutputResult, givenoutput);

            var outputId = outputObject.getId();
            var giveOutputId = givenOutputObject.getId();
            var customerId = outputObject.getCustomer_id();
            var givenOutputCustomerId = givenOutputObject.getCustomer_id();
            var outputAccepted = outputObject.getAccepted();
            var givenOutputAccepted = givenOutputObject.getAccepted();

            if(outputId.equals(giveOutputId) && customerId.equals(givenOutputCustomerId) && outputAccepted.equals(givenOutputAccepted))
                isSame = true;
        }
        if(isSame)
            return "Output is same as the given output";
        else
            return "Outputs don't match";
    }

    private LoadResult buildResult(LoadResult loadResult, Map<String, String> result) {
        loadResult.setId(result.get("id"));
        loadResult.setCustomer_id(result.get("customer_id"));
        loadResult.setAccepted(Boolean.valueOf(result.get("accepted")));

        return loadResult;
    }


}
