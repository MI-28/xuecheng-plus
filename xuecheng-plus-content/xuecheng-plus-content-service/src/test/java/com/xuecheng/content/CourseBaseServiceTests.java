package com.xuecheng.content;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author Zihao Qin
 * @Date 2023/12/14 10:40
 */
@Slf4j
@SpringBootTest
public class CourseBaseServiceTests {
    @Autowired
    private CourseBaseInfoService courseBaseInfoService;

    @Test
    public void testQueryCourseBaseList(){
        PageParams pageParams = new PageParams(1l, 3l);
        QueryCourseParamsDto courseParamsDto = new QueryCourseParamsDto();
        courseParamsDto.setAuditStatus("202002");
        PageResult<CourseBase> pageResult = courseBaseInfoService.queryCourseBaseList(pageParams, courseParamsDto);
        System.out.println(pageResult);
    }

    @Test
    public void testCreateCourseBase(){

    }
}
