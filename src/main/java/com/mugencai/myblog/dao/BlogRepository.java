package com.mugencai.myblog.dao;

import com.mugencai.myblog.pojo.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;


//继承于Jpa，传递Blog对象，主页类型
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query("select b from Blog b where b.recommend = true")
    List<Blog> findTop(Pageable pageable);

    //自定义查询标题数据的方法
    @Query("select b from Blog b where b.title like ?1")
    Page<Blog> findByQuery(String query, Pageable pageable);

    //updateViews方法:更新文章浏览量用（p43后半）
    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views + 1 where b.id = ?1" )
    int updateViews(Long id);




    //自定义查询博客数据所有年份的方法
    @Query("select function('date_format', b.updateTime, '%Y') from Blog b group by function('date_format', b.updateTime, '%Y') order by function('date_format', b.updateTime, '%Y') desc")
    List<String> findGroupYear();

    //自定义按年份查询博客数据的方法
    @Query("select b from Blog b where function('date_format', b.updateTime, '%Y') = ?1")
    List<Blog> findbyYear(String year);


}
