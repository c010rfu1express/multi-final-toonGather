package com.multi.toonGather.recruit.model.dto.free;

import lombok.Data;

import java.util.Date;

@Data
public class FreePayDTO {
    private int order_no;
    private int member_no;
    private int board_no;
    private int quantity;
    private int price;
    private Date ordered_date;
    private String status;

}
