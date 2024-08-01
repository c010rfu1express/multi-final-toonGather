package com.multi.toonGather.social.model.dto;

import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.webtoon.model.dto.WebtoonDTO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 다이어리 정보를 담는 데이터 전송 객체 (DTO)
 *
 * 이 클래스는 다이어리의 기본 정보(번호, 제목, 내용, 별점 등)와
 * 관련된 웹툰 정보, 작성자 정보, 그리고 생성/수정 일자 및 조회수를 포함합니다.
 *
 * @author : seoyun
 * @fileName : DiaryDTO
 * @since : 2024-07-24
 */
@Data
public class DiaryDTO {
    private int diaryNo;
    private UserDTO writer;
    private WebtoonDTO webtoon;
    private String title;
    private String content;
    private char statusCode;  // 'C'이면 '정주행중', 'R'이면 '재탕중'
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private int viewCnt;
}
