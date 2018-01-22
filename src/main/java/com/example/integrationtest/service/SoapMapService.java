package com.example.integrationtest.service;

import com.example.integrationtest.dto.DataMap;
import com.predic8.schema.*;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SoapMapService implements MapService {

    public Map<String, DataMap> retrieveMap(String url){
        Map<String, DataMap> result = new HashMap<>();
        WSDLParser parser = new WSDLParser();
        Definitions wsdl = parser.parse(url);
        for (Schema schema: wsdl.getLocalTypes().getSchemas()) {
            parseSchema(schema, result);
        }
        return result;
    }

    private void parseSchema(Schema schema, Map<String, DataMap> result) {
        for (ComplexType complexType: schema.getComplexTypes()) {
            SchemaComponent model = complexType.getModel();
            if (model instanceof Sequence) {
                Sequence sequence = (Sequence) model;
                parseSequence(sequence, result);
            }
        }
    }

    private void parseSequence(Sequence sequence, Map<String, DataMap> result) {
        for (SchemaComponent schemaComponent: sequence.getParticles()) {
            String key = schemaComponent.getName();
            if (schemaComponent instanceof Element) {
                String type = ((Element) schemaComponent).getType().getLocalPart();
                result.put(key,new DataMap(type.toUpperCase(), null));
            }
        }
    }
}
