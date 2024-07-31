package com.multi.toonGather.introduction.service;

import com.multi.toonGather.introduction.model.dto.JournalDTO;
import com.multi.toonGather.introduction.model.dto.JournalFileDTO;
import com.multi.toonGather.introduction.model.mapper.JournalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    public void insertJournal(String title, String content, MultipartFile file) throws Exception {
        JournalDTO journalDTO = new JournalDTO();
        journalDTO.setTitle(title);
        journalDTO.setContent(content);

        // Insert the journal and get the generated ID
        journalMapper.insertJournal(journalDTO);

        // Now the journalDTO should have the generated journalNo
        int journalNo = journalDTO.getJournalNo();

        if (!file.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs(); // 디렉토리가 존재하지 않으면 생성
            }
//            String filePath = STATIC_PATH + fileName;
            String filePath = UPLOAD_DIR + File.separator + fileName;
            file.transferTo(new File(filePath));

            JournalFileDTO fileDTO = new JournalFileDTO();
            fileDTO.setJournalNo(journalNo);
            fileDTO.setFileName(fileName);
            fileDTO.setFilePath(filePath);
            fileDTO.setFileType(file.getContentType());
            System.out.println("journalfiledto 테스트 :" + fileDTO.toString());

            journalMapper.insertJournalFile(fileDTO);
        }
    }

    public List<JournalDTO> getAllJournalsWithFiles() {
        // 모든 소식 가져오기
        List<JournalDTO> journals = journalMapper.selectAllJournals();

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
