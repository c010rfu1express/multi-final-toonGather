package com.multi.toonGather.recruit.model.dto.free;

import lombok.Data;

import java.util.Date;

@Data
public class FreeDTO {
    private int board_no;
    private int writer;
    private String title;
    private String content;
    private String img;
    private int price;
    private Date created_date;
    private Date modified_date;
//    private MemberDTO memNo;

}
