package com.toy.troller.project.wbs.issue.web;

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
import com.toy.troller.model.WbsIssueManage;
import com.toy.troller.project.wbs.issue.model.WbsIssueDto;
import com.toy.troller.project.wbs.issue.service.WbsIssueService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/project/wbs/issue")
public class WbsIssueController {
	
	private WbsIssueService wbsIssueService;
	
	@GetMapping("/{cmd}")
	@ResponseBody
	public Object projectWbsIssueGetMapping(@PathVariable("cmd") String cmd, @ModelAttribute("WbsIssueDto") WbsIssueDto wbsIssueDto
								, @AuthenticationPrincipal LoginUserInfo user) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		switch(cmd) {
		case "list":
			List<WbsIssueManage> list = wbsIssueService.findList(wbsIssueDto);
			result.put("list", list);
			result.put("totCnt", list.size());
			break;
		case "detail":
			result.put("data", wbsIssueService.findDetail(wbsIssueDto));
			break;
		default:
			break;
		}
		
		return result;
	}
	
	@PostMapping("/{cmd}")
	@ResponseBody
	public Object projectWbsIssuePostMapping(@PathVariable("cmd") String cmd, @ModelAttribute("WbsIssueDto") WbsIssueDto wbsIssueDto
									, @AuthenticationPrincipal LoginUserInfo user) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		System.out.println(wbsIssueDto.toString());
		switch(cmd) {
		case "save":
			wbsIssueService.save(wbsIssueDto, user);
			result.put("result", true);
			result.put("msg", "성공");
			break;
		case "update":
			wbsIssueService.update(wbsIssueDto, user);
			result.put("result", true);
			result.put("msg", "성공");
			break;
		case "delete":
			wbsIssueService.delete(wbsIssueDto, user);
			result.put("result", true);
			result.put("msg", "성공");
			break;
		default:
			break;
		}
		return result;
	}
}
