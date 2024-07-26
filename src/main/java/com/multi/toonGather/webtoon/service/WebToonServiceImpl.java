package com.multi.toonGather.webtoon.service;

import com.multi.toonGather.webtoon.model.CommentDTO;
import com.multi.toonGather.webtoon.model.WebtoonDTO;
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


}
