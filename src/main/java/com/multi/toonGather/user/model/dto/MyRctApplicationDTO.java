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
public class MyRctApplicationDTO {
    private int applyNo;
    private int boardNo;
    private int writer;
    private String applyTitle;
    private String applyContent;
    private String applyImg;
    private LocalDateTime applyCreatedDate;
    private boolean applyCheckStatus;

    private int jobWriter;
    private String jobTitle;
    private String jobContent;
    private String jobLocation;
    private String jobImg;
    private LocalDateTime jobCreatedDate;
    private LocalDateTime jobModifiedDate;
    private LocalDateTime jobDeadLine;

    //조인2하여 가져올 것 (from users)
    private String applyUserId;//

    //시간 view에 뿌려줄 때
    public String getFormattedApplyCreatedDate() {
        if(applyCreatedDate == null) return "미정";
        return TimeAgoUtils.formatTimeAgo(applyCreatedDate);
    }
}
