package com.multi.toonGather.introduction.model.dto;

import lombok.Data;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MerchanDTO {
    private int merchanNo;           // 상품 게시글 번호
    private String title;            // 상품 제목
    private Integer regularPrice;        // 정가 (null 가능)
    private Integer discountPrice;       // 할인가 (null 가능)
    private Integer shippingCost;        // 배송비 (null 가능)
    private String merchanInfo;      // 상품 정보
    private String content;          // 상품 내용
    private String site;             // 관련 웹사이트
    private LocalDateTime postingDate; // 게시 일시

    private List<MerchanFileDTO> merchanFiles;

    public String getFormattedRegularPrice() {
        return NumberFormat.getInstance().format(this.regularPrice);
    }

    public String getFormattedDiscountPrice() {
        return NumberFormat.getInstance().format(this.discountPrice);
    }

    public String getFormattedShippingCost() {
        return NumberFormat.getInstance().format(this.shippingCost);
    }

//    public String getFormattedRegularPrice() {
//        return this.regularPrice != null ? NumberFormat.getInstance().format(this.regularPrice) : "가격 미정";
//    }
//
//    public String getFormattedDiscountPrice() {
//        return this.discountPrice != null ? NumberFormat.getInstance().format(this.discountPrice) : "할인가 없음";
//    }
//
//    public String getFormattedShippingCost() {
//        return this.shippingCost != null ? NumberFormat.getInstance().format(this.shippingCost) : "무료 배송";
//    }

}
