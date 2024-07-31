package com.multi.toonGather.webtoon;

import com.multi.toonGather.webtoon.model.dto.WebtoonDTO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
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
                    if (content.getInt("id") == 3318) {
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
        webtoonDTO.setWebtoon_id("2043"); // 예시로 2043을 설정
        webtoonDTO.setPlatform(2); // 예시로 플랫폼 2를 설정
        webtoonDTO.setThumbnail_url(thumbnailUrl);
        webtoonDTO.setWebtoon_name(title);
        webtoonDTO.setAuthor(String.join("/", authors));
        webtoonDTO.setTags(tagsString);
        webtoonDTO.setGenre(g);

        // 데이터 출력 (옵션)
        System.out.println("WebtoonDTO: " + webtoonDTO);
            }
        }
    }
}