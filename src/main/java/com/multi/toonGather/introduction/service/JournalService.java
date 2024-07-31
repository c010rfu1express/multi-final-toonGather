package com.multi.toonGather.introduction.service;

import com.multi.toonGather.introduction.model.dto.JournalDTO;
import com.multi.toonGather.introduction.model.dto.JournalFileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JournalService {
    JournalDTO getJournal(int journalNo);
    List<JournalFileDTO> getJournalFiles(int journalNo);
    List<JournalDTO> getAllJournals();
    JournalDTO getJournalByTitle(String title);
    void insertJournal(String title, String content, MultipartFile file) throws Exception;
    List<JournalDTO> getAllJournalsWithFiles();
    JournalDTO getJournalByTitleWithFile(String title);
}
