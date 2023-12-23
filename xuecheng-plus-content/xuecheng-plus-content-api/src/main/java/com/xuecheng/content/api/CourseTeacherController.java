package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CourseTeacherDto;
import com.xuecheng.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Zihao Qin
 * @Date 2023/12/21 17:02
 */
@Api(tags = "教师信息接口")
@RestController
@RequestMapping("/courseTeacher")
public class CourseTeacherController {
    @Autowired
    private CourseTeacherService courseTeacherService;

    @ApiOperation("展示课程信息")
    @GetMapping("/list/{courseId}")
    public List<CourseTeacherDto> listCourseTeacher(@PathVariable Long courseId) {
        return courseTeacherService.listCourseTeacher(courseId);
    }

    @ApiOperation("新增教师")
    @PostMapping
    public void saveCourseTeacher(@RequestBody CourseTeacherDto courseTeacherDto) {
        courseTeacherService.saveCourseTeacher(courseTeacherDto);
    }

    @ApiOperation("删除教师")
    @DeleteMapping("/course/{courseId}/{teacherId}")
    public void deleteCourseTeacher(@PathVariable("courseId") Long courseId, @PathVariable("teacherId") Long teacherId) {
        courseTeacherService.deleteCourseTeacher(courseId, teacherId);
    }
}
