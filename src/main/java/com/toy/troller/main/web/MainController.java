package com.toy.troller.main.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.toy.troller.member.model.LoginUserInfo;
import com.toy.troller.member.service.MemberService;
import com.toy.troller.project.model.ProjectDto;
import com.toy.troller.project.service.ProjectService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class MainController {
	
	MemberService memberService;
	ProjectService projectService;
	
	@GetMapping("/")
	public String main(Model model) {
		
		model.addAttribute("memberList", memberService.findList());
		model.addAttribute("projectList", projectService.findList(new ProjectDto()));
		return "./main";
	}
	
	@GetMapping("/wjdals")
	public String wjdals() {
		return "./test/wjdals";
	}
	
	//퍼블리싱 테스트페이지(dialog)
	@GetMapping("/test")
    public String test(@AuthenticationPrincipal LoginUserInfo user) {
        return "./dialog/dialog";
    }
	
	//퍼블리싱 테스트페이지(summernote)
		@GetMapping("/test2")
	    public String test2(@AuthenticationPrincipal LoginUserInfo user) {
	        return "./infoshare_input";
	    }
	
	@GetMapping("/{cmd}")
	public String mainFlag(@PathVariable(required = false) String cmd) {
		
		switch(cmd) {
		case "project":
			return "./project/project";
		default:
			return "./main";
		}
	}
}
