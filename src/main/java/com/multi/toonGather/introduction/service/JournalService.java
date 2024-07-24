package com.multi.toonGather.introduction.service;

import com.multi.toonGather.introduction.model.dto.JournalDTO;
import com.multi.toonGather.introduction.model.dto.JournalFileDTO;
import com.multi.toonGather.introduction.model.mapper.JournalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
