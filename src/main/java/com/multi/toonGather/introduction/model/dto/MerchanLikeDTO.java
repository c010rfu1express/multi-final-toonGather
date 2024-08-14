package com.multi.toonGather.introduction.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MerchanLikeDTO {
    private int likedNo;        // 좋아요 번호
    private int merchanNo;      // 상품 게시글 번호
    private int userNo;         // 회원번호
    private LocalDateTime likedDate; // 좋아요 날짜

}
