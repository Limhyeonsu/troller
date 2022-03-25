package com.toy.troller.project.wbs.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.toy.troller.model.ClientManage;
import com.toy.troller.model.ProjectManage;
import com.toy.troller.model.WbsManage;

public interface WbsRepository extends JpaRepository<WbsManage, Long>, QuerydslPredicateExecutor<WbsManage>{
	Optional<WbsManage> findByWbsId(String wbsId);
	Optional<List<WbsManage>> findAllByProjectManage(ProjectManage proectManage);
	//Optional<List<WbsManage>> findAllByProId(Long proId);
	//Optional<List<WbsManage>> findAllByProId(Long ProId);
}
