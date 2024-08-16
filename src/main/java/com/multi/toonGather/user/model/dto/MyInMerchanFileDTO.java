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
public class MyInMerchanFileDTO {
    private int merchanFileNo;
    private int merchanNo;
    private String fileName;
    private String filePath;
    private String fileType;
    private LocalDateTime uploadDate;
}
