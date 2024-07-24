package com.multi.toonGather.webtoon.model.dao;

import com.multi.toonGather.webtoon.model.WebtoonDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WebToonMapper {
    WebtoonDTO WebToonSelectOne(WebtoonDTO webtoonDTO);

    int increaseCount(WebtoonDTO webtoonDTO);
}
