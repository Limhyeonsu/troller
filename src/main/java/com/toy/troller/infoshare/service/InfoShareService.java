package com.toy.troller.infoshare.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.troller.infoshare.model.InfoShareDto;
import com.toy.troller.member.service.MemberRepository;
import com.toy.troller.model.*;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class InfoShareService {

	private MemberRepository memberRepository;
    private InfoShareManageRepository infoShareManageRepository;
    private InfoShareFileManageRepository infoShareFileManageRepository;
    private static final Logger logger = LoggerFactory.getLogger(InfoShareService.class);

    @PersistenceContext
	private EntityManager em;

    //게시판 목록 조회
	public PageResultDto<InfoShareDto,InfoShareManage> findAll(PageRequestDto requestDto) {
		Pageable pageable = requestDto.getPageable(Sort.by("regDt").descending());

		QInfoShareManage qInfoShareManage = QInfoShareManage.infoShareManage;

		BooleanBuilder booleanBuilder = getSearch(requestDto);  // 검색조건 쿼리
		booleanBuilder.and(qInfoShareManage.delYn.contains("N"));  //삭제되지 않은 게시글만 불러오기
		Page<InfoShareManage> result = infoShareManageRepository.findAll(booleanBuilder, pageable);
		
		Function<InfoShareManage, InfoShareDto> fn = (entity ->
				InfoShareDto.builder()
						.nttId(entity.getNttId())
						.nttGb(entity.getNttGb())
						.title(entity.getTitle())
						.content(entity.getContent())
						.regDt(entity.getRegDt())
						.userName(memberRepository.findByUserId(entity.getRegUser()).get().getName())
						.saveFileName(infoShareFileManageRepository.findByNttId(entity.getNttId()))
						.userId(entity.getRegUser())
						.delYn(entity.getDelYn())
						.build());
		return new PageResultDto<>(result, fn);
	}
    
	//게시글 등록
    public void register(InfoShareDto dto, String userId) {
    	logger.info("== 게시글 등록(service) proc....");
    	//게시글 테이블에 등록
    	InfoShareManage entity = infoShareManageRepository.save(
    			InfoShareManage.build()
    			.nttGb(dto.getNttGb())
    			.title(dto.getTitle())
    			.content(dto.getContent())
    			.regDt(LocalDateTime.now())
    			.regUser(userId)
    			.delYn("N")
    			.build()
    			);
    	
    	//첨부파일이 있다면 첨부파일 테이블에 등록
    	if(dto.getFile() != null || !dto.getFile().isEmpty()) {
	    	InfoShareFileManage fileEntity = infoShareFileManageRepository.save(
	    			InfoShareFileManage.build()
	    			.infoShareManage(entity)
	    			.origName(dto.getFile().getOriginalFilename())
	    			.saveName(dto.getSaveFileName())
	    			.regDt(entity.getRegDt())   //저장일시는 게시글 저장일시랑 같아야하나?
	    			.regUser(entity.getRegUser())
	    			.delYn("N")
	    			.build()
	    			);
	    	logger.info("File entity ::: " + fileEntity.toString());
    	}
    			
    	logger.info("entity ::: " + entity.toString());
    }
    
    //게시글 상세조회
    public InfoShareDto getInfoShareDetail(Long nttId) {
    	logger.info("== 게시글 상세 페이지(service)....");
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		QInfoShareFileManage qInfoShareFileManage = QInfoShareFileManage.infoShareFileManage;
		List<InfoShareFileManage> resultFile = queryFactory.selectFrom(qInfoShareFileManage)
				.where(qInfoShareFileManage.infoShareManage().nttId.eq(nttId), (qInfoShareFileManage.delYn.eq("N")))
				.fetch();

    	Optional<InfoShareManage> result = infoShareManageRepository.findById(nttId);
		//MultipartFile file = infoShareFileManageRepository.findByNttId(nttId);

    	InfoShareDto dto = InfoShareDto.builder()
				.nttId(result.get().getNttId())
				.nttGb(result.get().getNttGb())
				.title(result.get().getTitle())
				.content(result.get().getContent())
				.regDt(result.get().getRegDt())
				//.userId(result.get().getRegUser())
				.userName(memberRepository.findByUserId(result.get().getRegUser()).get().getName())
				.userId(result.get().getRegUser())
				//.saveFileName(infoShareFileManageRepository.findByNttId(nttId))
				.saveFileName(resultFile.size() == 0 ? null : resultFile.get(0).getSaveName())
				.originFileName(resultFile.size() == 0 ? null : resultFile.get(0).getOrigName())
				.build();

    	logger.info("getFiles() ::: " + dto.getFile());
    	return result.isPresent() ? dto : null;

    }
    
    //검색 querydsl 처리
    private BooleanBuilder getSearch(PageRequestDto requestDto) {
    	String type = requestDto.getType();
    	BooleanBuilder booleanBuilder = new BooleanBuilder();
    	QInfoShareManage qInfoShareManage = QInfoShareManage.infoShareManage;
    	String keyword = requestDto.getKeyword();
    	BooleanExpression expression = qInfoShareManage.nttId.gt(0L);
    	booleanBuilder.and(expression);
    	
    	if(type == null || type.trim().length() == 0) {
    		return booleanBuilder;
    	}
    	
    	// 검색 조건 지정
    	BooleanBuilder conditionBuilder = new BooleanBuilder();
    	if(type.contains("c")) {   // 카테고리
    		conditionBuilder.or(qInfoShareManage.nttGb.contains(keyword));
    	}
    	if(type.contains("t")) {   // 제목 
    		conditionBuilder.or(qInfoShareManage.title.contains(keyword));
    	}
    	
    	booleanBuilder.and(conditionBuilder);
    	
    	return booleanBuilder;
    	
    }

	//게시글 삭제
	public void remove(Long nttId) {
		logger.info("== 게시글 삭제(service) proc....");

		Optional<InfoShareManage> result = infoShareManageRepository.findById(nttId);

		if(result.isPresent()) {
			InfoShareManage entity = result.get();

			entity.setDelYn("Y");
			infoShareManageRepository.save(entity);
		}
	}

	//첨부파일 삭제
	@Transactional
	public long removeFile(Long nttId, String saveFileName){
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		QInfoShareFileManage qInfoShareFileManage = QInfoShareFileManage.infoShareFileManage;

		long result = queryFactory.update(qInfoShareFileManage).where(qInfoShareFileManage.infoShareManage().nttId.eq(nttId).and(qInfoShareFileManage.saveName.eq(saveFileName)))
				.set(qInfoShareFileManage.delYn, "Y")
				.execute();

		logger.info("result ::: " + result);
		return result;
	}

	//게시글 수정
	public void modify(InfoShareDto dto, String userId) {
		logger.info("==== 게시글 수정 proc ====");
		//수정은 카테고리, 제목, 내용, 첨부파일 가능
		//String beforeFileNm = req.getParameter("beforeFileNm");
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		QInfoShareFileManage qInfoShareFileManage = QInfoShareFileManage.infoShareFileManage;
		InfoShareFileManage resultFile = null;
		if(dto.getSaveFileName() != null && !dto.getSaveFileName().isEmpty()) {
			 resultFile = queryFactory.selectFrom(qInfoShareFileManage)
					.where(qInfoShareFileManage.infoShareManage().nttId.eq(dto.getNttId()), (qInfoShareFileManage.delYn.eq("N")),
							qInfoShareFileManage.saveName.eq(dto.getSaveFileName()))
					.fetchOne();
		}
		Optional<InfoShareManage> result = infoShareManageRepository.findById(dto.getNttId());

		if(result.isPresent()) {
			InfoShareManage entity = result.get();

			entity.setNttGb(dto.getNttGb());
			entity.setTitle(dto.getTitle());
			entity.setContent(dto.getContent());
			entity.setUpdateDt(LocalDateTime.now());
			entity.setUpdateUser(userId);

			InfoShareFileManage fileEntity = resultFile;
			if (fileEntity == null) {
				fileEntity = new InfoShareFileManage();
			}
			if (dto.getSaveFileName() != null && !dto.getSaveFileName().isEmpty()) {
				fileEntity.setInfoShareManage(entity);
				fileEntity.setOrigName(dto.getFile().getOriginalFilename());
				fileEntity.setSaveName(dto.getSaveFileName());
				fileEntity.setDelYn("N");
				if (resultFile == null) {
					fileEntity.setRegDt(LocalDateTime.now());
					fileEntity.setRegUser(userId);
				} else {
					fileEntity.setUpdateDt(LocalDateTime.now());
					fileEntity.setUpdateUser(userId);
				}
				infoShareFileManageRepository.save(fileEntity);
			}
			infoShareManageRepository.save(entity);
		}
	}
}
