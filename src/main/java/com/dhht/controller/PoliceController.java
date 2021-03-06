//package com.dhht.controller;
//
//import com.alibaba.fastjson.JSONObject;
//import com.dhht.common.JsonObjectBO;
//import com.dhht.model.RecordDepartment;
//import com.dhht.model.RecordPolice;
//import com.dhht.model.User;
//import com.dhht.service.police.PoliceService;
//import com.dhht.service.recordDepartment.Impl.RecordDepartmentServiceImp;
//import com.dhht.service.recordDepartment.RecordDepartmentService;
//import com.github.pagehelper.PageInfo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DuplicateKeyException;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping(value = "/sys/police")
//
//public class PoliceController {
//
//    @Autowired
//    private PoliceService policeService;
//
//    @Autowired
//    private RecordDepartmentService recordDepartmentService;
//
//    //private JSONObject jsonObject = new JSONObject();
//
//    @RequestMapping(value = "/info")
//    public JsonObjectBO selectByRole(@RequestBody Map map,HttpServletRequest httpServletRequest){
//        int pageSum = (Integer)map.get("pageSize");
//        int pageNum = (Integer)map.get("pageNum");
//       // User user = (User)httpServletRequest.getSession().getAttribute("user");
//        JSONObject jsonObject = new JSONObject();
//
//        PageInfo<RecordPolice> pageInfo = new PageInfo<RecordPolice>();
//        try{
//            pageInfo = policeService.selectByRole("330100",pageSum,pageNum);
//            jsonObject.put("Police",pageInfo);
//        }catch (Exception e){
//            return JsonObjectBO.exception(e.getMessage());
//        }
//        return JsonObjectBO.success("查询成功",jsonObject);
//    }
//
//    @RequestMapping(value = "/selectByOfficeCode")
//    public JsonObjectBO selectByOfficeCode(@RequestBody Map map) {
//        int pageSum = (Integer) map.get("pageSize");
//        int pageNum = (Integer) map.get("pageNum");
//        String Code = (String) map.get("code");
//        JSONObject jsonObject = new JSONObject();
//
//        PageInfo<RecordPolice> recordPolice = new PageInfo<RecordPolice>();
//        try {
//            recordPolice = policeService.selectByOfficeCode(Code, pageSum, pageNum);
//            jsonObject.put("Police", recordPolice);
//        } catch (Exception e) {
//            return JsonObjectBO.exception(e.getMessage());
//        }
//        return JsonObjectBO.success("查询成功", jsonObject);
//    }
//
//    @RequestMapping(value = "/selectAllPolice")
//    public JsonObjectBO selectAllPolice(@RequestBody Map map){
//        int pageSum = (Integer)map.get("pageSize");
//        int pageNum = (Integer)map.get("pageNum");
//        JSONObject jsonObject = new JSONObject();
//        PageInfo<RecordPolice> pageInfo = new PageInfo<RecordPolice>();
//        try{
//            pageInfo = policeService.selectAllPolice(pageSum,pageNum);
//            jsonObject.put("Police",pageInfo);
//        }catch (Exception e){
//            return JsonObjectBO.exception(e.getMessage());
//        }
//        return JsonObjectBO.success("查询成功",jsonObject);
//    }
//
//    @RequestMapping(value = "/delete")
//    public JsonObjectBO deleteById(@RequestBody Map map){
//        String telphone = (String) map.get("telphone");
//       boolean result = false;
//        try {
//            result = policeService.deleteByTelphone(telphone);
//        }catch (Exception e){
//            return JsonObjectBO.exception(e.getMessage());
//        }
//        if(result){
//            return JsonObjectBO.ok("删除成功");
//        }else {
//            return JsonObjectBO.error("删除失败");
//        }
//    }
//
//    @RequestMapping(value = "/insert")
//    public JsonObjectBO insert(@RequestBody RecordPolice recordPolice){
//        System.out.println(recordPolice.toString());
//        boolean result = false;
//        try {
//            result = policeService.insert(recordPolice);
//        }catch (DuplicateKeyException d){
//            JsonObjectBO.exception("警号冲突");
//        }catch (Exception e){
//            JsonObjectBO.exception(e.getMessage());
//        }
//        if(result){
//            return JsonObjectBO.ok("添加成功");
//        }else {
//            return JsonObjectBO.error("添加失败");
//        }
//    }
//
//    @RequestMapping(value = "/selectByPoliceCode")
//    public JsonObjectBO selectByPoliceCode(@RequestBody Map map){
//        String code = (String)map.get("code");
//        RecordPolice recordPolice = new RecordPolice();
//        JSONObject jsonObject = new JSONObject();
//
//        try {
//            recordPolice = policeService.selectByPoliceCode(code);
//            jsonObject.put("police",recordPolice);
//        }catch (Exception e ){
//            JsonObjectBO.exception(e.getMessage());
//        }
//        return JsonObjectBO.success("查询成功",jsonObject);
//    }
//
//    @RequestMapping(value = "/update")
//    public JsonObjectBO update(@RequestBody  RecordPolice recordPolice){
//       // RecordPolice recordPolice =(RecordPolice) map.get("police");
//        boolean result = false;
//
//        try {
//            result = policeService.updateByPrimaryKey(recordPolice);
//        }catch (Exception e ){
//            JsonObjectBO.exception(e.getMessage());
//        }
//        if(result){
//            return JsonObjectBO.ok("修改成功");
//        }
//        return JsonObjectBO.error("修改失败");
//    }
//
//    @RequestMapping(value = "/selectById")
//    public JsonObjectBO selectById(@RequestBody Map map){
//        String id = (String)map.get("id");
//        RecordPolice recordPolice = new RecordPolice();
//        JSONObject jsonObject = new JSONObject();
//
//        try{
//            recordPolice = policeService.selectById(id);
//            jsonObject.put("police",recordPolice);
//        }catch (Exception e ){
//            JsonObjectBO.exception(e.getMessage());
//        }
//        return JsonObjectBO.success("查询成功",jsonObject);
//    }
//
//    @RequestMapping(value = "/selectDepartment")
//    public JsonObjectBO selectDepartment(HttpServletRequest httpServletRequest){
//        User user = (User)httpServletRequest.getSession().getAttribute("user");
//        JSONObject jsonObject = new JSONObject();
//        List<RecordDepartment> recordDepartments =  new ArrayList<>();
//        try {
//            recordDepartments = recordDepartmentService.selectByDistrictId(user.getDistrictId());
//            jsonObject.put("department",recordDepartments);
//        }catch (Exception e){
//            return JsonObjectBO.exception(e.getMessage());
//        }
//        return JsonObjectBO.success("查询成功",jsonObject);
//    }
//
//}
