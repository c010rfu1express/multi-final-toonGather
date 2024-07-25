package com.multi.toonGather.cs.service;

import com.multi.toonGather.cs.model.dto.CsCategoryDTO;
import com.multi.toonGather.cs.model.dto.QuestionFilesDTO;
import com.multi.toonGather.cs.model.mapper.CsMapper;
import com.multi.toonGather.cs.model.dto.QuestionDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Transient;
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
    public List<QuestionDTO> myQuestionList(int userNo) throws Exception {
        System.out.println("CsServiceImpl myQuestionList 도착.");

        return csMapper.myQuestionList(userNo);
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
}
