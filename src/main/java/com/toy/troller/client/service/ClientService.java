package com.toy.troller.client.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.toy.troller.client.model.ClientDto;
import com.toy.troller.member.model.LoginUserInfo;
import com.toy.troller.member.service.MemberRepository;
import com.toy.troller.model.ClientManage;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClientService {
	
	ClientRepository clientRepository;
	

	public List<ClientManage> findList(ClientDto clientDto){
		return clientRepository.findAll();
	}
	
	public ClientManage findDetail(ClientDto clientDto) {
		return clientRepository.findById(clientDto.getClientId())
				.orElseThrow(() -> new IllegalArgumentException("조회된 데이터가 없습니다."));
	}
	
	public void save(ClientDto clientDto, LoginUserInfo user) {
		ClientManage client = clientDto.toEntity();
		client.getCudInfo().setRegDt(LocalDate.now());
		client.getCudInfo().setRegUser(user.getUsername());
		clientRepository.save(client);
	}
	
	public void update(ClientDto clientDto, LoginUserInfo user) {
		ClientManage client = clientDto.toEntity();
		client.getCudInfo().setUpdateDt(LocalDate.now());
		client.getCudInfo().setUpdateUser(user.getUsername());
		clientRepository.save(client);
	}
	
	public void delete(ClientDto clientDto, LoginUserInfo user) {
		Optional<ClientManage> clientWrapper = clientRepository.findById(clientDto.getClientId());
		clientWrapper.ifPresent(selectClient -> {
			selectClient.getCudInfo().setDelDt(LocalDate.now());
			selectClient.getCudInfo().setDelYn("Y");
			selectClient.getCudInfo().setDelUser(user.getUsername());
			clientRepository.save(selectClient);
		});
	}
}
