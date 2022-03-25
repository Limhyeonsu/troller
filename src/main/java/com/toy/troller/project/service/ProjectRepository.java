package com.toy.troller.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.toy.troller.model.ClientManage;
import com.toy.troller.model.ProjectManage;

public interface ProjectRepository extends JpaRepository<ProjectManage, Long>, QuerydslPredicateExecutor<ProjectManage>{
	Optional<ProjectManage> findByProId(Long proId);
	Optional<List<ProjectManage>> findAllByTitle(String name);
}
