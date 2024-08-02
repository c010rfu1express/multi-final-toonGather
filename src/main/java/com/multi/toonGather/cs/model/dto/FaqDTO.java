package com.multi.toonGather.cs.model.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class FaqDTO {
    private int csFaqNo;
    private String csFaqTitle;
    private String csFaqContent;
    private int csFaqWriterNo;
    private String nickname;
    private int csFaqViewCount;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
}
