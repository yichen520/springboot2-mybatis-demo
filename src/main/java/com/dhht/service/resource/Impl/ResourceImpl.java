package com.dhht.service.resource.Impl;

import com.dhht.dao.ResourceMapper;
import com.dhht.model.Makedepartment;
import com.dhht.model.Resource;
import com.dhht.service.resource.ResourceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("ResourceService")
public class ResourceImpl  implements ResourceService {
    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public Resource selectByPrimaryKey(String id) {
        return resourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Resource> selectAllResource() {
        List<Resource> resources = new ArrayList<Resource>();
        resources = findParent();
        setAllChildren(resources);
       // System.out.println("数据:"+resources.size());
        return resources;
    }

   // @Override
   // public List<Resource> findAllResource(){
    //   return selectAllResource();
 //   }

    @Override
    public int insertResource(Resource resource) {
       return resourceMapper.insert(resource);
    }

    @Override
    public int deleteByPrimaryKey(String id) {
        return  resourceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(Resource record) {
        return  resourceMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Resource> findChild(String Id){
        return resourceMapper.selectByParentID(Id);
    }

    @Override
    public List<Resource> findResourceByRole(List<String> id) {
         List<Resource> list = new ArrayList<Resource>();
         for(String ID:id){
             list.add(selectByPrimaryKey(ID));
         }
         List<Resource> resources = findParent(list);
         setAllChildren(resources,list);
         return resources;
    }

    //递归查找子节点,进入数据库查找
    public void setAllChildren(List<Resource> resources){
        for (Resource resource:resources) {
           List<Resource> list = resourceMapper.selectByParentID(resource.getId());
           if(list.size()>0){
               resource.setChildren(list);
           }
           setAllChildren(list);
        }
    }

    //查找非依赖父节点，进数据库查找
    public List<Resource> findParent(){
        return resourceMapper.selectByParentID("0");
    }

    //查找权限父节点
    public List<Resource> findParent(List<Resource> list){
        List<Resource> resources = new ArrayList<Resource>();
        for (Resource r: list) {
            if(r.getParentId().equals("0")){
                resources.add(r);
            }
        }
        return resources;
    }

    //查找权限子节点
    public void setAllChildren(List<Resource> parent,List<Resource> resources){
        for (Resource resource:parent) {
            List<Resource> list = findInList(resources,resource.getId());
            if(list.size()>0){
                resource.setChildren(list);
            }
            setAllChildren(list,resources);
        }
    }

    //在给定集合中根据parentID查找数据
    public List<Resource> findInList(List<Resource> resources,String id){
        List<Resource> list = new ArrayList<Resource>();
        for(Resource resource:resources){
            if(resource.getParentId().equals(id)){
                list.add(resource);
            }
        }
        return list;
    }





}