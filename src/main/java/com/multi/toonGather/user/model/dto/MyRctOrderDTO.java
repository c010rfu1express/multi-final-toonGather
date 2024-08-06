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
}
