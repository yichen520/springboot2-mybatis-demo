package com.dhht.service.resource;

import com.dhht.model.Menus;
import com.dhht.model.Resource;
import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ResourceService {

    //根据ID查询资源
    Resource selectByPrimaryKey(String id);

    //查询所有资源
    List<Resource> selectAllResource();

    //分页查询所有资源
  //  List<Resource> findAllResource();

    //增加资源
    int insertResource(Resource resource);

    //删除资源
    int deleteByPrimaryKey(String id);

    //修改资源
    int updateByPrimaryKey(Resource record);

    //根据ID查找子节点
    List<Resource> findChild(String id);

    //根据角色权限集合获取资源
    List<Resource> findResourceByRole(List<String> id);

    //查找资源菜单
    List<Menus> findMenusByRole(List<String> id);

    //获取所有非倚赖资源
    List<Map> selectRequiredResource();


}
