package com.multi.toonGather.social.model.dto;

import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.webtoon.model.dto.WebtoonDTO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 리뷰 정보를 담는 데이터 전송 객체 (DTO)
 *
 * 이 클래스는 리뷰의 기본 정보(번호, 제목, 내용, 별점 등)와
 * 관련된 웹툰 정보, 작성자 정보, 그리고 생성/수정 일자 및 조회수를 포함합니다.
 *
 * @author : seoyun
 * @fileName : ReviewDTO
 * @since : 2024-07-24
 */
@Data
public class ReviewDTO {
    private int reviewNo;
    private int webtoonNo;
    private UserDTO writer;
    private WebtoonDTO webtoon;
    private String title;
    private String content;
    private int starRating;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private int viewCnt;
}
