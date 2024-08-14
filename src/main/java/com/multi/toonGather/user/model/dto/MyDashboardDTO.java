package com.multi.toonGather.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyDashboardDTO {
    private int userNo;
    private int wtWebtoon;
    private int wtComment;
    private int soReview;
    private int soDiary;
    private int rctJob;
    private int rctFree;
    private int inJournal;
    private int inEvent;
    private int inMerchan;
    private int Cs;
}
