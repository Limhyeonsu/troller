package com.toy.troller.project.issue.web;

import java.util.HashMap;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toy.troller.member.model.LoginUserInfo;
import com.toy.troller.model.IssueManage;
import com.toy.troller.project.issue.model.IssueDto;
import com.toy.troller.project.issue.service.IssueService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/project/issue")
public class IssueController {
	
	private IssueService IssueService;
	
	@GetMapping("/{cmd}")
	@ResponseBody
	public Object projectWbsIssueGetMapping(@PathVariable("cmd") String cmd, @ModelAttribute("issueDto") IssueDto issueDto
								, @AuthenticationPrincipal LoginUserInfo user) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		switch(cmd) {
		case "list":
			List<IssueManage> list = IssueService.findList(issueDto);
			result.put("list", list);
			result.put("totCnt", list.size());
			break;
		case "detail":
			result.put("data", IssueService.findDetail(issueDto));
			break;
		default:
			break;
		}
		
		return result;
	}
	
	@PostMapping("/{cmd}")
	@ResponseBody
	public Object projectWbsIssuePostMapping(@PathVariable("cmd") String cmd, @ModelAttribute("issueDto") IssueDto issueDto
									, @AuthenticationPrincipal LoginUserInfo user) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		System.out.println(issueDto.toString());
		switch(cmd) {
		case "save":
			IssueService.save(issueDto, user);
			result.put("result", true);
			result.put("msg", "성공");
			break;
		case "update":
			IssueService.update(issueDto, user);
			result.put("result", true);
			result.put("msg", "성공");
			break;
		case "delete":
			IssueService.delete(issueDto, user);
			result.put("result", true);
			result.put("msg", "성공");
			break;
		default:
			break;
		}
		return result;
	}
}
