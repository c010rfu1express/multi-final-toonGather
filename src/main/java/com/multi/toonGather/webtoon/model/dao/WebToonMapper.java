package com.multi.toonGather.webtoon.model.dao;

import com.multi.toonGather.webtoon.model.CommentDTO;
import com.multi.toonGather.webtoon.model.WebtoonDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WebToonMapper {
    WebtoonDTO WebToonSelectOne(WebtoonDTO webtoonDTO);

    int increaseCount(WebtoonDTO webtoonDTO);

   List<CommentDTO> Commentlist(WebtoonDTO webtoonDTO);

    int insertComment(CommentDTO commentDTO);
}
