package com.multi.toonGather.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyRctOrderDTO {
    private int orderNo;
    private int memberNo;
    private int boardNo;
    private int quantity;
    private int price;
    private LocalDateTime orderedDate;
    private char status;
    //추가된 것(rct_free_pay)
    private String bankName;
    private String account;
    private String buyerName;
    private String contact;
    private char typeCode;


    //조인하여 가져올 것 (from rct_free)
    private int writer; //userno통해 닉네임(from users)
    private String title;
    private String content;
    private String img;
    private int totalPrice;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    //조인2하여 가져올 것 (from users)
    private String nickname;//

    //시간 view에 뿌려줄 때
    public String getFormattedOrderedDate() {
        if(orderedDate == null) return "미정";
        return TimeAgoUtils.formatTimeAgo(orderedDate);
    }
}
