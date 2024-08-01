package com.multi.toonGather.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyInEventDTO {
    private int likedNo;
    private int userNo;
    private LocalDateTime likedDate;

    private int eventNo;
    private String title;
    private int eventCategoryCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private int cost;
    private String site;
    private String place;
    private String address;
    private String coordinates;
    private String content;
    private LocalDateTime postingDate;
}
