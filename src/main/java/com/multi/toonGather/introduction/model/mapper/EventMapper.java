package com.multi.toonGather.introduction.model.mapper;

import com.multi.toonGather.introduction.model.dto.EventCategoryDTO;
import com.multi.toonGather.introduction.model.dto.EventDTO;
import com.multi.toonGather.introduction.model.dto.EventFileDTO;
import com.multi.toonGather.introduction.model.dto.EventLikeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EventMapper {
    List<EventDTO> selectAllEvents(@Param("offset") int offset, @Param("limit") int limit);

    List<EventFileDTO> selectFilesByEventNo(int eventNo);

    int getTotalCount();

    List<EventCategoryDTO> selectAllEventCategories();

    void insertEvent(EventDTO eventDTO);

    void insertEventFile(EventFileDTO fileDTO);

    EventDTO selectEventByNo(int eventNo);

    void deleteFiles(int eventNo);

    void deleteEvent(int eventNo);

    int countLikesByEventNo(int eventNo);

    EventCategoryDTO getEventCategoryByCode(int eventCategoryCode);

    boolean existsByEventNoAndUserNo(@Param("eventNo") int eventNo, @Param("userNo") int userNo);

    void deleteLike(@Param("eventNo") int eventNo, @Param("userNo") int userNo);

    void insertLike(EventLikeDTO like);

    void updateEvent(EventDTO event);

    void deleteEventFileBySavedName(String savedName);

    int countEventsByTitleKeyword(String s);

    List<EventDTO> selectEventsByTitleKeyword(@Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit    );

    void deleteLikes(int eventNo);
}
