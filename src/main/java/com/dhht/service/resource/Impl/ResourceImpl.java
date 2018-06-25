package com.dhht.service.resource.Impl;

import com.dhht.annotation.Sync;
import com.dhht.dao.ResourceMapper;
import com.dhht.model.Menus;
import com.dhht.model.Resource;
import com.dhht.service.resource.ResourceService;
import com.dhht.util.MenuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        if(resource.getParentId()==""||resource.getParentId()==null){
            resource.setParentId("0");
        }
        return resourceMapper.insert(resource);
    }

    @Override
    public int deleteByPrimaryKey(String id) {
        int result = 0;
        try {
            List<Resource> list = resourceMapper.selectByParentID(id);
            for (Resource r:list) {
               result = resourceMapper.deleteByPrimaryKey(r.getId());
            }
            result = resourceMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            return 0;
           // e.printStackTrace();
        }
        return result;
    }

    @Override
    public int updateByPrimaryKey(Resource record) {
        if(record.getParentId()==null||record.getParentId()==""){
            record.setParentId("0");
        }
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

    @Override
    public List<Menus> findMenusByRole(List<String> id) {
        List<Menus> list = new ArrayList<Menus>();
        for (String ID:id) {
            list.add(resourceMapper.selectMenusByID(ID));
        }
        List<Menus> menus = findMenuParent(list);
        setAllMenuChildren(menus,list);
        return menus;
    }

    @Override
    public List<Map> selectRequiredResource() {
        List<Map<String,Object>> result = new ArrayList<>();
        List<Menus> list = resourceMapper.selectRequiredResource();
        List<Menus> menus = findMenuParent(list);
        setAllMenuChildren(menus,list);
        return MenuUtil.getSimpeResource(menus);
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



    //-----------------权限资源相关部分--------------------------//
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

    //---------------菜单资源相关部分-----------------------------//
    public List<Menus> findMenuParent(List<Menus> list){
        List<Menus> menus = new ArrayList<Menus>();
        for (Menus m: list) {
            if(m.getParentId().equals("0")){
                menus.add(m);
            }
        }
        return menus;
    }

    //查找权限子节点
    public void setAllMenuChildren(List<Menus> parent,List<Menus> menus){
        for (Menus menu:parent) {
            List<Menus> list = findMenuInList(menus,menu.getId());
            if(list.size()>0){
                menu.setChildren(list);
            }
            setAllMenuChildren(list,menus);
        }
    }

    //在给定集合中根据parentID查找数据
    public List<Menus> findMenuInList(List<Menus> menus,String id){
        List<Menus> list = new ArrayList<Menus>();
        for(Menus menu:menus){
            if(menu.getParentId().equals(id)){
                list.add(menu);
            }
        }
        return list;
    }




}
