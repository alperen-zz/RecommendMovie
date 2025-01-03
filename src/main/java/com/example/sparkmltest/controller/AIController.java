package com.example.sparkmltest.controller;


import com.example.sparkmltest.service.GenerativeAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/makeJoke")
public class AIController {

    @Autowired
    private GenerativeAIService generativeAIService;


    @GetMapping("/{topic}")
    public String makeJoke(@PathVariable String topic){

        return this.generativeAIService.getJoke(topic);

    }


}
