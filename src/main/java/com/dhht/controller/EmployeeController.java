package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private FileService fileService;


    private static Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    /**
     * 菜单
     *
     * @param httpServletRequest
     * @return
     */
    @Log("获取从业人员菜单")
    @RequestMapping(value = "/menus", method = RequestMethod.GET)
    public JsonObjectBO selectAllEmployee(HttpServletRequest httpServletRequest) {
        //String id = (String)map.get("districtId");
        JSONObject jsonObject = new JSONObject();
        //User user = (User) httpServletRequest.getSession().getAttribute("user");
        try {
            List<DistrictMenus> list = districtService.selectMakeDepartmentMenus("330000");
            jsonObject.put("menus", list);
            return JsonObjectBO.success("查询成功", jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.getMessage());
        }
    }

    /**
     * 在职从业人员列表
     *
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
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功", jsonObject);
    }


    /**
     * 从业人员的添加
     *
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
            return JsonObjectBO.exception(e.getMessage());
        }
    }

/*
    RestMapping(value = "recordDepartment")
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
*/

    /**
     * 修改从业人员
     *
     * @param map
     * @return
     */
    @Log("修改从业人员")
    @RequestMapping(value = "/update")
    public JsonObjectBO update(@RequestBody Map map) {
        try {
            return ResultUtil.getResult(employeeService.updateEmployee(map));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.getMessage());
        }
    }

    /**
     * 删除从业人员
     *
     * @param map
     * @return
     */
    @Log("删除从业人员")
    @RequestMapping(value = "/delete")
    public JsonObjectBO delete(@RequestBody Map map) {
        String id = (String) map.get("id");

        try {
            return ResultUtil.getResult(employeeService.deleteEmployee(id));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.getMessage());
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
            List<Employee> list = employeeService.seletHistory(flag);
            jsonObject.put("history", list);
            return JsonObjectBO.success("查询成功", jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.getMessage());
        }
    }

    /**
     * 头像上传接口
     * @param request
     * @param file
     * @return
     */
    @Log("头像上传")
    @RequestMapping(value = "/upload", produces = "application/json;charset=UTF-8")
    public JsonObjectBO headFileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return JsonObjectBO.error("请选择上传文件");
        }
        try {
            File uploadFile = fileService.insertFile(request, file);
            if (uploadFile != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("file", uploadFile);
                return JsonObjectBO.success("头像上传成功", jsonObject);
            } else {
                return JsonObjectBO.error("头像上传失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("头像文件失败");
        }
    }

    /**
     * emp表中存入URL字段
     * @param map
     * @return
     */
    @Log("头像URL保存")
    @RequestMapping(value = "/getUrl")
    public JsonObjectBO getUrl(@RequestBody Map map){
        String id = (String)map.get("id");
        String url = (String)map.get("url");

        try {
            return ResultUtil.getResult(employeeService.updateHeadById(id,url));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("头像文件失败");
        }
    }
}
