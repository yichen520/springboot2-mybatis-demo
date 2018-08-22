package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.AccessResult;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Role;
//import com.dhht.service.user.RoleService;
import com.dhht.service.resource.ResourceService;
import com.dhht.service.user.RoleService;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/sys/role")
public class RoleController extends JsonObjectBO {
    @Autowired
    private RoleService roleService;
    @Autowired
    private ResourceService resourceService;

    private JSONObject jsonObject = new JSONObject();

    private static Logger logger = Logger.getLogger(RoleController.class);

    @Log("查询角色")
    @RequestMapping("info")
    public JsonObjectBO getList(@RequestBody(required = false) Map map) {
        if (map == null){
            try {
                List<Role> roles = roleService.getRoleListNopage();
                jsonObject.put("roles", roles);
                return  JsonObjectBO.success("查询角色成功",jsonObject);
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
                return JsonObjectBO.exception(e.toString());
            }
        }else {
        int pageNum = (Integer) map.get("pageNum");
        int pageSize = (Integer) map.get("pageSize");

        try {
            PageInfo<Role> roles = roleService.getRoleList(pageNum, pageSize);
            jsonObject.put("roles", roles);
            return  JsonObjectBO.success("查询角色成功13333",jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
    }

    }

    @Log("新增角色")
    @RequestMapping("add")
    public JsonObjectBO save(@RequestBody Role role) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            AccessResult result = roleService.save(role);
            jsonObjectBO.setMessage(result.getResultMsg());
            jsonObjectBO.setCode(result.getIsSuccess());
            return jsonObjectBO;
        }catch (DuplicateKeyException e) {
            return JsonObjectBO.exception("角色名已存在,新增角色失败");
        }
        catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }

    }

    /**
     * 修改角色
     */
    @Log("修改角色")
    @RequestMapping("/update")
    public JsonObjectBO updataRole(@RequestBody Role role) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            AccessResult result = roleService.updataRole(role);
            jsonObjectBO.setMessage(result.getResultMsg());
            jsonObjectBO.setCode(result.getIsSuccess());
            return jsonObjectBO;

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }

    }


    /**
     * 删除角色
     */
    @Log("删除角色")
    @RequestMapping("delete")
    public JsonObjectBO deleteRole(@RequestBody Role role) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {

            int users = roleService.deleteRole(role.getId());
            if(users == 1){
                jsonObjectBO.setMessage("删除角色成功");
                jsonObjectBO.setCode(1);
                return jsonObjectBO;
            }else {
                jsonObjectBO.setMessage("删除角色失败");
                jsonObjectBO.setCode(-1);
                return jsonObjectBO;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
    }

    /**
     * 角色管理获取所有非倚赖项
     */
    @RequestMapping(value = "requiredResourceInfo")
    public JsonObjectBO RoleRequiredResource(){
        try {
            jsonObject.put("resource",resourceService.selectRequiredResource());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
        return JsonObjectBO.success("查询成功",jsonObject);

    }
}