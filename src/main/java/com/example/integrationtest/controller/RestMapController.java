package com.example.integrationtest.controller;

import com.example.integrationtest.dto.MapRestParameterDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RestMapController {

    public static final String MAPPER_VIEW = "mapper-view";

    @RequestMapping("/")
    public String getIndex(Model model){
        model.addAttribute("param", new MapRestParameterDTO());
        return MAPPER_VIEW;
    }
}
