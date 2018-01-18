package com.example.integrationtest;

import com.example.integrationtest.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.MessageChannel;

@SpringBootApplication
@Configuration
public class IntegrationTestApplication {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AuditService auditService;

	public static void main(String[] args) {
		SpringApplication.run(IntegrationTestApplication.class, args);
	}

	@Bean
	public IntegrationFlow httpOutboundGatewayUserIntegration(){
	    return IntegrationFlows.from("channel")
                .handle(Http.outboundGateway("http://localhost:8080/users/1")
                        .charset("UTF-8")
                        .httpMethod(HttpMethod.GET)
                        .expectedResponseType(String.class))

                .handle(auditService, "update")
                .get();
    }

    @Bean
    public ApplicationRunner runner(){
	    return args -> {
            SimpleGateway gateway = applicationContext.getBean(SimpleGateway.class);
            gateway.execute("Start");
        };
    }

    @MessagingGateway
    interface SimpleGateway {
        @Gateway(requestChannel = "channel")
        void execute(String message);
    }
}
