package com.jpozarycki.springaiintro.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OpenAIServiceImplTest {
    @Autowired
    OpenAIService openAIService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getAnswer() {
        String answer = openAIService.getAnswer("Write a snake game in python");
        System.out.println("Got the answer: ");
        System.out.println(answer);
    }
}