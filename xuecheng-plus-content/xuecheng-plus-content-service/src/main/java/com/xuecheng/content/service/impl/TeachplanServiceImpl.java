package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author Zihao Qin
 * @Date 2023/12/20 11:54
 */
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements TeachplanService {
    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> getTreeNodes(Long courseId) {
        if(courseId == null) XueChengPlusException.cast("课程 ID 为空");

        List<TeachplanDto> teachplanDtos = teachplanMapper.selectTreeNodes(courseId);

        return teachplanDtos;
    }

    /**
     * 新增/修改课程计划
     * @Author Zihao Qin
     * @Date 2023/12/21 14:56
     **/
    @Override
    public void saveTeachplan(SaveTeachplanDto teachplan) {
        // 计算小节个数
        LambdaQueryWrapper<Teachplan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Teachplan::getParentid, teachplan.getParentid());
        wrapper.eq(Teachplan::getCourseId, teachplan.getCourseId());
        Integer count = teachplanMapper.selectCount(wrapper);

        if(teachplan.getId() == null){
            // 保存课程计划
            Teachplan t = new Teachplan();
            BeanUtils.copyProperties(teachplan, t);
            t.setOrderby(count+1);
            t.setCreateDate(LocalDateTime.now());
            t.setChangeDate(LocalDateTime.now());
            int insert = teachplanMapper.insert(t);
            if (insert <= 0) {
                XueChengPlusException.cast("课程章节保存失败");
            }
            return;
        }

        // 修改课程章节
        Teachplan t = teachplanMapper.selectById(teachplan.getId());
        if (t == null) {
            XueChengPlusException.cast("课程章节 ID 不存在");
        }
        BeanUtils.copyProperties(teachplan, t);
        t.setChangeDate(LocalDateTime.now());
        teachplanMapper.updateById(t);
    }

    // 删除 Teachplan
    @Override
    public void deleteTeachplan(Long id) {
        // 获取 Teachplan
        Teachplan teachplan = teachplanMapper.selectById(id);
        if (teachplan == null) {
            XueChengPlusException.cast("教学计划 ID 为空");
        }

        // 如果当前非一级节点，直接删除
        if(teachplan.getParentid() != 0){
            deleteTeachplanMedia(id);
            teachplanMapper.deleteById(id);
            return;
        }

        // 如果为一级节点，先删除子节点，在删除自身
        LambdaQueryWrapper<Teachplan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Teachplan::getParentid, id);
        List<Teachplan> teachplans = teachplanMapper.selectList(wrapper);
        for (Teachplan t : teachplans) {
            deleteTeachplanMedia(t.getId());
            teachplanMapper.deleteById(t.getId());
        }
        teachplanMapper.deleteById(id);
    }

    // 上移章节
    @Override
    public void moveUpTeachplan(Long id) {
        // 校验
        if (id == null) XueChengPlusException.cast("课程计划 ID 为空");

        // 查询当前课程计划
        Teachplan teachplan = teachplanMapper.selectById(id);
        if(teachplan == null) XueChengPlusException.cast("课程计划不存在");

        int order = teachplan.getOrderby();
        if(order == 1) return;

        // 查询上一个课程计划
        LambdaQueryWrapper<Teachplan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Teachplan::getOrderby,order-1);
        wrapper.eq(Teachplan::getParentid, teachplan.getParentid());
        wrapper.eq(Teachplan::getCourseId, teachplan.getCourseId());
        Teachplan t = teachplanMapper.selectOne(wrapper);

        // 移动
        t.setOrderby(order);
        teachplan.setOrderby(order-1);
        teachplanMapper.updateById(t);
        teachplanMapper.updateById(teachplan);
    }

    // 下移章节
    @Override
    public void moveDownTeachplan(Long id) {
        // 校验
        if (id == null) XueChengPlusException.cast("课程计划 ID 为空");

        // 查询当前课程计划
        Teachplan teachplan = teachplanMapper.selectById(id);
        if(teachplan == null) XueChengPlusException.cast("课程计划不存在");

        // 查询同级章节数目
        LambdaQueryWrapper<Teachplan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Teachplan::getParentid, teachplan.getParentid());
        wrapper.eq(Teachplan::getCourseId, teachplan.getCourseId());
        Integer count = teachplanMapper.selectCount(wrapper);

        int order = teachplan.getOrderby();
        if(order == count) return;

        // 查询上一个课程计划
        LambdaQueryWrapper<Teachplan> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Teachplan::getOrderby,order+1);
        wrapper1.eq(Teachplan::getParentid, teachplan.getParentid());
        wrapper1.eq(Teachplan::getCourseId, teachplan.getCourseId());
        Teachplan t = teachplanMapper.selectOne(wrapper1);

        // 移动
        t.setOrderby(order);
        teachplan.setOrderby(order+1);
        teachplanMapper.updateById(t);
        teachplanMapper.updateById(teachplan);
    }


    // 删除 TeachplanMedia
    public void deleteTeachplanMedia(Long id){
        if (id == null) return;
        LambdaQueryWrapper<TeachplanMedia> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeachplanMedia::getTeachplanId, id);
        teachplanMediaMapper.delete(wrapper);
    }
}
