package com.multi.toonGather.recruit.model.dto.free;

import com.multi.toonGather.user.model.dto.UserDTO;
import lombok.Data;

import java.util.Date;

@Data
public class FreeReviewDTO {
    private int review_no;
    private int writer;
    private int board_no;
    private String content;
    private int star_rating;
    private Date created_date;
    private UserDTO member;

}
