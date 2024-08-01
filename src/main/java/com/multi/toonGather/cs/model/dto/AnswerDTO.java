package com.multi.toonGather.cs.model.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AnswerDTO {
    private int csANo;
    private int csQNo;
    private String csAContent;
    private int csAWriterNo;
    private String nickname;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
}
