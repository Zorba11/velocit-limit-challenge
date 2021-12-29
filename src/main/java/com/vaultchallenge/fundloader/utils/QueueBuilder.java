package com.vaultchallenge.fundloader.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

@NoArgsConstructor
public class QueueBuilder {
    private ObjectMapper mapper = new ObjectMapper();

    private String dataString;

    @SneakyThrows
    public Queue<Map<String, String>> buildQueueFromFile(String fileURI) {
        Queue<Map<String, String>> fundLoadQueue = new ArrayDeque<Map<String, String>>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileURI));
            while((dataString = br.readLine()) != null) {
                JsonNode loadRequest = mapper.readTree(dataString);
                Map<String, String> result = mapper.convertValue(loadRequest, new TypeReference<Map<String, String>>(){});
                if(loadRequest.has("id"))
                    fundLoadQueue.add(result);
            }
        } catch (FileNotFoundException e ) {
            e.printStackTrace();
            throw new FileNotFoundException();
        }
        return fundLoadQueue;
    }
}
