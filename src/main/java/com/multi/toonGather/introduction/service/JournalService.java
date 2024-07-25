package com.multi.toonGather.introduction.service;

import com.multi.toonGather.introduction.model.dto.JournalDTO;
import com.multi.toonGather.introduction.model.dto.JournalFileDTO;
import com.multi.toonGather.introduction.model.mapper.JournalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class JournalService {
    @Autowired
    private JournalMapper journalMapper;

    public JournalDTO getJournal(int journalNo) {
        return journalMapper.selectJournalByNo(journalNo);
    }

    public List<JournalFileDTO> getJournalFiles(int journalNo) {
        return journalMapper.selectFilesByJournalNo(journalNo);
    }

    public List<JournalDTO> getAllJournals() {
        return journalMapper.selectAllJournals();
    }

    @Transactional
    public void insertJournal(String title, String content, MultipartFile file) throws Exception {
        JournalDTO journalDTO = new JournalDTO();
        journalDTO.setTitle(title);
        journalDTO.setContent(content);

        journalMapper.insertJournal(journalDTO);

        if (!file.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String staticPath = "src/main/resources/static/images/journal/";
            File directory = new File(staticPath);
            if (!directory.exists()) {
                directory.mkdirs(); // 디렉토리가 존재하지 않으면 생성
            }
            String filePath = staticPath + fileName;
            file.transferTo(new File(filePath));

            JournalFileDTO fileDTO = new JournalFileDTO();
            fileDTO.setJournalNo(journalDTO.getJournalNo());
            fileDTO.setFileName(fileName);
            fileDTO.setFilePath(filePath);
            fileDTO.setFileType(file.getContentType());

            journalMapper.insertJournalFile(fileDTO);
        }
    }
}
