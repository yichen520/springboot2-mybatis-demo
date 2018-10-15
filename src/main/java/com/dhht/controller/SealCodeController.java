package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.SealCode;
import com.dhht.model.User;
import com.dhht.service.seal.SealCodeService;
import com.dhht.util.ResultUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "seal/sealcode")
public class SealCodeController implements InitializingBean {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SealCodeService sealCodeService;

    /**
     * 初始化加载印章编号
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<SealCode> sealCodes = sealCodeService.selectRecordDepartmentCode();
        for(SealCode sealCode : sealCodes){
            String code = sealCodeService.selectSealCode(sealCode.getRecordDepartmentCode());
            if(code ==null){
                //redisTemplate.opsForValue().set(code.substring(0,6),0);
                return;
            }
            Integer temp = Integer.parseInt(code.substring(6));
            redisTemplate.opsForValue().set(code.substring(0,6),temp);
        }
    }

    /**
     * 展示区域印章编号的最大值
      * @param httpServletRequest
     * @return
     */
    @RequestMapping("/info")
    public JsonObjectBO info(HttpServletRequest httpServletRequest){
        try {
            User user = (User) httpServletRequest.getSession().getAttribute("user");
            JSONObject jsonObject = new JSONObject();

            List<SealCode> sealCodes = sealCodeService.info(user.getDistrictId());
            jsonObject.put("sealCodes",sealCodes);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取印章编号信息失败");
        }
    }

    /**
     * 修改最大的印章编号
     * @param sealCode
     * @return
     */
    @RequestMapping("/update")
    public JsonObjectBO update(@RequestBody SealCode sealCode){
        try {
            int result = sealCodeService.updateSealCode(sealCode);
            return ResultUtil.getResult(result);
        }catch (Exception e){
            return JsonObjectBO.exception("修改失败");
        }
    }

    /**
     * 锁定redis
     * @param sealCode
     * @return
     */
    @RequestMapping("/lock")
    public JsonObjectBO lock(@RequestBody SealCode sealCode){
        try {
            boolean result = sealCodeService.lockSealCode(sealCode.getDistrictId());
            if(result){
                return JsonObjectBO.ok("锁定成功");
            }
            return JsonObjectBO.error("锁定失败");
        }catch (Exception e){
            return JsonObjectBO.exception("锁定异常");
        }
    }

}
