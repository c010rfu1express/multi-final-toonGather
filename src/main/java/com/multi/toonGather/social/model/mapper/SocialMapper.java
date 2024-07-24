package com.multi.toonGather.social.model.mapper;

import com.multi.toonGather.social.model.dto.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Please explain the class!!
 *
 * @author : seoyun
 * @fileName : SocialMapper
 * @since : 2024-07-24
 */
@Mapper
public interface SocialMapper {
    void createReview(ReviewDTO review);
    List<ReviewDTO> getReviewsByUserId(Long userId);
}
