package com.project.cafesns.service;


import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.search.KeywordDto;
import com.project.cafesns.model.dto.search.SearchDto;
import com.project.cafesns.model.entitiy.Cafe;
import com.project.cafesns.model.entitiy.Hashtag;
import com.project.cafesns.model.entitiy.Post;
import com.project.cafesns.repository.CafeRepository;
import com.project.cafesns.repository.HashtagRepository;
import com.project.cafesns.result.CalculatorImp;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class SearchService {

    //repository
    private final CafeRepository cafeRepository;
    private final HashtagRepository hashtagRepository;

    //calculator
    private final CalculatorImp calculatorImp;

    //검색
    //TODO : 아직 Stream 사용법을 몰라서 키워드마다 Repository조회를 안해도 될거같고, 굉장히 비효율적인것 같다 Stream공부해서 전체 리스트 한번만 조회받고 거기서 찾아내는 방식을 시도해보자
    //-> 검색 기능 강화 완료 Stream, map, JSONsimple 활용해서 지역 빅데이터 활용한 검색기능 구현
    public ResponseEntity<?> search(String keyword) throws IOException, ParseException {

        //들어온 keyword 글자단위로 분해
        String[] alphabet = keyword.split("");

        //hashmap 키 값(JSON객체 생성시 필요)
        String[] address = {"서울특별시", "부산광역시", "인천광역시", "대구광역시",
                "광주광역시", "대전광역시", "울산광역시", "세종특별자치시",
                "경기도", "강원도", "충청북도", "충청남도", "경상북도",
                "경상남도", "전라북도", "전라남도", "제주특별자치도"};

        //전국 지역 hashmap 변수 선언
        Map<String, List<String>> regionList = new HashMap<>();

        //전체 카페 리스트 조회
        List<Cafe> cafeList = cafeRepository.findAll();

        //필요한 List 변수 선언
        List<Cafe> filterAddressList = new ArrayList<>();
        List<Cafe> filterNameList = new ArrayList<>();
        List<Cafe> filterHashtagList = new ArrayList<>();

        //검색어 틀 생성
        KeywordDto keywordDto = new KeywordDto();
        List<String> typeList = new ArrayList<>();
        List<String> keywordOfAddressList = new ArrayList<>();

        //JSON 파일토대로 JSON 객체 만들어주기
        Reader reader = new FileReader("src/main/resources/address.json");
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        JSONArray data = (JSONArray) jsonObject.get("data");

        //전국 지역 hashmap 리스트 생성
        for (int i = 0; i < data.size(); i++) {
            JSONObject jsonObject1 = (JSONObject) data.get(i);
            JSONArray addressList = (JSONArray) jsonObject1.get(address[i]);
            regionList.put(address[i], addressList);
        }

        //keyword가 지역에 해당한다면 해당하는 지역값 뽑아서 저장
        for (String key : regionList.keySet()) {
            if (key.contains(keyword)) {
                keywordOfAddressList.add(key);
            } else {
                for (int i = 0; i < regionList.get(key).size(); i++) {
                    if (regionList.get(key).get(i).contains(keyword)) {
                        keywordOfAddressList.add(regionList.get(key).get(i));
                    }
                }
            }
        }

        //keyword로 카페리스트에서 이름으로 검색 -> 검색 결과 존재시 keyword는 카페 이름으로 검색한것으로 판단
        for (String a : alphabet) {
            filterNameList.addAll(cafeList.stream()
                    .filter(cafe -> (cafe.getCafename().contains(a) || cafe.getCafename().contains(a.toUpperCase())))
                    .collect(Collectors.toList()));
        }

        //검색어의 타입을 판단해서 지정
        //키워드가 주소 목록에 존재하면 검색어 타입에 지역 추가
        if (!keywordOfAddressList.isEmpty()) {
            typeList.add("지역");
            keywordDto.setType(typeList);
            keywordDto.setKeyword(keywordOfAddressList.stream().distinct().collect(Collectors.toList())); //중복되는 검색어 제거
        }

        //키워드로 카페를 찾아서 있으면 검색어 타입에 카페이름 추가
        if (!filterNameList.isEmpty()) {
            typeList.add("카페이름");
            keywordDto.setType(typeList);
            filterNameList.clear();
        }

        //해시태그로 검색한 카페
        if (keyword.startsWith("#")) {
            List<Hashtag> hashtagList = hashtagRepository.findAllByHashtagContains(keyword);
            List<Post> postList = new ArrayList<>();
            hashtagList.forEach(hashtag -> postList.add(hashtag.getPost()));
            postList.forEach(post -> filterHashtagList.add(post.getCafe()));
            filterHashtagList.stream().distinct().collect(Collectors.toList());
        } else {
            try {
                //지역으로 검색한 카페
                if (keywordDto.getType().contains("지역")) {
                    for (String a : keywordOfAddressList) {
                        filterAddressList.addAll(cafeList.stream()
                                .filter(cafe -> cafe.getAddress().contains(a))
                                .collect(Collectors.toList()));
                    }
                    System.out.println("지역으로 검색했습니다.");
                }

                //이름으로 검색한 카페
                if (keywordDto.getType().contains("카페이름")) {
                    for (String a : alphabet) {
                        filterNameList.addAll(cafeList.stream()
                                .filter(cafe -> (cafe.getCafename().contains(a) || cafe.getCafename().contains(a.toUpperCase())))
                                .collect(Collectors.toList()));

                    }
                    System.out.println("카페 이름으로 검색했습니다.");
                }
            } catch (NullPointerException e) {
                return ResponseEntity.status(404).body(ResponseDto.builder().result(false).message("검색 결과가 없습니다.").build());
            }
        }
        //검색 결과 리스트 생성
        List<Cafe> resultList = Stream.of(filterNameList, filterAddressList, filterHashtagList).flatMap(Collection::stream).distinct().collect(Collectors.toList());

        if (resultList.isEmpty()) {
            return ResponseEntity.status(404).body(ResponseDto.builder().result(false).message("검색 결과가 없습니다.").build());
        }
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message(keywordDto.getType() + "으로 검색").data(getSearchResult(resultList)).build());
    }

        //검색 결과 만드는 함수
        public List<SearchDto> getSearchResult (List < Cafe > cafeList) {
            List<SearchDto> searchResult = new ArrayList<>();
            for (Cafe cafe : cafeList) {
                searchResult.add(SearchDto.builder().cafeid(cafe.getId())
                        .cafeid(cafe.getId())
                        .cafename(cafe.getCafename())
                        .avgstar(calculatorImp.getAvgStar(cafe.getPostList()))
                        .logoimg(cafe.getUser() == null ? "" : cafe.getUser().getLogoimg())
                        .address(cafe.getAddress())
                        .addressdetail(cafe.getAddressdetail())
                        .zonenum(cafe.getZonenum()).build());
            }
            return searchResult;
        }
    }
