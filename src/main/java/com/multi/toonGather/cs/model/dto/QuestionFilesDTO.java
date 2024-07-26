package com.multi.toonGather.cs.model.dto;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class QuestionFilesDTO {
    private int csQFileNo;
    private int csQNo;
    private String originName;
    private String savedName;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
}
