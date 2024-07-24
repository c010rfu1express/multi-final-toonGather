package com.multi.toonGather.webtoon.model;

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

    // 생성자
    public WebtoonDTO() {
    }

    // Getter 및 Setter 메서드
    public int getWebtoonNo() {
        return webtoonNo;
    }

    public void setWebtoonNo(int webtoonNo) {
        this.webtoonNo = webtoonNo;
    }

    public String getWebtoonId() {
        return webtoonId;
    }

    public void setWebtoonId(String webtoonId) {
        this.webtoonId = webtoonId;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getWebtoonName() {
        return webtoonName;
    }

    public void setWebtoonName(String webtoonName) {
        this.webtoonName = webtoonName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    // toString 메서드 (선택 사항)
    @Override
    public String toString() {
        return "WebtoonDTO{" +
                "webtoonNo=" + webtoonNo +
                ", webtoonId='" + webtoonId + '\'' +
                ", platform=" + platform +
                ", webtoonName='" + webtoonName + '\'' +
                ", author='" + author + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", genre='" + genre + '\'' +
                ", tags='" + tags + '\'' +
                ", count=" + count +
                '}';
    }
}
