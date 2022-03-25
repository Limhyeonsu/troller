package com.toy.troller.model;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDto<DTO, EN> {
    private List<DTO> dtoList;        //dto 리스트
    private int totalPage;            // 총 페이지 번호
    private int page;                 // 현재 페이지 번호
    private int size;                 // 목록 사이즈
    private int start, end;           // 시작페이지, 끝페이지 번호
    private boolean prev, next;       // 이전, 다음
    private List<Integer> pageList;   // 페이지 번호 목록

    public PageResultDto(Page<EN> result, Function<EN, DTO> fn){
        dtoList = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {
        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();

        int tmpEnd = (int)(Math.ceil(page/5.0) * 5);    //테스트 후 10으로 수정하기
        start = tmpEnd - 4;   //테스트 후 9로 수정하기
        prev = start > 1;
        end = totalPage > tmpEnd ? tmpEnd: totalPage;
        next = totalPage > tmpEnd;
        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
