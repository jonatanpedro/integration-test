package com.example.integrationtest.controller;

import com.example.integrationtest.dto.MapRestParameterDTO;
import com.example.integrationtest.repository.DatabaseMetaRepository;
import com.example.integrationtest.service.RestMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest")
public class RestMapAjaxController {

    public static final String EMPTY = "";
    @Autowired
    private DatabaseMetaRepository databaseMetaRepository;

    @Autowired
    private RestMapService restMapService;

    @GetMapping(value = "/tables")
    public List<String> requestTableNames(){
        return databaseMetaRepository.retrieveAvaliableTables();
    }

    @PostMapping("/retrieve")
    public  Map<String, String> mapRequestUrl(@RequestBody MapRestParameterDTO param) {

        Map<String, String> outPutMap = null;

        if (param != null && (param.getUrl() != null && !EMPTY.equals(param.getUrl()))) {
            outPutMap = restMapService.retrieveRestMap(param.getUrl());
        }

        return outPutMap;
    }
}
