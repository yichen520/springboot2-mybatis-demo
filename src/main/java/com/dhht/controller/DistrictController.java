package com.dhht.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.District;
import com.dhht.model.DistrictMenus;
import com.dhht.model.SysLog;
import com.dhht.model.User;
import com.dhht.service.District.DistrictService;
import com.dhht.sms.SmsSingleSender;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/sys/district")
public class DistrictController
{
    @Autowired
    private DistrictService districtService;

    @Autowired
     private StringRedisTemplate template;
    @Autowired
    private RedisTemplate redisTemplate;

//    @Override
//    public void afterPropertiesSet() throws Exception {
//        List<DistrictMenus> district = districtService.selectAllDistrict();
//        if(district== null) {
//            return ;
//        }
//        if(!template.hasKey("District")){
//            template.opsForValue().append("District", JSON.toJSONString(district));
//        }
////        else{
////            template.delete("District");
////        }
//
//    }

    /**
     * 查看所有区域
     * @return
     */
    @Log("查看所有区域")
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public JsonObjectBO selectAllDistrict(){

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        List<DistrictMenus> district = districtService.selectAllDistrict();
//        String districts = template.opsForValue().get("District");
//        List<DistrictMenus> district = JSON.parseArray(districts,DistrictMenus.class);
        jsonObject.put("District",district);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        return jsonObjectBO;
    }

    /**
     * 插入
     * @param map
     * @return
     */
    @RequestMapping(value = "insert")
    public JsonObjectBO insert(@RequestBody Map map){
        String districtId = (String)map.get("districtId");
        String parentId = (String)map.get("parentId");
        String districtName = (String)map.get("districtName");
        JsonObjectBO jsonObjectBO = districtService.insert(districtId,parentId,districtName);
        return jsonObjectBO;
    }

    /**
     * 删除
     * @param map
     * @return
     */
    @RequestMapping(value = "delete")
    public JsonObjectBO delete(@RequestBody Map map){
        String districtId = (String)map.get("districtId");
        JsonObjectBO jsonObjectBO = districtService.delete(districtId);
        return jsonObjectBO;
    }

    /**
     * 根据用户查询区域
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/select")
    public JsonObjectBO selectByRole(HttpServletRequest httpServletRequest){
       User user = (User) httpServletRequest.getSession().getAttribute("user");

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        List<DistrictMenus> district = districtService.selectOneDistrict(user.getDistrict().getDistrictId());

        jsonObject.put("District",district);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        return jsonObjectBO;

    }



}
