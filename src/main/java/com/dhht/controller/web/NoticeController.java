package com.dhht.controller.web;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Notice;
import com.dhht.model.NoticeSimple;
import com.dhht.model.User;
import com.dhht.service.message.NoticeService;
import com.dhht.util.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "message/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    private static Logger logger = LoggerFactory.getLogger(NoticeController.class);

    /**
     * 公告添加
     * @param notice
     * @param httpServletRequest
     * @return
     */
    @Log("公告添加")
    @RequestMapping(value = "/insert")
    public JsonObjectBO insert(@RequestBody Notice notice,HttpServletRequest httpServletRequest){
        try {
            User user = (User)httpServletRequest.getSession().getAttribute("user");
            return ResultUtil.getResult(noticeService.insert(notice,user));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("添加失败");
        }
    }

    /**
     * 展示用户已发送的公告
     * @param httpServletRequest
     * @param map
     * @return
     */
    @Log("查看已发送的公告")
    @RequestMapping(value = "/info")
    public JsonObjectBO info(HttpServletRequest httpServletRequest, @RequestBody Map map){
         User user = (User) httpServletRequest.getSession().getAttribute("user");
        int pageNum = (Integer) map.get("pageNum");
        int pageSize = (Integer) map.get("pageSize");
        JSONObject jsonObject = new JSONObject();
        try{
            PageHelper.startPage(pageNum,pageSize);
            List<Notice> list = noticeService.selectByUserName(user.getUserName());
            PageInfo pageInfo = new PageInfo(list);
            jsonObject.put("message",pageInfo);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }finally {
            PageHelper.clearPage();
        }
    }

    /**
     * 首页展示公告
     * @param httpServletRequest
     * @return
     */
    @Log("首页展示公告")
    @RequestMapping(value = "/show")
    public JsonObjectBO show(HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.put("message",noticeService.selectByNum(user.getDistrictId()));
            return JsonObjectBO.success("公告显示成功",jsonObject);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("一个未知异常，请刷新界面");
        }
    }

    /**
     * 拉取公告列表
     * @param httpServletRequest
     * @param map
     * @return
     */
    @Log("获取公告列表")
    @RequestMapping(value = "/noticeList")
    public JsonObjectBO noticeList(HttpServletRequest httpServletRequest, @RequestBody Map map){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        int pageNum = (Integer)map.get("pageNum");
        int pageSize = (Integer)map.get("pageSize");
        JSONObject jsonObject = new JSONObject();
        try {
            PageHelper.startPage(pageNum,pageSize);
            List<NoticeSimple> notices = noticeService.selectNoticeList(user.getDistrictId());
            PageInfo<NoticeSimple> pageInfo = new PageInfo(notices);
            jsonObject.put("notice",pageInfo);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            System.out.println(e.toString());
            return JsonObjectBO.exception("获取列表失败！");
        }finally {
            PageHelper.clearPage();
        }
    }

    /**
     * 获取详情页
     * @param map
     * @return
     */
    @Log("获取详情页")
    @RequestMapping(value = "/noticeDetail")
    public JsonObjectBO selectNoticeDetail(@RequestBody Map map){
        String id = (String)map.get("id");
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("notice",noticeService.selectNoticeDetail(id));
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("获取详情失败！");
        }
    }

    /**
     * 删除公告
     * @param map
     * @return
     */
    @Log("删除公告")
    @RequestMapping(value = "/delete")
    public JsonObjectBO deleteNotice(@RequestBody Map map){
        try {
            String id = (String) map.get("id");
            return ResultUtil.getResult(noticeService.delete(id));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("删除失败");
        }
    }


    /**
     * 修改公告
     * @param notice
     * @return
     */
    @Log("修改公告")
    @RequestMapping(value = "/update")
    public JsonObjectBO updateNotice(@RequestBody Notice notice){
        try {
            return ResultUtil.getResult(noticeService.update(notice));
        }catch (Exception e ){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("修改失败！");
        }
    }

}
