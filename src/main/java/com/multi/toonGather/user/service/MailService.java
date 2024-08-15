package com.multi.toonGather.user.service;

import jakarta.mail.internet.MimeMessage;

public interface MailService {

    static void createNumber() {

    }

    MimeMessage CreateMail(String mail);

    int sendMail(String mail);

}
