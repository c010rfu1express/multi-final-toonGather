package com.multi.toonGather.webtoon.model.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class WtUserLogDTO {
    private int logNo;
    private int webtoonNo;
    private int userNo;
    private int count;
    private Timestamp createdAt;
}
