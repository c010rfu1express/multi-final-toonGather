package com.multi.toonGather.common.model.dto;

import lombok.Data;

@Data
public class PageNDTO {
    private int start;
    private int end;
    private int page;

    public void setStartEnd(int page, int N) {
        start = (page - 1) * N;
        end = page * N;
    }
}
