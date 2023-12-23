package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

/**
 * @Author Zihao Qin
 * @Date 2023/12/14 9:40
 */
public interface CourseBaseInfoService extends IService<CourseBase> {
    // 分页查询课程
    PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto courseParamsDto);

    // 新增课程
    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);

    // 按 ID 查询课程
    CourseBaseInfoDto getCourseBaseById(Long id);

    // 修改课程基本信息
    CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto);

    // 删除课程信息
    void deleteCourseBase(Long id);
}
