package com.multi.toonGather.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyInMerchanDTO {
    private int likedNo;
    private int merchanNo;
    private int userNo;
    private LocalDateTime likedDate;

    private String title;
    private int regularPrice;
    private int discountPrice;
    private int shippingCost;
    private String merchanInfo;
    private String content;
    private LocalDateTime postingDate;

    //조인을 피한 대신 MyService가 복잡해졌다.
    private List<MyInMerchanFileDTO> merchanFiles;

    //시간 view에 뿌려줄 때
    public String getFormattedPostingDate() {
        return TimeAgoUtils.formatTimeAgo(postingDate);
    }
}
