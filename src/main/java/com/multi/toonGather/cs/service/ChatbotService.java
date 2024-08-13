package com.multi.toonGather.cs.service;

public interface ChatbotService {

    String getWelcomeMessage(String userId);
    String sendMessage(String userId, String message);
}
