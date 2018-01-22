package com.example.integrationtest.controller;

import com.example.integrationtest.dto.DataMap;
import com.example.integrationtest.dto.Flow;
import com.example.integrationtest.dto.ServiceMapParameterDTO;
import com.example.integrationtest.enums.ServiceType;
import com.example.integrationtest.service.DbMapService;
import com.example.integrationtest.service.FlowService;
import com.example.integrationtest.service.RestMapService;
import com.example.integrationtest.service.SoapMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static com.example.integrationtest.util.FieldUtil.*;

@RestController
@RequestMapping("/rest")
public class RestMapAjaxController {

    public static final String EMPTY = "";

    @Autowired
    private RestMapService restMapService;

    @Autowired
    private SoapMapService soapMapService;

    @Autowired
    private DbMapService dbMapService;

    @Autowired
    private FlowService flowService;

    @GetMapping(value = "/tables")
    public List<String> requestTableNames(){
        return dbMapService.retrieveDbTables();
    }

    @PostMapping("/retrieve")
    public  Map<String, DataMap> mapRequestUrl(@RequestBody ServiceMapParameterDTO param) {

        Map<String, DataMap> outPutMap = null;

        if (param != null && (param.getUrl() != null && !EMPTY.equals(param.getUrl()))) {
            if (ServiceType.REST == param.getServiceType()) {
                outPutMap = restMapService.retrieveRestMap(param.getUrl());
            } else {
                outPutMap = soapMapService.retrieveSoapMap(param.getUrl());
            }
        }

        return outPutMap;
    }

    @GetMapping(value = "/columns/{table_name}")
    public Map<String, DataMap> requestColumnNames(@PathVariable("table_name") String tableName){
        return dbMapService.retrieveDbColumns(tableName);
    }

    @GetMapping(value = "/datatypes")
    public List<String> requestDataTypes(){
        return Stream.of(BOOLEAN, STRING, NUMBER, FLOAT).collect(Collectors.toList());
    }

    @PostMapping("/saveflow")
    public String saveFlow(@RequestBody Flow flow){
        flow.setId(UUID.randomUUID().toString());
        flowService.createFlow(flow);
        return null;
    }

}
