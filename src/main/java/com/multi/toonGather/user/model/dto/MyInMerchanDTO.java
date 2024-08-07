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
public class MyInMerchanDTO {
    private int likedNo;
    private int merchanNo;
    private int userNo;
    private LocalDateTime likedDate;

    private String title;
    private int regularPrice;
    private int discountPrice;
    private int shippingCost;
    private String merchanInfo;
    private String content;
    private LocalDateTime postingDate;
}
