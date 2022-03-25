package com.toy.troller.project.issue.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.toy.troller.model.IssueManage;

public interface IssueRepository extends JpaRepository<IssueManage, Long>{
	Optional<IssueManage> findByIssueId(String issueId);
	Optional<List<IssueManage>> findAllByProId(Long proId);
}
