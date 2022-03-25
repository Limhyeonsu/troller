package com.toy.troller.member.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.web.multipart.MultipartFile;

import com.toy.troller.model.UserInfo;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class MemberDto{
	private Long uniqId;
	private String userId;
	private String password;
	private String name;
	private String phoneNum;
	private String email;
	private String image;
	private String note;
	private MultipartFile imageFile;
	//private int auth_code;
	
//	@Builder
//	public MemberDto(Long uniqId, String email, String password) {
//		this.uniqId = uniqId;
//		this.email = email;
//		this.password = password;
//	}
	
	public UserInfo toEntity() {
		return UserInfo.build()
					.userId(userId)
					//.email(email)
					.password(password)
					.uniqId(uniqId)
					.name(name)
					.image(image)
					.note(note)
					.build();
	}
}
