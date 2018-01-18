package com.example.integrationtest.service;

import org.springframework.integration.transformer.GenericTransformer;

public class RestMapperTransformationService implements GenericTransformer<String, Object>{
    @Override
    public Object transform(String s) {

        return s;
    }
}
