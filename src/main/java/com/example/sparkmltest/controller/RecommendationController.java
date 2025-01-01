package com.example.sparkmltest.controller;

import com.example.sparkmltest.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<String>> getRecommendations(@PathVariable int userId){
        List<String> recommenadtionList = recommendationService.getRecommendations(userId);
        return ResponseEntity.ok(recommenadtionList);

    }

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
