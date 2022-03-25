package com.toy.troller.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/** 2021.08.29. 임현수
 * 정보공유 첨부파일 Entity class
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_INFO_SHARE_FILE_MANAGE")
public class InfoShareFileManage {

    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long fileId;                // 파일 id

    @ManyToOne(targetEntity=InfoShareManage.class)
    @JoinColumn(name = "ntt_id")
    InfoShareManage infoShareManage;  //게시판 join

    @Column(name = "orig_name")
    String origName;            //파일 원본 이름

    @Column(name = "save_name")
    String saveName;            //파일 저장 이름

    @Column(name = "reg_dt")
    LocalDateTime regDt;        //저장 일시

    @Column(name = "reg_user")
    String regUser;             //저장 유저

    @Column(name = "update_dt")
    LocalDateTime updateDt;     //수정 일시

    @Column(name = "update_user")
    String updateUser;          //수정 유저

    @Column(name = "del_dt")
    LocalDateTime delDt;        //삭제 일시

    @Column(name = "del_yn")
    String delYn;               //삭제 여부

    @Column(name = "del_user")
    String delUser;             //삭제 유저

    @Builder(builderMethodName = "build")
    public InfoShareFileManage(Long fileId,InfoShareManage infoShareManage, String origName, String saveName, LocalDateTime regDt, String regUser,
                           LocalDateTime updateDt, String updateUser, LocalDateTime delDt, String delYn, String delUser) {
        this.fileId = fileId;
        this.infoShareManage = infoShareManage;
        this.origName = origName;
        this.saveName = saveName;
        this.regDt = regDt;
        this.regUser = regUser;
        this.updateDt = updateDt;
        this.updateUser = updateUser;
        this.delDt = delDt;
        this.delYn = delYn;
        this.delUser = delUser;
    }
}
