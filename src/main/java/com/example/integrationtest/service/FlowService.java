package com.example.integrationtest.service;

import com.example.integrationtest.dto.Flow;
import com.example.integrationtest.flow.TransformerFactory;
import com.example.integrationtest.repository.FlowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowBuilder;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.stereotype.Service;

@Service
public class FlowService {

    @Autowired
    private FlowRepository repository;

    @Autowired
    private TransformerFactory transformerFactory;

    public void createFlow(Flow flow){
        repository.save(flow);
    }

    public IntegrationFlow executeFlow(String id){

        Flow flow = repository.findById(id).orElse(null);

        boolean hasDataChanged = flow.getSourceMap()
                .entrySet()
                .stream()
                .anyMatch(entry -> entry.getValue().getDestination() != null);


        IntegrationFlowBuilder intFlowBuilder = IntegrationFlows.from("channel");

        intFlowBuilder = intFlowBuilder
                .handle(Http.outboundGateway(flow.getUrlBase())
                .charset("UTF-8")
                .httpMethod(HttpMethod.GET)
                .expectedResponseType(String.class));

        intFlowBuilder = intFlowBuilder.transform(new JsonToObjectTransformer());

        if(hasDataChanged){
            intFlowBuilder = intFlowBuilder.transform(transformerFactory.createTransformer(RestMapperTransformationService.class));
        }

        if(flow.getUseTable()){
            intFlowBuilder = intFlowBuilder.handle("");
        }

        if(flow.getUseConsole()){
            intFlowBuilder = intFlowBuilder.handle("");
        }

        return intFlowBuilder.get();
    }
}
