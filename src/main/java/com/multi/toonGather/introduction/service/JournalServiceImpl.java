package com.multi.toonGather.introduction.service;

import com.multi.toonGather.introduction.model.dto.*;
import com.multi.toonGather.introduction.model.mapper.JournalMapper;
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
public class JournalServiceImpl implements JournalService {
    @Autowired
    private JournalMapper journalMapper;

//    private static final String UPLOAD_DIR = "C:/workspace/multi/imgJournal/";
    static String projectRootPath = System.getProperty("user.dir");
    private static final String UPLOAD_DIR = Paths.get(projectRootPath, "src", "main", "webapp", "uploadFiles", "introduction").toString();

    @Override
    public JournalDTO getJournal(int journalNo) {
        return journalMapper.selectJournalByNo(journalNo);
    }

    @Override
    public List<JournalFileDTO> getJournalFiles(int journalNo) {
        return journalMapper.selectFilesByJournalNo(journalNo);
    }


    @Override
    public JournalDTO getJournalByTitle(String title) {
        return journalMapper.selectJournalByTitle(title);
    }

    @Override
    @Transactional
    public void insertJournal(String title, String content, MultipartFile file, HttpServletRequest request) throws Exception {
        JournalDTO journalDTO = new JournalDTO();
        journalDTO.setTitle(title);
        journalDTO.setContent(content);

        // Insert the journal and get the generated ID
        journalMapper.insertJournal(journalDTO);
        JournalFileDTO fileDTO = new JournalFileDTO();
        // Now the journalDTO should have the generated journalNo
        int journalNo = journalDTO.getJournalNo();

        String root = request.getSession().getServletContext().getRealPath("/");
        System.out.println("root : " + root);
        String filePath = root + "/uploadFiles";

        File mkdir = new File(filePath);
        if (!mkdir.exists()) {
            mkdir.mkdirs();
        }

        String originName = file.getOriginalFilename();
        if (originName != null && !originName.isEmpty()) {
            String ext = originName.substring(originName.lastIndexOf("."));
            String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

            // 파일 저장
            try {
                file.transferTo(new File(filePath + "/" + savedName));

                fileDTO.setJournalNo(journalNo);
                fileDTO.setFileName(savedName);
            } catch (IOException e) {
                System.out.println("File upload error : " + e);
                new File(filePath + "/" + savedName).delete();
                throw new Exception("File upload failed, rolling back transaction.", e); // 예외를 던져 트랜잭션을 롤백함
            }
        } // if origin not null

        fileDTO.setFilePath(filePath);
        fileDTO.setFileType(file.getContentType());
        System.out.println("journalfiledto 테스트 :" + fileDTO.toString());

        journalMapper.insertJournalFile(fileDTO);

    }

//    public List<JournalDTO> getAllJournalsWithFiles() {
//        // 모든 소식 가져오기
//        List<JournalDTO> journals = journalMapper.selectAllJournals();
////        System.out.println("journals 확인" + journals[0].toString());
////        System.out.println("journals 확인" + journals[0].journalFiles[0].toString());
//        // 각 소식에 대한 첨부파일 가져오기
//        for (JournalDTO journal : journals) {
//            List<JournalFileDTO> journalFiles = journalMapper.selectFilesByJournalNo(journal.getJournalNo());
//            journal.setJournalFiles(journalFiles); // JournalDTO에 파일 리스트 설정
//        }
//
//        return journals;
//
//
//    }

    @Override
    public List<JournalDTO> getAllJournalsWithFiles(int offset, int limit) {
        // 모든 소식 가져오기
        List<JournalDTO> journals = journalMapper.selectAllJournals(offset, limit);
        System.out.println("서비스 메소드 에서 offset=" + offset + " ,pageSize=" + limit);
        System.out.println("serviceimple getalljouranlswithfiels(offset,limit) Retrieved journals: " + journals);

        // 각 소식에 대한 첨부파일 가져오기
        for (JournalDTO journal : journals) {
            List<JournalFileDTO> journalFiles = journalMapper.selectFilesByJournalNo(journal.getJournalNo());
            journal.setJournalFiles(journalFiles); // JournalDTO에 파일 리스트 설정
        }

        return journals;


    }

//    public JournalDTO getJournalByTitleWithFile(String title) {
//        return journalMapper.selectJournalByTitle(title);
//    }

    @Override
    public JournalDTO getJournalByTitleWithFile(String title) {
        // 제목으로 소식 가져오기
        JournalDTO journal = journalMapper.selectJournalByTitle(title);
        System.out.println("확인1 : " + journal.toString());

        if (journal != null) {
            // 소식에 첨부된 파일 리스트 가져오기
            System.out.println("확인2 : " + journal.getJournalNo());
            List<JournalFileDTO> journalFiles = journalMapper.selectFilesByJournalNo(journal.getJournalNo());
            System.out.println("확인3 : " + journalFiles);
            journal.setJournalFiles(journalFiles); // JournalDTO에 파일 리스트 설정
        }

        return journal;
    }


    @Override
    public JournalDTO getJournalByNoWithFiles(int journalNo) {
        // 번호로 소식 가져오기
        JournalDTO journal = journalMapper.selectJournalByNo(journalNo);
        System.out.println("getJournalByNoWithFiles 확인1 : " + journal.toString());

        if (journal != null) {
            // 소식에 첨부된 파일 리스트 가져오기
            System.out.println("확인2 : " + journal.getJournalNo());
            List<JournalFileDTO> journalFiles = journalMapper.selectFilesByJournalNo(journal.getJournalNo());
            System.out.println("확인3 : " + journalFiles);
            journal.setJournalFiles(journalFiles); // JournalDTO에 파일 리스트 설정
        }

        return journal;
    }

