package com.toy.troller.infoshare.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * 2021.08.07.임현수
 * 정보공유 게시판 DTO
 */
@Builder
@AllArgsConstructor
@Data
public class InfoShareDto {
    private Long nttId;         //게시판 id
    private String nttGb;       //게시판 구분 (1.java 2.js 3.기타)
    private String title;       //제목
    private String content;     //내용
    LocalDateTime regDt;        //작성 일시
    private String userName;    //작성자 이름
    private String userId;      //작성자 id
    //private List<MultipartFile> file; //파일
    private MultipartFile file; //파일
    private String saveFileName;  //저장 파일 명
    private String originFileName;  //원본 파일 명
    private String delYn;

}
