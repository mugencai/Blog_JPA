package com.mugencai.myblog.web.admin;


import com.mugencai.myblog.pojo.Blog;
import com.mugencai.myblog.pojo.User;
import com.mugencai.myblog.service.BlogService;
import com.mugencai.myblog.service.TagService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.templateparser.text.AbstractTextTemplateParser;

import javax.servlet.http.HttpSession;



@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    //拿到所有TypeService
    @Autowired
    private TypeService typeService;


    //拿到所有BlogService
    @Autowired
    private TagService tagService;


    //按照更新时间降序显示博文信息
    @GetMapping("/blogs")
    //传入pageable,blog,model对象
    public String blogs(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) {
        //获取所有分类
        model.addAttribute("types ", typeService.listType());
        //分页
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs";
    }

    //博客查询
    @PostMapping("/blogs/search")
    //传入pageable,blog,model对象
    public String search(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) {
        //分页方式查询
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        //返回代码片段,以便实现页面的局部刷新
        return "admin/blogs :: blogList";
    }

    //博客新增
    @GetMapping("/blogs/input")
    public String input(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
        model.addAttribute("blog", new Blog());
        return "admin/blogs-input";
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    //博客修改
    //获取指定id的blog对象
    @GetMapping("/blogs/{id}/input")
    public String EditInput(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        //初始化是将TypeId转换为字符串
        blog.init();
//        model.addAttribute("types", typeService.listType());
//        model.addAttribute("tags", tagService.listTag());
        model.addAttribute("blog", blog);
        return "admin/blogs-input";
    }

    //将博文提交
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        //拿到当前登录用户的对象
        blog.setUser((User) session.getAttribute("user"));
        //拿到博文种类的Id
        blog.setType(typeService.getType(blog.getType().getId()));
        //拿到博文tag的Id
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b = blogService.saveBlog(blog);
        if (b == null) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_LIST;
    }

    @GetMapping("/blogs/{id}/delete")
    //将保存或发布的博文删除
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        //删除博客主键
        blogService.deleteBlog(id);
        //提示页面
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/blogs";
    }


}
