package com.mugencai.myblog.service;

import com.mugencai.myblog.pojo.Tag;
import com.mugencai.myblog.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {

    //新增完保存
    Tag saveTag(Tag tag);

    //查询id查询tag数据库
    Tag getTag(Long id);

    //通过名称查询tag数据库
    Tag getTagByName(String name);

    //分页查询
    Page<Tag> listTag(Pageable pageable);

    //Tag的所有数据
    List<Tag> listTag();

    //显示tag的条数
    List<Tag> listTagTop(Integer size);

    //tags的所有id
    List<Tag> listTag(String ids);

    //更新
    Tag updateTag(Long id, Tag tag);

    //删除
    void deleteTag(Long id);
}
