package com.multi.toonGather.webtoon.service;

import com.multi.toonGather.webtoon.model.WebtoonDTO;

public interface WebToonService {
    WebtoonDTO WebToonSelectOne(WebtoonDTO webtoonDTO)throws Exception;

    boolean increaseCount(WebtoonDTO webtoonDTO)throws Exception;
}
