package com.multi.toonGather.introduction.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MerchanFileDTO {
    private int merchanFileNo;     // 첨부파일 번호
    private int merchanNo;         // 상품 게시글 번호
    private String fileName;       // 파일명
    private String filePath;       // 파일 경로
    private String fileType;       // 파일 타입 "thumbnail" or "detail"
    private LocalDateTime uploadDate; // 업로드 일자
}
