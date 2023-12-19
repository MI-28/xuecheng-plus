package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
        // 参数合法性校验
        if (StringUtils.isBlank(addCourseDto.getName())) {
            XueChengPlusException.cast("课程名称为空");
        }

        if (StringUtils.isBlank(addCourseDto.getMt())) {
            XueChengPlusException.cast("课程分类为空");
        }

        if (StringUtils.isBlank(addCourseDto.getSt())) {
            XueChengPlusException.cast("课程分类为空");
        }

        if (StringUtils.isBlank(addCourseDto.getGrade())) {
            XueChengPlusException.cast("课程等级为空");
        }

        if (StringUtils.isBlank(addCourseDto.getTeachmode())) {
            XueChengPlusException.cast("教育模式为空");
        }

        if (StringUtils.isBlank(addCourseDto.getUsers())) {
            XueChengPlusException.cast("适应人群为空");
        }

        if (StringUtils.isBlank(addCourseDto.getCharge())) {
            XueChengPlusException.cast("收费规则为空");
        }

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
}