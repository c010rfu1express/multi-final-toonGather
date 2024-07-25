package com.multi.toonGather.social.model.dto;

import lombok.Data;

/**
 * 리뷰 작성자 정보를 담는 데이터 전송 객체 (DTO)
 *
 * 이 클래스는 리뷰 작성자의 기본 정보(사용자 번호, ID, 닉네임)와
 * 프로필 이미지 경로를 포함합니다.
 *
 * @author : seoyun
 * @fileName : WriterDTO
 * @since : 2024-07-24
 */
@Data
public class WriterDTO {
    private int userNo;
    private String userId;
    private String nickname;
    private String profileImagePath;
}
