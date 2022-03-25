package com.toy.troller.infoshare.service;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import com.toy.troller.model.InfoShareFileManage;
/**
 * 2021.08.31. 임현수
 * 정보공유 게시판 File Repository
 */
public interface InfoShareFileManageRepository extends JpaRepository<InfoShareFileManage, Long>, QuerydslPredicateExecutor<InfoShareFileManage>{
	
	@Query("SELECT saveName FROM InfoShareFileManage WHERE ntt_id = :nttId AND delYn = 'N'")
	public String findByNttId(@Param("nttId")Long nttId);
	
}
