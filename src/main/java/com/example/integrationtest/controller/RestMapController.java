package com.example.integrationtest.controller;

import com.example.integrationtest.dto.MapRestParameterDTO;
import com.example.integrationtest.service.RestMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class RestMapController {

    public static final String MAPPER_VIEW = "mapper-view";
    public static final String EMPTY = "";
    @Autowired
    private RestMapService restMapService;

    @RequestMapping("/")
    public String getIndex(Model model){
        model.addAttribute("param", new MapRestParameterDTO());
        return MAPPER_VIEW;
    }

    @PostMapping("/map")
    public String mapRequestUrl(@ModelAttribute MapRestParameterDTO param, Model model) {

        Map<String, String> outPutMap = null;

        if (param != null && (param.getUrl() != null && !EMPTY.equals(param.getUrl()))) {
            outPutMap = restMapService.retrieveRestMap(param.getUrl());
        }

        model.addAttribute("param", new MapRestParameterDTO());
        model.addAttribute("outPutMap", outPutMap);

        return MAPPER_VIEW;
    }
}
