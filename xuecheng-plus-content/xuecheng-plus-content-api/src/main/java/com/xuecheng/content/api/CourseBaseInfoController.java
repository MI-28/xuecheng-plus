package com.xuecheng.content.api;

import com.xuecheng.base.exception.ValidationGroups;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Zihao Qin
 * @Date 2023/12/4 21:06
 */
@RestController
@Api(tags = "课程基础信息模块")
@RequestMapping("/course")
public class CourseBaseInfoController {
    @Autowired
    private CourseBaseInfoService courseBaseInfoService;

    @ApiOperation("分页查询课程")
    @PostMapping("/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDto queryCourseParams) {
        PageResult<CourseBase> pageResult = courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParams);
        return pageResult;
    }

    @ApiOperation("新增课程")
    @PostMapping
    public CourseBaseInfoDto createCourseBase(@RequestBody @Validated(ValidationGroups.Update.class) AddCourseDto addCourseDto) {
        // 获取机构ID
        Long companyId = 10010l;
        return courseBaseInfoService.createCourseBase(companyId, addCourseDto);
    }

    @ApiOperation("按 ID 查询课程")
    @GetMapping("/{id}")
    public CourseBaseInfoDto getCourseBaseById(@PathVariable Long id) {
        return courseBaseInfoService.getCourseBaseById(id);
    }

    @ApiOperation("修改课程信息")
    @PutMapping
    public CourseBaseInfoDto updateCourseBase(@RequestBody @Validated EditCourseDto dto){
        Long companyId = 10010l;
        return courseBaseInfoService.updateCourseBase(companyId, dto);
    }

    @ApiOperation("删除课程信息")
    @DeleteMapping("/{id}")
    public void deleteCourseBase(@PathVariable Long id) {
        courseBaseInfoService.deleteCourseBase(id);
    }
}
