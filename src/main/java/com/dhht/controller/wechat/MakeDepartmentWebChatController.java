package com.dhht.controller.wechat;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.dao.MakeDepartmentSealPriceMapper;
import com.dhht.model.*;
import com.dhht.model.pojo.MakedepartmentSimplePO;
import com.dhht.service.employee.EmployeeService;
import com.dhht.service.evaluate.EvaluateService;
import com.dhht.service.make.MakeDepartmentSealPriceService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.seal.SealService;
import com.dhht.util.ResultUtil;
import com.dhht.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/weChat/make")
public class MakeDepartmentWebChatController extends WeChatBaseController {

    @Autowired
    private MakeDepartmentService makeDepartmentService;
    @Autowired
    private MakeDepartmentSealPriceService makeDepartmentSealPriceService;
    @Autowired
    private MakeDepartmentSealPriceMapper   makeDepartmentSealPriceMapper;
    @Autowired
    private SealService sealService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private EvaluateService evaluateService;
    @Autowired
    private EmployeeService employeeService;


    @RequestMapping(value = "/selectByName")
    public JsonObjectBO selectByAllName(@RequestBody Map map){
        try {
            String name = (String)map.get("name");
            Makedepartment makedepartment = makeDepartmentService.selectByAllName(name);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("makeDepartment",makedepartment);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("查询失败");
        }
    }

    @RequestMapping(value = "/selectEmpByMakeCode")
    public JsonObjectBO selectEmpByMakeCode(@RequestBody Map map){
        try {
            String makeCode = (String)map.get("code");
            List<Employee> employees = employeeService.selectByDepartmentCode(makeCode);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("employee",employees);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("查询失败");
        }
    }

    /**
     * 制作单位价格数据
     * @param map
     * @return
     */
    @RequestMapping(value = "/sealPriceInfo",method = RequestMethod.POST)
    public JsonObjectBO getSealPrice(@RequestBody Map map,HttpServletResponse httpServletResponse){
        try {
            init(httpServletRequest,httpServletResponse);
            String makeDepartmentFlag = (String)map.get("makeDepartmentFlag");
            String sealType = (String)map.get("sealType");
            JSONObject jsonObject = new JSONObject();
            MakeDepartmentSealPrice makeDepartmentSealPrice = makeDepartmentSealPriceService.selectByMakeDepartmentFlagAndType(makeDepartmentFlag,sealType);
            jsonObject.put("sealPrice",makeDepartmentSealPrice);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("查询制作单位价格失败");
        }
    }

    @RequestMapping("/sealPrice")
    public JsonObjectBO sealPrice(@RequestBody Map map,HttpServletResponse httpServletResponse){
        try {
            init(httpServletRequest,httpServletResponse);
            String makeDepartmentFlag=(String)map.get("makeDepartmentFlag");
            JSONObject jsonObject =new JSONObject();
            List<MakeDepartmentSealPrice> makeDepartmentSealPrices =makeDepartmentSealPriceMapper.selectByMakeDepartmentFlag(makeDepartmentFlag);
            jsonObject.put("makeDepartmentSealPrices",makeDepartmentSealPrices);
            return JsonObjectBO.success("价格获取成功",jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(),"价格获取失败");
        }
    }




    @RequestMapping(value = "/selectMakedePartment",method = RequestMethod.POST)
    public JsonObjectBO selectMakedePartment(@RequestBody MakedepartmentSimplePO makedepartmentSimplePO,HttpServletResponse httpServletResponse){
        try {
            init(httpServletRequest,httpServletResponse);
            JSONObject jsonObject = new JSONObject();
            if (makedepartmentSimplePO.getCityName()!=null && makedepartmentSimplePO.getCityName()!="") {

                List<MakedepartmentSimplePO> makedepartmentSimplePOs = makeDepartmentService.selectMakedePartment(makedepartmentSimplePO);
                jsonObject.put("makedepartmentList",makedepartmentSimplePOs);
            }
            if(makedepartmentSimplePO.getRegion()!=null && makedepartmentSimplePO.getRegion().length!=0){

                String[] districtArray = makedepartmentSimplePO.getRegion();
                String districtId ;
                if(districtArray.length == 1){
                    districtId = districtArray[0].substring(0,2);
                }else if(districtArray.length == 2){
                    districtId = districtArray[1].substring(0,4);
                }else {
                    districtId = districtArray[2].substring(0,6);
                }

                makedepartmentSimplePO.setDepartmentAddress(districtId);
                List<MakedepartmentSimplePO> makedepartmentSimplePOs = makeDepartmentService.selectMakedePartment(makedepartmentSimplePO);
                jsonObject.put("makedepartmentList",makedepartmentSimplePOs);
            }

            return JsonObjectBO.success("查询制作单位成功",jsonObject);
        }catch (Exception e){
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(),"查询制作单位失败");
        }
    }


    @RequestMapping("/detail")
    public JsonObjectBO makeDetail(@RequestBody Map map,HttpServletResponse httpServletResponse){
        try {
            init(httpServletRequest,httpServletResponse);
            String id = (String) map.get("id");
            JSONObject jsonObject = new JSONObject();
            MakedepartmentSimplePO makedepartment = makeDepartmentService.selectMakedepartmentSimplePODetailById(id);
            jsonObject.put("makeDepartment",makedepartment);
            return JsonObjectBO.success("获取详情成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取制作单位详情失败");
        }
    }

    @Log("查询评价")
    @RequestMapping("/evaluate/info")
    public JsonObjectBO info( @RequestBody Evaluate evaluate,HttpServletResponse httpServletResponse){
        try {
            init(httpServletRequest,httpServletResponse);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("evaluate",evaluateService.selectEvaluateList(evaluate));
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage("查询失败",e.getMessage());
        }
    }

    @Log("新增制作单位评价")
    @RequestMapping("/evaluate/insert")
    public JsonObjectBO insert( @RequestBody Evaluate evaluate,HttpServletResponse httpServletResponse){
        try {
            init(httpServletRequest,httpServletResponse);
            WeChatUser weChatUser = currentUser();
            return  ResultUtil.getResult(evaluateService.insert(evaluate,weChatUser));
        }catch (Exception e){
            e.printStackTrace();

            return JsonObjectBO.exception("评价失败");
        }
    }
}
