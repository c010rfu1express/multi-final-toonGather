package com.multi.toonGather.introduction.model.mapper;

import com.multi.toonGather.introduction.model.dto.MerchanDTO;
import com.multi.toonGather.introduction.model.dto.MerchanFileDTO;
import com.multi.toonGather.introduction.model.dto.MerchanLikeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MerchanMapper {

    List<MerchanDTO> selectAllMerchans(@Param("offset") int offset, @Param("limit") int limit);

    List<MerchanFileDTO> selectFilesByMerchanNo(int merchanNo);

    int getTotalCount();

    void insertMerchan(MerchanDTO merchanDTO);

    void insertMerchanFile(MerchanFileDTO fileDTO);

    MerchanDTO selectMerchanByNo(int merchanNo);

    void deleteFiles(int merchanNo);

    void deleteMerchan(int merchanNo);

    int countLikesByMerchanNo(int merchanNo);

    boolean existsByMerchanNoAndUserNo(@Param("merchanNo") int merchanNo, @Param("userNo") int userNo);

    void deleteLike(@Param("merchanNo") int merchanNo, @Param("userNo") int userNo);

    void insertLike(MerchanLikeDTO like);

    void updateMerchan(MerchanDTO merchan);

    void deleteMerchanFileBySavedName(String savedName);

    int countMerchansByTitleKeyword(String keyword);

    List<MerchanDTO> selectMerchansByTitleKeyword(@Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);

    void deleteLikes(int merchanNo);
}
