//package com.dhht.controller;
//
//
//import com.alibaba.fastjson.JSONObject;
//import com.dhht.common.CurrentUser;
////import com.dhht.common.FastDFSClient;
//import com.dhht.common.JsonObjectBO;
//import com.dhht.model.FastDFSFile;
//import com.dhht.model.File;
//import com.dhht.service.tools.FileService;
//import com.dhht.util.UUIDUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Date;
//import java.util.UUID;
//
//@RestController
//
//public class UploadController {
//
//    @Autowired
//    private FileService fileService;
//
//
//    private static Logger logger = LoggerFactory.getLogger(UploadController.class);
//
//    @RequestMapping(value="/upload",produces="application/json;charset=UTF-8")
//    public JsonObjectBO singleFileUpload(@RequestParam("file") MultipartFile file,@RequestParam("flag")String flag) {
//        if (file.isEmpty()) {
//            return JsonObjectBO.error("请选择上传文件");
//        }
//        try {
//            String path=saveFile(file);
//            File file1=new File();
//            file1.setId(UUIDUtil.generate());
//            file1.setCreateTime(new Date(System.currentTimeMillis()));
//            file1.setFileName(file.getOriginalFilename());
//            file1.setFilePath(path);
//            file1.setSealCode(flag);
//
//            //插入到文件表
//             if(fileService.insertFile(file1)>0){
//                 return JsonObjectBO.success("文件上传成功",null);
//             }else {
//                 return JsonObjectBO.error("文件上传失败");
//             }
//        } catch (Exception e) {
//            logger.error("upload file failed",e);
//            logger.error(e.getMessage(), e);
//            return JsonObjectBO.exception("上传文件失败");
//        }
//    }
//
//    //将文件保存在fastdfs文件服务器中
//    public String saveFile(MultipartFile multipartFile) throws IOException {
//        String[] fileAbsolutePath={};
//        String fileName=multipartFile.getOriginalFilename();
//        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
//        byte[] file_buff = null;
//        InputStream inputStream=multipartFile.getInputStream();
//        if(inputStream!=null){
//            int len1 = inputStream.available();
//            file_buff = new byte[len1];
//            inputStream.read(file_buff);
//        }
//        inputStream.close();
//        FastDFSFile file = new FastDFSFile(fileName, file_buff, ext);
//        try {
//            fileAbsolutePath = FastDFSClient.upload(file);  //upload to fastdfs
//        } catch (Exception e) {
//            logger.error("upload file Exception!",e);
//        }
//        if (fileAbsolutePath==null) {
//            logger.error("upload file failed,please upload again!");
//        }
//        String path=FastDFSClient.getTrackerUrl()+fileAbsolutePath[0]+ "/"+fileAbsolutePath[1];
//        return path;
//    }
//
//}