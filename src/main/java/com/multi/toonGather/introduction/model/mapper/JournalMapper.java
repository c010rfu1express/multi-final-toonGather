package com.multi.toonGather.introduction.model.mapper;

import com.multi.toonGather.introduction.model.dto.JournalDTO;
import com.multi.toonGather.introduction.model.dto.JournalFileDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface JournalMapper {

    @Insert("INSERT INTO in_journal (title, content) VALUES (#{title}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "journalNo")
    int insertJournal(JournalDTO journalDTO);

    @Insert("INSERT INTO in_journal_files (journal_no, file_name, file_path, file_type) VALUES (#{journalNo}, #{fileName}, #{filePath}, #{fileType})")
    int insertJournalFile(JournalFileDTO journalFileDTO);

    @Select("SELECT * FROM in_journal WHERE journal_no = #{journalNo}")
    JournalDTO selectJournalByNo(int journalNo);

    //틀렸을 수 있음.
//    @Select("SELECT * FROM in_journal WHERE title = #{title}")
//    JournalDTO selectJournalByTitle(String title);

    @Select("SELECT journal_no AS journalNo, title, content, posting_date AS postingDate FROM in_journal WHERE title = #{title}")
    JournalDTO selectJournalByTitle(String title);


//    @Select("SELECT * FROM in_journal_files WHERE journal_no = #{journalNo}")
//    List<JournalFileDTO> selectFilesByJournalNo(int journalNo);

    @Select("SELECT journal_file_no AS journalFileNo, " +
            "journal_no AS journalNo, " +
            "file_name AS fileName, " +
            "file_path AS filePath, " +
            "file_type AS fileType, " +
            "upload_date AS uploadDate " +
            "FROM in_journal_files " +
            "WHERE journal_no = #{journalNo}")
    List<JournalFileDTO> selectFilesByJournalNo(int journalNo);


    @Select("SELECT * FROM in_journal ORDER BY posting_date DESC")
    List<JournalDTO> selectAllJournals();










    //    int insertJournal(JournalDTO journalDTO) throws Exception;
//
//    @Update("UPDATE in_journal SET ")
//    int updateJournal(JournalDTO journalDTO ) throws Exception;
}
