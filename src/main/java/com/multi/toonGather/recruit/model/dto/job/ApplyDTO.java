package com.multi.toonGather.recruit.model.dto.job;

import com.multi.toonGather.user.model.dto.UserDTO;
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
    private String contact;
    private Date created_date;
    private UserDTO member;
}
