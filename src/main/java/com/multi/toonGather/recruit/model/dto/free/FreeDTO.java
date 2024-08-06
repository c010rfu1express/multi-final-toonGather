package com.multi.toonGather.recruit.model.dto.free;

import com.multi.toonGather.user.model.dto.UserDTO;
import lombok.Data;

import java.util.Date;

@Data
public class FreeDTO {
    private int board_no;
    private int writer;
    private String title;
    private String content;
    private String img;
    private int price;
    private String kakao_pg;
    private String inicis_pg;
    private String bank_name;
    private String account;
    private Date created_date;
    private Date modified_date;
    private UserDTO member;
    private String type_code;

}
