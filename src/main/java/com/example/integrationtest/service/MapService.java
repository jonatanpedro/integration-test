package com.example.integrationtest.service;

import com.example.integrationtest.dto.DataMap;

import java.util.Map;

public interface MapService {
    Map<String, DataMap> retrieveMap(String url);
}
