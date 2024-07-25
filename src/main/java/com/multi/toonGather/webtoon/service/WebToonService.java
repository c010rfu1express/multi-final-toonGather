package com.multi.toonGather.webtoon.service;

import com.multi.toonGather.webtoon.model.CommentDTO;
import com.multi.toonGather.webtoon.model.WebtoonDTO;

import java.util.List;

public interface WebToonService {
    WebtoonDTO WebToonSelectOne(WebtoonDTO webtoonDTO)throws Exception;

    boolean increaseCount(WebtoonDTO webtoonDTO)throws Exception;

    List<CommentDTO> Commentlist(WebtoonDTO webtoonDTO)throws Exception;
}
