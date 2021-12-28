package com.vaultchallenge.fundloader.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

public class LoadReader {
    private Queue<Map<String, String>> fundLoadQueue = new ArrayDeque<Map<String, String>>();
    private ObjectMapper mapper = new ObjectMapper();
    private String fundInputsURI = "src/inputs/input.txt";
    private String fundLoadString;

    @SneakyThrows
    public Queue<Map<String, String>> readInput() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fundInputsURI));
            while((fundLoadString = br.readLine()) != null) {
                JsonNode loadRequest = mapper.readTree(fundLoadString);
                Map<String, String> result = mapper.convertValue(loadRequest, new TypeReference<Map<String, String>>(){});
                if(loadRequest.has("id"))
                    fundLoadQueue.add(result);
            }
        } catch (FileNotFoundException e ) {
            e.printStackTrace();
        }
        return fundLoadQueue;
    }
}
