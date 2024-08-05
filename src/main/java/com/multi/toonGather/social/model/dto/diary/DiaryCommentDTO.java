package com.multi.toonGather.social.model.dto.diary;

import com.multi.toonGather.user.model.dto.UserDTO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 다이어리 댓글 정보를 담는 데이터 전송 객체(DTO)
 *
 * 이 클래스는 다이어리 댓글의 정보를 저장하고 전달하는 데 사용됩니다.
 * 댓글 번호, 연관된 다이어리 번호, 댓글 작성자 정보, 댓글 내용, 작성 일시 등을 포함합니다.
 *
 * @author : seoyun
 * @fileName : DiaryCommentDTO
 * @since : 2024-08-03
 */
@Data
public class DiaryCommentDTO {
    private int commentNo;
    private int diaryNo;
    private UserDTO commenter;
    private String content;
    private LocalDateTime createdDate;
}
