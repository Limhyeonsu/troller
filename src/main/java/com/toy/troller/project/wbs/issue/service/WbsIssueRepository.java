package com.toy.troller.project.wbs.issue.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.toy.troller.model.WbsIssueManage;
import com.toy.troller.model.WbsManage;


public interface WbsIssueRepository extends JpaRepository<WbsIssueManage, Long>, QuerydslPredicateExecutor<WbsIssueManage>{
	Optional<WbsIssueManage> findByWbsIssueId(String wbsIssueId);
	Optional<List<WbsIssueManage>> findAllByProId(Long proId);
}
