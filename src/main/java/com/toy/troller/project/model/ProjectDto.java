package com.toy.troller.project.model;

import org.springframework.web.multipart.MultipartFile;

import com.toy.troller.model.ClientManage;
import com.toy.troller.model.ProjectManage;

import lombok.Data;

@Data
public class ProjectDto {
	private Long proId;
	private String title;
	private String content;
	private int cost;
	private Long clientId;
	private String image;
	private ClientManage clientManage;
	private MultipartFile imageFile;
	
	public ProjectManage toEntity() {
		return ProjectManage.build()
				.proId(proId)
				.title(title)
				.content(content)
				.cost(cost)
//				.clientId(clientId)
				.image(image)
				.clientManage(clientManage)
				.build();
	}
}
