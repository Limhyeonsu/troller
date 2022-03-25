package com.toy.troller.client.web;

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
import org.springframework.web.servlet.ModelAndView;

import com.toy.troller.client.model.ClientDto;
import com.toy.troller.client.service.ClientService;
import com.toy.troller.member.model.LoginUserInfo;
import com.toy.troller.member.service.MemberService;
import com.toy.troller.member.web.MemberController;
import com.toy.troller.model.ClientManage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping(value="/client")
public class CilentController {
	
	private ClientService clientService;

	
	@GetMapping("/{cmd}")
	@ResponseBody
	public Object clientGetMapping(@PathVariable("cmd") String cmd, @ModelAttribute("clientDto") ClientDto clientDto
								, @AuthenticationPrincipal LoginUserInfo user) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		switch(cmd) {
		case "list":
			List<ClientManage> list = clientService.findList(clientDto);
			result.put("list", list);
			result.put("totCnt", list.size());
			break;
		case "detail":
			result.put("data", clientService.findDetail(clientDto));
			break;
		default:
			break;
		}
		
		return result;
	}
	
	@PostMapping("/{cmd}")
	@ResponseBody
	public Object clientPostMapping(@PathVariable("cmd") String cmd, @ModelAttribute("clientDto") ClientDto clientDto
									, @AuthenticationPrincipal LoginUserInfo user) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		System.out.println(clientDto.toString());
		switch(cmd) {
		case "save":
			clientService.save(clientDto, user);
			result.put("result", true);
			result.put("msg", "성공");
			break;
		case "update":
			clientService.update(clientDto, user);
			result.put("result", true);
			result.put("msg", "성공");
			break;
		case "delete":
			clientService.delete(clientDto, user);
			result.put("result", true);
			result.put("msg", "성공");
			break;
		default:
			break;
		}
		return result;
	}
	
}
