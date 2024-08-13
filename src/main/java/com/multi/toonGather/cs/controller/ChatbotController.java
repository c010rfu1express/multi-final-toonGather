package com.multi.toonGather.cs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.multi.toonGather.cs.model.dto.ChatbotRequest;
import com.multi.toonGather.cs.service.ChatbotService;
import com.multi.toonGather.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @GetMapping("/chatbotPage")
    public String getChatbotPage() {
        return "/cs/chatbotPage";
    }

    @GetMapping("/welcome")
    public ResponseEntity<Map<String, String>> getWelcomeMessage(@AuthenticationPrincipal CustomUserDetails c) throws JsonProcessingException {

        String userId;

        if (c != null && "" + c.getUserDTO().getUserNo() != null) {
            userId = String.valueOf(c.getUserDTO().getUserNo());
        } else {
            userId = "0";
        }

        String welcomeMessage = chatbotService.getWelcomeMessage(userId);
        System.out.println("chatbotResponse : " + welcomeMessage);

        // JSON 문자열을 파싱하여 description 필드만 추출
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseMap = objectMapper.readValue(welcomeMessage, new TypeReference<Map<String, Object>>() {});

        String description = null;

        if (responseMap.containsKey("bubbles")) {
            List<Map<String, Object>> bubbles = (List<Map<String, Object>>) responseMap.get("bubbles");
            if (bubbles.size() > 0 && bubbles.get(0).containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) bubbles.get(0).get("data");
                if (data.containsKey("description")) {
                    description = (String) data.get("description");
                }
            }
        }

        // 클라이언트에게 단순히 description만 반환
        Map<String, String> result = new HashMap<>();
        result.put("description", description != null ? description : "No valid description found");

        return ResponseEntity.ok(result);
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendMessage(@AuthenticationPrincipal CustomUserDetails c,
                                                           @RequestBody ChatbotRequest request) throws JsonProcessingException {

        System.out.println("request : " + request);

        String userId;

        if (c != null && "" + c.getUserDTO().getUserNo() != null) {
            userId = String.valueOf(c.getUserDTO().getUserNo());
        } else {
            userId = "0";
        }

        String chatbotResponse = chatbotService.sendMessage(userId, request.getMessage());

        System.out.println("chatbotResponse : " + chatbotResponse);

        // JSON 문자열을 파싱하여 description 필드만 추출
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseMap = objectMapper.readValue(chatbotResponse, new TypeReference<Map<String, Object>>() {});

        String description = null;

        if (responseMap.containsKey("bubbles")) {
            List<Map<String, Object>> bubbles = (List<Map<String, Object>>) responseMap.get("bubbles");
            if (bubbles.size() > 0 && bubbles.get(0).containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) bubbles.get(0).get("data");
                if (data.containsKey("description")) {
                    description = (String) data.get("description");
                }
            }
        }

        // 클라이언트에게 단순히 description만 반환
        Map<String, String> result = new HashMap<>();
        result.put("description", description != null ? description : "No valid description found");

        return ResponseEntity.ok(result);
    }
}
