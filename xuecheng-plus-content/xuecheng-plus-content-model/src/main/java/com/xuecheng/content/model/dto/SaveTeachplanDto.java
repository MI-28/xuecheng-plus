package com.xuecheng.content.model.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新增 大章节、小章节、修改章节DTO
 * @Author Zihao Qin
 * @Date 2023/12/21 14:46
 */
@Data
@ToString
public class SaveTeachplanDto {

    /***
     * 教学计划id
     */
    private Long id;

    /**
     * 课程计划名称
     */
    @NotBlank(message = "课程章节名称不能为空")
    private String pname;

    /**
     * 课程计划父级Id
     */
    @NotNull
    private Long parentid;

    /**
     * 层级，分为1、2、3级
     */
    @NotNull
    private Integer grade;

    /**
     * 课程类型:1视频、2文档
     */
    private String mediaType;

    /**
     * 课程标识
     */
    private Long courseId;

    /**
     * 课程发布标识
     */
    private Long coursePubId;

    /**
     * 是否支持试学或预览（试看）
     */
    private String isPreview;
}
