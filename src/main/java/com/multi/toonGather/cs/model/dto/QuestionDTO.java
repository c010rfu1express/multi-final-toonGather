package com.multi.toonGather.cs.model.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class QuestionDTO {
    private int csQNo;
    private String csQTitle;
    private String csQContent;
    private int csQWriterNo;
    private String nickname;
    private CsCategoryDTO csQCategory;
    private CsStatusDTO csQStatus;
    private int csQViewCount;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
}
