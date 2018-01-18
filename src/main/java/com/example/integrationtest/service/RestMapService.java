package com.example.integrationtest.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestMapService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public Map<String, String> retrieveRestMap(String url){

        Map<String, String> result = new HashMap<>();
        try {
            String responseJson = new RestTemplate().getForObject(url, String.class);
            Map<String, Object> map = new ObjectMapper().readValue(responseJson, new TypeReference<Map<String, Object>>(){});
            readTypes(map, result);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }


    public void readTypes(final Map<String, Object> map, final Map<String, String> result){
        map.entrySet()
                .stream()
                .forEach(entry -> {
                    if("ARRAY".equals(identifyType(entry.getValue()))) {
                        List<LinkedHashMap<String, Object>> list = (List<LinkedHashMap<String, Object>>) entry.getValue();
                        Map<String, Object> subMap = new HashMap<>();
                        for (LinkedHashMap<String, Object> map1 : list) {
                            subMap.putAll(map1);
                        }
                        readTypes(subMap, result);
                    }else if("MAP".equals(identifyType(entry.getValue()))){
                        Map<String, Object> subMap = (LinkedHashMap<String, Object>)entry.getValue();
                        readTypes(subMap, result);
                    }else{
                        result.put(entry.getKey(),identifyType(entry.getValue()));
                    }
                });
    }

    public String identifyType(Object type){
        if(type instanceof Number){
            return "NUMBER";
        }else if(type instanceof String){
            return "STRING";
        }else if(type instanceof List){
            return "ARRAY";
        }else if(type instanceof Boolean) {
            return "BOOLEAN";
        }else if(type instanceof LinkedHashMap){
            return "MAP";
        } else{
            return null;
        }
    }
}
