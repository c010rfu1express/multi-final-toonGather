package com.multi.toonGather.social.service;

import com.multi.toonGather.social.model.dto.ReviewDTO;

import java.util.List;

/**
 * Please explain the class!!
 *
 * @author : seoyun
 * @fileName : SocialService
 * @since : 2024-07-24
 */
public interface SocialService {
    void createReview(ReviewDTO review);
    List<ReviewDTO> getReviewsByUserId(Long userId);
}
