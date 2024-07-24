package com.multi.toonGather.social.service;

import com.multi.toonGather.social.model.dao.SocialMapper;
import com.multi.toonGather.social.model.dto.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Please explain the class!!
 *
 * @author : seoyun
 * @fileName : SocialServiceImpl
 * @since : 2024-07-23
 */

@Service
public class SocialServiceImpl implements SocialService {

    private final SocialMapper socialMapper;

    @Autowired
    public SocialServiceImpl(SocialMapper socialMapper) {
        this.socialMapper = socialMapper;
    }

    @Override
    public void createReview(ReviewDTO review) {
        socialMapper.createReview(review);
    }

    @Override
    public List<ReviewDTO> getReviewsByUserId(Long userId) {
        return socialMapper.getReviewsByUserId(userId);
    }
}
