package com.hr.management.system.common.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {
    private List<T> data;
    private int page;
    private int size;
    private long total;
    private int totalPages;
}