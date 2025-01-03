package com.example.sparkmltest.service;


import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerativeAIService {

    private final OpenAiChatModel chatModel;


    @Autowired
    public GenerativeAIService (OpenAiChatModel chatModel){
        this.chatModel = chatModel;
    }


    public String getJoke(String topic){


        Prompt prompt = new Prompt("Tell me a makeJoke about "+topic);

        return this.chatModel.call(prompt).getResult().getOutput().getText();


    }

    public String getNewMovieRecommendation(List<String> movies){

        Prompt prompt = new Prompt("Make me a new movie recommendation based on the favorite movies "+movies+"Tell me what the movie is about, the director and leading roles.");
        return this.chatModel.call(prompt).getResult().getOutput().getText();
    }



}
