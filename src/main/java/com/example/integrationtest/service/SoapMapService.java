package com.example.integrationtest.service;

import com.example.integrationtest.dto.DataMap;
import com.example.integrationtest.dto.MapSoapParameterDTO;
import com.predic8.schema.*;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.PortType;
import com.predic8.wsdl.WSDLParser;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SoapMapService {

    public Map<String, DataMap> retrieveMap(MapSoapParameterDTO param) {
        Map<String, DataMap> result = new HashMap<>();
        WSDLParser parser = new WSDLParser();
        Definitions wsdl = parser.parse(param.getUrl());
        Optional<PortType> optionalPort = filterWsdlPort(param, wsdl);
        optionalPort.ifPresent(port -> parsePort(param, result, wsdl, port));
        return result;
    }

    private void parsePort(MapSoapParameterDTO param, Map<String, DataMap> result, Definitions wsdl, PortType port) {
        Optional<Operation> optionalOperation = filterWsdlOperation(param, port);
        optionalOperation.ifPresent(operation -> parseOperation(result, wsdl, operation));
    }

    private void parseOperation(Map<String, DataMap> result, Definitions wsdl, Operation operation) {
        String outputName = operation.getOutput().getName();
        wsdl.getLocalTypes().getSchemas().forEach(schema -> parseSchema(result, outputName, schema));
    }

    private void parseSchema(Map<String, DataMap> result, String outputName, Schema schema) {
        Optional<Element> optionalOutputElement = getOutputElement(outputName, schema);
        optionalOutputElement.ifPresent(outputElement -> parseOutputElement(result, schema, outputElement));
    }

    private void parseOutputElement(Map<String, DataMap> result, Schema schema, Element outputElement) {
        List<SchemaComponent> outputComponents = ((Sequence) ((ComplexType) outputElement.getEmbeddedType()).getModel()).getParticles();
        outputComponents.forEach(outputComponent -> parseOutputComponent(result, schema, outputComponent));
    }

    private void parseOutputComponent(Map<String, DataMap> result, Schema schema, SchemaComponent outputComponent) {
        String componentName = outputComponent.getName();
        List<ComplexType> outputComponentTypes = getOutputComponentTypes(schema, componentName);
        outputComponentTypes.forEach(outputComponentType -> getComponentAttributes(result, outputComponentType));
    }

    private void getComponentAttributes(Map<String, DataMap> result, ComplexType outputComponentType) {
        Sequence sequence = (Sequence) outputComponentType.getModel();
        sequence.getParticles().forEach(particle -> {
            String key = particle.getName();
            String type = ((Element) particle).getType().getLocalPart();
            result.put(key, new DataMap(type.toUpperCase(), null));
        });
    }

    private List<ComplexType> getOutputComponentTypes(Schema schema, String componentName) {
        return schema.getComplexTypes()
                .stream()
                .filter(complexType -> complexType.getName().equals(componentName))
                .collect(Collectors.toList());
    }

    private Optional<Element> getOutputElement(String outputName, Schema schema) {
        return schema.getElements()
                .stream()
                .filter(element -> element.getName().equals(outputName))
                .findFirst();
    }

    private Optional<Operation> filterWsdlOperation(MapSoapParameterDTO param, PortType portType) {
        return portType.getDefinitions().getOperations()
                .stream()
                .filter(operation -> operation.getName().equals(param.getOperation()))
                .findFirst();
    }

    private Optional<PortType> filterWsdlPort(MapSoapParameterDTO param, Definitions wsdl) {
        return wsdl.getPortTypes()
                .stream()
                .filter(portType -> portType.getName().equals(param.getPort()))
                .findFirst();
    }
}
