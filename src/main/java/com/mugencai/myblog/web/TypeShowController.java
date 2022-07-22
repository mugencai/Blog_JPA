package com.mugencai.myblog.web;


import com.mugencai.myblog.pojo.Type;
import com.mugencai.myblog.service.BlogService;
import com.mugencai.myblog.service.TypeService;
import com.mugencai.myblog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TypeShowController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model) {
        List<Type> types = typeService.listTypeTop(10000);
        if (id == -1) {
            id = types.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        //拿到所有分类
        model.addAttribute("types", types);
        //获得一组查询后的数据
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        //拿现在的选中种类的id
        model.addAttribute("activeTypeId", id);
        return "types";
    }


}
