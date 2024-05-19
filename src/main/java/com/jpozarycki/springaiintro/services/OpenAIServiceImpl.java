package com.jpozarycki.springaiintro.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpozarycki.springaiintro.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.parser.BeanOutputParser;
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
        log.info("I was called with question: {}", question);
        PromptTemplate promptTemplate = new PromptTemplate(question.question());
        Prompt prompt = promptTemplate.create();
        ChatResponse chatResponse = chatClient.call(prompt);
        log.info("I got a response: {}", chatResponse.getResult().getOutput().getContent());
        return new Answer(chatResponse.getResult().getOutput().getContent());
    }

    @Override
    public GetCapitalResponse getCapital(GetCapitalRequest getCapitalRequest) {
        BeanOutputParser<GetCapitalResponse> parser = new BeanOutputParser<>(GetCapitalResponse.class);
        String format = parser.getFormat();
        log.info("Format: {}", format);

        PromptTemplate promptTemplate = new PromptTemplate(getCapitalPromptResource);
        Prompt prompt = promptTemplate.create(Map.of(
                "country", getCapitalRequest.country(),
                "format", format));
        ChatResponse chatResponse = chatClient.call(prompt);

        log.info(chatResponse.getResult().getOutput().getContent());

        return parser.parse(chatResponse.getResult().getOutput().getContent());
    }

    @Override
    public GetCapitalWithInfoResponse getCapitalWithInfo(GetCapitalRequest getCapitalRequest) {
        BeanOutputParser<GetCapitalWithInfoResponse> parser = new BeanOutputParser<>(GetCapitalWithInfoResponse.class);
        String format = parser.getFormat();
        log.info("Format with info: {}", format);

        PromptTemplate promptTemplate = new PromptTemplate(getCapitalWithInfoPromptResource);
        Prompt prompt = promptTemplate.create(Map.of(
                "country", getCapitalRequest.country(),
                "formatWithInfo", format));
        ChatResponse chatResponse = chatClient.call(prompt);

        return parser.parse(chatResponse.getResult().getOutput().getContent());
    }
}
