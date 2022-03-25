package com.toy.troller.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Entity
@Table(name="TB_PROJECT_WBS_ISSUE_MANAGE")
public class WbsIssueManage {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long wbsIssueId;
	
	@Column
	private Long issueId;
	
	@Column
	private Long proId;
	
	@Column(nullable = false, length = 300)
	private String title;
	
	@Column(nullable = false, length = 500)
	private String content;
	
	@Embedded
	private CUDInfo cudInfo = new CUDInfo();
	
	@Builder(builderMethodName="build")
	public WbsIssueManage(Long wbsIssueId, Long proId, Long issueId, String title, String content) {
		this.wbsIssueId = wbsIssueId;
		this.proId = proId;
		this.issueId = issueId;
		this.title = title;
		this.content = content;
	}
}
