package com.dhht.controller;


import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.CurrentUser;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.FileInfo;
import com.dhht.model.User;
import com.dhht.model.pojo.FileInfoVO;
import com.dhht.service.tools.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;
    @Value("FilePath")
    private String filePath;

    private static Logger logger = LoggerFactory.getLogger(FileController.class);

    /**
     * 上传文件
     * @param request
     * @param file
     * @return
     */
    @Log("上传文件")
    @RequestMapping(value="/upload",produces="application/json;charset=UTF-8")
    public JsonObjectBO singleFileUpload(HttpServletRequest request,@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return JsonObjectBO.error("请选择上传文件");
        }
        try {
            byte[] fileBuff = null;
            InputStream inputStream = file.getInputStream();
            if(inputStream != null){
                int len1 = inputStream.available();
                fileBuff = new byte[len1];
                inputStream.read(fileBuff);
            }
            inputStream.close();

            String fileName = file.getOriginalFilename();
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

            User user = CurrentUser.currentUser(request.getSession());

            FileInfo fileInfo = fileService.save(fileBuff, fileName, ext, "", FileService.CREATE_TYPE_UPLOAD, user.getId(), user.getRealName());

            if(fileInfo != null){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("file",fileInfo);
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

    /**
     * 下载文件
     * @param id
     * @return
     */
    @Log("下载文件")
    @RequestMapping(value="/download",produces="application/json;charset=UTF-8")
    public ResponseEntity<byte[]> download(@RequestParam("id") String id) {
        FileInfoVO fileInfoVO = fileService.readFile(id);

        try {
            //请求头
            HttpHeaders headers = new HttpHeaders();

            //解决文件名乱码
//            String fileName = new String((fileInfoVO.getFileName()).getBytes("UTF-8"),"iso-8859-1");

            //通知浏览器以attachment（下载方式）打开
//            headers.setContentDispositionFormData("attachment", fileInfoVO.getFileName());

            //application/octet-stream二进制流数据（最常见的文件下载）。
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<byte[]>(fileInfoVO.getFileData(), headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}