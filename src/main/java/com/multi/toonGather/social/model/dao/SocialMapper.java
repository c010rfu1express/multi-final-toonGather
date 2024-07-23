package com.multi.toonGather.social.model.dao;

import com.multi.toonGather.social.model.dto.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 *
 * @author : seoyun
 * @fileName : SocialMapper
 * @since : 2024-07-23
 */

@Mapper
public interface SocialMapper {
    void createReview(ReviewDTO review);
    List<ReviewDTO> getReviewsByUserId(Long userId);
}
