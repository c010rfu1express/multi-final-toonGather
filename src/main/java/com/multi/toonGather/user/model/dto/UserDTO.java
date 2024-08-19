package com.multi.toonGather.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
   private int userNo;//
   private char typeCode;
   private char authCode;
   private String userId;//
   private String password;//
   private String nickname;//
   private String contactNumber;//
   private String email;//
   private String profileImagePath;
   private String bio;//
   private char gender;//
   private LocalDate dateOfBirth;//
   private String realName;
   private boolean termsAgreement;//
   private LocalDateTime termsAgreementDatetime;
   private LocalDateTime registrationDatetime;
   private LocalDateTime lastModifiedDatetime;
   private char withdrawn;

   //DB 외 영역 (생년월일 처리를 위함)
   private String year;
   private String month;
   private String day;
   private String confirmPassword;
   private String mailNumber;
}
