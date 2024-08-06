package com.multi.toonGather.introduction.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JournalLikeDTO {
    private int likedNo;
    private int journalNo;
    private int userNo;
    private LocalDateTime likedDate;
}
