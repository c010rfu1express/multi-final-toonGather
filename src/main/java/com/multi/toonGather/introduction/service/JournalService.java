package com.multi.toonGather.introduction.service;

import com.multi.toonGather.introduction.model.dto.JournalDTO;
import com.multi.toonGather.introduction.model.dto.JournalFileDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JournalService {

    JournalDTO getJournal(int journalNo);

    List<JournalFileDTO> getJournalFiles(int journalNo);


    JournalDTO getJournalByTitle(String title);

    void insertJournal(String title, String content, MultipartFile file, HttpServletRequest request) throws Exception;

//    List<JournalDTO> getAllJournalsWithFiles();
    List<JournalDTO> getAllJournalsWithFiles(int offset, int limit);

    JournalDTO getJournalByTitleWithFile(String title);

    boolean updateJournal(JournalDTO journalDTO, MultipartFile file, HttpServletRequest request) throws Exception;

    JournalDTO getJournalByNoWithFiles(int journalNo);

    void deleteJournalByTitle(String title) throws Exception;

    int countLikesByJournalNo(int journalNo);

    boolean toggleLike(int journalNo, int userNo);

//    List<JournalDTO> searchJournalsByTitle(String keyword);
    List<JournalDTO> searchJournalsByTitle(String keyword, int offset, int pageSize);

    int getTotalCount();

    int countJournalsByTitleKeyword(String keyword);
}
