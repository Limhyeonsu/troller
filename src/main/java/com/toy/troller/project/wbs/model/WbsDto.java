package com.toy.troller.project.wbs.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.toy.troller.model.ProjectManage;
import com.toy.troller.model.WbsManage;

import lombok.Data;

@Data
public class WbsDto {
	private Long wbsId;
	private Long proId;
	private String depth;
	private String parent;
	private String title;
	private String content;
	private Long developer;
	private String str_schStartDt;
	private String str_realStartDt;
	private String str_schEndDt;
	private String str_realEndDt;

	private ProjectManage projectManage;
	
	private LocalDate schStartDt;
	private LocalDate realStartDt;
	private LocalDate schEndDt;
	private LocalDate realEndDt;
	
	public WbsManage toEntity() {
		return WbsManage.build()
				.wbsId(wbsId)
//				.proId(proId)
				.depth(depth != null ? Integer.parseInt(depth) : 0)
				.parent(parent != null ? Integer.parseInt(parent) : 0)
				.title(title)
				.content(content)
//				.developer(developer)
				.schStartDt(LocalDate.parse(str_schStartDt, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				//.realStartDt(LocalDate.parse(str_realStartDt, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.schEndDt(LocalDate.parse(str_schEndDt, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				//.realEndDt(LocalDate.parse(str_realEndDt, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.build();
	}
}
