package com.toy.troller.project.web;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.toy.troller.member.model.LoginUserInfo;
import com.toy.troller.model.ProjectManage;
import com.toy.troller.project.model.ProjectDto;
import com.toy.troller.project.service.ProjectService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping(value="/project")
public class ProjectController {
	
	private ProjectService projectService;
	
	@GetMapping("/{cmd}")
	@ResponseBody
	public Object projectGetMapping(@PathVariable("cmd") String cmd, @ModelAttribute("projectDto") ProjectDto projectDto
								, @AuthenticationPrincipal LoginUserInfo user, HttpServletRequest req) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		switch(cmd) {
		case "list":
			List<ProjectManage> list = projectService.findList(projectDto);
			result.put("list", list);
			result.put("totCnt", list.size());
			break;
		case "detail":
			result.put("data", projectService.findDetail(projectDto));
			break;
		default:
			break;
		}
		
		return result;
	}
	
	@PostMapping("/{cmd}")
	@ResponseBody
	public Object projectPostMapping(@PathVariable("cmd") String cmd, @ModelAttribute("projectDto") ProjectDto projectDto
									, @AuthenticationPrincipal LoginUserInfo user, HttpServletRequest req) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		System.out.println(projectDto.toString());
		switch(cmd) {
		case "save":
			projectService.save(projectDto, user, req);
			result.put("result", true);
			result.put("msg", "성공");
			break;
		case "update":
			projectService.update(projectDto, user);
			result.put("result", true);
			result.put("msg", "성공");
			break;
		case "delete":
			projectService.delete(projectDto, user);
			result.put("result", true);
			result.put("msg", "성공");
			break;
		default:
			break;
		}
		return result;
	}
}
