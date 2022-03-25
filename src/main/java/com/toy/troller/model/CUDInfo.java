package com.toy.troller.model;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@DynamicInsert
public class CUDInfo{
	
	@Column(length = 1)
	@ColumnDefault("\'Y\'")
	private String useYn;
	
	@Column
	private LocalDate regDt;
	
	@Column(length = 30)
	private String regUser;
	
	@Column
	private LocalDate updateDt;
	
	@Column(length = 30)
	private String updateUser;
	
	@Column
	private LocalDate delDt;
	
	@Column(length = 1)
	private String delYn;
	
	@Column(length = 30)
	private String delUser;
}
