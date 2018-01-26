package com.example.integrationtest.dto;

import com.example.integrationtest.enums.ServiceType;

public class MapRestParameterDTO {
    private String url;
    private ServiceType serviceType;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }
}
