package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;

import java.util.List;

/**
 * @Author Zihao Qin
 * @Date 2023/12/20 11:52
 */
public interface TeachplanService extends IService<Teachplan> {
    // 查询 TreeNodes
    List<TeachplanDto> getTreeNodes(Long courseId);

    // 新增/修改 Teachplan
    void saveTeachplan(SaveTeachplanDto teachplan);

    // 删除 Teachplan
    void deleteTeachplan(Long id);

    // 上移章节
    void moveUpTeachplan(Long id);

    // 下移章节
    void moveDownTeachplan(Long id);
}
