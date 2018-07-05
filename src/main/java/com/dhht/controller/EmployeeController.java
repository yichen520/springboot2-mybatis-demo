package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.service.District.DistrictService;
import com.dhht.service.employee.EmployeeService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.util.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/sys/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private RecordDepartmentService recordDepartmentService;
    @Autowired
    private MakeDepartmentService makeDepartmentService;

    private JSONObject jsonObject = new JSONObject();

    /**
     * 菜单
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/menus",method = RequestMethod.GET)
    public JsonObjectBO selectAllEmployee(HttpServletRequest httpServletRequest){
      //String id = (String)map.get("districtId");
      JSONObject jsonObject = new JSONObject();
     //User user = (User) httpServletRequest.getSession().getAttribute("user");
      try{
          List<DistrictMenus> list = districtService.selectMakeDepartmentMenus("330000");
          jsonObject.put("menus",list);
          return JsonObjectBO.success("查询成功",jsonObject);
      }catch (Exception e){
          return JsonObjectBO.exception(e.getMessage());
      }
    }

    /**
     * 从业人员列表
     * @param map
     * @return
     */
    @RequestMapping(value = "/info")
    public JsonObjectBO selectByDepartmentCode(@RequestBody Map map){
        int pageSize = (Integer) map.get("pageSize");
        int pageNum = (Integer) map.get("pageNum");
        String DepartmentCode = (String) map.get("code");
        try {
             PageHelper.startPage(pageNum,pageSize);
             PageInfo<Employee> pageInfo = new PageInfo<Employee>(employeeService.selectByDepartmentCode(DepartmentCode));
             jsonObject.put("employee",pageInfo);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    /**
     * 从业人员的添加
     * @param employee
     * @return
     */
    @RequestMapping(value = "insert")
    public JsonObjectBO insertEmployee(@RequestBody Employee employee,HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        MakeDepartmentSimple makeDepartmentSimple = makeDepartmentService.selectByLegalTephone(user.getTelphone());
        employee.setEmployeeDepartmentCode(makeDepartmentSimple.getDepartmentCode());
        try {
            return ResultUtil.getResult(employeeService.insertEmployee(employee));
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
    }


    /**
     * 添加时获取备案单位的信息
     * @param map
     * @return
     */
    @RequestMapping(value = "recordDepartment")
    public JsonObjectBO selectRecordDepartment(@RequestBody Map map){
        String districtId = (String)map.get("districtId");
        JSONObject jsonObject = new JSONObject();
        try {
            List<RecordDepartment> recordDepartments = recordDepartmentService.selectByDistrictId(districtId);
            jsonObject.put("recordDepartment",recordDepartments);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
    }



}
