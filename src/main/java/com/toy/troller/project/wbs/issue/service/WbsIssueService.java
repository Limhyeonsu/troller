package com.toy.troller.project.wbs.issue.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.toy.troller.member.model.LoginUserInfo;
import com.toy.troller.model.WbsIssueManage;
import com.toy.troller.model.WbsManage;
import com.toy.troller.project.wbs.issue.model.WbsIssueDto;
import com.toy.troller.project.wbs.model.WbsDto;
import com.toy.troller.project.wbs.service.WbsRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WbsIssueService {
	
	private WbsIssueRepository wbsIssueRepository;
	
	public List<WbsIssueManage> findList(WbsIssueDto wbsIssuedto){
		return wbsIssueRepository.findAllByProId(wbsIssuedto.getProId())
				.orElseThrow(() -> new IllegalArgumentException("조회된 데이터가 없습니다."));
	}
	
	public WbsIssueManage findDetail(WbsIssueDto wbsIssuedto) {
		return wbsIssueRepository.findById(wbsIssuedto.getWbsIssueId())
				.orElseThrow(() -> new IllegalArgumentException("조회된 데이터가 없습니다."));
	}
	
	public void save(WbsIssueDto wbsIssuedto, LoginUserInfo user) {	
		WbsIssueManage wbsIssue = wbsIssuedto.toEntity();
		wbsIssue.getCudInfo().setRegDt(LocalDate.now());
		wbsIssue.getCudInfo().setRegUser(user.getUsername());
		wbsIssueRepository.save(wbsIssue);
	}
	
	public void update(WbsIssueDto wbsIssuedto, LoginUserInfo user) {	
		WbsIssueManage wbsIssue = wbsIssuedto.toEntity();
		wbsIssue.getCudInfo().setUpdateDt(LocalDate.now());
		wbsIssue.getCudInfo().setUpdateUser(user.getUsername());
		wbsIssueRepository.save(wbsIssue);
	}
	
	public void delete(WbsIssueDto wbsIssuedto, LoginUserInfo user) {
		Optional<WbsIssueManage> wbsIssueWrapper = wbsIssueRepository.findById(wbsIssuedto.getWbsIssueId());
		wbsIssueWrapper.ifPresent(selectWbsIssue -> {
			selectWbsIssue.getCudInfo().setDelDt(LocalDate.now());
			selectWbsIssue.getCudInfo().setDelYn("Y");
			selectWbsIssue.getCudInfo().setDelUser(user.getUsername());
			wbsIssueRepository.save(selectWbsIssue);
		});
	}
}
