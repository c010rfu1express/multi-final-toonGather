package com.multi.toonGather.cs.service;

import com.multi.toonGather.cs.model.dto.*;
import com.multi.toonGather.cs.model.mapper.CsMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class CsServiceImpl implements CsService {

    // csMapper 선언 및 연결 부분
    private final CsMapper csMapper;
    @Autowired
    public CsServiceImpl(CsMapper csMapper) {
        this.csMapper = csMapper;
    }

    // 내 문의글 목록 조회
    @Override
    public List<QuestionDTO> myQuestionList(int userNo, int offset, int pageSize) throws Exception {
        return csMapper.myQuestionList(userNo, offset, pageSize);
    }

    // 카테고리 목록 조회
    @Override
    public List<CsCategoryDTO> getCategories() throws Exception {
        return csMapper.getCategories();
    }

    // 문의글 작성
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertQuestion(QuestionDTO question, MultipartFile[] images, HttpServletRequest request) throws Exception {
        try {
            // 문의글 삽입
            csMapper.insertQuestion(question);

            String root = request.getSession().getServletContext().getRealPath("/");
            System.out.println("root : " + root);
            String filePath = root + "/uploadFiles/cs";

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
                        QuestionFilesDTO questionFilesDTO = new QuestionFilesDTO();
                        questionFilesDTO.setCsQNo(question.getCsQNo());
                        questionFilesDTO.setOriginName(originName);
                        questionFilesDTO.setSavedName(savedName);
                        csMapper.insertQuestionFile(questionFilesDTO);
                    } catch (IOException e) {
                        System.out.println("File upload error : " + e);
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

    // 문의글 상세 조회
    @Override
    public QuestionDTO getQuestionById(int csQNo) throws Exception {
        return csMapper.getQuestionById(csQNo);
    }

    // 문의글 상세 조회 이미지 조회
    @Override
    public List<QuestionFilesDTO> getQuestionByQuestionId(int csQNo) throws Exception {
        return csMapper.getQuestionFilesByQuestionId(csQNo);
    }

    // 문의글 수정 메소드
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQuestion(QuestionDTO question, List<String> existingImages, List<String> removedImages, MultipartFile[] images, HttpServletRequest request) throws Exception {

        csMapper.updateQuestion(question);

        String root = request.getSession().getServletContext().getRealPath("/");
        String filePath = root + "/uploadFiles/cs";

        // 기존 이미지 삭제 처리
        if (removedImages != null && !removedImages.isEmpty()) {
            for (String savedName : removedImages) {
                File fileToDelete = new File(filePath + "/" + savedName);
                if (fileToDelete.exists()) {
                    fileToDelete.delete();
                }
                csMapper.deleteQuestionFileBySavedName(savedName);
            }
        }

        // 새로운 이미지 저장
        if (images != null && images.length > 0) {
            for (MultipartFile image : images) {
                String originName = image.getOriginalFilename();
                if (originName != null && !originName.isEmpty()) {
                    String ext = originName.substring(originName.lastIndexOf("."));
                    String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                    // 파일 저장
                    try {
                        image.transferTo(new File(filePath + "/" + savedName));
                        QuestionFilesDTO questionFilesDTO = new QuestionFilesDTO();
                        questionFilesDTO.setCsQNo(question.getCsQNo());
                        questionFilesDTO.setOriginName(originName);
                        questionFilesDTO.setSavedName(savedName);
                        csMapper.insertQuestionFile(questionFilesDTO);
                    } catch (IOException e) {
                        new File(filePath + "/" + savedName).delete();
                        throw new Exception("File upload error", e);
                    }
                }
            }
        }

        return true;

    }

    @Override
    @Transactional
    public boolean deleteQuestion(int csQNo, HttpServletRequest request) throws Exception {
        try {
            // 파일 정보 가져오기
            List<QuestionFilesDTO> files = csMapper.getQuestionFilesByQuestionId(csQNo);

            // 문의글 삭제 (ON DELETE CASCADE 적용)
            csMapper.deleteQuestion(csQNo);

            // 파일 삭제
            for (QuestionFilesDTO file : files) {
                File delFile = new File(request.getSession().getServletContext().getRealPath("/") + "/uploadFiles/cs/" + file.getSavedName());
                if (delFile.exists()) {
                    delFile.delete();
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete question");
        }
    }

    @Override
    public List<QuestionDTO> questionList(int offset, int limit) throws Exception {
        return csMapper.questionList(offset, limit);
    }

    @Override
    public List<AnswerDTO> getAnswerList(int csQNo) throws Exception {
        return csMapper.getAnswerList(csQNo);
    }

    @Override
    public boolean insertAnswer(AnswerDTO answerDTO) throws Exception {
        return csMapper.insertAnswer(answerDTO);
    }

    @Override
    public List<FaqDTO> faqList() throws Exception {
        return csMapper.faqList();
    }

    @Override
    public boolean insertFaq(FaqDTO faq) throws Exception {
        return csMapper.insertFaq(faq);
    }

    @Override
    public FaqDTO getFaqById(int csFaqNo) throws Exception {
        return csMapper.getFaqById(csFaqNo);
    }

    @Override
    public boolean updateFaq(FaqDTO faq) throws Exception {
        return csMapper.updateFaq(faq);
    }

    @Override
    public boolean deleteFaq(int csFaqNo) throws Exception {
        return csMapper.deleteFaq(csFaqNo);
    }

    @Override
    public int getTotalCount() throws Exception {
        return csMapper.getTotalCount();
    }

    @Override
    public int getTotalCountById(int userNo) throws Exception {
        return csMapper.getTotalCountById(userNo);
    }

    @Override
    public int countSearchQuestions(String searchType, String keyword) throws Exception {
        return csMapper.countSearchQuestions(searchType, keyword);
    }

    @Override
    public List<QuestionDTO> searchQuestions(String searchType, String keyword, int offset, int pageSize) throws Exception {
        return csMapper.searchQuestions(searchType, keyword, offset, pageSize);
    }
}
