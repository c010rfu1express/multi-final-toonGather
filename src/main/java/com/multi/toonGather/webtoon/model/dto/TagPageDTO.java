package com.multi.toonGather.webtoon.model.dto;

import lombok.Data;

@Data
public class TagPageDTO {
	private int start;
	private int end;
	private int page;
	private String Genre1;
	private String Genre2;
	private String tag1;
	private String tag2;
	private String tag3;
	private String tag4;
	private String platform;

	public void setStartEndTag(int page) {
		start = (page - 1) * 50;
		end = 50;
	}
	
}
