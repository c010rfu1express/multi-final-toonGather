package com.multi.toonGather.webtoon.service;

import com.multi.toonGather.webtoon.model.dto.*;
import com.multi.toonGather.webtoon.model.dao.WebToonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebToonServiceImpl implements WebToonService{

    @Autowired
    WebToonMapper webToonMapper;

    @Override
    public WebtoonDTO WebToonSelectOne(WebtoonDTO webtoonDTO) throws Exception {
        webtoonDTO=webToonMapper.WebToonSelectOne(webtoonDTO);
        return webtoonDTO;
    }

    @Override
    public boolean increaseCount(WebtoonDTO webtoonDTO) throws Exception {
        int result=webToonMapper.increaseCount(webtoonDTO);
        if(result>0){
            return true;
        }
        return false;
    }

    @Override
    public List<CommentDTO> Commentlist(WebtoonDTO webtoonDTO) throws Exception {
        List<CommentDTO> list=webToonMapper.Commentlist(webtoonDTO);
        return list;
    }

    @Override
    public int insertComment(CommentDTO commentDTO) throws Exception {
        int result=webToonMapper.insertComment(commentDTO);
        return result;
    }

    @Override
    public int updateComment(CommentDTO commentDTO) throws Exception {
        int result=webToonMapper.updateComment(commentDTO);
        return result;
    }

    @Override
    public int deleteComment(CommentDTO commentDTO) throws Exception{
        int result=webToonMapper.deleteComment(commentDTO);
        return result;
    }

    @Override
    public WtUserLogDTO selrctLog(WtUserLogDTO dto) throws Exception {
        WtUserLogDTO wtUserLogDTO=webToonMapper.selrctLog(dto);
        return wtUserLogDTO;
    }

    @Override
    public int insertLog(WtUserLogDTO wtUserLogDTO) {
        int result=webToonMapper.insertLog(wtUserLogDTO);
        return result;
    }

    @Override
    public int updateLog(WtUserLogDTO wtUserLogDTO) {
        int result=webToonMapper.updateLog(wtUserLogDTO);
        return result;
    }

    @Override
    public int webToonInsert(WebtoonDTO webtoonDTO) {
        int result=webToonMapper.webToonInsert(webtoonDTO);
        return result;
    }

    @Override
    public List<WebtoonDTO> searchWebtoon(TagPageDTO tagPageDTO) {
        List<WebtoonDTO> webtoonDTOS=webToonMapper.searchWebtoon(tagPageDTO);
        return webtoonDTOS;
    }

    @Override
    public WtUserSaveDTO WebToonSelectSave(WtUserSaveDTO wtUserSaveDTO) throws Exception {
        wtUserSaveDTO=webToonMapper.WebToonSelectSave(wtUserSaveDTO);
        return wtUserSaveDTO;
    }

    @Override
    public int insertSave(WtUserSaveDTO wtUserSaveDTO) throws Exception {

        int result=webToonMapper.insertSave(wtUserSaveDTO);
        return result;
    }

    @Override
    public int deleteSave(WtUserSaveDTO wtUserSaveDTO) throws Exception {
        int result=webToonMapper.deleteSave(wtUserSaveDTO);
        return result;
    }

    @Override
    public int countWebtoon(TagPageDTO tagPageDTO) {
        int count=webToonMapper.countWebtoon(tagPageDTO);
        return count;
    }

    @Override
    public List<CommentLikeDTO> commentLikeList(CommentLikeDTO commentLikeDTO) {
        List<CommentLikeDTO>list=webToonMapper.commentLikeList(commentLikeDTO);
        return list;
    }

    @Override
    public int likeInsert(CommentLikeDTO commentLikeDTO) {
        int result=webToonMapper.likeInsert(commentLikeDTO);
        if(result>0){
            System.out.println();
            result=webToonMapper.likeUpdateUP(commentLikeDTO);
        }
        return result;
    }

    @Override
    public int likeDelete(CommentLikeDTO commentLikeDTO) {
        int result=webToonMapper.likeDelet(commentLikeDTO);
        if(result>0){
            System.out.println();
            result=webToonMapper.likeUpdateDown(commentLikeDTO);
        }
        return result;
    }

    @Override
    public int dislikeInsert(CommentLikeDTO commentLikeDTO) {
        int result=webToonMapper.dislikeInsert(commentLikeDTO);
        if(result>0){
            System.out.println();
            result=webToonMapper.dislikeUpdateUP(commentLikeDTO);
        }
        return result;
    }

    @Override
    public int dislikeDelete(CommentLikeDTO commentLikeDTO) {
        int result=webToonMapper.likeDelet(commentLikeDTO);
        if(result>0){
            System.out.println();
            result=webToonMapper.dislikeUpdateDown(commentLikeDTO);
        }
        return result;
    }


}
