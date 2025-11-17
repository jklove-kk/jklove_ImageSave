package com.liujie.pictureBackend.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一分页包装返回类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {

    /**
     * 当前页号
     */
    private int page = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认降序）
     */
    private String sortOrder = "descend";
}
