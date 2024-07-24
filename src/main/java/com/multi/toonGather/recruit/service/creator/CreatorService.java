package com.multi.toonGather.recruit.service.creator;


import com.multi.toonGather.recruit.model.dto.creator.CreatorDTO;

public interface CreatorService {

    void insertCreator(CreatorDTO creatorDTO) throws Exception;

    void updateMember(int member_no) throws Exception;
}
