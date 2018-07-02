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
import org.springframework.beans.factory.annotation.Autowired;
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

    private Role role = new Role();

    private JSONObject jsonObject = new JSONObject();

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

    @Log("查询角色")
    @RequestMapping("info")
    public JsonObjectBO getList(@RequestBody(required = false) Map map) {
        if (map == null){
            try {
                List<Role> roles = roleService.getRoleListNopage();
                jsonObject.put("roles", roles);
                return  JsonObjectBO.success("查询角色成功",jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
                return JsonObjectBO.error("查询角色失败");
            }
        }else {
        int pageNum = (Integer) map.get("pageNum");
        int pageSize = (Integer) map.get("pageSize");

        try {
            PageInfo<Role> roles = roleService.getRoleList(pageNum, pageSize);
            jsonObject.put("roles", roles);
            return  JsonObjectBO.success("查询角色成功",jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.error("查询角色失败");
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
        } catch (Exception e) {
            return JsonObjectBO.exception("新增角色失败");
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

    /**
     * 角色管理获取所有非倚赖项
     */
    @RequestMapping(value = "requiredResourceInfo")
    public JsonObjectBO RoleRequiredResource(){
        try {
            jsonObject.put("resource",resourceService.selectRequiredResource());
        }catch (Exception e){
            JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);

    }
}