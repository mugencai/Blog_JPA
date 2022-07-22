package com.mugencai.myblog.service;

import com.mugencai.myblog.pojo.Blog;
import com.mugencai.myblog.pojo.Type;
import com.mugencai.myblog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getBlog(Long id);

    //获取博文内容并转换
    Blog getAndConvert(Long id);

    //查询一组数据(分页参数pageable, 查询参数blog)
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    //查询分页对象
    Page<Blog> listBlog(Pageable pageable);

    //根据tagId查询分页对象
    Page<Blog> listBlog(Long tagId, Pageable pageable);

    //标题查询
    Page<Blog> listBlog(String query, Pageable pageable);

    //推荐对象
    List<Blog> listRecommendBlogTop(Integer size);

    //各年份对应数据列表
    Map<String, List<Blog>> archiveBlog();

    //Blog条数
    Long countBlog();

    //存储
    Blog saveBlog(Blog blog);

    //更新
    Blog updateBlog(Long id, Blog blog);

    //删除
    void deleteBlog(Long id);



}
