package com.multi.toonGather.introduction.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventFileDTO {
    private int eventFileNo;
    private int eventNo;
    private String fileName;
    private String filePath;
    private String fileType;
    private LocalDateTime uploadDate;
}
