package com.multi.toonGather.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{
    private final JavaMailSender javaMailSender;
    private static final String senderEmail= "hgk.2024.multi@gmail.com";
    private static int number;
    private static String pwString;

    // 랜덤으로 숫자 생성
    public static void createNumber() {
        number = (int)(Math.random() * (90000)) + 100000; //(int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    // 랜덤으로 비밀번호 생성
    public static void createTempPw() {
        // 문자 집합 정의
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()-_=+[]{}|;:,.<>?/";
        String allChars = uppercase + lowercase + digits + specialChars;

        // 비밀번호 길이
        int passwordLength = 20;
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(passwordLength);

        // 최소 1개의 문자 집합에서 문자 추가
        password.append(uppercase.charAt(random.nextInt(uppercase.length())));
        password.append(lowercase.charAt(random.nextInt(lowercase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));

        // 나머지 길이 랜덤 문자로 채우기
        for (int i = 4; i < passwordLength; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        // 비밀번호 무작위화
        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(passwordLength);
            char temp = password.charAt(i);
            password.setCharAt(i, password.charAt(randomIndex));
            password.setCharAt(randomIndex, temp);
        }

        pwString = password.toString();
    }


    public MimeMessage CreateMail(String mail, boolean isPasswordMail) {
        createNumber();
        if (isPasswordMail) {
            createTempPw(); // 비밀번호 생성
        }

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject(isPasswordMail ? "[toonGather] 임시 비밀번호" : "[toonGather] 이메일 인증");
            String body = "";
            if (isPasswordMail) {
                body += "<h3>" + "[toonGather] 요청하신 임시 비밀번호입니다." + "</h3>";
                body += "<h1>" + pwString + "</h1>";
            } else {
                body += "<h3>" + "[toonGather] 요청하신 인증 번호입니다." + "</h3>";
                body += "<h1>" + number + "</h1>";
            }
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public int sendMail(String mail) {
        MimeMessage message = CreateMail(mail, false);
        javaMailSender.send(message);

        return number;
    }

    public String sendMailPw(String mail) {
        MimeMessage message = CreateMail(mail, true);
        javaMailSender.send(message);

        return pwString;
    }
}
