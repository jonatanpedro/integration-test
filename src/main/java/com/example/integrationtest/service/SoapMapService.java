package com.example.integrationtest.service;

import com.predic8.schema.*;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;

import java.net.URISyntaxException;
import java.net.URL;

public class SoapMapService {

    public static void main(String[] args) {
        new SoapMapService().parse();
    }

    public void parse() {
        try {
            URL wsdlURL = this.getClass().getResource("/rubi_Synccom_senior_g5_rh_fp_integracaoOracle.wsdl");
            WSDLParser parser = new WSDLParser();
            Definitions wsdl = parser.parse(wsdlURL.toURI().toString());
            for (Schema schema: wsdl.getLocalTypes().getSchemas()) {
                parseSchema(schema);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void parseSchema(Schema schema) {
        for (ComplexType complexType: schema.getComplexTypes()) {
            SchemaComponent model = complexType.getModel();
            if (model instanceof Sequence) {
                Sequence sequence = (Sequence) model;
                parseSequence(sequence);
            }
        }
    }

    private void parseSequence(Sequence sequence) {
        for (SchemaComponent schemaComponent: sequence.getParticles()) {
            String name = schemaComponent.getName();
            if (schemaComponent instanceof Element) {
                String localPart = ((Element) schemaComponent).getType().getLocalPart();
                System.out.println("Attr name: " + name + ", Attr type: " + localPart);
            }
        }
    }
}
