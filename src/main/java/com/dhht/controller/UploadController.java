package com.dhht.controller;


import com.alibaba.fastjson.JSONObject;
//import com.dhht.common.FastDFSClient;
import com.dhht.common.CurrentUser;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.File;
import com.dhht.model.User;
import com.dhht.service.tools.FileService;
import com.dhht.util.FileUtil;
import com.dhht.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController

public class UploadController {

    @Autowired
    private FileService fileService;


    private static Logger logger = LoggerFactory.getLogger(UploadController.class);

    /**
     * 删除文件（fastdfs文件服务器）
     * @param map  文件的url链接路径
     * @return
     */
    @RequestMapping(value="/deleteFile",produces="application/json;charset=UTF-8")
    public JsonObjectBO deleteFile(@RequestBody Map map){
        String filePath = (String) map.get("filePath");
        try {
            boolean deleteFile =fileService.deleteFile(filePath);
            if(deleteFile){
                return JsonObjectBO.success("文件删除成功",null);
            }else {
                return JsonObjectBO.error("文件删除失败");
            }
        } catch (Exception e) {
            logger.error("upload file failed",e);
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("上传删除失败");
        }

    }

    /**
     * 上传到fastdfs文件服务器
     * @param request
     * @param file
     * @return
     */
    @RequestMapping(value="/upload",produces="application/json;charset=UTF-8")
    public JsonObjectBO singleFileUpload(HttpServletRequest request,@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return JsonObjectBO.error("请选择上传文件");
        }
        try {
            File uploadFile =fileService.insertFile(request,file);
            if(uploadFile!=null){
                JSONObject jsonObject =new JSONObject();
                jsonObject.put("file",uploadFile);
                return JsonObjectBO.success("文件上传成功",jsonObject);
            }else {
                return JsonObjectBO.error("文件上传失败");
            }
        } catch (Exception e) {
            logger.error("upload file failed",e);
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("上传文件失败");
        }
    }
    //上传到本地
    @RequestMapping(value="/uploadLocal",produces="application/json;charset=UTF-8")
    public JsonObjectBO uploadLocal(HttpServletRequest request,@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return JsonObjectBO.error("请选择上传文件");
        }
        try {
            String fileName = file.getOriginalFilename();
            String path =ClassUtils.getDefaultClassLoader().getResource("").getPath();
            // String filePath = request.getServletContext().getRealPath("upload/");
            FileUtil.uploadFile(file.getBytes(), path, fileName);

            File file1=new File();
            file1.setId(UUIDUtil.generate());
            file1.setCreateTime(new Date(System.currentTimeMillis()));
            file1.setFileName(file.getOriginalFilename());
            file1.setFilePath(fileName);
            User user = CurrentUser.currentUser(request.getSession());
            file1.setOperationRecordId(user.getRealName());

            if(fileService.insertLocal(file1)){
                JSONObject jsonObject =new JSONObject();
                jsonObject.put("file",fileName);
                return JsonObjectBO.success("上传文件成功",jsonObject);
            }else {
                return JsonObjectBO.exception("上传文件失败");
            }
        } catch (Exception e) {
            logger.error("upload file failed",e);
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("上传文件失败");
        }
    }

    @RequestMapping(value="/deleteLocalFile",produces="application/json;charset=UTF-8")
    public JsonObjectBO deleteLocalFile(@RequestBody Map map){
        String filePath = (String) map.get("filePath");
        try {
            boolean deleteFile =fileService.deleteLocalFile(filePath);
            if(deleteFile){
                return JsonObjectBO.success("文件删除成功",null);
            }else {
                return JsonObjectBO.error("文件删除失败");
            }
        } catch (Exception e) {
            logger.error("upload file failed",e);
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("上传删除失败");
        }

    }


}