package com.toy.troller.project.wbs.web;

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

import com.toy.troller.client.model.ClientDto;
import com.toy.troller.client.service.ClientService;
import com.toy.troller.member.model.LoginUserInfo;
import com.toy.troller.model.ClientManage;
import com.toy.troller.model.WbsManage;
import com.toy.troller.project.wbs.model.WbsDto;
import com.toy.troller.project.wbs.service.WbsService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/project/wbs")
public class WbsController {
	
	private WbsService wbsService;

	@GetMapping("/{cmd}")
	@ResponseBody
	public Object wbsGetMapping(@PathVariable("cmd") String cmd, @ModelAttribute("wbsDto") WbsDto wbsDto
								, @AuthenticationPrincipal LoginUserInfo user) {
		//wbsService.test();
		HashMap<String, Object> result = new HashMap<String, Object>();
		switch(cmd) {
		case "list":
			List<WbsManage> list = wbsService.findList(wbsDto);
			result.put("list", list);
			result.put("totCnt", list.size());
			break;
		case "detail":
			result.put("data", wbsService.findDetail(wbsDto));
			break;
		case "treelist":
			result = wbsService.findTreeList(wbsDto);
		default:
			break;
		}
		
		return result;
	}
	
	@PostMapping("/{cmd}")
	@ResponseBody
	public Object wbsPostMapping(@PathVariable("cmd") String cmd, @ModelAttribute("wbsDto") WbsDto wbsDto
									, @AuthenticationPrincipal LoginUserInfo user) {
		HashMap<String, Object> result = new HashMap<String, Object>();

		switch(cmd) {
		case "save":
			wbsService.save(wbsDto, user);
			result.put("result", true);
			result.put("msg", "성공");
			break;
		case "update":
			wbsService.update(wbsDto, user);
			result.put("result", true);
			result.put("msg", "성공");
			break;
		case "delete":
			wbsService.delete(wbsDto, user);
			result.put("result", true);
			result.put("msg", "성공");
			break;
		default:
			break;
		}
		return result;
	}
}
