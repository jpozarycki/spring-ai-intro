package com.jpozarycki.springaiintro.services;

import com.jpozarycki.springaiintro.model.*;

public interface OpenAIService {
    String getAnswer(String question);
    Answer getAnswer(Question question);

    GetCapitalResponse getCapital(GetCapitalRequest getCapitalRequest);

    GetCapitalWithInfoResponse getCapitalWithInfo(GetCapitalRequest getCapitalRequest);
}
