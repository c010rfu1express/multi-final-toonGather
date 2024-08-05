package com.multi.toonGather.webtoon.controller;

import com.multi.toonGather.security.CustomUserDetails;
import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.webtoon.model.dto.CommentDTO;
import com.multi.toonGather.webtoon.model.dto.WebtoonDTO;
import com.multi.toonGather.webtoon.model.dto.WtUserLogDTO;
import com.multi.toonGather.webtoon.model.dto.WtUserSaveDTO;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class WebtoonController {

    @Autowired
    private final WebToonService webToonService;

    public WebtoonController(WebToonService webToonService) {
        this.webToonService = webToonService;
    }

    @GetMapping(value = {"/","webtoon/test"})
    public String Webtoon( Model model){
        LocalDate currentDate = LocalDate.now();

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
            model.addAttribute("comments",comments);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(c!=null){
        userDTO.setUserNo(c.getUserDTO().getUserNo());
        wtUserSaveDTO.setWebtoonNo(webtoonDTO.getWebtoon_no());
        wtUserSaveDTO.setUserNo(c.getUserDTO().getUserNo());
            try {
        wtUserSaveDTO=webToonService.WebToonSelectSave(wtUserSaveDTO);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println(wtUserSaveDTO);
        model.addAttribute("save",wtUserSaveDTO);
        model.addAttribute("user",userDTO);
        model.addAttribute("one",webtoonDTO);


        return "webtoon/one";
    }

    @GetMapping("/webtoon/search")
    public String searchWebtoon(@RequestParam("search") String search,
                                @RequestParam("Method") String Method,
                                @RequestParam("platform") String platform,Model model) throws UnsupportedEncodingException {



        List<WebtoonDTO>webtoonDTOS=new ArrayList<>();
        if(Method.equals("tags")){
            WebtoonDTO webtoonDTO=new WebtoonDTO();
            webtoonDTO.setTags(search);
            if(platform.equals("NAVER")){
                webtoonDTO.setPlatform(1);
            }else if(platform.equals("KAKAO")){
                webtoonDTO.setPlatform(2);
            }
            webtoonDTOS=webToonService.searchWebtoon(webtoonDTO);


        }
        model.addAttribute("Method",Method);

        model.addAttribute("platform",platform);

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
                ,"timetable_fri","timetable_sat","timetable_sun"};
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
                            g=genreFilters.getString(1);
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

                String thumbnailUrl = webtoonData.getString("titleImageA");

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
                webtoonDTO.setThumbnail_url(thumbnailUrl);
                webtoonDTO.setWebtoon_name(title);
                webtoonDTO.setAuthor(String.join("/", authors));
                webtoonDTO.setTags(tagsString);
                webtoonDTO.setGenre(g);

                // 데이터 출력 (옵션)
                System.out.println("WebtoonDTO: " + webtoonDTO);
                return webtoonDTO;
            }
        }
        return kkao;
    }



}
