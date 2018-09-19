package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.IndexCount;
import com.dhht.model.IndexOverview;
import com.dhht.model.Seal;
import com.dhht.model.User;
import com.dhht.service.index.EmployeeIndexService;
import com.dhht.service.index.RecordDepartmentIndexService;
import com.dhht.service.seal.SealService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private RecordDepartmentIndexService recordDepartmentIndexService;
    @Autowired
    private EmployeeIndexService employeeIndexService;
    @Autowired
    private SealService sealService;

    /**
     * 备案单位信息总览
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/recordDepartment/overview")
    public JsonObjectBO recordDepartmentOverview(HttpServletRequest httpServletRequest){
        try {
            User user = (User) httpServletRequest.getSession().getAttribute("user");
            JSONObject jsonObject = new JSONObject();
            List<IndexOverview> indexOverviews = recordDepartmentIndexService.overview(user.getDistrictId());
            jsonObject.put("overview",indexOverviews);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取数据总览失败");
        }
    }

    /**
     * 备案综合排行
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/recordDepartment/ranking")
    public JsonObjectBO recordDepartmentRanking(HttpServletRequest httpServletRequest){
        try{
            User user =(User) httpServletRequest.getSession().getAttribute("user");
            JSONObject jsonObject = new JSONObject();
            List<IndexCount> indexCounts = recordDepartmentIndexService.ranking(user.getDistrictId());
            jsonObject.put("ranking",indexCounts);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取排行数据失败");
        }
    }

    /**
     * 备案单位折线图
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/recordDepartment/polyline")
    public JsonObjectBO recordDepartmentPolyline(HttpServletRequest httpServletRequest){
        try {
            User user = (User)httpServletRequest.getSession().getAttribute("user");
            JSONObject jsonObject = new JSONObject();
            List<IndexCount> indexCounts = recordDepartmentIndexService.polyline(user.getDistrictId());
            jsonObject.put("polyline",indexCounts);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取折线数据失败");
        }
    }


    /**
     * 从业人员饼图
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/employee/piechart")
    public JsonObjectBO employeePiechart(HttpServletRequest httpServletRequest){
        try {
            User user = (User)httpServletRequest.getSession().getAttribute("user");
            JSONObject jsonObject = new JSONObject();
            List<IndexCount> result = employeeIndexService.piechart(user);
            jsonObject.put("piechart",result);
            return JsonObjectBO.success("查询成功",jsonObject);

        }catch (Exception e){
            return JsonObjectBO.exception("获取印章统计数据失败");
        }
    }

    /**
     * 从业人员首页的印章列表
     * @param httpServletRequest
     * @param map
     * @return
     */
    @Log("首页印章列表")
    @RequestMapping("/seal")
    public JsonObjectBO employeeSealList(HttpServletRequest httpServletRequest, @RequestBody Map map){
        try {
            User user = (User)httpServletRequest.getSession().getAttribute("user");
            int pageNum = (Integer)map.get("pageNum");
            int pageSize = (Integer)map.get("pageSize");
            JSONObject jsonObject = new JSONObject();
            PageInfo<Seal> sealPageInfo = sealService.seal(user,"","","07",pageNum,pageSize);
            jsonObject.put("seal",sealPageInfo);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("查询印章列表失败");
        }
    }
}
