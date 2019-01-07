package com.dhht.service.ems.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhht.common.Cache;
import com.dhht.model.Ems;
import com.dhht.service.ems.EmsService;
import com.dhht.util.ResultUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service(value = "emsService")
public class EmsServiceImp implements EmsService {

    @Value("${file.local.ems}")
    private String filePath;
    @Override
    public int insertEms(Ems ems) throws Exception {
       try {
           if (ems == null) {
               return ResultUtil.isNoEms;
           }
           JSONObject jsonObject = new JSONObject();
           Cache.put("ems", ems);
           String emsJson = JSON.toJSONString(ems);
           byteOutStream(filePath, emsJson);
       }catch (IOException e) {
           e.printStackTrace();
           return ResultUtil.isError;
       }
       return ResultUtil.isSuccess;

    }

    public static void byteOutStream(String filePath,String jsonString) throws Exception {

        //1:使用File类创建一个要操作的文件路径
        File file = new File(filePath+"ems/ems.txt");
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

    }
}
