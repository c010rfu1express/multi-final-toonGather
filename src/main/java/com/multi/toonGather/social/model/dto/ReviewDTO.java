package com.multi.toonGather.social.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 *
 * @author : seoyun
 * @fileName : ReviewDTO
 * @since : 2024-07-23
 */

@Data
public class ReviewDTO {
    private Long reviewNo;
    private Long webtoonNo;
    private WriterDTO writer;
    private String title;
    private String content;
    private int starRating;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private int viewCnt;
}
