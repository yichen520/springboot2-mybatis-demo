package com.dhht.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.service.District.DistrictService;
import com.dhht.service.examine.MinitorService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.punish.PunishService;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 2018/7/2 create by xzp
 */
@RestController
@RequestMapping(value = "punish")
public class PunishController {

    @Autowired
    private MakeDepartmentService makeDepartmentService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private PunishService punishService;

    @Autowired
    private RecordDepartmentService recordDepartmentService;

    private static Logger logger = LoggerFactory.getLogger(PunishController.class);

    /**
     *对制作单位进行惩罚操作
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/makedepartment/add")
    public JsonObjectBO punish(HttpServletRequest httpServletRequest,@RequestBody MakePunishRecord makePunishRecord){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        try {
              if (punishService.insertPunish(user,makePunishRecord)){
                  return JsonObjectBO.success("制作单位惩罚成功",null);
              }else {
                  return JsonObjectBO.error("制作单位惩罚失败");
              }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.getMessage());
        }
    }
    /**
     *对制作单位进行查询操作
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/makedepartment/find")
    public JsonObjectBO find(HttpServletRequest httpServletRequest,@RequestBody Map map){
        String makedepartmentName = (String)map.get("makedepartmentName");
        String startTime = (String) map.get("startTime");
        String endTime = (String) map.get("endTime");
        String districtId = (String) map.get("district");
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        if (districtId == null){
            districtId = StringUtil.getDistrictId(user.getDistrictId());
        }else{
            districtId = StringUtil.getDistrictId(districtId);
        }

        Integer pageSize =(Integer) map.get("pageSize");
        Integer pageNum =(Integer) map.get("pageNum");
        JSONObject jsonObject = new JSONObject();
        try {
            PageHelper.startPage(pageNum, pageSize);
            PageInfo<MakePunishRecord>  pageInfo =new PageInfo<MakePunishRecord> (punishService.findPunish(makedepartmentName,startTime,endTime,districtId));
            jsonObject.put("punishInfo",pageInfo);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }






}
