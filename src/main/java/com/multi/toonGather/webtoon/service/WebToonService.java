package com.multi.toonGather.webtoon.service;

import com.multi.toonGather.webtoon.model.dto.CommentDTO;
import com.multi.toonGather.webtoon.model.dto.WebtoonDTO;
import com.multi.toonGather.webtoon.model.dto.WtUserLogDTO;

import java.util.List;

public interface WebToonService {
    WebtoonDTO WebToonSelectOne(WebtoonDTO webtoonDTO)throws Exception;

    boolean increaseCount(WebtoonDTO webtoonDTO)throws Exception;

    List<CommentDTO> Commentlist(WebtoonDTO webtoonDTO)throws Exception;

    int insertComment(CommentDTO commentDTO)throws Exception;

    int updateComment(CommentDTO commentDTO)throws Exception;

    int deleteComment(CommentDTO commentNo)throws Exception;

    WtUserLogDTO selrctLog(WtUserLogDTO dto)throws Exception;

    int insertLog(WtUserLogDTO wtUserLogDTO);

    int updateLog(WtUserLogDTO wtUserLogDTO);
}
