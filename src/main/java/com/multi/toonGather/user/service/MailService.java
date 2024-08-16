package com.multi.toonGather.user.service;

import jakarta.mail.internet.MimeMessage;

public interface MailService {

    static void createNumber() {

    }

    MimeMessage CreateMail(String mail, boolean isPasswordMail);

    int sendMail(String mail);
    String sendMailPw(String mail);

}
