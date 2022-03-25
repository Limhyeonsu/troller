package com.toy.troller.member.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toy.troller.model.UserInfo;

public interface MemberRepository extends JpaRepository<UserInfo, Long>{
	Optional<UserInfo> findByEmail(String email);
	Optional<UserInfo> findByUserId(String userId);
	List<UserInfo> findAll();
	//[2021.10.16] 임현수 추가
	Optional<UserInfo> findByUserIdAndAndName(String userId, String name);
}
