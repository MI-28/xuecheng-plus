package com.xuecheng.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeachplanMapper extends BaseMapper<Teachplan> {
    List<TeachplanDto> selectTreeNodes(Long courseId);
}
