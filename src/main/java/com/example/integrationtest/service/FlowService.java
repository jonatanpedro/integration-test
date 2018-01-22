package com.example.integrationtest.service;

import com.example.integrationtest.dto.Flow;
import com.example.integrationtest.repository.FlowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowBuilder;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.stereotype.Service;

@Service
public class FlowService {

    @Autowired
    private FlowRepository repository;

    /*@Autowired
    private IntegrationGateway integrationGateway;*/

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


                /*.handle(Http.outboundGateway("http://localhost:8080/users/1")
                        .charset("UTF-8")
                        .httpMethod(HttpMethod.GET)
                        .expectedResponseType(String.class))

                .handle(auditService, "update")
                .get();*/


        if(hasDataChanged){
            intFlowBuilder = intFlowBuilder.transform("");
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
