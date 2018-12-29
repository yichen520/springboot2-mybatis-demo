package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Evaluate;
import com.dhht.model.Examine;
import com.dhht.model.User;
import com.dhht.service.evaluate.EvaluateService;
import com.dhht.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 徐正平
 * @Date 2018/12/28 17:38
 */
@RestController
@RequestMapping(value = "/evaluate")
public class EvaluateController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(EvaluateController.class);
    @Autowired
    private EvaluateService evaluateService;

    @Log("新增制作单位评价")
    @RequestMapping("/insert")
    public JsonObjectBO insert( @RequestBody Evaluate evaluate){
        try {
            return  ResultUtil.getResultInfo(evaluateService.insert(evaluate,currentUser()));
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("评价失败");
        }
    }

    @Log("查询评价")
    @RequestMapping("/info")
    public JsonObjectBO info(  Evaluate evaluate){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("evaluate",evaluateService.selectEvaluateList(evaluate));
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exceptionWithMessage("查询失败",e.getMessage());
        }
    }

    @Log("删除评价")
    @RequestMapping("/delete")
    public JsonObjectBO info(@RequestBody Map map){
        String id = (String) map.get("id");
        try {
            return  ResultUtil.getResultInfo(evaluateService.deleteEvaluate(id));
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exceptionWithMessage("删除失败",e.getMessage());
        }
    }
}
