package com.multi.toonGather.recruit.service.creator;


import com.multi.toonGather.recruit.model.dto.creator.CreatorDTO;
import com.multi.toonGather.recruit.model.mapper.CreatorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorServiceImpl implements CreatorService {
    private final CreatorMapper creatorMapper;


    @Override
    public void insertCreator(CreatorDTO creatorDTO) throws Exception {
        int result = creatorMapper.insertCreator(creatorDTO);
        if (result == 0) new Exception("창작자 등록 실패");
    }

    @Override
    public void updateMember(int member_no) throws Exception {
        int result = creatorMapper.updateMember(member_no);
        if (result > 0) new Exception(("회원 정보 수정 실패"));
    }

    @Override
    public void updateInfo(CreatorDTO creatorDTO) throws Exception {
        int result = creatorMapper.updateInfo(creatorDTO);
        if (result > 0) new Exception(("정보 등록 실패"));
    }

    @Override
    public CreatorDTO findInfo(int writer) throws Exception {
        CreatorDTO creatorDTO = creatorMapper.selectInfo(writer);
        if (creatorDTO != null) new Exception("정보 조회 실패");
        return creatorDTO;
    }
}
