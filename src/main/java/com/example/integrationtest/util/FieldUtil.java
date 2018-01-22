package com.example.integrationtest.util;

import java.lang.reflect.Array;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FieldUtil {

    public static final String STRING = "STRING";
    public static final String NUMBER = "NUMBER";
    public static final String BOOLEAN = "BOOLEAN";
    public static final String MAP = "MAP";
    public static final String ARRAY = "ARRAY";
    public static final String FLOAT = "FLOAT";

    private FieldUtil() {}

    public static String identifyType(Object type){
        if (type instanceof Float){
            return FLOAT;
        }else if(type instanceof Number){
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

    public static Class<?> identifyType(String type){
        switch (type){
            case FLOAT:
                return Float.class;
            case NUMBER:
                return Number.class;
            case STRING:
                return String.class;
            case ARRAY:
                return Array.class;
            case BOOLEAN:
                return Boolean.class;
            case MAP:
                return Map.class;
            default:
                return null;
        }
    }

}
