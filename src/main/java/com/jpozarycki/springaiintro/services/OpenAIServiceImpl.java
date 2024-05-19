package com.jpozarycki.springaiintro.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpozarycki.springaiintro.model.Answer;
import com.jpozarycki.springaiintro.model.GetCapitalRequest;
import com.jpozarycki.springaiintro.model.Question;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAIServiceImpl implements OpenAIService {
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    @Value("classpath:templates/get-capital-prompt.st")
    private Resource getCapitalPromptResource;
    @Value("classpath:templates/get-capital-with-info-prompt.st")
    private Resource getCapitalWithInfoPromptResource;

    @Override
    public String getAnswer(String question) {
        PromptTemplate promptTemplate = new PromptTemplate(question);
        Prompt prompt = promptTemplate.create();
        ChatResponse chatResponse = chatClient.call(prompt);
        return chatResponse.getResult().getOutput().getContent();
    }

    @Override
    public Answer getAnswer(Question question) {
        log.debug("I was called with question: {}", question);
        PromptTemplate promptTemplate = new PromptTemplate(question.question());
        Prompt prompt = promptTemplate.create();
        ChatResponse chatResponse = chatClient.call(prompt);
        log.debug("I got a response: {}", chatResponse.getResult().getOutput().getContent());
        return new Answer(chatResponse.getResult().getOutput().getContent());
    }

    @Override
    public Answer getCapital(GetCapitalRequest getCapitalRequest) {
        PromptTemplate promptTemplate = new PromptTemplate(getCapitalPromptResource);
        Prompt prompt = promptTemplate.create(Map.of("country", getCapitalRequest.country()));
        ChatResponse chatResponse = chatClient.call(prompt);
        log.debug(chatResponse.getResult().getOutput().getContent());
        String responseString;
        try {
            JsonNode jsonNode = objectMapper.readTree(chatResponse.getResult().getOutput().getContent());
            responseString = jsonNode.get("answer").asText();
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Failed to parse response", ex);
        }

        return new Answer(responseString);
    }

    @Override
    public Answer getCapitalWithInfo(GetCapitalRequest getCapitalRequest) {
        PromptTemplate promptTemplate = new PromptTemplate(getCapitalWithInfoPromptResource);
        Prompt prompt = promptTemplate.create(Map.of("country", getCapitalRequest.country()));
        ChatResponse chatResponse = chatClient.call(prompt);

        return new Answer(chatResponse.getResult().getOutput().getContent());
    }
}
