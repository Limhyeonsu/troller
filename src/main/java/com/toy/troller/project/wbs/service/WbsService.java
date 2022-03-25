package com.toy.troller.project.wbs.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.troller.member.model.LoginUserInfo;
import com.toy.troller.model.ProjectManage;
import com.toy.troller.model.QWbsManage;
import com.toy.troller.model.WbsManage;
import com.toy.troller.project.service.ProjectRepository;
import com.toy.troller.project.wbs.model.WbsDto;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@Repository
@AllArgsConstructor
public class WbsService{
	
	private WbsRepository wbsRepository;
	
	private ProjectRepository projectRepository;
	
	@PersistenceContext
    private EntityManager em;
	
	public List<WbsManage> findList(WbsDto wbsDto){
//		return wbsRepository.findAllByProId(wbsDto.getProId())
//				.orElseThrow(() -> new IllegalArgumentException("조회된 데이터가 없습니다."));
		//return wbsRepository.findAll();
//		System.out.println(wbsDto.toString());
		ProjectManage projectManage = projectRepository.findByProId(wbsDto.getProId())
			.orElseThrow(() -> new IllegalArgumentException("조회된 데이터가 없습니다."));
//		
//		
		return wbsRepository.findAllByProjectManage(projectManage)
				.orElseThrow(() -> new IllegalArgumentException("조회된 데이터가 없습니다."));
		
		
//		return wbsRepository.findAllByProId(wbsDto.getProId())
//				.orElseThrow(() -> new IllegalArgumentException("조회된 데이터가 없습니다."));
	}
	
	public WbsManage findDetail(WbsDto wbsDto) {
		WbsManage wbsManage = wbsRepository.findById(wbsDto.getWbsId())
								.orElseThrow(() -> new IllegalArgumentException("조회된 데이터가 없습니다."));
		
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		QWbsManage qwbsManage = QWbsManage.wbsManage;
		
		queryFactory.selectFrom(qwbsManage)
					.where(qwbsManage.wbsId.eq(wbsDto.getWbsId()))
					.fetchOne();
		
		return wbsManage;
	}
	
	public HashMap<String, Object> findTreeList(WbsDto wbsDto){
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		
		QWbsManage wbsManage = QWbsManage.wbsManage;
		HashMap<String, Object> treeResult = new HashMap<String, Object>();
		
		List<WbsManage> list = selectWbsTree(wbsDto.getProId(), 0);		
		
//		for(WbsManage second : list) {
//			second.setSubWbs(selectWbsTree(wbsDto.getProId(), second.getWbsId()));
//			
//			for(WbsManage third : second.getSubWbs()) {
//				third.setSubWbs(selectWbsTree(wbsDto.getProId(), third.getWbsId()));
//			}
//			
//		}
		
		treeResult.put("list", list);
		
		return treeResult;	
	}
	
	public List<WbsManage> selectWbsTree(long proId, long parent){
		
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		
		QWbsManage wbsManage = QWbsManage.wbsManage;
		
		return queryFactory.selectFrom(wbsManage)
		//.where(wbsManage.proId.eq(proId))
		.where(wbsManage.parent.eq(parent))
		.fetch();
	}

	
	public void save(WbsDto wbsDto, LoginUserInfo user) {
		WbsManage wbs = wbsDto.toEntity();
		wbs.getCudInfo().setRegDt(LocalDate.now());
		wbs.getCudInfo().setRegUser(user.getUsername());
		
		ProjectManage projectManage = projectRepository.findById(wbsDto.getProId())
			.orElseThrow(() -> new IllegalArgumentException("선택된 프로젝트에대한 정보가 없습니다."));
		
		wbs.setProjectManage(projectManage);
		
		wbsRepository.save(wbs);
	}
	
	public void update(WbsDto wbsDto, LoginUserInfo user) {	
		WbsManage wbs = wbsRepository.save(wbsDto.toEntity());
		wbs.getCudInfo().setUpdateDt(LocalDate.now());
		wbs.getCudInfo().setUpdateUser(user.getUsername());
		wbsRepository.save(wbs);
	}
	
	public void delete(WbsDto wbsDto, LoginUserInfo user) {
		Optional<WbsManage> wbsWrapper = wbsRepository.findById(wbsDto.getWbsId());
		wbsWrapper.ifPresent(selectWbs -> {
			selectWbs.getCudInfo().setDelDt(LocalDate.now());
			selectWbs.getCudInfo().setDelYn("Y");
			selectWbs.getCudInfo().setDelUser(user.getUsername());
			wbsRepository.save(selectWbs);
		});
	}
	
	public void test() {
		System.out.println("test@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		
		QWbsManage wbsManage = QWbsManage.wbsManage;
		
		List<WbsManage> result = queryFactory.selectFrom(wbsManage).where(wbsManage.title.eq("test")).fetch();
		
		for(WbsManage val : result) {
			System.out.println(val.toString());
		}
		System.out.println("end@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		
	}
}
