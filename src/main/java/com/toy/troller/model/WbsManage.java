package com.toy.troller.model;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Entity
@Table(name="TB_PROJECT_WBS_MANAGE")
public class WbsManage {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long wbsId;
	
//	@Column
//	private Long proId;
	
	@Column(nullable = false)
	private int depth;
	
	@Column(nullable = false)
	private long parent;
	
	@Column(nullable = false, length = 300)
	private String title;
	
	@Column(nullable = false, length = 500)
	private String content;
	
//	@Column
//	private String developer;
	
	@Column
	private LocalDate schStartDt;
	
	@Column
	private LocalDate schEndDt;
	
	@Column
	private LocalDate realStartDt;
	
	@Column
	private LocalDate realEndDt;
	
	@Column(length = 2)
	private String completeYn;
	
	@Column(length = 1)
	@ColumnDefault("\'Y\'")
	private String modifyYn;
	
	@Embedded
	private CUDInfo cudInfo = new CUDInfo();
	
//	@OneToMany(mappedBy = "parent")
//	private List<WbsManage> subWbs;
	
	@ManyToOne
	@JoinColumn(name="PRO_ID", updatable = false)
	private ProjectManage projectManage;
	
	@OneToOne
	@JoinColumn(name="developer", updatable = false)
	UserInfo userinfo;
	
	@Builder(builderMethodName="build")
	public WbsManage(Long wbsId, int depth, int parent, String title, String content, UserInfo userInfo
			, LocalDate schStartDt, LocalDate realStartDt, LocalDate schEndDt, LocalDate realEndDt, ProjectManage projectManage) {
		this.wbsId = wbsId;
//		this.proId = proId;
		this.depth = depth;
		this.parent = parent;
		this.title = title;
		this.content = content;
//		this.developer = developer;
		this.schStartDt = schStartDt;
		this.realStartDt = realStartDt;
		this.schEndDt = schEndDt;
		this.realEndDt = realEndDt;
		this.userinfo = userInfo;
		this.projectManage = projectManage;
	}
	
}
