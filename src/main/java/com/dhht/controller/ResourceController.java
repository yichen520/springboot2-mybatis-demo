package com.dhht.controller;

import com.dhht.common.JsonObjectBO;
import com.dhht.model.Makedepartment;
import com.dhht.model.Resource;
import com.dhht.service.resource.ResourceService;

import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageInfo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/sys/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;



   //根据ID查找资源
   @RequestMapping(value = "/selcect")
    public JsonObjectBO selectresouer(@RequestBody Map map){

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();

        String Id = map.get("key").toString();
        Resource resource = resourceService.selectByPrimaryKey(Id);

       jsonObject.put("Resource",resource);
       jsonObjectBO.setData(jsonObject);
       jsonObjectBO.setCode(1);
       return jsonObjectBO;

   }

    //查询所有资源
    @RequestMapping(value = "/info")
    public JsonObjectBO selectAllResource(@RequestBody Map map){
        int pageNum =(Integer) map.get("current");
        int pageSize =(Integer)map.get("pageSize");

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();

        PageInfo<Resource> resource = resourceService.findAllResourceBySize(pageNum,pageSize);
        jsonObject.put("Resource",resource);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        return jsonObjectBO;

    }

    //根据Id删除资源
    @RequestMapping(value = "/delete")
    public JsonObjectBO deleteResource(@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        String Id = map.get("key").toString();
        int flag = resourceService.findChild(Id).size();
        if(flag>0) {
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("删除资源失败");
        }else {
            int result = resourceService.deleteByPrimaryKey(Id);
            if (result > 0) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("删除资源成功");
            } else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("删除资源失败");
            }
        }
        return jsonObjectBO;
    }

   //添加资源
    @RequestMapping(value = "/add")
    public JsonObjectBO insertResourcr(@RequestBody Resource resource){
        //为资源添加一个UUID
        resource.setId(UUIDUtil.generate());

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();

        int result = resourceService.insertResource(resource);
        if(result>0){
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("添加资源成功");
        }else {
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("添加资源失败");
        }
        return jsonObjectBO;
    }

    //修改资源
    @RequestMapping(value = "/update")
    public JsonObjectBO updateResource(@RequestBody Resource resource){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();

        int result = resourceService.updateByPrimaryKey(resource);
        if(result>0){
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("更新资源成功");
        }else {
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("更新资源失败");
        }
        return jsonObjectBO;
    }

    //查找权限下的资源
    @RequestMapping(value = "/RoleResource")
    public JsonObjectBO selectRoleResource(@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();

        List<String> list = (List<String>)map.get("resourceIds");
        List<Resource> resources = resourceService.findResourceByRole(list);

        jsonObject.put("Resource",resources);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        return jsonObjectBO;
    }

}
