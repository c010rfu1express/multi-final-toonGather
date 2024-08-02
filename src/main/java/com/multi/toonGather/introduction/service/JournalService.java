package com.multi.toonGather.introduction.service;

import com.multi.toonGather.introduction.model.dto.JournalDTO;
import com.multi.toonGather.introduction.model.dto.JournalFileDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JournalService {
    JournalDTO getJournal(int journalNo);
    List<JournalFileDTO> getJournalFiles(int journalNo);
    List<JournalDTO> getAllJournals();
    JournalDTO getJournalByTitle(String title);
    void insertJournal(String title, String content, MultipartFile file, HttpServletRequest request) throws Exception;
    List<JournalDTO> getAllJournalsWithFiles();
    JournalDTO getJournalByTitleWithFile(String title);
    public boolean updateJournal(JournalDTO journalDTO, MultipartFile file, HttpServletRequest request) throws Exception;
    public JournalDTO getJournalByNoWithFiles(int journalNo);
    public void deleteJournalByTitle(String title) throws Exception;
}
