package com.multi.toonGather.webtoon.service;

import com.multi.toonGather.webtoon.model.dto.*;

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

    int webToonInsert(WebtoonDTO webtoonDTO);

    List<WebtoonDTO> searchWebtoon(TagPageDTO webtoonDTO);

    WtUserSaveDTO WebToonSelectSave(WtUserSaveDTO wtUserSaveDTO)throws Exception;

    int insertSave(WtUserSaveDTO wtUserSaveDTO)throws Exception;

    int deleteSave(WtUserSaveDTO wtUserSaveDTO)throws Exception;

    int countWebtoon(TagPageDTO tagPageDTO);

    List<CommentLikeDTO> commentLikeList(CommentLikeDTO commentLikeDTO);

    int likeInsert(CommentLikeDTO commentLikeDTO);

    int likeDelete(CommentLikeDTO commentLikeDTO);

    int dislikeInsert(CommentLikeDTO commentLikeDTO);

    int dislikeDelete(CommentLikeDTO commentLikeDTO);

    List<CommentDTO> CommentBestList(WebtoonDTO webtoonDTO);
}
