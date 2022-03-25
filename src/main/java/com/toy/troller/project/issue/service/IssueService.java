package com.toy.troller.project.issue.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.toy.troller.member.model.LoginUserInfo;
import com.toy.troller.model.IssueManage;
import com.toy.troller.project.issue.model.IssueDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class IssueService {
	
	private IssueRepository issueRepository;
	
	public List<IssueManage> findList(IssueDto Issuedto){
		return issueRepository.findAllByProId(Issuedto.getProId())
				.orElseThrow(() -> new IllegalArgumentException("조회된 데이터가 없습니다."));
	}
	
	public IssueManage findDetail(IssueDto Issuedto) {
		return issueRepository.findById(Issuedto.getIssueId())
				.orElseThrow(() -> new IllegalArgumentException("조회된 데이터가 없습니다."));
	}
	
	public void save(IssueDto Issuedto, LoginUserInfo user) {	
		IssueManage issue = Issuedto.toEntity();
		issue.getCudInfo().setRegDt(LocalDate.now());
		issue.getCudInfo().setRegUser(user.getUsername());
		issueRepository.save(issue);
	}
	
	public void update(IssueDto Issuedto, LoginUserInfo user) {	
		IssueManage issue = Issuedto.toEntity();
		issue.getCudInfo().setUpdateDt(LocalDate.now());
		issue.getCudInfo().setUpdateUser(user.getUsername());
		issueRepository.save(issue);
	}
	
	public void delete(IssueDto Issuedto, LoginUserInfo user) {
		Optional<IssueManage> IssueWrapper = issueRepository.findById(Issuedto.getIssueId());
		IssueWrapper.ifPresent(selectIssue -> {
			selectIssue.getCudInfo().setDelDt(LocalDate.now());
			selectIssue.getCudInfo().setDelYn("Y");
			selectIssue.getCudInfo().setDelUser(user.getUsername());
			issueRepository.save(selectIssue);
		});
	}
}
