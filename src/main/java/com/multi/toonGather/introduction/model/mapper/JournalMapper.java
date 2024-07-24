package com.multi.toonGather.introduction.model.mapper;

import com.multi.toonGather.introduction.model.dto.JournalDTO;
import com.multi.toonGather.introduction.model.dto.JournalFileDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface JournalMapper {

//    int insertJournal(JournalDTO journalDTO) throws Exception;
//
//    @Update("UPDATE in_journal SET ")
//    int updateJournal(JournalDTO journalDTO ) throws Exception;

    @Select("SELECT * FROM in_journal WHERE journal_no = #{journalNo}")
    JournalDTO selectJournalByNo(int journalNo);

    @Select("SELECT * FROM in_journal_files WHERE journal_no = #{journalNo}")
    List<JournalFileDTO> selectFilesByJournalNo(int journalNo);


}
