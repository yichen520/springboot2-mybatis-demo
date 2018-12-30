package com.dhht.controller.web;

import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Resource;
import com.dhht.service.resource.ResourceService;

import com.dhht.util.UUIDUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/sys/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    private static Logger logger = Logger.getLogger(ResourceController.class);

    @Log("查询所有资源")
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public JsonObjectBO selectAllResource(){

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
       List<Resource> resource = resourceService.selectAllResource();
        jsonObject.put("Resource",resource);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        return jsonObjectBO;

    }

    @Log("删除资源")
    @RequestMapping(value = "/delete")
    public JsonObjectBO deleteResource(@RequestBody Map map){
        String Id = (String) map.get("id");
        int result = 0;
        try {
           result =  resourceService.deleteByPrimaryKey(Id);
        }catch (Exception e){
            return JsonObjectBO.error(e.getMessage());
        }
        if(result==0){
            return JsonObjectBO.error("删除失败");
        }
        return JsonObjectBO.ok("删除成功");

        /* 有子节点不能删除父节点，方法暂时废除
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
        }*/

    }

   @Log("添加资源")
    @RequestMapping(value = "/add")
    public JsonObjectBO insertResource(@RequestBody Resource resource){
        resource.setId(UUIDUtil.generate());
        if(resource.getParentId() == ""||resource.getParentId()==null){
            resource.setParentId("0");
        }
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
       jsonObject.put("resource",resource);
        int result = resourceService.insertResource(resource);
        if(result>0){
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("添加资源成功");
            jsonObjectBO.setData(jsonObject);
        }else {
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("添加资源失败");
        }
        return jsonObjectBO;
    }

    @Log("修改资源")
    @RequestMapping(value = "/update")
    public JsonObjectBO updateResource(@RequestBody Resource resource){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();

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

    @Log("查找权限下的资源")
    @RequestMapping(value = "/selectRoleResource")
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

    @Log("查找所有非倚赖资源")
    @RequestMapping(value = "requiredResourceInfo")
    public JsonObjectBO selectRequiredResource(){
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map> menus = resourceService.selectRequiredResource();
            jsonObject.put("resource",menus);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

}
