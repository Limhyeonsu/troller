package com.toy.troller.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import javax.persistence.*;

/** 2021.08.07. 임현수
 * 정보공유 게시판 Entity class
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_INFO_SHARE_MANAGE")
public class InfoShareManage {

    @Id
    @Column(name="ntt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nttId;                 //게시판 id

    @Column(name="ntt_gb")
    private String nttGb;               //게시판 구분 (1.java 2.js 3.기타)
    
    @Column(name="title", columnDefinition = "TEXT")
    private String title;               //제목
    
    @Column(name="content")
    private String content;             //내용
    
    @Column(name="reg_dt")
    private LocalDateTime regDt;        //작성 일시
    
    @Column(name="reg_user")
    private String regUser;             //작성자
    
    @Column(name="update_dt")
    private LocalDateTime updateDt;     //수정 일시
    
    @Column(name="update_user")
    private String updateUser;          //수정자
    
    @Column(name="del_dt")
    private LocalDateTime delDt;        //삭제 일시
    
    @Column(name="del_yn")
    private String delYn;               //삭제 여부
    
    @Column(name="del_user")
    private String delUser;             //삭제자

    @Builder(builderMethodName = "build")
    public InfoShareManage(Long nttId, String nttGb, String title, String content, LocalDateTime regDt, String regUser,
    		LocalDateTime updateDt, String updateUser, LocalDateTime delDt, String delYn, String delUser) {
        this.nttId = nttId;
    	this.nttGb = nttGb;
        this.title = title;
        this.content = content;
        this.regDt = regDt;
        this.regUser = regUser;
        this.updateDt = updateDt;
        this.updateUser = updateUser;
        this.delDt = delDt;
        this.delYn = delYn;
        this.delUser = delUser;
    }
}
