package com.multi.toonGather.webtoon.service;

import com.multi.toonGather.webtoon.model.WebtoonDTO;
import com.multi.toonGather.webtoon.model.dao.WebToonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
