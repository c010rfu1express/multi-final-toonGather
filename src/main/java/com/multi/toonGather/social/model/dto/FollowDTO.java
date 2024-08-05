package com.multi.toonGather.social.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Please explain the class!!
 *
 * @author : seoyun
 * @fileName : FollowDTO
 * @since : 2024-08-03
 */
@Data
public class FollowDTO {
    private int followNo;
    private int followerNo;
    private int followingNo;
    private LocalDateTime followedDate;
}
