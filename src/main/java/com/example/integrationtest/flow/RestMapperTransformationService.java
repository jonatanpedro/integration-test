package com.example.integrationtest.flow;

import com.example.integrationtest.dto.DataMap;
import com.example.integrationtest.dto.Flow;
import com.example.integrationtest.util.FieldUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.Message;

import java.util.Map;
import java.util.stream.Collectors;

public class RestMapperTransformationService implements GenericTransformer<Message<?>, Message<?>>{

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FormattingConversionService conversionService;

    @Override
    public Message<?> transform(Message<?> message) {

        try {
            Map<String, Object> objectMap = (Map<String, Object>) message.getPayload();
            Flow flow = (Flow) message.getHeaders().get("flow");
            Map<String, DataMap> conversions = flow
                    .getSourceMap()
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().getDestination() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            objectMap.entrySet().stream().map(entry -> {
                conversions
                        .entrySet()
                        .stream()
                        .filter(stringDataMapEntry -> entry.getKey().equals(stringDataMapEntry.getKey()))
                        .findFirst()
                        .ifPresent(dataEntry -> entry.setValue(conversionService.convert(entry.getValue(), FieldUtil.identifyType(dataEntry.getValue().getDestination()))));
                return entry;
            });

            return MessageBuilder
                    .withPayload(objectMap)
                    .copyHeaders(message.getHeaders())
                    .build();

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return message;
    }
}
