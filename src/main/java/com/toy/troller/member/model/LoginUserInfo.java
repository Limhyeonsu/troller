package com.toy.troller.member.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginUserInfo extends User{
	private String name;
	
	public LoginUserInfo(String userId, String password, String name,List<GrantedAuthority> authorities) {
		super(userId, password
                , true, true, true, true
                , authorities);
		this.name = name;
	}
}
