package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;

import com.dhht.model.pojo.SealVO;
import com.dhht.service.District.DistrictService;
import com.dhht.service.employee.EmployeeService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.seal.SealService;
import com.dhht.service.tools.HistoryService;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.dhht.sync.SyncDataType;
import com.dhht.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * create by fyc 2018/7/23
 */
@RestController
@RequestMapping(value = "/information")
public class InformationController {
    @Autowired
    private MakeDepartmentService makeDepartmentService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private UseDepartmentService useDepartmentService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private SealService sealService;
    @Autowired
    private HistoryService historyService;

    private static Logger logger = LoggerFactory.getLogger(InformationController.class);


    /**
     * 管理员查询制作单位接口
     * @param httpServletRequest
     * @param map
     * @return
     */
    @Log("获取制作单位列表")
    @RequestMapping(value = "/makeDepartment")
    public JsonObjectBO makeDepartmentInfo(HttpServletRequest httpServletRequest, @RequestBody Map map){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        int pageNum = (Integer)map.get("pageNum");
        int pageSize = (Integer)map.get("pageSize");
        String districtId = (String)map.get("districtId");
        String name = (String)map.get("name");
        String status = (String)map.get("status");


        PageHelper.startPage(pageNum,pageSize);
        List<MakeDepartmentSimple> list = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        try {
            if (districtId == null) {
                list = makeDepartmentService.selectInfo(user.getDistrictId(), name, status);
            } else {
                list = makeDepartmentService.selectInfo(districtId, name, status);
            }
            PageInfo<MakeDepartmentSimple> pageInfo = new PageInfo<>(list);
            jsonObject.put("makeDepartment",pageInfo);
            return JsonObjectBO.success("查询成功",jsonObject);
        } catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }finally {
            PageHelper.clearPage();
        }
    }

    /**
     * 查询制作单位详情
     * @param map
     * @return
     */
    @Log("查询制作单位详情")
    @RequestMapping(value = "/makeDepartmentDetail")
    public JsonObjectBO makeDepartmentDetail(@RequestBody Map map){
        String id = (String) map.get("id");
        JSONObject jsonObject = new JSONObject();

        try {
            Makedepartment makedepartment = makeDepartmentService.selectDetailById(id);
            jsonObject.put("makeDepartment",makedepartment);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("查询制作单位详情失败");
        }
    }

    @Log("制作单位历史")
    @RequestMapping(value = "/makeDepartmentHistory")
    public JsonObjectBO makeDepartmentHistory(@RequestBody Map map){
        String flag = (String)map.get("flag");
        List<OperatorRecord> result = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            result = historyService.showUpdteHistory(flag,SyncDataType.MAKEDEPARTMENT);
            jsonObject.put("makeDepartment",result);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    /**
     * 管理员查询从业人员
     * @param map
     * @return
     */
    @Log("获取从业人员列表")
    @RequestMapping(value = "/employee")
    public JsonObjectBO employeeInfo(@RequestBody Map map,HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        int pageNum = (Integer)map.get("pageNum");
        int pageSize = (Integer)map.get("pageSize");
        int status = (Integer) map.get("status");
        String name = (String)map.get("name");
        String code = (String)map.get("code");
        JSONObject jsonObject = new JSONObject();
        try {
            if(code==null||code==""){
                code = user.getDistrictId();
            }
            PageInfo pageInfo= employeeService.selectEmployeeInfo(code,status,name,pageNum,pageSize);
            jsonObject.put("employee",pageInfo);
            return JsonObjectBO.success("查询成功", jsonObject);
        } catch (Exception e) {
            return JsonObjectBO.exception(e.toString());
        }

    }

    @Log("获取从业人员列表")
    @RequestMapping(value = "/employee/nopage")
    public JsonObjectBO employeenopage(@RequestBody Map map,HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");

        int status = 1;
        String name = (String)map.get("name");
        String code = (String)map.get("code");

        JSONObject jsonObject = new JSONObject();
        List<Employee> employees =new ArrayList<>();
        try {
            if(code==null||code==""){
                code = user.getDistrictId();
            }
             employees= employeeService.selectWorkEmployee(code,name);
            jsonObject.put("employee",employees);
            return JsonObjectBO.success("查询成功", jsonObject);
        } catch (Exception e) {
            return JsonObjectBO.exception(e.toString());
        }
    }

    /**
     * 获取从业人员详情
     * @param map
     * @return
     */
    @Log("从业人员详情")
    @RequestMapping(value = "/employeeDetail")
    public JsonObjectBO employeeDetail(@RequestBody Map map){
        try {
            String id = (String)map.get("id");
            JSONObject jsonObject = new JSONObject();
            Employee employee = employeeService.selectEmployeeByID(id);
            jsonObject.put("employee",employee);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取从业人员详情失败");
        }
    }

    /**
     * 从业人员查询历史记录
     * @param map
     * @return
     */
    @Log("查询历史记录")
    @RequestMapping(value = "/employeeHistory")
    public JsonObjectBO showHistory(@RequestBody Map map) {
        String flag = (String) map.get("flag");
        JSONObject jsonObject = new JSONObject();

        try {
            List<OperatorRecord> list = historyService.showUpdteHistory(flag, SyncDataType.EMPLOYEE);
            jsonObject.put("history", list);
            return JsonObjectBO.success("查询成功", jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.getMessage());
        }
    }

    /**
     * 查询使用单位
     * @param map
     * @return
     */
    @Log("获取使用单位列表")
    @RequestMapping(value = "/useDepartment")
    public JsonObjectBO userDepartmentInfo(@RequestBody Map map,HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        String districtId = (String) map.get("districtId");
        String status = (String)map.get("status");
        String code = (String)map.get("code");
        String name = (String)map.get("name");
        int pageNum = (Integer)map.get("pageNum");
        int pageSize = (Integer) map.get("pageSize");

        try{
            String localDistrictId = StringUtil.DistrictUtil(user.getDistrictId())[0]+"0000";
            return useDepartmentService.find(localDistrictId,code,name,districtId,status,pageNum,pageSize);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("使用单位列表获取失败");
        }finally{
            PageHelper.clearPage();
        }
    }

    /**
     * 印章信息
     *
     * @param httpServletRequest
     * @param sealOperator
     * @return
     */
    @Log("印章信息")
    @RequestMapping("/seal")
    public JsonObjectBO seal(HttpServletRequest httpServletRequest, @RequestBody SealOperator sealOperator) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        User user = (User) httpServletRequest.getSession(true).getAttribute("user");
        String telphone = user.getTelphone();
        String useDepartmentName = sealOperator.getSeal().getUseDepartmentName();
        String useDepartmentCode = sealOperator.getSeal().getUseDepartmentCode();
        String status = sealOperator.getSeal().getSealStatusCode();
        int pageNum = sealOperator.getPageNum();
        int pageSize = sealOperator.getPageSize();
        try {
            PageInfo<Seal> seal = sealService.seal(user,useDepartmentName, useDepartmentCode, status, pageNum, pageSize);
            jsonObject.put("seal", seal);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("查询成功");
            return jsonObjectBO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("发生异常！");
        }finally {
            PageHelper.clearPage();
        }
    }

    @Log("印章信息")
    @RequestMapping("/mdseal")
    public JsonObjectBO mdSeal(HttpServletRequest httpServletRequest, @RequestBody SealOperator sealOperator) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        User user = (User) httpServletRequest.getSession(true).getAttribute("user");
        String telphone = user.getTelphone();
        String useDepartmentName = sealOperator.getSeal().getUseDepartmentName();
        String useDepartmentCode = sealOperator.getSeal().getUseDepartmentCode();
        String status = sealOperator.getSeal().getSealStatusCode();
        int pageNum = sealOperator.getPageNum();
        int pageSize = sealOperator.getPageSize();
        try {
            PageInfo<Seal> seal = sealService.mdSeal(user,useDepartmentName, useDepartmentCode, status, pageNum, pageSize);
            jsonObject.put("seal", seal);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("查询成功");
            return jsonObjectBO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("发生异常！");
        }finally {
            PageHelper.clearPage();
        }
    }

    /**
     * 查看印章详情
     *
     * @param map
     * @return
     */
    @Log("查看印章详情")
    @RequestMapping(value = "sealDetail")
    public JsonObjectBO selectDetailById(@RequestBody Map map) {
        JSONObject jsonObject = new JSONObject();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String id = (String) map.get("id");
        try {
            SealVO sealVo = sealService.selectDetailById(id);
            jsonObject.put("sealVo", sealVo);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setData(jsonObject);
            return jsonObjectBO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("发生异常！");
        }
    }
    /**
     * 查询使用单位详情
     * @param map
     * @return
     */
    @Log("获取使用单位详情")
    @RequestMapping(value = "/useDepartmentDetail")
    public JsonObjectBO useDepartmentDetail(@RequestBody Map map){
        String id = (String)map.get("id");
        JSONObject jsonObject = new JSONObject();
        try {
            UseDepartment useDepartment = useDepartmentService.selectDetailById(id);
            jsonObject.put("useDepartment",useDepartment);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e ){
            return JsonObjectBO.exception("获取使用单位详情失败！");
        }
    }

    /**
     * 查看使用单位历史
     * @param map
     * @return
     */
    @Log("查看历史")
    @RequestMapping(value = "/useDepartmentHistory" , method = RequestMethod.POST)
    public JsonObjectBO showMore(@RequestBody Map map){
        String flag = (String)map.get("flag");
        try {
            JsonObjectBO jsonObjectBO = useDepartmentService.showHistory(flag);
            return jsonObjectBO;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("查询使用单位历史失败");
        }
    }

    /**
     * 获取区域列表
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/district")
    public JsonObjectBO selectDistrict(HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        JSONObject jsonObject = new JSONObject();
        List<DistrictMenus> districtMenus = new ArrayList<>();
        try {
            districtMenus = districtService.selectOneDistrict(user.getDistrictId());
            jsonObject.put("districtMenus",districtMenus);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    /**
     *制作单位菜单接口
     * @param httpServletRequest
     * @return
     */
    @Log("制作单位菜单")
    @RequestMapping(value = "/districtMakeDepartment")
    public JsonObjectBO selectMakeDepartmentByDistrict(HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        JSONObject jsonObject = new JSONObject();

        try{
            List<DistrictMenus> list = districtService.selectMakeDepartmentMenus(user.getDistrictId());
            jsonObject.put("districtMenus",list);
            return JsonObjectBO.success("菜单返回成功",jsonObject);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("制作单位信息获取失败");
        }
    }
    @Log("制作单位查看详情")
    @RequestMapping(value = "/makeDepartmentSealDetail")
    public JsonObjectBO sealDetails(@RequestBody Map map){
        JSONObject jsonObject = new JSONObject();
        String id = (String)map.get("id");

        try {
            SealVO sealVO = makeDepartmentService.sealDetails(id);
            jsonObject.put("sealDetails",sealVO);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            JsonObjectBO.exception(e.toString());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }


    /**
     *根据制作单位查找印章列表
     */
    @Log("获取印章列表")
    @RequestMapping(value = "/selectSeal")
    public JsonObjectBO selectSeal(HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        JSONObject jsonObject = new JSONObject();
        List<Seal> seals = new ArrayList<>();
        try {
            seals = makeDepartmentService.selectSeal(user);
            jsonObject.put("seals",seals);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
    }


    
}
