package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.FileInfo;
import com.dhht.model.Notice;
import com.dhht.model.NoticeSimple;
import com.dhht.model.User;
import com.dhht.service.message.NoticeService;
import com.dhht.service.tools.FileService;
import com.dhht.service.user.UserService;
import com.dhht.util.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "message/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;

    /**
     * 附件上传的接口
     * @param multipartFiles
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/upload",produces = "application/json;charset=UTF-8")
    public JsonObjectBO upload( @RequestParam("file") MultipartFile multipartFiles, HttpServletRequest httpServletRequest){
        JSONObject jsonObject = new JSONObject();
        try {
                FileInfo file = fileService.insertFile(httpServletRequest,multipartFiles);
               if(file==null){
                   return JsonObjectBO.error("文件上传失败");
               }
           jsonObject.put("file",file);
           return JsonObjectBO.success("文件上传成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.error("文件上传时发生错误");
        }
    }

    /**
     * 公告添加
     * @param notice
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/insert")
    public JsonObjectBO insert(@RequestBody Notice notice,HttpServletRequest httpServletRequest){
        try {
            User user = (User)httpServletRequest.getSession().getAttribute("user");
            return ResultUtil.getResult(noticeService.insert(notice,user));
        }catch (Exception e){
            return JsonObjectBO.exception("添加失败");
        }
    }

    /**
     * 展示用户已发送的公告
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping(value = "/info")
    public JsonObjectBO info(HttpServletRequest httpServletRequest, @RequestBody Map map){
         User user = (User) httpServletRequest.getSession().getAttribute("user");
        int pageNum = (Integer) map.get("pageNum");
        int pageSize = (Integer) map.get("pageSize");
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(pageNum,pageSize);

        try{
            List<Notice> list = noticeService.selectByUserName(user.getUserName());
            PageInfo pageInfo = new PageInfo(list);
            jsonObject.put("message",pageInfo);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception(e.toString());
        }
    }

    /**
     * 首页展示公告
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/show")
    public JsonObjectBO show(HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.put("message",noticeService.selectByNum(user.getDistrictId()));
            return JsonObjectBO.success("公告显示成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
    }

    /**
     * 拉取公告列表
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping(value = "/noticeList")
    public JsonObjectBO noticeList(HttpServletRequest httpServletRequest, @RequestBody Map map){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        int pageNum = (Integer)map.get("pageNum");
        int pageSize = (Integer)map.get("pageSize");
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(pageNum,pageSize);

        try {
            List<NoticeSimple> notices = noticeService.selectNoticeList(user.getDistrictId());
            PageInfo<NoticeSimple> pageInfo = new PageInfo(notices);
            jsonObject.put("notice",pageInfo);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
    }

    /**
     * 获取详情页
     * @param map
     * @return
     */
    @RequestMapping(value = "/noticeDetail")
    public JsonObjectBO selectNoticeDetail(@RequestBody Map map){
        String id = (String)map.get("id");
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("notice",noticeService.selectNoticeDetail(id));
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取详情失败！");
        }
    }

    /**
     * 删除公告
     * @param map
     * @return
     */
    @RequestMapping(value = "/delete")
    public JsonObjectBO deleteNotice(@RequestBody Map map){
        String id = (String)map.get("id");
        return ResultUtil.getResult(noticeService.delete(id));
    }


    /**
     * 修改公告
     * @param notice
     * @return
     */
    @RequestMapping(value = "/update")
    public JsonObjectBO updateNotice(@RequestBody Notice notice){
        try {
            return ResultUtil.getResult(noticeService.update(notice));
        }catch (Exception e ){
            return JsonObjectBO.exception("修改失败！");
        }
    }

}
