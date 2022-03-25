package com.toy.troller.model;

import java.util.Collection;
import java.util.Iterator;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.catalina.Group;
import org.apache.catalina.Role;
import org.apache.catalina.UserDatabase;
import org.hibernate.annotations.Target;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Entity
@Table(name = "TB_USER_INFO")
public class UserInfo{
	@Id
	@Column
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long uniqId;
	
	@Column(nullable = false, unique = true, length = 30)
	private String userId;
	
	@Column(nullable = false, length = 200)
	private String password;
	
	@Column(nullable = false, length = 30)
	private String name;
	
	@Column(length = 30)
	private String phoneNum;
	
	@Column(length = 30)
	private String email;
	
	@Column
	private String image;
	
	@Column
	private String note;
	
	@Embedded
	private CUDInfo cudInfo = new CUDInfo();
	/*@OneToOne
	@JoinColumn(name = "AUTH_CODE")
	private long auth_code;*/
	
	/*
	 @Transient - column으로 사용하지 않는 변수
	 @Column(insertable = false, updatable = false) 인설트, 업데이트 제외
	 */
	
	@Builder(builderMethodName = "build")
	public UserInfo(Long uniqId, String userId, String password, String name, String image, String note) {
		this.uniqId = uniqId;
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.image = image;
		this.note = note;
	}
	
	
}
