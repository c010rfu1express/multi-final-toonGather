package com.multi.toonGather.introduction.service;

import com.multi.toonGather.introduction.model.dto.MerchanDTO;
import com.multi.toonGather.introduction.model.dto.MerchanFileDTO;
import com.multi.toonGather.introduction.model.dto.MerchanLikeDTO;
import com.multi.toonGather.introduction.model.mapper.MerchanMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class MerchanServiceImpl implements MerchanService {

    @Autowired
    private MerchanMapper merchanMapper;

    static String projectRootPath = System.getProperty("user.dir");
    private static final String UPLOAD_DIR = Paths.get(projectRootPath, "src", "main", "webapp", "uploadFiles", "introduction").toString();

    @Override
    public int countMerchansByTitleKeyword(String keyword) {
        return merchanMapper.countMerchansByTitleKeyword("%" + keyword + "%");
    }

    @Override
    public List<MerchanDTO> searchMerchansByTitle(String keyword, int offset, int limit) {
        List<MerchanDTO> merchans = merchanMapper.selectMerchansByTitleKeyword("%" + keyword + "%", offset, limit);

        // 각 상품에 대한 첨부파일 가져오기
        for (MerchanDTO merchan : merchans) {
            List<MerchanFileDTO> merchanFiles = merchanMapper.selectFilesByMerchanNo(merchan.getMerchanNo());
            merchan.setMerchanFiles(merchanFiles); // MerchanDTO에 파일 리스트 설정
        }

        return merchans;
    }

    @Override
    public int getTotalCount() {
        return merchanMapper.getTotalCount();
    }

    @Override
    public List<MerchanDTO> getAllMerchansWithFiles(int offset, int pageSize) {
        List<MerchanDTO> merchans = merchanMapper.selectAllMerchans(offset, pageSize);

        // 각 상품에 대한 첨부파일 가져오기
        for (MerchanDTO merchan : merchans) {
            List<MerchanFileDTO> merchanFiles = merchanMapper.selectFilesByMerchanNo(merchan.getMerchanNo());
            merchan.setMerchanFiles(merchanFiles); // MerchanDTO에 파일 리스트 설정
        }

        return merchans;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertMerchan(MerchanDTO merchanDTO, MultipartFile[] images, MultipartFile[] detailImages, HttpServletRequest request) throws Exception {
        try {
            // 1. 상품 정보 DB에 삽입 & merchanNo 가져오기
            merchanMapper.insertMerchan(merchanDTO);

            String root = request.getSession().getServletContext().getRealPath("/");
            String filePath = root + "/uploadFiles";

            File mkdir = new File(filePath);
            if (!mkdir.exists()) {
                mkdir.mkdirs();
            }

            for (MultipartFile image : images) {
                String originName = image.getOriginalFilename();
                if (originName != null && !originName.isEmpty()) {
                    String ext = originName.substring(originName.lastIndexOf("."));
                    String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                    // 파일 저장
                    try {
                        image.transferTo(new File(filePath + "/" + savedName));
                        MerchanFileDTO fileDTO = new MerchanFileDTO();
                        fileDTO.setMerchanNo(merchanDTO.getMerchanNo());
                        fileDTO.setFileName(savedName);
                        fileDTO.setFilePath(filePath);
                        fileDTO.setFileType(image.getContentType());

                        // Insert the file information into the database
                        merchanMapper.insertMerchanFile(fileDTO);
                    } catch (IOException e) {
                        new File(filePath + "/" + savedName).delete();
                        throw new Exception("File upload failed, rolling back transaction.", e); // 예외를 던져 트랜잭션을 롤백함
                    }
                }
            }
            for (MultipartFile image : detailImages) {
                String originName = image.getOriginalFilename();
                if (originName != null && !originName.isEmpty()) {
                    String ext = originName.substring(originName.lastIndexOf("."));
                    String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                    // 파일 저장
                    try {
                        image.transferTo(new File(filePath + "/" + savedName));
                        MerchanFileDTO fileDTO = new MerchanFileDTO();
                        fileDTO.setMerchanNo(merchanDTO.getMerchanNo());
                        fileDTO.setFileName(savedName);
                        fileDTO.setFilePath(filePath);
                        fileDTO.setFileType("detail");

                        // Insert the file information into the database
                        merchanMapper.insertMerchanFile(fileDTO);
                    } catch (IOException e) {
                        new File(filePath + "/" + savedName).delete();
                        throw new Exception("File upload failed, rolling back transaction.", e); // 예외를 던져 트랜잭션을 롤백함
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // 트랜잭션 롤백을 위해 예외를 다시 던짐
        }
    }

    @Override
    public MerchanDTO getMerchanByNoWithFiles(int merchanNo) {
        MerchanDTO merchan = merchanMapper.selectMerchanByNo(merchanNo);

        if (merchan != null) {
            List<MerchanFileDTO> merchanFiles = merchanMapper.selectFilesByMerchanNo(merchan.getMerchanNo());
            merchan.setMerchanFiles(merchanFiles); // MerchanDTO에 파일 리스트 설정
        }

        return merchan;
    }

    @Override
    public void deleteMerchanByNo(Integer merchanNo) throws Exception {
        try {
            // merchanNo으로 게시글 찾기
            MerchanDTO merchan = merchanMapper.selectMerchanByNo(merchanNo);
            if (merchan != null) {
                // 파일 삭제
                List<MerchanFileDTO> files = merchanMapper.selectFilesByMerchanNo(merchan.getMerchanNo());
                String filePath = UPLOAD_DIR;
                for (MerchanFileDTO file : files) {
                    File existingFile = new File(filePath + "/" + file.getFileName());
                    if (existingFile.exists()) {
                        existingFile.delete();
                    }
                }
                // 파일 정보 삭제
                merchanMapper.deleteFiles(merchan.getMerchanNo());
                // 좋아요 정보 삭제
                merchanMapper.deleteLikes(merchan.getMerchanNo());
                // 게시글 삭제
                merchanMapper.deleteMerchan(merchan.getMerchanNo());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to delete merchan", e);
        }
    }

    @Override
    public int countLikesByMerchanNo(int merchanNo) {
        return merchanMapper.countLikesByMerchanNo(merchanNo);
    }

    @Override
    public boolean toggleLike(int merchanNo, int userNo) {
        boolean exists = merchanMapper.existsByMerchanNoAndUserNo(merchanNo, userNo);
        if (exists) {
            merchanMapper.deleteLike(merchanNo, userNo);
            return false; // 좋아요 취소
        } else {
            MerchanLikeDTO like = new MerchanLikeDTO();
            like.setMerchanNo(merchanNo);
            like.setUserNo(userNo);
            merchanMapper.insertLike(like);
            return true; // 좋아요 추가
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateMerchan(MerchanDTO merchan, List<String> existingImages, List<String> removedImages, MultipartFile[] images, List<String> existingDetailImages, List<String> removedDetailImages, MultipartFile[] detailImages, HttpServletRequest request) throws Exception {
        try {
            // 상품 업데이트
            merchanMapper.updateMerchan(merchan);

            String root = request.getSession().getServletContext().getRealPath("/");
            String filePath = root + "/uploadFiles";

            // 기존 이미지 삭제 처리 (썸네일)
            if (removedImages != null && !removedImages.isEmpty()) {
                for (String savedName : removedImages) {
                    File fileToDelete = new File(filePath + "/" + savedName);
                    if (fileToDelete.exists()) {
                        fileToDelete.delete();
                    }
                    merchanMapper.deleteMerchanFileBySavedName(savedName);
                }
            }

            // 기존 이미지 삭제 처리 (상세정보)
            if (removedDetailImages != null && !removedDetailImages.isEmpty()) {
                for (String savedName : removedDetailImages) {
                    File fileToDelete = new File(filePath + "/" + savedName);
                    if (fileToDelete.exists()) {
                        fileToDelete.delete();
                    }
                    merchanMapper.deleteMerchanFileBySavedName(savedName);
                }
            }

            // 새로운 이미지 저장 (썸네일)
            if (images != null && images.length > 0) {
                for (MultipartFile image : images) {
                    String originName = image.getOriginalFilename();
                    if (originName != null && !originName.isEmpty()) {
                        String ext = originName.substring(originName.lastIndexOf("."));
                        String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                        // 파일 저장
                        try {
                            image.transferTo(new File(filePath + "/" + savedName));
                        } catch (IOException e) {
                            new File(filePath + "/" + savedName).delete();
                            throw new Exception("File upload error", e);
                        }

                        MerchanFileDTO fileDTO = new MerchanFileDTO();
                        fileDTO.setMerchanNo(merchan.getMerchanNo());
                        fileDTO.setFileName(savedName);
                        fileDTO.setFilePath(filePath);
                        fileDTO.setFileType(image.getContentType());

                        merchanMapper.insertMerchanFile(fileDTO);
                    }
                }
            }
            // 새로운 이미지 저장 (상세정보)
            if (detailImages != null && detailImages.length > 0) {
                for (MultipartFile image : detailImages) {
                    String originName = image.getOriginalFilename();
                    if (originName != null && !originName.isEmpty()) {
                        String ext = originName.substring(originName.lastIndexOf("."));
                        String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                        // 파일 저장
                        try {
                            image.transferTo(new File(filePath + "/" + savedName));
                        } catch (IOException e) {
                            new File(filePath + "/" + savedName).delete();
                            throw new Exception("File upload error", e);
                        }

                        MerchanFileDTO fileDTO = new MerchanFileDTO();
                        fileDTO.setMerchanNo(merchan.getMerchanNo());
                        fileDTO.setFileName(savedName);
                        fileDTO.setFilePath(filePath);
                        fileDTO.setFileType("detail");

                        merchanMapper.insertMerchanFile(fileDTO);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            throw new Exception("Update merchan failed", e);
        }
    }
}
