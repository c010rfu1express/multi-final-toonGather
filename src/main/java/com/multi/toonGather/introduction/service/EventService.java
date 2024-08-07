package com.multi.toonGather.introduction.service;

import com.multi.toonGather.introduction.model.dto.EventCategoryDTO;
import com.multi.toonGather.introduction.model.dto.EventDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {
    int countEventsByTitleKeyword(String keyword);

    List<EventDTO> searchEventsByTitle(String keyword, int offset, int pageSize);

    int getTotalCount();

    List<EventDTO> getAllEventsWithFiles(int offset, int pageSize);

    List<EventCategoryDTO> getAllEventCategories();

    void insertEvent(EventDTO eventDTO, MultipartFile file, HttpServletRequest request) throws Exception;
}
