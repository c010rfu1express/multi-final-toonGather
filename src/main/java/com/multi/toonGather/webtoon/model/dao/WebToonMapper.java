package com.multi.toonGather.webtoon.model.dao;

import com.multi.toonGather.webtoon.model.dto.CommentDTO;
import com.multi.toonGather.webtoon.model.dto.WebtoonDTO;
import com.multi.toonGather.webtoon.model.dto.WtUserLogDTO;
import com.multi.toonGather.webtoon.model.dto.WtUserSaveDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WebToonMapper {
    WebtoonDTO WebToonSelectOne(WebtoonDTO webtoonDTO);

    int increaseCount(WebtoonDTO webtoonDTO);

   List<CommentDTO> Commentlist(WebtoonDTO webtoonDTO);

    int insertComment(CommentDTO commentDTO);

    int updateComment(CommentDTO commentDTO);

    int deleteComment(CommentDTO commentDTO);

    WtUserLogDTO selrctLog(WtUserLogDTO dto);

    int insertLog(WtUserLogDTO wtUserLogDTO);

    int updateLog(WtUserLogDTO wtUserLogDTO);

    int webToonInsert(WebtoonDTO webtoonDTO);

    List<WebtoonDTO> searchWebtoon(WebtoonDTO webtoonDTO);

    WtUserSaveDTO WebToonSelectSave(WtUserSaveDTO wtUserSaveDTO);

    int insertSave(WtUserSaveDTO wtUserSaveDTO);

    int deleteSave(WtUserSaveDTO wtUserSaveDTO);
}
