package com.toy.troller.client.model;

import com.toy.troller.model.CUDInfo;
import com.toy.troller.model.ClientManage;

import lombok.Data;

@Data
public class ClientDto{
	private Long clientId;
	private String name;
	private String phoneNumber;
	private String district;
	
	public ClientManage toEntity() {
		return ClientManage.build()
				.clientId(clientId)
				.name(name)
				.phoneNumber(phoneNumber)
				.district(district)
				.build();
	}
}
