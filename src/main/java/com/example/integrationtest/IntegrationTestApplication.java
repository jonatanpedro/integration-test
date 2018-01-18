package com.example.integrationtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class IntegrationTestApplication {

    @Autowired
    private ApplicationContext applicationContext;

	public static void main(String[] args) {
		SpringApplication.run(IntegrationTestApplication.class, args);
	}

	/*@Bean
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
    }*/
}
