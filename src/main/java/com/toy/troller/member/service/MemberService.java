package com.toy.troller.member.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.toy.troller.common.Common;
import com.toy.troller.member.model.LoginUserInfo;
import com.toy.troller.member.model.MemberDto;
import com.toy.troller.model.UserInfo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberService  implements UserDetailsService{

	private MemberRepository memberRepository;
	
	public Long joinUser(MemberDto memberDto, LoginUserInfo user, HttpServletRequest req) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        
        if(!memberDto.getImageFile().isEmpty()) {
        	memberDto.setImage(Common.saveFile("/resources/userImages/", memberDto.getImageFile(), true));
        }
        
        return memberRepository.save(memberDto.toEntity()).getUniqId();
	}
	
	public void update(MemberDto memberDto, HttpServletRequest req) {
		//[2021.10.17]임현수 주석 : 사용자 아이디도 수정 가능하므로 아이디로 찾으면 안되서 수정함
		//Optional<UserInfo> userInfoWrapper = memberRepository.findByUserId(memberDto.getUserId());
		Optional<UserInfo> userInfoWrapper = memberRepository.findById(memberDto.getUniqId());

		userInfoWrapper.ifPresent(selectUser -> {
			if(memberDto.getPassword() != null) {
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
			}
			if(!memberDto.getImageFile().isEmpty()) {
				memberDto.setImage(Common.saveFile("/resources/userImages/", memberDto.getImageFile(), true));
			}
			memberRepository.save(memberDto.toEntity());
		});
	}
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		Optional<UserInfo> userInfoWrapper = memberRepository.findByUserId(userId);
		UserInfo userInfo = userInfoWrapper.get();
		List<GrantedAuthority> authorities = new ArrayList<>();

		return new LoginUserInfo(userInfo.getUserId(), userInfo.getPassword(), userInfo.getName(), authorities);
		//		return memberRepository.findByEmail(email)
//				.orElseThrow(() -> new UsernameNotFoundException((email)));
	}
	
	public List<UserInfo> findList(){
		return memberRepository.findAll();
	}
	//[2021.10.16]임현수 추가 : 유저 상세정보 가져오기
	public UserInfo getUserInfo(MemberDto user) {
		Optional<UserInfo> userInfo = memberRepository.findByUserIdAndAndName(user.getUserId(), user.getName());
		return userInfo.get();
	}
}
