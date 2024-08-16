package com.multi.toonGather.user.controller;

import com.multi.toonGather.user.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/mail")
public class MailController {
    private final MailService mailService;
    private int number; // 이메일 인증 숫자를 저장하는 변수

    // 인증 이메일 전송
    @PostMapping("/mailSend")
    public HashMap<String, Object> mailSend(@RequestBody Map<String, String> payload) {
        HashMap<String, Object> map = new HashMap<>();
        System.out.println("mailsend 실행됨");
        String mail = payload.get("mail");

        try {
            number = mailService.sendMail(mail);
            String num = String.valueOf(number);

            map.put("success", Boolean.TRUE);
            map.put("number", num);
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }

        return map;
    }

    // 인증번호 일치여부 확인
    @GetMapping("/mailCheck")
    public ResponseEntity<Boolean> mailCheck(@RequestParam("userNumber") String userNumber) {
        boolean isMatch = userNumber.equals(String.valueOf(number));
        return ResponseEntity.ok(isMatch);
    }

}
