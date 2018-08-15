package com.dhht.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.service.employee.EmployeeService;
import com.dhht.service.punish.PunishService;
import com.dhht.service.user.UserLoginService;
import com.dhht.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 2018/7/2 create by xzp
 */
@RestController
@RequestMapping(value = "punish")
public class PunishController {

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PunishService punishService;

    @Autowired
    private StringRedisTemplate template;

    @Value("${sms.template.employeepunish}")
    private int employeepunish ;
    @Value("${sms.template.makedepartmentpunish}")
    private int makedepartmentpunish ;
    @Value("${expireTime}")
    private long expireTime;

    private static Logger logger = LoggerFactory.getLogger(PunishController.class);

    /**
     *对制作单位进行惩罚操作
     * @param httpServletRequest
     * @return
     */

    @RequestMapping(value = "/makedepartment/add")
    public JsonObjectBO punish(HttpServletRequest httpServletRequest,@RequestBody MakePunishRecord makePunishRecord){
        User user = validatetoken(httpServletRequest);
        if (user == null){
            return   JsonObjectBO.sessionLose("session失效");
        }
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
        User user = validatetoken(httpServletRequest);
        if (user == null){
            return   JsonObjectBO.sessionLose("session失效");
        }
        String makedepartmentName = (String)map.get("makedepartmentName");
        String startTime = (String) map.get("startTime");
        String endTime = (String) map.get("endTime");
        String districtId = (String) map.get("districtId");
        if (districtId == null || districtId == ""){
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

    /**
     *查询制作单位下所有从业人员
     * @return
     */
    @RequestMapping(value = "/employee/find")
    public JsonObjectBO employee(HttpServletRequest httpServletRequest,@RequestBody Map map){
        User user = validatetoken(httpServletRequest);
        if (user == null){
            return   JsonObjectBO.sessionLose("session失效");
        }
        String departmentCode = (String) map.get("departmentCode");
        JSONObject jsonObject = new JSONObject();
        try {
           List<Employee> employees = employeeService.selectByDepartmentCode(departmentCode);
            jsonObject.put("employees",employees);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    /**
     *对从业人员进行惩罚操作
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/employee/add")
    public JsonObjectBO employeeadd(HttpServletRequest httpServletRequest,@RequestBody EmployeePunishRecord employeePunish){
        User user = validatetoken(httpServletRequest);
        if (user == null){
            return   JsonObjectBO.sessionLose("session失效");
        }
        try {
            if (punishService.insertEmployeePunish(user,employeePunish)){
                return JsonObjectBO.success("从业人员惩罚成功",null);
            }else {
                return JsonObjectBO.error("从业人员惩罚失败");
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.getMessage());
        }
    }

    /**
     *对从业人员处罚进行查询操作
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/employee/info")
    public JsonObjectBO employeefind(HttpServletRequest httpServletRequest,@RequestBody Map map){
        User user = validatetoken(httpServletRequest);
        if (user == null){
            return   JsonObjectBO.sessionLose("session失效");
        }
        String makedepartmentName = (String)map.get("makedepartmentName");
        String startTime = (String) map.get("startTime");
        String endTime = (String) map.get("endTime");
        String districtId = (String) map.get("districtId");
        if (districtId == null || districtId == ""){
            districtId = StringUtil.getDistrictId(user.getDistrictId());
        }else{
            districtId = StringUtil.getDistrictId(districtId);
        }
        Integer pageSize =(Integer) map.get("pageSize");
        Integer pageNum =(Integer) map.get("pageNum");
        JSONObject jsonObject = new JSONObject();
        try {
            PageHelper.startPage(pageNum, pageSize);
            PageInfo<EmployeePunishRecord>  pageInfo =new PageInfo<EmployeePunishRecord> (punishService.findEmployeePunish(makedepartmentName,startTime,endTime,districtId));
            jsonObject.put("punishInfo",pageInfo);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    /**
     * 惩罚制作单位获取验证码
     */
    @RequestMapping(value = "/punishMakedepartmentCode")
    public JsonObjectBO getCheckCode(HttpServletRequest httpServletRequest,@RequestBody Map map){
        User user = validatetoken(httpServletRequest);
        if (user == null){
            return   JsonObjectBO.sessionLose("session失效");
        }
        String phone = (String)map.get("telphone");
        String departmentName = (String)map.get("departmentName");
        String code = StringUtil.createRandomVcode();

        ArrayList<String> params = new ArrayList<String>();
        params.add(departmentName);
        params.add(code);

        if (punishService.sendcode(phone,makedepartmentpunish,params)){
            return  JsonObjectBO.ok("获取验证码成功");
        }else{
            return  JsonObjectBO.error("获取验证码失败");
        }

    }
    /**
     * 惩罚从业人员获取验证码
     */
    @RequestMapping(value = "/punishEmployeeCode")
    public JsonObjectBO punishEmployeeCode(HttpServletRequest httpServletRequest,@RequestBody Map map){
        User user = validatetoken(httpServletRequest);
        if (user == null){
            return   JsonObjectBO.sessionLose("session失效");
        }
        String phone = (String)map.get("telphone");
        String code = StringUtil.createRandomVcode();
        ArrayList<String> params = new ArrayList<String>();
        params.add(code);
        if (punishService.sendcode1(phone,employeepunish,params)){
            return  JsonObjectBO.ok("获取验证码成功");
        }else{
            return JsonObjectBO.error("获取验证码失败");
        }
    }

    @Log("处罚验证手机号")
    @RequestMapping(value ="checkPhone", method = RequestMethod.POST)
    public JsonObjectBO checkPhone(@RequestBody SMSCode smsCode){
        try {
            return userLoginService.checkAPPPhoneAndIDCard(smsCode);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("发送短信发生异常");
        }
    }
    public  User validatetoken(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("token");
        if(template.hasKey(token)){
            template.expire(token,expireTime,TimeUnit.SECONDS);
            String user = template.opsForValue().get(token);
            User currentUser =  JSON.parseObject(user,User.class);
          return currentUser;
        }else {
            return null;
        }
    }

}
