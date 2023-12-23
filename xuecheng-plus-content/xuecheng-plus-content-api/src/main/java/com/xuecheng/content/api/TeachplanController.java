package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Zihao Qin
 * @Date 2023/12/20 10:36
 */
@Api(tags = "课程计划接口")
@RestController
@RequestMapping("/teachplan")
public class TeachplanController {
    @Autowired
    private TeachplanService teachplanService;

    @ApiOperation("查询课程树形结构")
    @GetMapping("/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId) {
        return teachplanService.getTreeNodes(courseId);
    }

    @ApiOperation("新增/修改课程")
    @PostMapping
    public void saveTeachplan(@RequestBody @Validated SaveTeachplanDto teachplan) {
        teachplanService.saveTeachplan(teachplan);
    }

    @ApiOperation("删除课程计划")
    @DeleteMapping("/{id}")
    public void deleteTeachplan(@PathVariable Long id) {
        teachplanService.deleteTeachplan(id);
    }

    @ApiOperation("上移章节")
    @PostMapping("/moveup/{id}")
    public void moveUpTeachplan(@PathVariable Long id){
        teachplanService.moveUpTeachplan(id);
    }

    @ApiOperation("下移章节")
    @PostMapping("/movedown/{id}")
    public void moveDownTeachplan(@PathVariable Long id){
        teachplanService.moveDownTeachplan(id);
    }


}
