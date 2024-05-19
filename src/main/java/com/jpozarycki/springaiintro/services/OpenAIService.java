package com.jpozarycki.springaiintro.services;

import com.jpozarycki.springaiintro.model.Answer;
import com.jpozarycki.springaiintro.model.GetCapitalRequest;
import com.jpozarycki.springaiintro.model.Question;

public interface OpenAIService {
    String getAnswer(String question);
    Answer getAnswer(Question question);

    Answer getCapital(GetCapitalRequest getCapitalRequest);

    Answer getCapitalWithInfo(GetCapitalRequest getCapitalRequest);
}
