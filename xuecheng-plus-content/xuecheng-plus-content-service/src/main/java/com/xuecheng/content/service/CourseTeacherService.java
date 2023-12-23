package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.CourseTeacherDto;
import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

/**
 * @Author Zihao Qin
 * @Date 2023/12/21 17:19
 */
public interface CourseTeacherService extends IService<CourseTeacher> {
    // 展示教师
    List<CourseTeacherDto> listCourseTeacher(Long courseId);
    // 新增/修改 教师
    void saveCourseTeacher(CourseTeacherDto courseTeacherDto);
    // 删除教师
    void deleteCourseTeacher(Long courseId, Long teacherId);
}
