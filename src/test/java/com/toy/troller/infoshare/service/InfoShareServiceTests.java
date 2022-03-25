package com.toy.troller.infoshare.service;

import com.toy.troller.infoshare.model.InfoShareDto;
import com.toy.troller.model.InfoShareManage;
import com.toy.troller.model.PageRequestDto;
import com.toy.troller.model.PageResultDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.apache.tomcat.jni.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
public class InfoShareServiceTests {

    @Autowired
    private InfoShareService service;

    //@Test
    public void testList() {
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(5).build();
        PageResultDto<InfoShareDto, InfoShareManage> resultDto = service.findAll(pageRequestDto);

        System.out.println("PREV : "+resultDto.isPrev());
        System.out.println("NEXT : "+resultDto.isNext());
        System.out.println("TOTAL : "+resultDto.getTotalPage());
        System.out.println("---------------------------------");
        for(InfoShareDto dto : resultDto.getDtoList()) {
            System.out.println(dto);
        }
        System.out.println("---------------------------------");
        resultDto.getPageList().forEach(i -> System.out.println(i));
    }
    
    //@Test
    public void testSearch() {
    	PageRequestDto pageRequestDto = PageRequestDto.builder()
    			.page(1)
    			.size(5)
    			.type("t")
    			.keyword("테스트")
    			.build();
    	PageResultDto<InfoShareDto, InfoShareManage> resultDto = service.findAll(pageRequestDto);
    	
    	for(InfoShareDto dto : resultDto.getDtoList()) {
            System.out.println(dto);
        }
    	System.out.println("---------------------------------");
        resultDto.getPageList().forEach(i -> System.out.println(i));
    }
    
    //@Test
    //@Transactional
    public void testRegister() {
    	String writerData = "str1,str2,str3,str4";
    	MockMultipartFile file = new MockMultipartFile("202108311661345211.png", "테스트.png", "multipart/form-data", writerData.getBytes(StandardCharsets.UTF_8));
    	
    	InfoShareDto dto = InfoShareDto.builder()
				.nttGb("2")
				.title("등록 테스트")
				.content("등록 테스트")
				.regDt(LocalDateTime.now())
				.userName("이면슈")
				.userId("lhs")
				.file(file)
				.build();
    	service.register(dto, "lhs");
    	
    }
    
    @Test
    public void testFindDetail() {
//    	service.getInfoShareDetail(1L);
    }
    
}
