package com.mugencai.myblog.service;

import com.mugencai.myblog.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;

public interface TypeService {

    //新增完保存
    Type saveType(Type type);

    //通过id查询type数据库
    Type getType(Long id);

    //通过名称查询type数据库
    Type getTypeByName(String name);

    //分页查询
    Page<Type> listType(Pageable pageable);

    //种类的所有数据
    List<Type> listType();

    //显示种类的条数
    List<Type> listTypeTop(Integer size);

    //更新
    Type updateType(Long id, Type type);

    //删除
    void deleteType(Long id);

}
