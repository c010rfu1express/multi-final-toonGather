package com.multi.toonGather.webtoon.model.dto;

import lombok.Data;

@Data
public class WebtoonDTO {
    private int webtoon_no;
    private String webtoon_id;
    private int platform;
    private String webtoon_name;
    private String author;
    private String thumbnail_url;
    private String genre;
    private String tags;
    private int count;
    private int user_count;
    private String encodedWebtoonName;


}
