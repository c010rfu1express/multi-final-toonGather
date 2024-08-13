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
public class MyCsQuestionDTO {
    private int csQNo;
    private String csQTitle;
    private String csQContent;
    private int csQWriterNo;
    private String csQCategoryCode;
    private String csQStatusCode;
    private int csQViewCount;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    //시간 view에 뿌려줄 때
    public String getFormattedCreatedDate() {
        return TimeAgoUtils.formatTimeAgo(createdDate);
    }
}
