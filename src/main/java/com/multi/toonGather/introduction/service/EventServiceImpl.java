package com.multi.toonGather.introduction.service;

import com.multi.toonGather.introduction.model.dto.*;
import com.multi.toonGather.introduction.model.mapper.EventMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventMapper eventMapper;


    static String projectRootPath = System.getProperty("user.dir");
    private static final String UPLOAD_DIR = Paths.get(projectRootPath, "src", "main", "webapp", "uploadFiles", "introduction").toString();


    @Override
    public int countEventsByTitleKeyword(String keyword) {
        return 0;
    }

    @Override
    public List<EventDTO> searchEventsByTitle(String keyword, int offset, int pageSize) {
        return List.of();
    }


    @Override
    public int getTotalCount() {
        return eventMapper.getTotalCount();
    }

    @Override
    public List<EventDTO> getAllEventsWithFiles(int offset, int pageSize) {
        // 모든 행사 가져오기
        System.out.println("getAllEventsWithFiles 실행");
        System.out.println("서비스 메소드 에서 offset=" + offset + " ,pageSize=" + pageSize);
        List<EventDTO> events = eventMapper.selectAllEvents(offset, pageSize);
        System.out.println("serviceimple getalleventswithfiels(offset,limit) Retrieved journals: " + events);

        // 각 소식에 대한 첨부파일 가져오기
        for (EventDTO event : events) {
            List<EventFileDTO> eventFiles = eventMapper.selectFilesByEventNo(event.getEventNo());
            event.setEventFiles(eventFiles); // JournalDTO에 파일 리스트 설정
        }

        return events;

    }

    @Override
    public List<EventCategoryDTO> getAllEventCategories() {
        return eventMapper.selectAllEventCategories();
    }

    @Override
    public void insertEvent(EventDTO eventDTO, MultipartFile file, HttpServletRequest request) throws Exception {
        // 1. 현재 날짜 및 시간 설정
//        eventDTO.setPostingDate(LocalDateTime.now());

        // 2. 이벤트 정보 DB에 삽입 & eventNo 가져오기
         eventMapper.insertEvent(eventDTO);
         int eventNo = eventDTO.getEventNo();

         // 3. 파일 저장 로직
         if(file != null && !file.isEmpty()){

             System.out.println("File object is not null.");
             System.out.println("File name: " + file.getOriginalFilename());
             System.out.println("File size: " + file.getSize());
             System.out.println("File content type: " + file.getContentType());

             EventFileDTO fileDTO = new EventFileDTO();
             String root = request.getSession().getServletContext().getRealPath("/");
             System.out.println("root : " + root);
             String filePath = root + "/uploadFiles";

             File mkdir = new File(filePath);
             if (!mkdir.exists()) {
                 mkdir.mkdirs();
             }

             String originName = file.getOriginalFilename();
             if (originName != null && !originName.isEmpty()) {
                 String ext = originName.substring(originName.lastIndexOf("."));
                 String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                 // 파일 저장
                 try {
                     file.transferTo(new File(filePath + "/" + savedName));

                     fileDTO.setEventNo(eventNo);
                     fileDTO.setFileName(savedName);
                     fileDTO.setFilePath(filePath);
                     fileDTO.setFileType(file.getContentType());
                     System.out.println("eventfiledto 테스트 :" + fileDTO.toString());

                     // Insert the file information into the database
                     eventMapper.insertEventFile(fileDTO);
                 } catch (IOException e) {
                     System.out.println("File upload error : " + e);
                     // Clean up the uploaded file if an error occurs
                     new File(filePath + "/" + savedName).delete();
                     throw new Exception("File upload failed, rolling back transaction.", e); // 예외를 던져 트랜잭션을 롤백함
                 }
             }// if origin not null

         }else{
             System.out.println("File object is null");
         }

    }

    @Override
    public EventDTO getEventByNoWithFiles(int eventNo) {
        // 번호로 행사 가져오기
        EventDTO event = eventMapper.selectEventByNo(eventNo);
        System.out.println("getEventByNoWithFiles 확인1 : " + event.toString());

        if (event != null) {
            // 행사에 첨부된 파일 리스트 가져오기
            System.out.println("확인2 : " + event.getEventNo());
            List<EventFileDTO> eventFiles = eventMapper.selectFilesByEventNo(event.getEventNo());
            System.out.println("확인3 : " + eventFiles);
            event.setEventFiles(eventFiles); // EventDTO에 파일 리스트 설정
        }

        return event;
    }

    @Override
    public void deleteEventByNo(Integer eventNo) throws Exception {
        try {
            // eventNo으로 게시글 찾기
            EventDTO event = eventMapper.selectEventByNo(eventNo);
            if (event != null) {
                // 파일 삭제
                List<EventFileDTO> files = eventMapper.selectFilesByEventNo(event.getEventNo());
                String filePath = UPLOAD_DIR;
                for (EventFileDTO file : files) {
                    File existingFile = new File(filePath + "/" + file.getFileName());
                    if (existingFile.exists()) {
                        existingFile.delete();
                    }
                }
                // 파일 정보 삭제
                eventMapper.deleteFiles(event.getEventNo());
                // 게시글 삭제
                eventMapper.deleteEvent(event.getEventNo());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to delete event", e);
        }
    }
}

