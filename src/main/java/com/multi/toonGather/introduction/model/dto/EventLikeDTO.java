package com.multi.toonGather.introduction.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventLikeDTO {
    private int likedNo;
    private int eventNo;
    private int userNo;
    private LocalDateTime likedDate;
}