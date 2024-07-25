package com.multi.toonGather.introduction.model.dto;

import lombok.Data;

@Data
public class JournalFileDTO {
    private int journalFileNo;
    private int journalNo;
    private String fileName;
    private String filePath;
    private String fileType;
    private String uploadDate;
}
