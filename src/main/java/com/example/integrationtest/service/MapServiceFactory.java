package com.example.integrationtest.service;

import com.example.integrationtest.enums.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapServiceFactory {

    private final RestMapService restMapService;
    private final SoapMapService soapMapService;

    @Autowired
    public MapServiceFactory(RestMapService restMapService, SoapMapService soapMapService) {
        this.restMapService = restMapService;
        this.soapMapService = soapMapService;
    }

    public MapService getService(ServiceType serviceType) {
        switch (serviceType) {
            case REST:
                return restMapService;
            case SOAP:
                return soapMapService;
        }
        throw new IllegalArgumentException("The map for service type: " + serviceType + ", does not exists.");
    }
}
