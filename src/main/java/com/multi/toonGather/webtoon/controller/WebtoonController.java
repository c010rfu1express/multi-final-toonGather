package com.multi.toonGather.webtoon.controller;

import com.multi.toonGather.security.CustomUserDetails;
import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.webtoon.model.dto.*;
import com.multi.toonGather.webtoon.service.WebToonService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class WebtoonController {

    @Autowired
    private final WebToonService webToonService;

    public WebtoonController(WebToonService webToonService) {
        this.webToonService = webToonService;
    }

    @GetMapping(value = {"/","webtoon/main"})
    public String Webtoon( Model model,@AuthenticationPrincipal CustomUserDetails c,
                           @RequestParam(value = "provider", required = false) String provider){
        LocalDate currentDate = LocalDate.now();
        System.out.println(provider);
        if(c!=null){
            LocalDate today = LocalDate.now();
            LocalDate dateOfBirth= c.getUserDTO().getDateOfBirth();
            model.addAttribute("isLoggedAge",Period.between(dateOfBirth, today).getYears());
            WtUserSaveDTO userDTO=new WtUserSaveDTO();
            userDTO.setUserNo(c.getUserDTO().getUserNo());
            userDTO.setPlatform(provider);


            List<WebtoonDTO> webtoonDTOList=webToonService.webtoonUserBest(userDTO);

            Map<String, Long> genreCountMap = webtoonDTOList.stream()
                    .flatMap(webtoon -> Arrays.stream(webtoon.getGenre().split("_"))  // 장르 추출
                            .filter(genre -> genre != null && !genre.isEmpty()&&!genre.equalsIgnoreCase("all")))  // 빈 문자열 필터링
                    .collect(Collectors.groupingBy(genre -> genre, Collectors.counting()));  // 그룹화 및 개수 집계

            // 가장 많이 나타나는 장르 찾기
            List<Map.Entry<String, Long>> topGenres = genreCountMap.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())  // 빈도 수 기준으로 내림차순 정렬
                    .limit(2)  // 상위 두 개 항목 선택
                    .collect(Collectors.toList());

            Map<String, Long> tagCountMap = webtoonDTOList.stream()
                    .flatMap(dto -> Arrays.stream(dto.getTags().split("[#]")))  // 태그 문자열 분열
                    .filter(tag -> !tag.isEmpty())  // 빈 문자열 필터링
                    .collect(Collectors.groupingBy(tag -> tag, Collectors.counting()));  // 그룹화 및 개수 집계

            // 빈도 수 기준으로 상위 두 개 태그 찾기
            List<Map.Entry<String, Long>> topTags = tagCountMap.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())  // 빈도 수 기준으로 내림차순 정렬
                    .limit(2)  // 상위 두 개 항목 선택
                    .collect(Collectors.toList());


            if (topGenres.isEmpty()) {
                webtoonDTOList=webToonService.webtoonBest();
                model.addAttribute("webtoonSelect",webtoonDTOList);
            } else {
                TagPageDTO dto=new TagPageDTO();
                dto.setPlatform(provider);
                System.out.println(dto);
                for (int i = 0; i < topTags.size(); i++) {
                    Map.Entry<String, Long> tage = topTags.get(i);
                    Map.Entry<String, Long> Genre = topGenres.get(i);
                    if(i==0){
                        dto.setTag1(tage.getKey());
                        dto.setGenre1(Genre.getKey());
                    }
                    if(i==1){
                        dto.setTag2(tage.getKey());
                        dto.setGenre2(Genre.getKey());
                    }
                    if(topGenres.size()<2){
                        if(topTags.size()>1)tage = topTags.get(1);
                        dto.setTag2(tage.getKey());
                        dto.setGenre2(Genre.getKey());
                        break;}
                }
                webtoonDTOList=webToonService.recommendWebtoon(dto);
                if(webtoonDTOList.size()<12){
                    dto.setTag3("");
                    webtoonDTOList=webToonService.recommendWebtoon(dto);

                }

                model.addAttribute("webtoonSelect",webtoonDTOList);
            }



        }else {
            model.addAttribute("isLoggedAge",-1);
           List<WebtoonDTO> webtoonDTOList=webToonService.webtoonBest();
            model.addAttribute("webtoonSelect",webtoonDTOList);
        }

        // 현재 날짜의 요일 가져오기
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

        // 요일을 영어로 출력하기 (앞 3글자, 대문자)
        String dayOfWeekShortString = dayOfWeek.getDisplayName(
                TextStyle.SHORT_STANDALONE, // SHORT_STANDALONE을 사용하여 요일의 약어를 가져옵니다.
                Locale.ENGLISH              // 영어 Locale을 사용합니다.
        ).substring(0, 3).toUpperCase();
        model.addAttribute("day",dayOfWeekShortString);

        return "webtoon/webtoonlist";
    }

    @GetMapping(value = {"webtoon/one"})
    public String Web_toon_One(WebtoonDTO webtoonDTO, Model model,@AuthenticationPrincipal CustomUserDetails c){


        UserDTO userDTO=new UserDTO();
        WebtoonDTO webtoonDTO1=new WebtoonDTO();
        WebtoonDTO resultDTO=new WebtoonDTO();
        WtUserSaveDTO wtUserSaveDTO=new WtUserSaveDTO();
        String id=webtoonDTO.getWebtoon_id();
        String[] words1=id.split("_");
        System.out.println(words1[0]);
        System.out.println(words1[1]);
        if(words1[0].equals("naver")){
            webtoonDTO1.setPlatform(1);
        }else {
            webtoonDTO1.setPlatform(2);
        }
        webtoonDTO1.setWebtoon_id(words1[1]);
        webtoonDTO1.setWebtoon_name(webtoonDTO.getWebtoon_name());
        webtoonDTO1.setAuthor(webtoonDTO.getAuthor());


        try {
            resultDTO=webToonService.WebToonSelectOne(webtoonDTO1);
            if(resultDTO==null){
                if(webtoonDTO1.getPlatform()==1){
                webtoonDTO1=naver(webtoonDTO1);
                }else {
                    webtoonDTO1=kkao(webtoonDTO1);
                }
                webToonService.webToonInsert(webtoonDTO1);
                resultDTO=webToonService.WebToonSelectOne(webtoonDTO1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        webtoonDTO.setWebtoon_no(resultDTO.getWebtoon_no());
        webtoonDTO.setGenre(resultDTO.getGenre());
        webtoonDTO.setTags(resultDTO.getTags());
        try {
            List<CommentDTO>comments=webToonService.Commentlist(webtoonDTO);
            List<CommentDTO>bestComments=webToonService.CommentBestList(webtoonDTO);

            Set<Integer> bestCommentNos = bestComments.stream()
                    .map(CommentDTO::getCommentNo)
                    .collect(Collectors.toSet());

            // comments 리스트에서 bestCommentNos에 포함된 댓글 제거
            comments = comments.stream()
                    .filter(comment -> !bestCommentNos.contains(comment.getCommentNo()))
                    .collect(Collectors.toList());

            System.out.println(comments.toArray());
            System.out.println(comments);
            System.out.println(bestComments);
            model.addAttribute("comments",comments);
            model.addAttribute("bestComments",bestComments);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(c!=null){
        userDTO.setUserNo(c.getUserDTO().getUserNo());
        wtUserSaveDTO.setWebtoonNo(webtoonDTO.getWebtoon_no());
        wtUserSaveDTO.setUserNo(c.getUserDTO().getUserNo());
            LocalDate today = LocalDate.now();
            LocalDate dateOfBirth= c.getUserDTO().getDateOfBirth();
            model.addAttribute("isLoggedAge",Period.between(dateOfBirth, today).getYears());
            try {
        wtUserSaveDTO=webToonService.WebToonSelectSave(wtUserSaveDTO);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            model.addAttribute("isLoggedAge",-1);
        }
        System.out.println(wtUserSaveDTO);
        CommentLikeDTO commentLikeDTO=new CommentLikeDTO();
        commentLikeDTO.setUserNo(userDTO.getUserNo());
        // CommentLikeDTO 객체 리스트 생성 및 초기화
        List<CommentLikeDTO> commentLikeList = webToonService.commentLikeList(commentLikeDTO);


        Map<Integer, CommentLikeDTO> commentLikeMap = commentLikeList.stream()
                .collect(Collectors.toMap(CommentLikeDTO::getCommentNo, like -> like));

        model.addAttribute("commentLikeMap",commentLikeMap);
        model.addAttribute("save",wtUserSaveDTO);
        model.addAttribute("user",userDTO);
        model.addAttribute("one",webtoonDTO);


        return "webtoon/one";
    }

    @GetMapping("/webtoon/search")
    public String searchWebtoon(@RequestParam("search") String search,
                                @RequestParam("Method") String Method,
                                TagPageDTO tagPageDTO,
                                @AuthenticationPrincipal CustomUserDetails c,
                                Model model) throws UnsupportedEncodingException {
        search=search.replace("#", "");


        if(c!=null){
            LocalDate today = LocalDate.now();
            LocalDate dateOfBirth= c.getUserDTO().getDateOfBirth();
            Period.between(dateOfBirth, today).getYears();

            model.addAttribute("isLoggedAge",Period.between(dateOfBirth, today).getYears());
        }else {
            model.addAttribute("isLoggedAge",0);
        }


        List<WebtoonDTO>webtoonDTOS=new ArrayList<>();
        if(Method.equals("tags")){
            String[]tags=search.split(",");
            if(tags.length==0)tagPageDTO.setTag1(search);
            if(tags.length>0)tagPageDTO.setTag1(tags[0]);
            if(tags.length>1) tagPageDTO.setTag2(tags[1]);
            if(tags.length>2) tagPageDTO.setTag3(tags[2]);

            int count=webToonService.countWebtoon(tagPageDTO);
            int totalPages=count/50+1;

            int visibleRange = 10; // 표시할 페이지 버튼 수

            // Calculate start and end page based on current page
            int startPage = Math.max(1, tagPageDTO.getPage() - (visibleRange / 2));
            int endPage = Math.min(totalPages, startPage+visibleRange-1);

            if (endPage - startPage + 1 < visibleRange) {
                if (startPage > 1) {
                    endPage = Math.min(totalPages, endPage + (visibleRange - (endPage - startPage + 1)));
                } else {
                    startPage = Math.max(1, endPage - visibleRange + 1);
                }
            }

            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);

            model.addAttribute("totalPages", totalPages);

            if(tagPageDTO.getPage()==0){
                tagPageDTO.setPage(1);
            }
            model.addAttribute("currentPage", tagPageDTO.getPage());
            tagPageDTO.setStartEndTag(tagPageDTO.getPage());
            webtoonDTOS=webToonService.searchWebtoon(tagPageDTO);


        }
        model.addAttribute("Method",Method);

        model.addAttribute("platform",tagPageDTO.getPlatform());

        for (WebtoonDTO webtoon : webtoonDTOS) {
            String encodedWebtoonName = URLEncoder.encode(webtoon.getWebtoon_name(), StandardCharsets.UTF_8.toString());
            webtoon.setEncodedWebtoonName(encodedWebtoonName); // 인코딩된 이름을 설정
        }
        model.addAttribute("searchTags",webtoonDTOS);

//        String encodedString = URLEncoder.encode(search, "UTF-8");
        model.addAttribute("search",search);


        return "webtoon/webtoonSearch";
    }

    @GetMapping("/webtoon/one/count")
    public ResponseEntity<Void> IncreaseCount(WebtoonDTO webtoonDTO,@AuthenticationPrincipal CustomUserDetails c) throws Exception {



        boolean result =webToonService.increaseCount(webtoonDTO);
        UserDTO dto=new UserDTO();
        if(c!=null){
            dto=c.getUserDTO();}


        WtUserLogDTO wtUserLogDTO=new WtUserLogDTO();
        wtUserLogDTO.setUserNo(dto.getUserNo());
        wtUserLogDTO.setWebtoonNo(webtoonDTO.getWebtoon_no());
        if(dto.getUserNo()>0){
            WtUserLogDTO resultDTO =webToonService.selrctLog(wtUserLogDTO);
            if(resultDTO==null){
                webToonService.insertLog(wtUserLogDTO);
            }else {
                webToonService.updateLog(resultDTO);
            }
        }
        if (result) {
            System.out.println("증가");
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/webtoon/one/comment")
    public String InsertComment(WebtoonDTO webtoonDTO, @RequestParam("content") String content,@AuthenticationPrincipal CustomUserDetails c) throws Exception {
        boolean result =true;
        System.out.println(content);
        CommentDTO commentDTO=new CommentDTO();
        commentDTO.setUserNo(c.getUserDTO().getUserNo());
        commentDTO.setContent(content);
        commentDTO.setWebtoonNo(webtoonDTO.getWebtoon_no());
        System.out.println(webtoonDTO);
        webToonService.insertComment(commentDTO);
            System.out.println("증가");
        String encodedString = URLEncoder.encode(webtoonDTO.getWebtoon_name(), "UTF-8");
            return "redirect:/webtoon/one?webtoon_id=" + webtoonDTO.getWebtoon_id()
                    +"&webtoon_name="+encodedString;

    }
    @PostMapping("/webtoon/one/update")
    public ResponseEntity<String> updateComment(@RequestBody CommentDTO commentDTO) throws Exception {
        try {
            // request에서 필요한 정보 추출

            // 댓글 수정 로직 호출 (예시: CommentService를 통해 댓글 수정)
            webToonService.updateComment(commentDTO);

            return ResponseEntity.ok("댓글 수정 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("댓글 수정 실패");
        }

    }
    @PostMapping("/webtoon/comment/delete")
    public String deleteComment(WebtoonDTO webtoonDTO, @RequestParam("commentNo") int commentNo) throws Exception {
        boolean result =true;
        System.out.println(commentNo);
        CommentDTO commentDTO=new CommentDTO();
        commentDTO.setCommentNo(commentNo);
        
        System.out.println(webtoonDTO);
        webToonService.deleteComment(commentDTO);
        System.out.println("삭제");
        String encodedString = URLEncoder.encode(webtoonDTO.getWebtoon_name(), "UTF-8");
        return "redirect:/webtoon/one?webtoon_id=" + webtoonDTO.getWebtoon_id()
                +"&webtoon_name="+encodedString;

    }

    @PostMapping("/webtoon/save/inset")
    public String insertSave(WebtoonDTO webtoonDTO,@AuthenticationPrincipal CustomUserDetails c) throws Exception {
       WtUserSaveDTO wtUserSaveDTO=new WtUserSaveDTO();
        wtUserSaveDTO.setUserNo(c.getUserDTO().getUserNo());
        wtUserSaveDTO.setWebtoonNo(webtoonDTO.getWebtoon_no());
        System.out.println(webtoonDTO);
       int result= webToonService.insertSave(wtUserSaveDTO);
        System.out.println("삭제"+result);
        System.out.println(webtoonDTO);
        String encodedString = URLEncoder.encode(webtoonDTO.getWebtoon_name(), "UTF-8");
        return "redirect:/webtoon/one?webtoon_id=" + webtoonDTO.getWebtoon_id()
                +"&webtoon_name="+encodedString;

    }

    @PostMapping("/webtoon/save/delete")
    public String deleteSave(WebtoonDTO webtoonDTO, @RequestParam("saveNo") int saveNo) throws Exception {

        WtUserSaveDTO wtUserSaveDTO=new WtUserSaveDTO();


        wtUserSaveDTO.setSaveNo(saveNo);

        int result= webToonService.deleteSave(wtUserSaveDTO);
        System.out.println("삭제");
        String encodedString = URLEncoder.encode(webtoonDTO.getWebtoon_name(), "UTF-8");
        System.out.println(encodedString);
        return "redirect:/webtoon/one?webtoon_id=" + webtoonDTO.getWebtoon_id()
                +"&webtoon_name="+encodedString;

    }




    public static WebtoonDTO naver(WebtoonDTO dto) throws IOException {
        // API 엔드포인트 URL 설정

        String apiUrl = "https://comic.naver.com/api/article/list/info?titleId="+dto.getWebtoon_id();

        // URL 객체 생성
        URL url = new URL(apiUrl);

        // HTTP 연결 설정
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // 응답 코드 확인
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            System.out.println("HTTP 요청 실패: " + responseCode);

        }

        // 응답 데이터 읽기
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // JSON 데이터를 JSONObject로 파싱
        JSONObject jsonObject = new JSONObject(response.toString());

        // 필요한 데이터 추출
        int titleId = jsonObject.getInt("titleId");
        String thumbnailUrl = jsonObject.getString("thumbnailUrl");
        String titleName = jsonObject.getString("titleName");

        // 작가 데이터 추출
        List<String> authors = new ArrayList<>();
        JSONArray communityArtists = jsonObject.getJSONArray("communityArtists");
        for (int i = 0; i < communityArtists.length(); i++) {
            JSONObject artist = communityArtists.getJSONObject(i);
            String authorName = artist.getString("name");
            authors.add(authorName);
        }

        // 태그 데이터 추출
        List<String> tags = new ArrayList<>();
        JSONArray curationTagList = jsonObject.getJSONArray("curationTagList");
        for (int i = 0; i < curationTagList.length(); i++) {
            JSONObject tag = curationTagList.getJSONObject(i);
            String tagName = tag.getString("tagName");
            tags.add(tagName);
        }

        // 장르 데이터 추출
        List<String> genreTypes = new ArrayList<>();
        JSONArray genreList = jsonObject.getJSONObject("gfpAdCustomParam").getJSONArray("genreTypes");
        for (int i = 0; i < genreList.length(); i++) {
            String genre = genreList.getString(i);
            genreTypes.add(genre);
        }

        // WebtoonDTO 객체 생성 및 데이터 설정
        WebtoonDTO webtoonDTO = new WebtoonDTO();
        webtoonDTO.setWebtoon_id(""+titleId);
        webtoonDTO.setPlatform(1);
        webtoonDTO.setThumbnail_url(thumbnailUrl);
        webtoonDTO.setWebtoon_name(titleName);
        webtoonDTO.setAuthor(String.join("/", authors));
        webtoonDTO.setGenre(String.join("", genreTypes));
        webtoonDTO.setTags(String.join("#", tags));

        // 데이터 출력 (옵션)
        System.out.println("WebtoonDTO: " + webtoonDTO);
        return webtoonDTO;
    }


    public static WebtoonDTO kkao(WebtoonDTO kkao) throws IOException {
        String[]week= new String[]{"timetable_mon","timetable_tue","timetable_wed","timetable_thu"
                ,"timetable_fri","timetable_sat","timetable_sun","timetable_completed"};
        // API 엔드포인트 URL 설정
        for(String x:week){


            String apiUrl = "https://gateway-kw.kakao.com/section/v2/timetables/days?placement="+x;

            // URL 객체 생성
            URL url = new URL(apiUrl);

            // HTTP 연결 설정
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 응답 코드 확인
            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println("HTTP 요청 실패: " + responseCode);
                throw new IOException("HTTP 요청 실패: " + responseCode);
            }

            // 응답 데이터 읽기
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // JSON 데이터를 JSONObject로 파싱
            JSONObject jsonObject = new JSONObject(response.toString());

            // 웹툰 데이터 추출
            JSONArray data = jsonObject.getJSONArray("data");

            // content.id가 2043인 경우만 필터링하여 데이터 추출
            JSONObject webtoonData = null;
            String g="";
            for (int i = 0; i < data.length(); i++) {
                JSONArray cardGroups = data.getJSONObject(i).getJSONArray("cardGroups");
                for (int j = 0; j < cardGroups.length(); j++) {
                    JSONArray cards = cardGroups.getJSONObject(j).getJSONArray("cards");
                    for (int k = 0; k < cards.length(); k++) {
                        JSONObject content = cards.getJSONObject(k).getJSONObject("content");
                        JSONArray genreFilters = cards.getJSONObject(k).getJSONArray("genreFilters");
                        if (content.getInt("id") ==Integer.parseInt(kkao.getWebtoon_id())) {
                            webtoonData = content;
                            if(genreFilters.length()>1){
                                g=genreFilters.getString(1);
                            }else{
                            g=genreFilters.getString(0);}
                            break;
                        }
                    }
                    if (webtoonData != null) {
                        break;
                    }
                }
                if (webtoonData != null) {
                    break;
                }
            }

            // 필요한 데이터 추출
            if(webtoonData!=null){
                String title = webtoonData.getString("title");

                JSONArray authorsArray = webtoonData.getJSONArray("authors");
                List<String> authors = new ArrayList<>();
                for (int i = 0; i < authorsArray.length(); i++) {
                    authors.add(authorsArray.getJSONObject(i).getString("name"));
                }

                String thumbnailUrl = webtoonData.getString("featuredCharacterImageA");

                JSONArray seoKeywordsArray = webtoonData.getJSONArray("seoKeywords");
                List<String> tags = new ArrayList<>();
                for (int i = 0; i < seoKeywordsArray.length(); i++) {
                    tags.add(seoKeywordsArray.getString(i));
                }
                String tagsString = String.join("#", tags);

                // WebtoonDTO 객체에 데이터 설정
                WebtoonDTO webtoonDTO = new WebtoonDTO();
                webtoonDTO.setWebtoon_id(kkao.getWebtoon_id()); // 예시로 2043을 설정
                webtoonDTO.setPlatform(2); // 예시로 플랫폼 2를 설정
                webtoonDTO.setThumbnail_url(thumbnailUrl+".png");
                webtoonDTO.setWebtoon_name(title);
                webtoonDTO.setAuthor(String.join("/", authors));
                webtoonDTO.setTags(tagsString);
                webtoonDTO.setGenre(g);
                System.out.println(webtoonDTO);
                System.out.println(webtoonDTO);
                System.out.println(webtoonDTO);System.out.println(webtoonDTO);

                System.out.println(webtoonDTO);
                System.out.println(webtoonDTO);


                // 데이터 출력 (옵션)
                System.out.println("WebtoonDTO: " + webtoonDTO);
                return webtoonDTO;
            }
        }
        return kkao;
    }

    @PostMapping("/webtoon/one/like")
    public ResponseEntity<String> likeComment(@RequestBody Map<String, Object> request,@AuthenticationPrincipal CustomUserDetails c) {
        int commentNo = Integer.parseInt((String.valueOf(request.get("commentNo"))));
        boolean isSelected= (boolean) request.get("isSelected");

        try {
            CommentLikeDTO commentLikeDTO=new CommentLikeDTO();
            commentLikeDTO.setCommentNo(commentNo);
            commentLikeDTO.setUserNo(c.getUserDTO().getUserNo());

            if (isSelected) {
                int result=webToonService.likeDelete(commentLikeDTO);
                int newLikeCount = -1;
                return ResponseEntity.ok(String.format("{\"success\": true, \"newLikeCount\": %d}", newLikeCount));
            } else {
                int result=webToonService.likeInsert(commentLikeDTO);
                int newLikeCount = 1;
                return ResponseEntity.ok(String.format("{\"success\": true, \"newLikeCount\": %d}", newLikeCount));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"success\": false}");
        }
    }

    @PostMapping("/webtoon/one/dislike")
    public ResponseEntity<String> dislikeComment(@RequestBody Map<String, Object> request,@AuthenticationPrincipal CustomUserDetails c) {
        int commentNo = Integer.parseInt((String.valueOf(request.get("commentNo"))));
        boolean isSelected= (boolean) request.get("isSelected");

        try {
            CommentLikeDTO commentLikeDTO=new CommentLikeDTO();
            commentLikeDTO.setCommentNo(commentNo);
            commentLikeDTO.setUserNo(c.getUserDTO().getUserNo());

            if (isSelected) {
                int result=webToonService.dislikeDelete(commentLikeDTO);
                int newLikeCount = -1;
                return ResponseEntity.ok(String.format("{\"success\": true, \"newLikeCount\": %d}", newLikeCount));
            } else {
                int result=webToonService.dislikeInsert(commentLikeDTO);
                int newLikeCount = 1;
                return ResponseEntity.ok(String.format("{\"success\": true, \"newLikeCount\": %d}", newLikeCount));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"success\": false}");
        }
    }




}
