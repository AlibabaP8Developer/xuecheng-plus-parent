package com.xuecheng.content.controller.api;

import com.xuecheng.content.model.dto.TeachplanDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public class TeachplanController {

    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId) {

        return null;
    }
}
