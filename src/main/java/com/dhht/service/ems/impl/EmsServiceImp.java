package com.dhht.service.ems.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhht.common.Cache;
import com.dhht.model.Ems;
import com.dhht.model.FileInfo;
import com.dhht.service.ems.EmsService;
import com.dhht.service.tools.FileService;
import com.dhht.util.DateUtil;
import com.dhht.util.ResultUtil;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service(value = "emsService")
public class EmsServiceImp implements EmsService {

    @Autowired
    private FileService fileService;

    @Value("${file.local.ems}")
    private String filePath;
    @Override
    public Map<String,Object> insertEms(Ems ems) throws Exception {
        Map<String,Object> map = new HashMap<>();
       try {
           if (ems == null) {
               map.put("status", "error");
               map.put("message","数据不存在");
               return map;
           }
           JSONObject jsonObject = new JSONObject();
           Cache.put("ems", ems);
           String emsJson = JSON.toJSONString(ems);
           String fileId = byteOutStream(ems.getAddresser(),filePath, emsJson);
           map.put("status", "ok");
           map.put("message","上传成功");
           map.put("fileId",fileId);
           return map;
       }catch (IOException e) {
           e.printStackTrace();
           map.put("status", "error");
           map.put("message","出现异常");
           return map;
       }

    }

    public  String  byteOutStream(String sender,String filePath,String jsonString) throws Exception {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(DateUtil.getCurrentTime());
        //1:使用File类创建一个要操作的文件路径
        File file = new File(filePath+"ems/"+date+sender+".txt");
        if(!file.getParentFile().exists()){ //如果文件的目录不存在
            file.getParentFile().mkdirs(); //创建目录

        }

        //2: 实例化对象
        OutputStream output = new FileOutputStream(file);

        //3: 准备好实现内容的输出
//        String msg = object.toString();
        byte data[] = jsonString.getBytes();
        output.write(data);
        output.close();
        FileInfo fileInfo = fileService.save(data, DateUtil.getCurrentTime() + "ems", "txt", "", FileService.CREATE_TYPE_UPLOAD,UUIDUtil.generate(),"ems");
        String fileId = fileInfo.getId();
        return fileId;

    }
}
