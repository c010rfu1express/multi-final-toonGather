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
    private String bank_name;
    private String account;
    private String buyer_name;
    private String email;
    private String phone;
    private String status;
    private String type_code;
    private Date ordered_date;

}
