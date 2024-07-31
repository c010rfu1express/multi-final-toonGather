package com.multi.toonGather.introduction.model.dto;

import lombok.Data;

@Data
public class JournalFileDTO {
    private int journalFileNo;
    private int journalNo;
    private String fileName;
    private String filePath;
    private String fileType;
    private String uploadDate;

//    private int journal_file_no;
//    private int journal_no;
//    private String file_name;
//    private String file_path;
//    private String file_type;
//    private String upload_date;
}
