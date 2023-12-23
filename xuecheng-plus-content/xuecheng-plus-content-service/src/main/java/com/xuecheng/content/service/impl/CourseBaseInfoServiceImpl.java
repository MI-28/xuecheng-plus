package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.*;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.*;
import com.xuecheng.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author Zihao Qin
 * @Date 2023/12/14 9:42
 */
@Slf4j
@Service
public class CourseBaseInfoServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements CourseBaseInfoService {
    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Autowired
    private CourseMarketMapper courseMarketMapper;

    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto courseParamsDto) {
        IPage<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());

        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();
        if(courseParamsDto != null){
            wrapper.like(StringUtils.isNotEmpty(courseParamsDto.getCourseName()), CourseBase::getName, courseParamsDto.getCourseName());
            wrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getAuditStatus()), CourseBase::getAuditStatus, courseParamsDto.getAuditStatus());
            wrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getPublishStatus()), CourseBase::getStatus, courseParamsDto.getPublishStatus());
        }

        courseBaseMapper.selectPage(page, wrapper);

        PageResult<CourseBase> pageResult = new PageResult<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
        return pageResult;
    }

    // 增删改 需要添加事务控制
    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto){
//        // 参数合法性校验
//        if (StringUtils.isBlank(addCourseDto.getName())) {
//            XueChengPlusException.cast("课程名称为空");
//        }
//
//        if (StringUtils.isBlank(addCourseDto.getMt())) {
//            XueChengPlusException.cast("课程分类为空");
//        }
//
//        if (StringUtils.isBlank(addCourseDto.getSt())) {
//            XueChengPlusException.cast("课程分类为空");
//        }
//
//        if (StringUtils.isBlank(addCourseDto.getGrade())) {
//            XueChengPlusException.cast("课程等级为空");
//        }
//
//        if (StringUtils.isBlank(addCourseDto.getTeachmode())) {
//            XueChengPlusException.cast("教育模式为空");
//        }
//
//        if (StringUtils.isBlank(addCourseDto.getUsers())) {
//            XueChengPlusException.cast("适应人群为空");
//        }
//
//        if (StringUtils.isBlank(addCourseDto.getCharge())) {
//            XueChengPlusException.cast("收费规则为空");
//        }

        // 向 course_base 写数据
        CourseBase courseBase = new CourseBase();
        BeanUtils.copyProperties(addCourseDto, courseBase);
        courseBase.setCompanyId(companyId);
        courseBase.setCreateDate(LocalDateTime.now());
        // 审核状态默认为未提交
        courseBase.setAuditStatus("202002");
        // 发布状态默认为未发布
        courseBase.setStatus("203001");

        // 插入数据库
        int res_saveCourseBase = courseBaseMapper.insert(courseBase);
        if(res_saveCourseBase<=0) XueChengPlusException.cast("课程添加失败");;

        // 向 course_market 写数据
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(addCourseDto, courseMarket);
        // 课程 ID
        courseMarket.setId(courseBase.getId());
        int res_saveCourseMarket = saveCourseMarket(courseMarket);
        if(res_saveCourseMarket<=0) XueChengPlusException.cast("添加营销信息失败");;

        // 查询 课程详细信息
        CourseBaseInfoDto courseBaseInfo = getCourseBaseInfo(courseBase.getId());

        return courseBaseInfo;
    }

    // 保存营销信息的方法
    // 如果营销信息存在则更新，不存在则添加
    private int saveCourseMarket(CourseMarket courseMarket){
        // 参数合法性校验
        String charge = courseMarket.getCharge();

        if (StringUtils.isEmpty(charge)) {
            XueChengPlusException.cast("收费规则为空");
        }

        if(charge.equals("201001")){
            Float price = courseMarket.getPrice();
            if(price == null || price.floatValue() <= 0){
                XueChengPlusException.cast("课程的价格必须大于0");
            }
        }

        // 查询营销信息，存在则更新，否则添加
        CourseMarket t = courseMarketMapper.selectById(courseMarket.getId());
        if (t == null) {
            // 插入数据库
            int insert = courseMarketMapper.insert(courseMarket);
            return insert;
        }else{
            BeanUtils.copyProperties(courseMarket, t);
            t.setId(courseMarket.getId());
            int update = courseMarketMapper.update(t, Wrappers.emptyWrapper());
            return update;
        }
    }

    // 查询课程的详细信息
    public CourseBaseInfoDto getCourseBaseInfo(long courseId){
        // 查询课程基本信息
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null) return null;
        // 查询课程营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);

        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase, courseBaseInfoDto);
        BeanUtils.copyProperties(courseMarket, courseBaseInfoDto);

        // 查询课程分类的名称
        CourseCategory courseCategoryA = courseCategoryMapper.selectById(courseBase.getMt());
        CourseCategory courseCategoryB = courseCategoryMapper.selectById(courseBase.getSt());
        courseBaseInfoDto.setMtName(courseCategoryA.getName());
        courseBaseInfoDto.setStName(courseCategoryB.getName());

        return courseBaseInfoDto;
    }

    // 按 ID 获取课程信息
    @Override
    public CourseBaseInfoDto getCourseBaseById(Long id) {
        if(id==null) XueChengPlusException.cast("课程 ID 为空");

        CourseBaseInfoDto res = new CourseBaseInfoDto();

        CourseBase courseBase = courseBaseMapper.selectById(id);
        if(courseBase == null) XueChengPlusException.cast("课程 ID 不存在");
        BeanUtils.copyProperties(courseBase, res);

        CourseMarket courseMarket = courseMarketMapper.selectById(id);
//        if(courseMarket == null) XueChengPlusException.cast("课程 ID 不存在");
        if(courseMarket!=null) BeanUtils.copyProperties(courseMarket, res);

        // 查询课程分类名称
        if(StringUtils.isBlank(res.getMt()) || StringUtils.isBlank(res.getSt())) return res;

        CourseCategory courseCategory1 = courseCategoryMapper.selectById(res.getMt());
        CourseCategory courseCategory2 = courseCategoryMapper.selectById(res.getSt());

        res.setMtName(courseCategory1.getName());
        res.setStName(courseCategory2.getName());

        return res;
    }

    // 更新课程信息
    @Transactional
    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto) {
        // 验证费用
        if(dto.getCharge().equals("201001") && (dto.getPrice()==null || dto.getPrice()<=0)) XueChengPlusException.cast("付费课程费用不能为0");

        // 获取 CourseBase
        CourseBase courseBase = courseBaseMapper.selectById(dto.getId());
        if(courseBase == null) XueChengPlusException.cast("课程 ID 不存在");

        // 校验机构ID
        // if(!companyId.equals(courseBase.getCompanyId())) XueChengPlusException.cast("禁止修改其他机构课程");

        // 更新 CourseBase
        BeanUtils.copyProperties(dto, courseBase);
        courseBase.setCompanyId(companyId);
        courseBase.setChangeDate(LocalDateTime.now());
        int update = courseBaseMapper.updateById(courseBase);
        if(update<=0) XueChengPlusException.cast("课程信息更新失败");

        // 更新 CourseMarket
        CourseMarket courseMarket = courseMarketMapper.selectById(dto.getId());
        // 如果不存在 courseMarket 信息，则新增
        if (courseMarket == null) {
            courseMarket = new CourseMarket();
            BeanUtils.copyProperties(dto, courseMarket);
            courseMarketMapper.insert(courseMarket);
        }else{
            // 存在则修改
            BeanUtils.copyProperties(dto, courseMarket);
            courseMarketMapper.updateById(courseMarket);
        }

        CourseBaseInfoDto courseBaseInfo = getCourseBaseInfo(dto.getId());

        return courseBaseInfo;
    }

    // 删除课程信息
    @Override
    public void deleteCourseBase(Long id) {
        // 校验
        if(id == null) XueChengPlusException.cast("课程ID不能为空");

        // 删除 课程基本信息
        courseBaseMapper.deleteById(id);

        // 删除 课程营销信息
        courseMarketMapper.deleteById(id);

        // 删除 课程教学计划
        LambdaQueryWrapper<Teachplan> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Teachplan::getCourseId, id);
        List<Teachplan> teachplans = teachplanMapper.selectList(wrapper1);
        // 删除 Teachplan 关联 Media 信息
        teachplans.stream().forEach(e->{
            LambdaQueryWrapper<TeachplanMedia> t = new LambdaQueryWrapper<>();
            t.eq(TeachplanMedia::getTeachplanId, e.getId());
            teachplanMediaMapper.delete(t);
        });
        teachplanMapper.delete(wrapper1);

        // 删除 课程教师信息
        LambdaQueryWrapper<CourseTeacher> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(CourseTeacher::getCourseId, id);
        courseTeacherMapper.delete(wrapper2);
    }
}