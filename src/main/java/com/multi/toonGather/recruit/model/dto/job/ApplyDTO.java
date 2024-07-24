package com.multi.toonGather.recruit.model.dto.job;

import lombok.Data;

import java.util.Date;

@Data
public class ApplyDTO {
    private int apply_no;
    private int board_no;
    private int writer;
    private String title;
    private String content;
    private String img;
    private Date created_date;

}
