package com.toy.troller.member.web;



import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toy.troller.client.model.ClientDto;
import com.toy.troller.member.model.LoginUserInfo;
import com.toy.troller.member.model.MemberDto;
import com.toy.troller.member.service.MemberService;
import com.toy.troller.model.ClientManage;
import com.toy.troller.model.UserInfo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@AllArgsConstructor
public class MemberController {
	private MemberService memberService;
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    
    // 회원가입 페이지
    @GetMapping("/user/signup")
    public String dispSignup() {
        return "./user/signup";
    }

    // 회원가입 처리
    @PostMapping("/user/joinuser")
    @ResponseBody
    public HashMap<String, Object> execSignup(@ModelAttribute("memberdto")MemberDto memberDto
    		, @AuthenticationPrincipal LoginUserInfo user, HttpServletRequest req) {
    	System.out.println(memberDto.toString());
    	memberService.joinUser(memberDto, user, req);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("msg", "성공");
        result.put("result", true);
        
        return result;
    }
    
    @PostMapping("/user/update")
    @ResponseBody
    public Object updateUser(@ModelAttribute("memberDto")MemberDto memberDto, @AuthenticationPrincipal LoginUserInfo user
        , HttpServletRequest req) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        memberService.update(memberDto, req);
        result.put("result", true);
        result.put("msg", "성공");
        return result;
    }

    // 로그인 페이지
    @GetMapping("/user/login")
    public String dispLogin() {
        return "./login/login";
    }

    // 접근 거부 페이지
    @GetMapping("/user/denied")
    public String dispDenied() {
        return "./denied";
    }
    
    @GetMapping("/user/{cmd}")
	@ResponseBody
	public Object memberGetMapping(@PathVariable("cmd") String cmd, @ModelAttribute("clientDto") MemberDto memberDto
								, @AuthenticationPrincipal LoginUserInfo user) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		switch(cmd) {
		case "list":
			List<UserInfo> list = memberService.findList();
			result.put("list", list);
			result.put("totCnt", list.size());
			break;
		case "detail":
		    //[2021.10.16] 임현수 추가
            if(memberDto.getUserId() == null) {
                memberDto.setUserId(user.getUsername());
                memberDto.setName(user.getName());
            }
            UserInfo userInfo = memberService.getUserInfo(memberDto);
            log.info("user info :::::::: "+userInfo);
            result.put("userInfo", userInfo);
			break;
		default:
			break;
		}
		
		return result;
	}
    
}
