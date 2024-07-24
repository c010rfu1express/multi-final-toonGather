package com.multi.toonGather.common.model.dto;

import lombok.Data;

@Data
public class PageDTO {
	private int start;
	private int end;
	private int page;

	public void setStartEnd(int page) {
		start = (page - 1) * 10;
		end = page * 10;
	}
	
}
