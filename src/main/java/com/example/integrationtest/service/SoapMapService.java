package com.example.integrationtest.service;

import com.example.integrationtest.dto.DataMap;
import com.example.integrationtest.dto.MapSoapParameterDTO;
import com.predic8.schema.*;
import com.predic8.wsdl.*;
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
        if (outputName != null) {
            wsdl.getLocalTypes().getSchemas().forEach(schema -> parseSchemaByOutputName(result, outputName, schema));
        } else {
            String outputMessage = operation.getOutput().getMessagePrefixedName().getLocalName();
            wsdl.getLocalTypes().getSchemas().forEach(schema -> parseSchemaByOutputMessage(result, outputMessage, schema, wsdl));
        }
    }

    private void parseSchemaByOutputMessage(Map<String, DataMap> result, String outputMessageName, Schema schema, Definitions wsdl) {
        Optional<Message> optionalOutputMessage = getOutputMessage(outputMessageName, wsdl);
        optionalOutputMessage.ifPresent(outputMessage -> parseOutputMessage(result, schema, outputMessage));
    }

    private void parseOutputMessage(Map<String, DataMap> result, Schema schema, Message outputMessage) {
        outputMessage.getParts().forEach(part -> parseComplexType(result, schema, part.getTypePN().getLocalName()));
    }

    private Optional<Message> getOutputMessage(String outputMessage, Definitions wsdl) {
        return wsdl.getMessages()
                .stream()
                .filter(message -> message.getName().equals(outputMessage))
                .findFirst();

    }

    private void parseSchemaByOutputName(Map<String, DataMap> result, String outputName, Schema schema) {
        Optional<Element> optionalOutputElement = getOutputElement(outputName, schema);
        optionalOutputElement.ifPresent(outputElement -> parseOutputElement(result, schema, outputElement));
    }

    private void parseOutputElement(Map<String, DataMap> result, Schema schema, Element outputElement) {
        List<SchemaComponent> outputComponents = ((Sequence) ((ComplexType) outputElement.getEmbeddedType()).getModel()).getParticles();
        outputComponents.forEach(outputComponent -> parseComplexType(result, schema, outputComponent.getName()));
    }

    private void parseComplexType(Map<String, DataMap> result, Schema schema, String complexTypeName) {
        List<ComplexType> outputComponentTypes = getOutputComponentTypes(schema, complexTypeName);
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
        List<ComplexType> complexTypes = schema.getComplexTypes()
                .stream()
                .filter(complexType -> complexType.getName().equals(componentName))
                .collect(Collectors.toList());
        if (complexTypes.isEmpty()) {
            complexTypes = getLocalTypesFromImportedSchemas(schema);
        }
        return complexTypes;
    }

    private List<ComplexType> getLocalTypesFromImportedSchemas(Schema schema) {
        return schema.getImportedSchemas()
                .stream()
                .map(Schema::getComplexTypes)
                .flatMap(List::stream)
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
