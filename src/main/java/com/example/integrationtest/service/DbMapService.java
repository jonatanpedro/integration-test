package com.example.integrationtest.service;

import com.example.integrationtest.dto.DataMap;
import com.example.integrationtest.repository.DatabaseMetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DbMapService {

    @Autowired
    private DatabaseMetaRepository databaseMetaRepository;

    public List<String> retrieveDbTables(){
        return databaseMetaRepository.retrieveAvaliableTables();
    }

    public Map<String, DataMap> retrieveDbColumns(String table){
        Map<String, String> map = databaseMetaRepository.retrieveAvaliableColumns(table);
        return map.entrySet()
                .stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), new DataMap(null, entry.getValue())))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
    }
}
