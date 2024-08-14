package com.multi.toonGather.introduction.service;

import com.multi.toonGather.introduction.model.dto.MerchanDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MerchanService {

    int countMerchansByTitleKeyword(String keyword);

    List<MerchanDTO> searchMerchansByTitle(String keyword, int offset, int limit);

    int getTotalCount();

    List<MerchanDTO> getAllMerchansWithFiles(int offset, int pageSize);

    boolean insertMerchan(MerchanDTO merchanDTO, MultipartFile[] images, MultipartFile[] detailImages, HttpServletRequest request) throws Exception;

    MerchanDTO getMerchanByNoWithFiles(int merchanNo);

    void deleteMerchanByNo(Integer merchanNo) throws Exception;

    int countLikesByMerchanNo(int merchanNo);

    boolean toggleLike(int merchanNo, int userNo);

    boolean updateMerchan(MerchanDTO merchan, List<String> existingImages, List<String> removedImages, MultipartFile[] images, List<String> existingDetailImages, List<String> removedDetailImages, MultipartFile[] detailImages, HttpServletRequest request) throws Exception;
}
