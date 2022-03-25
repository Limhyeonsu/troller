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
@Table(name="TB_PROJECT_ISSUE_MANAGE")
public class IssueManage {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long issueId;
	
	@Column
	private Long proId;
	
	@Column
	private int issueCodeId;

	@Column(nullable = false, length = 300)
	private String title;
	
	@Column(nullable = false, length = 500)
	private String content;
	
	@Column(length = 30)
	private String developer;
	
	@Column
	private LocalDateTime occDt;
	
	@Column(length = '1')
	@ColumnDefault("\'Y\'")
	private String modifyYn;
	
	@Embedded
	private CUDInfo cudInfo = new CUDInfo();
	
	@Builder(builderMethodName="build")
	public IssueManage(Long issueId, Long proId, Long wbsIssueId, String title, String content, String developer, LocalDateTime occDt, String modifyYn) {
		this.issueId = issueId;
		this.proId = proId;
		this.title = title;
		this.content = content;
		this.developer = developer;
		this.occDt = occDt;
		this.modifyYn = modifyYn;
	}
}
