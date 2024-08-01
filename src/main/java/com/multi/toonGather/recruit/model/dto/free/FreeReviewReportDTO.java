package com.multi.toonGather.recruit.model.dto.free;

import com.multi.toonGather.user.model.dto.UserDTO;
import lombok.Data;

import java.util.Date;

@Data
public class FreeReviewReportDTO {
    private int report_no;
    private int review_no;
    private int reporter;
    private String content;
    private Date reported_date;
    private UserDTO member;

}
