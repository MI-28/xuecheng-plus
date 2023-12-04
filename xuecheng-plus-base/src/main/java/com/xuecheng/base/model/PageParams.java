package com.xuecheng.base.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页封装类
 * @Author Zihao Qin
 * @Date 2023/12/4 20:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageParams {
    private Long pageNo = 1L;
    private Long pageSize = 10L;
}