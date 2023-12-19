package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Zihao Qin
 * @Date 2023/12/18 14:45
 */
@Slf4j
@RestController
@RequestMapping("/course-category")
public class CourseCategoryController {
    @Autowired
    private CourseCategoryService courseCategoryService;

    @GetMapping("/tree-nodes")
    private List<CourseCategoryTreeDto> queryTreeNodes(){
        return courseCategoryService.queryTreeNodes("1");
    }
}
