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
public class MyInJournalDTO {
    // in_journal_like 테이블의 컬럼
    private int likedNo;
    private int userNo;
    private LocalDateTime likedDate;

    // in_journal 테이블의 컬럼
    private int journalNo;
    private String title;
    private String content;
    private LocalDateTime postingDate;

}
