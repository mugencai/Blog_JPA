package com.mugencai.myblog.service;

import com.mugencai.myblog.pojo.Comment;

import java.util.List;

public interface CommentService {

    //获取评论列表
    List<Comment> listCommentByBlogId(Long blogId);

    //保存评论列表
    Comment saveComment(Comment comment);
}
