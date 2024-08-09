package com.multi.toonGather.webtoon.model.dao;

import com.multi.toonGather.webtoon.model.dto.*;
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

    List<WebtoonDTO> searchWebtoon(TagPageDTO tagPageDTO);

    WtUserSaveDTO WebToonSelectSave(WtUserSaveDTO wtUserSaveDTO);

    int insertSave(WtUserSaveDTO wtUserSaveDTO);

    int deleteSave(WtUserSaveDTO wtUserSaveDTO);

    int countWebtoon(TagPageDTO tagPageDTO);

    List<CommentLikeDTO> commentLikeList(CommentLikeDTO commentLikeDTO);

 int likeInsert(CommentLikeDTO commentLikeDTO);

 int likeUpdateUP(CommentLikeDTO commentLikeDTO);

 int likeDelet(CommentLikeDTO commentLikeDTO);

 int likeUpdateDown(CommentLikeDTO commentLikeDTO);

 int dislikeInsert(CommentLikeDTO commentLikeDTO);

 int dislikeUpdateUP(CommentLikeDTO commentLikeDTO);


 int dislikeUpdateDown(CommentLikeDTO commentLikeDTO);

 List<CommentDTO> CommentBestList(WebtoonDTO webtoonDTO);

 List<WebtoonDTO> webtoonBest();
}
