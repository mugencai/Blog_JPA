package com.mugencai.myblog.web;


import com.mugencai.myblog.pojo.Comment;
import com.mugencai.myblog.pojo.User;
import com.mugencai.myblog.service.BlogService;
import com.mugencai.myblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    //返回列表片段
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    //点击按钮后接收信息
    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session) {
        //拿到前端传送的id
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        //强转变换为User对象
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
//            comment.setNickname(user.getNickname());
        } else {
            comment.setAvatar(avatar);
        }

        //set头像
        comment.setAvatar(avatar);
        //保存对象
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }


}
