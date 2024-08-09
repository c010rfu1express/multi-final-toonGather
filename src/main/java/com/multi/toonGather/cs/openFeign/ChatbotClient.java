package com.multi.toonGather.cs.openFeign;

import com.multi.toonGather.cs.model.dto.ChatbotRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "chatbotClient", url = "${naver.clova.chatbot.url}")
public interface ChatbotClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    String chatbotRequest(
            @RequestHeader("X-NCP-CHATBOT_SIGNATURE") String signature,
            @RequestBody ChatbotRequest request
    );
}
