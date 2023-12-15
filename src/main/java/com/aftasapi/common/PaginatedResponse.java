package com.aftasapi.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.List;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponse<T extends java.io.Serializable > implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 1L;

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
