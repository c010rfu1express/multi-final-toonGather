package com.multi.toonGather.webtoon.model.dto;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class CommentDTO {

    private int commentNo;
    private int webtoonNo;
    private int userNo;
    private String content;
    private int liked;
    private int dislike;
    private String nickname;
    private int likeCode;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
