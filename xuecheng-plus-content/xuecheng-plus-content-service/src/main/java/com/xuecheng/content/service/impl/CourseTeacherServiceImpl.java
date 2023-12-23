package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.dto.CourseTeacherDto;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zihao Qin
 * @Date 2023/12/21 17:20
 */
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements CourseTeacherService {
    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    // 展示课程教师
    @Override
    public List<CourseTeacherDto> listCourseTeacher(Long courseId) {
        if(courseId == null) XueChengPlusException.cast("课程 ID 不能为空");

        LambdaQueryWrapper<CourseTeacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseTeacher::getCourseId, courseId);
        List<CourseTeacher> courseTeachers = courseTeacherMapper.selectList(wrapper);

        if(courseTeachers == null || courseTeachers.size() == 0) return null;

        List<CourseTeacherDto> res = new ArrayList<>();

        courseTeachers.stream().forEach(e->{
            CourseTeacherDto courseTeacherDto = new CourseTeacherDto();
            BeanUtils.copyProperties(e, courseTeacherDto);
            res.add(courseTeacherDto);
        });

        return res;
    }

    // 新增/修改 教师
    @Override
    public void saveCourseTeacher(CourseTeacherDto courseTeacherDto) {
        // 校验
        if(courseTeacherDto == null) XueChengPlusException.cast("教师信息为空");
        if(StringUtils.isBlank(courseTeacherDto.getTeacherName())) XueChengPlusException.cast("教师名称为空");
        if(StringUtils.isBlank(courseTeacherDto.getPosition())) XueChengPlusException.cast("教师职位为空");
        if(courseTeacherDto.getCourseId() == null) XueChengPlusException.cast("课程ID为空");

        // 新增
        if (courseTeacherDto.getId() == null) {
            CourseTeacher courseTeacher = new CourseTeacher();
            BeanUtils.copyProperties(courseTeacherDto, courseTeacher);
            courseTeacher.setCreateDate(LocalDateTime.now());
            int insert = courseTeacherMapper.insert(courseTeacher);
            if(insert <= 0) XueChengPlusException.cast("教师新增失败，请稍后重试");
            return;
        }

        // 修改
        CourseTeacher t = courseTeacherMapper.selectById(courseTeacherDto.getId());
        BeanUtils.copyProperties(courseTeacherDto, t);
        int update = courseTeacherMapper.updateById(t);
        if(update <= 0) XueChengPlusException.cast("教师修改失败，请稍后重试");
    }

    // 删除教师
    @Override
    public void deleteCourseTeacher(Long courseId, Long teacherId) {
        // 校验
        if(courseId == null) XueChengPlusException.cast("课程ID为空");
        if(teacherId == null) XueChengPlusException.cast("教师ID为空");

        // 删除
        LambdaQueryWrapper<CourseTeacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseTeacher::getCourseId, courseId);
        wrapper.eq(CourseTeacher::getId, teacherId);
        courseTeacherMapper.delete(wrapper);
    }
}
