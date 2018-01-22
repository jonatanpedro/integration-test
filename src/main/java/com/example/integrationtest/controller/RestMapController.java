package com.example.integrationtest.controller;

import com.example.integrationtest.dto.Flow;
import com.example.integrationtest.dto.MapRestParameterDTO;
import com.example.integrationtest.repository.FlowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RestMapController {

    public static final String MAPPER_VIEW = "mapper-view";
    public static final String MAPPER_FLOWS = "flows";

    @Autowired
    private FlowRepository flowRepository;

    @RequestMapping("/")
    public String getIndex(Model model){
        model.addAttribute("param", new MapRestParameterDTO());
        return MAPPER_VIEW;
    }

    @RequestMapping("/flows")
    public String getFlows(Model model){

        List<Flow> flows = flowRepository.findAll();
        model.addAttribute("flows", flows);

        return MAPPER_FLOWS;
    }
}
