package com.multi.toonGather.introduction.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MerchanDTO {
    private int merchanNo;           // 상품 게시글 번호
    private String title;            // 상품 제목
    private int regularPrice;        // 정가
    private int discountPrice;       // 할인가
    private int shippingCost;        // 배송비
    private String merchanInfo;      // 상품 정보
    private String content;          // 상품 내용
    private LocalDateTime postingDate; // 게시 일시

    private List<MerchanFileDTO> merchanFiles;
}
