package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.CurrentUser;
import com.dhht.service.tools.HistoryService;
import com.dhht.sync.SyncDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.service.District.DistrictService;
import com.dhht.service.employee.EmployeeService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.service.tools.FileService;
import com.dhht.util.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/make/employee")
public class EmployeeController  {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private RecordDepartmentService recordDepartmentService;
    @Autowired
    private MakeDepartmentService makeDepartmentService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RedisTemplate redisTemplate;


    private static Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    /**
     * 菜单
     * @param httpServletRequest
     * @return
     */
    @Log("获取从业人员菜单")
    @RequestMapping(value = "/menus", method = RequestMethod.GET)
    public JsonObjectBO selectAllEmployee(HttpServletRequest httpServletRequest) {
        //String id = (String)map.get("districtId");
        JSONObject jsonObject = new JSONObject();
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        try {
            List<DistrictMenus> list = districtService.selectMakeDepartmentMenus(user.getDistrictId());
            jsonObject.put("menus", list);
            return JsonObjectBO.success("查询成功", jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("菜单获取失败");
        }
    }

    /**
     * 在职从业人员列表
     * @param map
     * @return
     */
    @Log("获取从业人员列表")
    @RequestMapping(value = "/info")
    public JsonObjectBO selectByDepartmentCode(@RequestBody Map map, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        int pageSize = (Integer) map.get("pageSize");
        int pageNum = (Integer) map.get("pageNum");
        int status = (Integer) map.get("status");
        String name = (String) map.get("name");
        String departmentCode = makeDepartmentService.selectCodeByLegalTelphone(user.getTelphone());
        JSONObject jsonObject = new JSONObject();
        try {
            PageHelper.startPage(pageNum, pageSize);
            if (status == 1) {
                PageInfo<Employee> pageInfo = new PageInfo<Employee>(employeeService.selectWorkEmployee(departmentCode, name));
                jsonObject.put("employee", pageInfo);
            } else if (status == 2) {
                PageInfo<Employee> pageInfo = new PageInfo<Employee>(employeeService.selectDeleteEmployee(departmentCode, name));
                jsonObject.put("employee", pageInfo);
            } else {
                PageInfo<Employee> pageInfo = new PageInfo<Employee>(employeeService.selectAllEmployee(departmentCode, name));
                jsonObject.put("employee", pageInfo);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("获取从业人员列表失败！");
        }finally {
            PageHelper.clearPage();
        }
        return JsonObjectBO.success("查询成功", jsonObject);
    }


    /**
     * 从业人员的添加
     * @param employee
     * @return
     */
    @Log("从业人员添加")
    @RequestMapping(value = "/insert")
    public JsonObjectBO insertEmployee(@RequestBody Employee employee, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        try {
            return ResultUtil.getResult(employeeService.insertEmployee(employee, user));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("添加失败！");
        }
    }


//    /**
//     * 从业人员的添加--import用
//     * @param employee
//     * @return
//     */
//    @Log("从业人员添加")
//    @RequestMapping(value = "/insertEmployeeImport")
//    public JsonObjectBO insertEmployeeImport(@RequestBody Employee employee, HttpServletRequest httpServletRequest) {
////        User user = (User) httpServletRequest.getSession().getAttribute("user");
//        try {
//            return ResultUtil.getResult(employeeService.insertEmployeeImport(employee));
//        } catch (Exception e) {
//            logger.error(e.getMessage(),e);
//            return JsonObjectBO.exception("添加失败！");
//        }
//    }


    /**
     * 修改从业人员
     * @param employee
     * @return
     */    @Log("修改从业人员")
    @RequestMapping(value = "/update")
    public JsonObjectBO update(@RequestBody Employee employee,HttpServletRequest httpServletRequest) {
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        try {
            return ResultUtil.getResult(employeeService.updateEmployee(employee,user));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("修改失败");
        }
    }

    /**
     * 删除从业人员
     * @param map
     * @return
     */
    @Log("删除从业人员")
    @RequestMapping(value = "/delete")
    public JsonObjectBO delete(@RequestBody Map map,HttpServletRequest httpServletRequest) {
        String id = (String) map.get("id");
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        try {
            return ResultUtil.getResult(employeeService.deleteEmployee(id,user));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("删除失败！");
        }
    }

    /**
     * 查询历史记录
     * @param map
     * @return
     */
    @Log("查询历史记录")
    @RequestMapping(value = "/history")
    public JsonObjectBO showHistory(@RequestBody Map map) {
        String flag = (String) map.get("flag");
        JSONObject jsonObject = new JSONObject();

        try {
            List<OperatorRecord> list = historyService.showUpdteHistory(flag, SyncDataType.EMPLOYEE);
            jsonObject.put("history", list);
            return JsonObjectBO.success("查询成功", jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("查询历史记录失败！");
        }
    }

    /**
     * emp表中存入URL字段
     * @param map
     * @return
     */
    @Log("头像URL保存")
    @RequestMapping(value = "/saveAvatar")
    public JsonObjectBO getUrl(@RequestBody Map map,HttpServletRequest httpServletRequest){
        String empId = (String)map.get("empId");
        String imgId = (String)map.get("imgId");
        User user = (User)httpServletRequest.getSession().getAttribute("user");

        try {
            return ResultUtil.getResult(employeeService.updateHeadById(empId,imgId,user));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("头像文件失败");
        }
    }

    @Log("从业人员详情")
    @RequestMapping(value ="/employeeDetail")
    public JsonObjectBO selectEmployeeDetail(@RequestBody Map map){
        try {
            String id = (String)map.get("id");
            JSONObject jsonObject = new JSONObject();
            Employee employee = employeeService.selectEmployeeByID(id);
            jsonObject.put("employee",employee);
            return JsonObjectBO.success("查询成功",jsonObject);
         }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("查看从业人员详情失败");
        }
    }

//    /**
//     * redis存储最大的从业人员编号
//     * @throws Exception
//     */
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        String emoloyeeCode = employeeService.selectMaxEmployeeCode();
//        if(emoloyeeCode == null) {
//            redisTemplate.opsForValue().set("employeeCode", 0);
//            return;
//        }
//        String temp = emoloyeeCode.substring(19);
//        Integer code = Integer.parseInt(temp);
//        if(!redisTemplate.hasKey("employeeCode")){
//            redisTemplate.opsForValue().set("employeeCode", code);
//        }else {
//            redisTemplate.opsForValue().getAndSet("employeeCode",code);
//        }
//    }


}
