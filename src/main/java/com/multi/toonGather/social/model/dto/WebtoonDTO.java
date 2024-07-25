package com.multi.toonGather.social.model.dto;

import lombok.Data;

/**
 * Please explain the class!!
 *
 * @author : seoyun
 * @fileName : WebtoonDTO
 * @since : 2024-07-24
 */
@Data
public class WebtoonDTO {
    private int webtoonNo;
    private String webtoonId;
    private int platform;
    private String webtoonName;
    private String author;
    private String thumbnailUrl;
    private String genre;
    private String tags;
    private int count;
}
