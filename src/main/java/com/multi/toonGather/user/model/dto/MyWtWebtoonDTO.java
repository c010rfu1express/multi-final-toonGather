package com.multi.toonGather.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyWtWebtoonDTO {
    private int saveNo;
    private int webtoonNo;
    private int userNo;

    private String webtoonId;
    private int platform;
    private String webtoonName;
    private String author;
    private String thumbnailUrl;
    private String genre;
    private String tags;
    private int count;

    public String getFirstHashtag() {
        if (tags == null || tags.isEmpty()) {
            return "";
        }

        // Find the start and end of the first hashtag
        int startIndex = tags.indexOf('#'); // The index of the first #
        if (startIndex == -1) {
            return "";
        }

        int endIndex = tags.indexOf('#', startIndex + 1); // The index of the second #
        if (endIndex == -1) {
            endIndex = tags.length(); // No second #, so take the rest of the string
        }

        return tags.substring(startIndex, endIndex);
    }
}
