package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;

import java.util.List;

/**
 * @Author Zihao Qin
 * @Date 2023/12/18 16:35
 */
public interface CourseCategoryService extends IService<CourseCategory> {
    // 查询课程分类
    List<CourseCategoryTreeDto> queryTreeNodes(String id);
}
