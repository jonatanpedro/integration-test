package com.example.integrationtest.flow;

import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;

@Component
public class TransformerFactory {
    public GenericTransformer createTransformer(Class<? extends GenericTransformer> transtormerClass){
        if(transtormerClass.getClass().getName().equals(RestMapperTransformationService.class.getName())){
            return new RestMapperTransformationService();
        }
        return null;
    }
}
