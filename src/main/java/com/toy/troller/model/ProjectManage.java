package com.toy.troller.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.IntSequenceGenerator;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Entity
@Table(name="TB_PROJECT_MANAGE")
//@JsonIdentityInfo(generator = IntSequenceGenerator.class, property="pro_id")
public class ProjectManage{
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="pro_id")
	private Long proId;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Column(nullable = false, length = 500)
	private String content;
	
	@Column
	private int cost;
	
//	@Column
//	private Long clientId;
	
	@Column
	private LocalDateTime startDt;
	
	@Column
	private LocalDateTime endDt;
	
	@Column
	private String image;
	
	@Embedded
	private CUDInfo cudInfo = new CUDInfo();
	
	@OneToOne
	@JoinColumn(name = "client_id", updatable = false)
	ClientManage clientManage;
//	@OneToMany(fetch = FetchType.EAGER)
//	@JoinColumn(name = "pro_id")
//	@OneToMany(mappedBy="projectManage", fetch = FetchType.EAGER)
//	private List<WbsManage> wbsManage= new ArrayList<>();
	
	@Builder(builderMethodName="build")
	public ProjectManage(Long proId, String title, String content, int cost, Long clientId, String image, ClientManage clientManage) {
		this.proId = proId;
		this.title = title;
		this.content = content;
		this.cost = cost;
//		this.clientId = clientId;
		this.image = image;
		this.clientManage = clientManage;
	}
}
