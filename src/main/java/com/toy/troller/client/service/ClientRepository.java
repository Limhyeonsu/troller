package com.toy.troller.client.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.toy.troller.model.ClientManage;
import com.toy.troller.model.UserInfo;

public interface ClientRepository extends JpaRepository<ClientManage, Long>, QuerydslPredicateExecutor<ClientManage>{
	Optional<ClientManage> findByClientId(String clientId);
	Optional<List<ClientManage>> findAllByName(String name);
}
