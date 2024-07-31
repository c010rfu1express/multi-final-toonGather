package com.multi.toonGather.introduction.model.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class JournalDTO {
    private int journalNo;
    private String title;
    private String content;
    private Date postingDate;

    //JournalDTO에 JournalFileDTO 리스트를 추가하여, 각 JournalDTO가 관련된 파일 목록을 가질 수 있게 합니다. 이를 통해, 템플릿에서 파일 경로에 쉽게 접근할 수 있습니다.
    private List<JournalFileDTO> journalFiles;
}
