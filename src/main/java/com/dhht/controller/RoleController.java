package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.AccessResult;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Makedepartment;
import com.dhht.model.Role;
import com.dhht.model.Users;
//import com.dhht.service.user.RoleService;
import com.dhht.service.user.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.xml.ws.Action;
import java.util.Map;
import java.util.Set;


@RestController
@SuppressWarnings("serial")
@RequestMapping("/sys/role")
public class RoleController extends JsonObjectBO {
    @Autowired
    private RoleService roleService;
    private Role role = new Role();

    // 资源IDs，用于接收设置角色资源设置时的资源id
    private Set<String> resourceIds;

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(RoleController.class);

    /**
     * 输出指定角色的资源id信息
     */
    @RequestMapping("getRoleResourceIds")
    public void getRoleResourceIds() {
        try {
            //  roleService.getRoleResourceIds(role.getId());
        } catch (Exception e) {
            logger.error("输出指定角色的资源id信息失败", e);
        }
    }

    /**
     * 加载所有可用于选择的资源
     */
    @RequestMapping("listResourcesForSelect")
    public void listResourcesForSelect() {
        try {
            //   super.writeJson(roleService.listAllResourcesForSelect());
        } catch (Exception e) {
            logger.error("输出指定角色的资源id信息失败", e);
        }
    }

    /**
     * 查询
     */
    @RequestMapping("info")
    public JsonObjectBO getList(@RequestBody Map map) {
        int pageNum = (Integer) map.get("pageNum");
        int pageSize = (Integer) map.get("pageSize");

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        try {
            PageInfo<Role> roles = roleService.getRoleList(pageNum, pageSize);
            jsonObject.put("roles", roles);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setMessage("查询角色成功");
            jsonObjectBO.setCode(1);
            return jsonObjectBO;

        } catch (Exception e) {
            jsonObjectBO.setMessage("查询角色失败");
            jsonObjectBO.setCode(-1);
            return jsonObjectBO;
        }

    }


    /**
     * 新增
     */
    @RequestMapping("add")
    public JsonObjectBO save(@RequestBody Role role) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            AccessResult result = roleService.save(role);
            jsonObjectBO.setMessage(result.getResultMsg());
            jsonObjectBO.setCode(result.getIsSuccess());
            return jsonObjectBO;
        } catch (Exception e) {
            logger.error("新增角色失败", e);
            jsonObjectBO.setMessage("新增角色失败");
            jsonObjectBO.setCode(-1);
            return jsonObjectBO;
        }

    }

    /**
     * 修改角色
     */
    @RequestMapping("update")
    public JsonObjectBO updataRole(@RequestBody Role role) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            AccessResult result = roleService.updataRole(role);
            jsonObjectBO.setMessage(result.getResultMsg());
            jsonObjectBO.setCode(result.getIsSuccess());
            return jsonObjectBO;

        } catch (Exception e) {
            logger.error("修改角色失败", e);
            jsonObjectBO.setMessage("修改角色失败");
            jsonObjectBO.setCode(-1);
            return jsonObjectBO;
        }

    }


    /**
     * 删除角色
     */
    @RequestMapping("delete")
    public JsonObjectBO deleteRole(@RequestBody Role role) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();

        Role role1 = roleService.findRoleById(role.getId());

        try {
//            if (role1.getIsSystem() == 1) {
//                jsonObjectBO.setMessage("内置角色不可删除");
//                jsonObjectBO.setCode(-1);
//                return jsonObjectBO;
//            }
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
            jsonObjectBO.setMessage(e.getCause().getMessage());
            jsonObjectBO.setCode(-1);
            return jsonObjectBO;
        }
    }
}