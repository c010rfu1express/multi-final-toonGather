package com.multi.toonGather.webtoon.model;

public class WebtoonDTO {
    String id;
    String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "WebtoonDTO{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
