package com.example.sparkmltest.service;


import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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



}
