package com.multi.toonGather.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyRctCreatorDTO {
    private int creatorNo;
    private int memberNo;
    private String content;
    private String img;
    private String registNo;
    private char status;
    private char typeCode;

}
