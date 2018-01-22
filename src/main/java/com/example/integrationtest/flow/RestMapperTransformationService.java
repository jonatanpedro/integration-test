package com.example.integrationtest.flow;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.integration.transformer.GenericTransformer;

import java.io.IOException;
import java.util.Map;

public class RestMapperTransformationService implements GenericTransformer<String, Object>{
    @Override
    public Object transform(String json) {

        try {
            Map<String, Object> map = new ObjectMapper().readValue(json, new TypeReference<Map<String, Object>>(){});


        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
}
