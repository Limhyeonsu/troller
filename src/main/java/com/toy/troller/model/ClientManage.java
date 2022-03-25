package com.toy.troller.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Entity
@Table(name = "TB_CLIENT_MANAGE")
public class ClientManage{
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long clientId;
	
	@Column(nullable = false, length = 30)
	private String name;
	
	@Column(length = 200)
	private String phoneNumber;
	
	@Column(length = 30)
	private String district;
	
	@Embedded
	private CUDInfo cudInfo = new CUDInfo();
	
	/*@OneToOne
	@JoinColumn(name = "AUTH_CODE")
	private long auth_code;*/
	
	@Builder(builderMethodName = "build")
	public ClientManage(Long clientId, String name, String phoneNumber, String district) {
		System.out.println(LocalDateTime.now());
		this.clientId = clientId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.district = district;
	}
}
