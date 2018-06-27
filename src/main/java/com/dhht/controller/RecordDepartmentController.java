package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.District;
import com.dhht.model.DistrictMenus;
import com.dhht.model.RecordDepartment;
import com.dhht.model.User;
import com.dhht.service.District.DistrictService;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/sys/department")
public class RecordDepartmentController {

    @Autowired
    private RecordDepartmentService recordDepartmentService;
    @Autowired
    private DistrictService districtService;

    //private JSONObject jsonObject = new JSONObject();

    /**
     * 根据角色获取备案单位
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping(value = "/selectByRole")
    public JsonObjectBO selectByRole(HttpServletRequest httpServletRequest,@RequestBody Map map){
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        int pageSize = (Integer) map.get("pageSize");
        int pageNum = (Integer)map.get("pageNum");
        JSONObject jsonObject = new JSONObject();
        PageInfo<RecordDepartment> pageInfo = new PageInfo<>();
        try {
            pageInfo = recordDepartmentService.selectByDistrictId(user.getDistrictId(),pageSize,pageNum);
            jsonObject.put("recordDepartment",pageInfo);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    /**
     * 根据区域查询下属备案单位
     * @param map
     * @return
     */
    @RequestMapping(value = "/selectByDistrict")
    public JsonObjectBO selectByDistrict(@RequestBody Map map){
        String id = (String) map.get("id");
        int pageSize = (Integer) map.get("pageSize");
        int pageNum = (Integer)map.get("pageNum");
        JSONObject jsonObject = new JSONObject();

        PageInfo<RecordDepartment> pageInfo = new PageInfo<>();
        try {
             pageInfo = recordDepartmentService.selectByDistrictId(id,pageSize,pageNum);
             jsonObject.put("recordDepartment",pageInfo);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    /**
     * 查询所有备案单位
     * @param map
     * @return
     */
    @RequestMapping(value = "/info")
    public JsonObjectBO selectAllRecordDepartment(@RequestBody Map map){
        int pageSize = (Integer)map.get("pageSize");
        int pageNum = (Integer) map.get("pageNum");
        JSONObject jsonObject = new JSONObject();

        PageInfo<RecordDepartment> pageInfo = new PageInfo<>();
        try{
            pageInfo = recordDepartmentService.selectAllRecordDepartMent(pageSize,pageNum);
            jsonObject.put("recordDepartment",pageInfo);
        }catch (Exception e ){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    /**
     * 添加备案单位
     * @param recordDepartment
     * @return
     */
    @RequestMapping(value = "/insert")
    public JsonObjectBO insert(@RequestBody RecordDepartment recordDepartment){
        boolean result = false;
        try {
            result = recordDepartmentService.insert(recordDepartment);

        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        if(result){
            return JsonObjectBO.ok("添加成功");
        }else {
            return JsonObjectBO.error("添加失败");
        }
    }

    @RequestMapping(value = "/selectDistrict")
    public JsonObjectBO selectDistrict(HttpServletRequest httpServletRequest){
        //User user = (User)httpServletRequest.getSession().getAttribute("user");
        JSONObject jsonObject = new JSONObject();
        List<DistrictMenus> districtMenus = new ArrayList<>();

        try {
            districtMenus = districtService.selectOneDistrict("330100");
            jsonObject.put("districtMenus",districtMenus);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }


}
