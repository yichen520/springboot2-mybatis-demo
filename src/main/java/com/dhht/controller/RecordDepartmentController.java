package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.District;
import com.dhht.model.DistrictMenus;
import com.dhht.model.RecordDepartment;
import com.dhht.model.User;
import com.dhht.service.District.DistrictService;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.util.ResultUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 2018/6/25 create by fyc
 */
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
     * 展示备案单位的列表
     * @param map
     * @return
     */
    @RequestMapping(value = "/info")
    public JsonObjectBO selectAllRecordDepartment(@RequestBody Map map,HttpServletRequest httpServletRequest){
        int pageSize = (Integer)map.get("pageSize");
        int pageNum = (Integer) map.get("pageNum");
        String districtId = (String)map.get("districtId");
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        JSONObject jsonObject = new JSONObject();

        PageInfo<RecordDepartment> pageInfo = new PageInfo<>();
        try{
            if(districtId==""||districtId==null){
                pageInfo = recordDepartmentService.selectByDistrictId(user.getDistrictId(),pageSize,pageNum);
                jsonObject.put("recordDepartment",pageInfo);
            }else {
                pageInfo = recordDepartmentService.selectByDistrictId(districtId,pageSize,pageNum);
                jsonObject.put("recordDepartment",pageInfo);
            }
        }catch (Exception e ){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    /**
     * 展示备案单位的列表
     * @param map
     * @return
     */
    @RequestMapping(value = "/showhistory")
    public JsonObjectBO showmore(@RequestBody Map map,HttpServletRequest httpServletRequest){
        String flag = (String)map.get("flag");
        JSONObject jsonObject = new JSONObject();
        try{
                List<RecordDepartment> recordDepartments= recordDepartmentService.showMore(flag);
                jsonObject.put("recordDepartments",recordDepartments);
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
        int result = 0;
        try {
            result = recordDepartmentService.insert(recordDepartment);
            return ResultUtil.getResult(result);
        }catch (DuplicateKeyException exception){
            return JsonObjectBO.error("该编号已经存在");
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
    }

    /**
     * 区域
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/selectDistrict")
    public JsonObjectBO selectDistrict(HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        JSONObject jsonObject = new JSONObject();
        List<DistrictMenus> districtMenus = new ArrayList<>();

        try {
            districtMenus = districtService.selectOneDistrict(user.getDistrictId());
            jsonObject.put("districtMenus",districtMenus);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    /**
     * 删除备案单位
     * @param map
     * @return
     */
    @RequestMapping(value = "/delete")
    public JsonObjectBO delete(@RequestBody Map map){
        String id = (String)map.get("id");
        int result = 0;

        try{
            result = recordDepartmentService.deleteById(id);
            return ResultUtil.getResult(result);
        }catch (Exception e ){
            return JsonObjectBO.exception(e.getMessage());
        }

    }

    /**
     * 更新备案单位
     * @param recordDepartment
     * @return
     */
    @RequestMapping(value = "/update")
    public JsonObjectBO update(@RequestBody RecordDepartment recordDepartment){
        int result = 0;

        try{
            result = recordDepartmentService.updateById(recordDepartment);
            return ResultUtil.getResult(result);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }

    }


}
