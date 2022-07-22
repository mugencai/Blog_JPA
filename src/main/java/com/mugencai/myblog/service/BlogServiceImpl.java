package com.mugencai.myblog.service;

import com.mugencai.myblog.NotFoundException;
import com.mugencai.myblog.dao.BlogRepository;
import com.mugencai.myblog.pojo.Blog;
import com.mugencai.myblog.pojo.Tag;
import com.mugencai.myblog.pojo.Type;
import com.mugencai.myblog.util.MarkdownUtils;
import com.mugencai.myblog.util.MyBeanUtils;
import com.mugencai.myblog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;


//加注解，标记为Service层
@Service
public class BlogServiceImpl implements BlogService{

    //注入Repository
    @Autowired
    private BlogRepository blogRepository;

    //根据主键查询，查出一个对象返回
    @Deprecated
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    //tag的关联查询：只要某文章的tags中出现tag,那么就查询出来
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                //关联查询
                Join join = root.join("tags");
                return cb.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    //分页动态查询实现
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            //Root拿字段，CriteriaQuery加条件
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                // 标题查询
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    //模糊查询
                    predicates.add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                //按类别查询
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                //是否推荐
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                ///将上述已传入数据的list转换为数组
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;}
        }, pageable);
    }

    //分页显示实现
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    //标题查询实现
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    //保存一个新的Blog对象进来
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        //如果为新增
        if (blog.getId() == null) {
            //初始化日期
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
        //如果为修改
        } else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    //更新
    @Deprecated
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        //先查询
        Blog b = blogRepository.getOne(id);
        //不存在的话就抛出异常
        if (b == null) {
            throw new NotFoundException("该博客不存在");
        }
        //存在的话就将查询的对象复制，赋值给b并保存b
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    //删除
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "updateTime"));
        return blogRepository.findTop(pageable);
    }


    //获取文章id并转换
    @Deprecated
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        //更新blog的浏览量
        blogRepository.updateViews(id);
        return b;
    }
    //Blog表中所有数据条数
    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    //归档实现
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findbyYear(year));
        }
        return map;
    }
}
