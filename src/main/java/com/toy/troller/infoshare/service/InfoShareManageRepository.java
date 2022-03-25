package com.toy.troller.infoshare.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.toy.troller.model.InfoShareManage;

/**
 * 2021.08.07. 임현수
 * 정보공유 게시판 Repository
 */
public interface InfoShareManageRepository extends JpaRepository<InfoShareManage, Long>, QuerydslPredicateExecutor<InfoShareManage>{

}
