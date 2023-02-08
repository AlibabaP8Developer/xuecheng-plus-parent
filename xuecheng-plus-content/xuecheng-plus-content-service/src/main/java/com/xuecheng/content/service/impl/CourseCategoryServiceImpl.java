package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程分类 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory> implements CourseCategoryService {

    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
        List<CourseCategoryTreeDto> categoryTreeDtos = courseCategoryMapper.selectTreeNodes(id);

        // 定义一个list作为最终返回的数据
        ArrayList<CourseCategoryTreeDto> courseCategoryTreeDtos = new ArrayList<>();

        // 为了方便找子结点的父结点，定义一个map
        Map<String, CourseCategoryTreeDto> nodeMap = new HashMap<>();

        // 将数据封装到list中，只包括了根结点的直接下属结点
        categoryTreeDtos.stream().forEach(item -> {
            nodeMap.put(item.getId(), item);
            if (item.getParentid().equals(id)) {
                courseCategoryTreeDtos.add(item);
            }
            // 找到该结点的父结点
            String parentid = item.getParentid();
            // 找到该结点的父结点对象
            CourseCategoryTreeDto parentNode = nodeMap.get(parentid);
            if (parentNode != null) {
                List childrenTreeNodes = parentNode.getChildrenTreeNodes();
                if (childrenTreeNodes == null) {
                    parentNode.setChildrenTreeNodes(new ArrayList<>());
                }
                // 找到子结点，放到父结点的childrenTreeNodes属性中
                parentNode.getChildrenTreeNodes().add(item);
            }
        });

        // 返回list中只包括了根结点的直接下属结点
        return categoryTreeDtos;
    }
}
