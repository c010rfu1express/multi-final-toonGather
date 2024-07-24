package com.multi.toonGather.recruit.model.dto.job;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class JobDTO {
    private int board_no;
    private int writer;
    private String title;
    private String content;
    private String location;
    private String img;
    private Date created_date;
    private Date modified_date;
    private LocalDate dead_line;
//    private MemberDTO memNo;

}
