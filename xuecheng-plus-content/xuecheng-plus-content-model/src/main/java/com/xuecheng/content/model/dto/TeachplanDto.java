package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import lombok.Data;

import java.util.List;

/**
 * @Author Zihao Qin
 * @Date 2023/12/20 10:37
 */
@Data
public class TeachplanDto extends Teachplan {
    private TeachplanMedia teachplanMedia;
    private List<Teachplan> teachPlanTreeNodes;
}
