package com.dhht.controller.wechat;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.SealAgent;
import com.dhht.service.seal.SealAgentWeChatService;
import com.dhht.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/weChat/sealAgent")
public class SealAgentWeChatController {
    @Autowired
    private SealAgentWeChatService sealAgentWeChatService;

    /**
     * 经办人增加
     * @param sealAgent
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/insertSealAgent")
    public JsonObjectBO insertSealAgent(@RequestBody SealAgent sealAgent, HttpServletRequest httpServletRequest){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try{
            String telphone = (String) httpServletRequest.getSession().getAttribute("mobilePhone");
            int result = sealAgentWeChatService.insertSealAgent(sealAgent,telphone);
            if(result==ResultUtil.isSuccess){
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("增加成功");
            }else{
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("增加失败");
            }
            return jsonObjectBO;
        }catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(),"增加失败");
        }
    }

    /**
     * 经办人修改
     * @param sealAgent
     * @return
     */
    @RequestMapping("/updateSealAgent")
    public JsonObjectBO updateSealAgent(@RequestBody SealAgent sealAgent){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try{
            int result = sealAgentWeChatService.updateSealAgent(sealAgent);
            if(result==ResultUtil.isSuccess){
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("修改成功");
            }else{
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("修改失败");
            }
            return jsonObjectBO;
        }catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(),"修改失败");
        }
    }

    /**
     * 删除经办人
     * @param map
     * @return
     */
    @RequestMapping("/deleteSealAgent")
    public JsonObjectBO deleteSealAgent(@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try{
            String id = (String)map.get("id");
            int result = sealAgentWeChatService.deleteSealAgent(id);
            if(result==ResultUtil.isSuccess){
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("删除成功");
            }else{
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("删除失败");
            }
            return jsonObjectBO;
        }catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(),"删除失败");
        }
    }


    /**
     * 经办人列表
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/sealAgentInfo")
    public JsonObjectBO sealAgentInfo(HttpServletRequest httpServletRequest){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        try{
            String telphone = (String) httpServletRequest.getSession().getAttribute("mobilePhone");
            List<SealAgent> result = sealAgentWeChatService.sealAgentList(telphone);
            jsonObject.put("sealAgents",result);
            jsonObjectBO.setData(jsonObject);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(),"查询失败");
        }
    }

    /**
     * 经办人详情
     * @param map
     * @return
     */
    @RequestMapping("/sealAgentDetails")
    public JsonObjectBO sealAgentDetails(@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        try{
            String id = (String)map.get("id");
            SealAgent result = sealAgentWeChatService.sealAgentDetails(id);
            jsonObject.put("SealAgent",result);
            jsonObjectBO.setData(jsonObject);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(),"查询失败");
        }
    }
}
