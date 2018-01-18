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

    @Autowired
    private RestMapService restMapService;

    @RequestMapping("/")
    public String getIndex(Model model){
        model.addAttribute("param", new MapRestParameterDTO());
        return "mapper-view";
    }

    @PostMapping("/map")
    public String mapRequestUrl(@ModelAttribute MapRestParameterDTO param, Model model) {

        Map<String, String> result = restMapService.retrieveRestMap(param.getUrl());

        model.addAttribute("param", new MapRestParameterDTO());
        model.addAttribute("jsonMap", result);

        return "mapper-view";
    }
}
