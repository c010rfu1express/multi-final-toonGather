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
public class MyRctJobDTO {
    private int boardNo;
    private int writer;
    private String title;
    private String content;
    private String location;
    private String img;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private LocalDateTime deadLine;

    //시간 view에 뿌려줄 때
    public String getFormattedDeadLine() {
        if(deadLine == null) return "미정";
        return TimeAgoUtils.formatTimeAgo(deadLine);
    }

}
