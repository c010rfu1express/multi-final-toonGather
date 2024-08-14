package com.multi.toonGather.user.model.dto;

import com.multi.toonGather.introduction.model.dto.JournalFileDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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

    //조인을 피한 대신 MyService가 복잡해졌다.
    private List<JournalFileDTO> journalFiles;

    //시간 view에 뿌려줄 때
    public String getFormattedPostingDate() {
        if(postingDate == null) return "미정";
        return TimeAgoUtils.formatTimeAgo(postingDate);
    }


}
