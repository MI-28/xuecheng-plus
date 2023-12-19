package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.service.CourseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author Zihao Qin
 * @Date 2023/12/18 16:41
 */
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory> implements CourseCategoryService {
    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
        List<CourseCategoryTreeDto> list = new ArrayList<>();

        // 获取所有分类信息
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNodes(id);

        // 找到每个节点的子节点，封装为 Map<String,List<CourseCategoryTreeDto>>
        Map<String, CourseCategoryTreeDto> map = courseCategoryTreeDtos.stream().collect(Collectors.toMap(key -> key.getId(), value -> value, (key1, key2) -> key2));

        courseCategoryTreeDtos.stream().filter(item->!id.equals(item.getId())).forEach(item->{
            if (id.equals(item.getParentid())) {
                list.add(item);
            }else{
                CourseCategoryTreeDto t = map.get(item.getParentid());
                if(t.getChildrenTreeNodes() == null){
                    t.setChildrenTreeNodes(new ArrayList<>());
                }
                t.getChildrenTreeNodes().add(item);
            }
        });

        return list;

//        // 获取所有节点信息
//        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNodes(id);
//
//        // 封装节点
//        List<CourseCategoryTreeDto> list = new ArrayList<>();
//
//        for (CourseCategoryTreeDto i : courseCategoryTreeDtos) {
//            if(i.getParentid() .equals(id)){
//                i.setChildrenTreeNodes(new ArrayList<>());
//                list.add(i);
//            } else if (list.size()>0 && i.getParentid().equals(list.get(list.size() - 1).getId()) ) {
//                list.get(list.size()-1).getChildrenTreeNodes().add(i);
//            }
//        }
//
//        return list;
    }
}
