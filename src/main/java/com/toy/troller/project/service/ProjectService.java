package com.toy.troller.project.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.toy.troller.client.service.ClientRepository;
import com.toy.troller.common.Common;
import com.toy.troller.member.model.LoginUserInfo;
import com.toy.troller.model.ProjectManage;
import com.toy.troller.project.model.ProjectDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProjectService {
	ProjectRepository projectRepository;
	ClientRepository clientRepository;
	
	public List<ProjectManage> findList(ProjectDto projectDto){
		return projectRepository.findAll();
	}
	
	public ProjectManage findDetail(ProjectDto projectDto) {
		return projectRepository.findById(projectDto.getProId())
				.orElseThrow(() -> new IllegalArgumentException("조회된 데이터가 없습니다."));
	}
	
	public void save(ProjectDto projectDto, LoginUserInfo user, HttpServletRequest req) {
		projectDto.setClientManage(clientRepository.findById(projectDto.getClientId())
									.orElseThrow(() -> new IllegalArgumentException("조회된 클라이언트가 없습니다.")));
		ProjectManage project =  projectDto.toEntity();
		project.getCudInfo().setRegDt(LocalDate.now());
		project.getCudInfo().setRegUser(user.getUsername());
		
		if(!projectDto.getImageFile().isEmpty()) {
			project.setImage(Common.saveFile("/resources/projectImages/", projectDto.getImageFile(), true));
        }
		projectRepository.save(project);
	}
	
	public void update(ProjectDto projectDto, LoginUserInfo user) {	
		ProjectManage project =  projectDto.toEntity();
		project.getCudInfo().setUpdateDt(LocalDate.now());
		project.getCudInfo().setUpdateUser(user.getUsername());
		projectRepository.save(project);
	}
	
	public void delete(ProjectDto projectDto, LoginUserInfo user) {
		Optional<ProjectManage> projectWrapper = projectRepository.findById(projectDto.getProId());
		projectWrapper.ifPresent(selectProject -> {
			selectProject.getCudInfo().setDelDt(LocalDate.now());
			selectProject.getCudInfo().setDelYn("Y");
			selectProject.getCudInfo().setDelUser(user.getUsername());
			projectRepository.save(selectProject);
		});
	}
}
