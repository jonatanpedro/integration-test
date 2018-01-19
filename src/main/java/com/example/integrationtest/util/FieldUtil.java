package com.example.integrationtest.util;

import java.util.LinkedHashMap;
import java.util.List;

public class FieldUtil {

    public static final String STRING = "STRING";
    public static final String NUMBER = "NUMBER";
    public static final String BOOLEAN = "BOOLEAN";
    public static final String MAP = "MAP";
    public static final String ARRAY = "ARRAY";

    private FieldUtil() {}

    public static String identifyType(Object type){
        if(type instanceof Number){
            return NUMBER;
        }else if(type instanceof String){
            return STRING;
        }else if(type instanceof List){
            return ARRAY;
        }else if(type instanceof Boolean) {
            return BOOLEAN;
        }else if(type instanceof LinkedHashMap){
            return MAP;
        } else{
            return null;
        }
    }
}
