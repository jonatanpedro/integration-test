package com.example.integrationtest.dto;

import com.example.integrationtest.enums.ServiceType;
import org.springframework.data.annotation.Id;

import java.util.Map;

public class Flow {

    @Id
    private String id;
    private String urlBase;
    private Boolean useTable;
    private Boolean useConsole;
    private String selectedTable;
    private Map<String, DataMap> sourceMap;
    private Map<String, DataMap> destinationMap;
    private ServiceType serviceType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlBase() {
        return urlBase;
    }

    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    public Boolean getUseTable() {
        return useTable;
    }

    public void setUseTable(Boolean useTable) {
        this.useTable = useTable;
    }

    public Boolean getUseConsole() {
        return useConsole;
    }

    public void setUseConsole(Boolean useConsole) {
        this.useConsole = useConsole;
    }

    public String getSelectedTable() {
        return selectedTable;
    }

    public void setSelectedTable(String selectedTable) {
        this.selectedTable = selectedTable;
    }

    public Map<String, DataMap> getSourceMap() {
        return sourceMap;
    }

    public void setSourceMap(Map<String, DataMap> sourceMap) {
        this.sourceMap = sourceMap;
    }

    public Map<String, DataMap> getDestinationMap() {
        return destinationMap;
    }

    public void setDestinationMap(Map<String, DataMap> destinationMap) {
        this.destinationMap = destinationMap;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public String toString() {
        return "Flow{" +
                "id='" + id + '\'' +
                ", urlBase='" + urlBase + '\'' +
                ", useTable=" + useTable +
                ", useConsole=" + useConsole +
                ", selectedTable='" + selectedTable + '\'' +
                ", sourceMap=" + sourceMap +
                ", destinationMap=" + destinationMap +
                '}';
    }
}
