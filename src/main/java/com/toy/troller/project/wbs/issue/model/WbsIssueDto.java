package com.toy.troller.project.wbs.issue.model;

import java.time.LocalDateTime;

import com.toy.troller.model.WbsIssueManage;
import com.toy.troller.project.wbs.model.WbsDto;

import lombok.Data;

@Data
public class WbsIssueDto {
	private Long wbsIssueId;
	private Long proId;
	private Long issueId;
//	private String title;
	private String content;
	private String str_schStartDt;
	private String str_schEndDt;
	
	public WbsIssueManage toEntity() {
		return WbsIssueManage.build()
				.wbsIssueId(wbsIssueId)
				.proId(proId)
				.issueId(issueId)
//				.title(title)
				.content(content)
				.build();
	}
}
