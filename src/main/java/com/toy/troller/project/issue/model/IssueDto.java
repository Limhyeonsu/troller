package com.toy.troller.project.issue.model;

import java.time.LocalDateTime;

import com.toy.troller.model.IssueManage;

import lombok.Data;

@Data
public class IssueDto {
	private Long issueId;
	private Long  proId;
	private String title;
	private String content;
	private String developer;
	private LocalDateTime occDt;
	private String modifyYn;
	
	public IssueManage toEntity() {
		return IssueManage.build()
				.issueId(issueId)
				.proId(proId)
				.title(title)
				.content(content)
				.developer(developer)
				.occDt(occDt)
				.modifyYn(modifyYn)
				.build();
	}
}
