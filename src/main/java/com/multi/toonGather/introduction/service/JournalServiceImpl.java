package com.multi.toonGather.introduction.service;

import com.multi.toonGather.introduction.model.dto.JournalDTO;
import com.multi.toonGather.introduction.model.dto.JournalFileDTO;
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

    public JournalDTO getJournal(int journalNo) {
        return journalMapper.selectJournalByNo(journalNo);
    }

    public List<JournalFileDTO> getJournalFiles(int journalNo) {
        return journalMapper.selectFilesByJournalNo(journalNo);
    }

    public List<JournalDTO> getAllJournals() {
        return journalMapper.selectAllJournals();
    }

    public JournalDTO getJournalByTitle(String title) {
        return journalMapper.selectJournalByTitle(title);
    }

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

    public List<JournalDTO> getAllJournalsWithFiles() {
        // 모든 소식 가져오기
        List<JournalDTO> journals = journalMapper.selectAllJournals();
//        System.out.println("journals 확인" + journals[0].toString());
//        System.out.println("journals 확인" + journals[0].journalFiles[0].toString());
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
}
