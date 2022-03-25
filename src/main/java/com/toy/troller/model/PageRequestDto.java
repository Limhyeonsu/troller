package com.toy.troller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * [2021.08.22] 임현수
 * 페이징 처리 및 검색 dto
 *
 */
@Builder
@AllArgsConstructor
@Data
public class PageRequestDto {

    private int page;
    private int size;
    private String type;
    private String keyword;

    public PageRequestDto() {
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page-1, size, sort);
    }
}