    @Override
    @Transactional
    public boolean updateJournal(JournalDTO journalDTO, MultipartFile file, HttpServletRequest request) throws Exception {


        try {

            journalMapper.updateJournal(journalDTO);

            String root = request.getSession().getServletContext().getRealPath("/");
            String filePath = root + "/uploadFiles";

            System.out.println("updatejournalserviceimpl check file null : ");
            System.out.println("Original Filename: " + file.getOriginalFilename());
            System.out.println("File Name: " + file.getName());
            System.out.println("File Size: " + file.getSize());
            System.out.println("File Content Type: " + file.getContentType());
            System.out.println("Is Empty: " + file.isEmpty());

            // 새로운 이미지가 첨부된 경우
            if (file != null && !file.isEmpty()) {
                // 기존 이미지 삭제
                List<JournalFileDTO> existingFiles = journalMapper.selectFilesByJournalNo(journalDTO.getJournalNo());

                for (JournalFileDTO fileDTO : existingFiles) {
                    File existingFile = new File(filePath + "/" + fileDTO.getFileName());
                    if (existingFile.exists()) {
                        existingFile.delete();
                    }
                }

                // 기존 이미지 데이터베이스에서 삭제
                journalMapper.deleteFiles(journalDTO.getJournalNo());

                // 새로운 이미지 저장
                JournalFileDTO fileDTO = new JournalFileDTO();
                String originName = file.getOriginalFilename();

                if (originName != null && !originName.isEmpty() && !file.isEmpty()) {
                    String ext = originName.substring(originName.lastIndexOf("."));

                    String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                    try {
                        file.transferTo(new File(filePath + "/" + savedName));



                        fileDTO.setJournalNo(journalDTO.getJournalNo());
                        fileDTO.setFileName(savedName);

                    } catch (IOException e) {
                        new File(filePath + "/" + savedName).delete();
                        throw new Exception("File upload error", e);
                    }
                }
                fileDTO.setFilePath(filePath);
                fileDTO.setFileType(file.getContentType());
                System.out.println("journalfiledto 수정페이지 서비스 :" + fileDTO.toString());
                journalMapper.insertJournalFile(fileDTO);
            }
            return true;
        } catch (Exception e) {
            throw new Exception("Update journal failed", e);
        }


    }

    @Override
    @Transactional
    public void deleteJournalByTitle(String title) throws Exception {
        try {
            // 제목으로 게시글 찾기
            JournalDTO journal = journalMapper.selectJournalByTitle(title);
            if (journal != null) {
                // 파일 삭제
                List<JournalFileDTO> files = journalMapper.selectFilesByJournalNo(journal.getJournalNo());
                String filePath = UPLOAD_DIR;
                for (JournalFileDTO file : files) {
                    File existingFile = new File(filePath + "/" + file.getFileName());
                    if (existingFile.exists()) {
                        existingFile.delete();
                    }
                }
                // 파일 정보 삭제
                journalMapper.deleteFiles(journal.getJournalNo());
                // 게시글 삭제
                journalMapper.deleteJournal(journal.getJournalNo());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to delete journal", e);
        }
    }

    //아래는 좋아요 관련 service
    public int countLikesByJournalNo(int journalNo) {
        return journalMapper.countLikesByJournalNo(journalNo);
    }

    @Override
    public boolean toggleLike(int journalNo, int userNo) {
        boolean exists = journalMapper.existsByJournalNoAndUserNo(journalNo, userNo);
        if (exists) {
            journalMapper.deleteLike(journalNo, userNo);
            return false; // 좋아요 취소
        } else {
            JournalLikeDTO like = new JournalLikeDTO();
            like.setJournalNo(journalNo);
            like.setUserNo(userNo);
            journalMapper.insertLike(like);
            return true; // 좋아요 추가
        }
    }

//    public List<JournalDTO> searchJournalsByTitle(String keyword) {
//        List<JournalDTO> journals = journalMapper.findByTitleContaining(keyword);
//
//        // 각 소식에 대한 첨부파일 가져오기
//        for (JournalDTO journal : journals) {
//            List<JournalFileDTO> journalFiles = journalMapper.selectFilesByJournalNo(journal.getJournalNo());
//            journal.setJournalFiles(journalFiles); // JournalDTO에 파일 리스트 설정
//        }
//        return journals;
//    }

    @Override
    public List<JournalDTO> searchJournalsByTitle(String keyword, int offset, int pageSize) {
        List<JournalDTO> journals = journalMapper.findByTitleContaining(keyword, offset, pageSize);

        // 각 소식에 대한 첨부파일 가져오기
        for (JournalDTO journal : journals) {
            List<JournalFileDTO> journalFiles = journalMapper.selectFilesByJournalNo(journal.getJournalNo());
            journal.setJournalFiles(journalFiles); // JournalDTO에 파일 리스트 설정
        }
        return journals;
    }

    @Override
    public int getTotalCount(){
        return journalMapper.getTotalCount();
    };

    @Override
    public int countJournalsByTitleKeyword(String keyword){
        return journalMapper.countByTitleContaining(keyword);
    };


}
