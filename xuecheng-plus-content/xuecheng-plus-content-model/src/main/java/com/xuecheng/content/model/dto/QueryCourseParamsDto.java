package com.xuecheng.content.model.dto;

import lombok.Data;

/**
 * 课程查询条件类
 * @Author Zihao Qin
 * @Date 2023/12/4 21:01
 */
@Data
public class QueryCourseParamsDto {
    //审核状态
    private String auditStatus;

    //课程名称
    private String courseName;

    //发布状态
    private String publishStatus;
}
