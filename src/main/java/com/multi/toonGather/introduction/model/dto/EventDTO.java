package com.multi.toonGather.introduction.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventDTO {
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

    // EventDTO에 EventFileDTO 리스트를 추가하여, 각 EventDTO가 관련된 파일 목록을 가질 수 있게 합니다.
    private List<EventFileDTO> eventFiles;
}