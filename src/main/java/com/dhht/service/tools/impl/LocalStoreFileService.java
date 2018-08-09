package com.dhht.service.tools.impl;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.dao.FileInfosMapper;
import com.dhht.model.FileInfos;
import com.dhht.service.tools.FileInfoService;
import com.dhht.service.tools.FileService;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
@Service("localStoreFileService")
public class LocalStoreFileService implements FileInfoService {
    /**
     * 本地存储根目录
     */
    @Value("${file.local.root}")
    private String rootDir;

    @Autowired
    private FileInfosMapper fileInfosMapper;


    @Override
    public FileInfos storeFile(byte[] fileData, String filename, String fileExt, String memo) {
        String relativeFullName = storeFile(fileData, filename, fileExt);
        if(relativeFullName == null) {
            return null;
        }
        return saveFileInfo(filename, fileExt, relativeFullName, memo);
    }

    /**
     * 保存文件信息
     * @param filename 文件真实名称
     * @param fileExt 文件扩展名
     * @param relativeFullName 文件相对路径
     * @param memo 备注
     * @return FileInfo 保存成功时。
     *
     */
    private FileInfos saveFileInfo(String filename, String fileExt, String relativeFullName, String memo) {
        FileInfos fileInfo = new FileInfos();
        fileInfo.setId(UUIDUtil.generate());
        fileInfo.setFileName(filename);
        fileInfo.setFileExt(fileExt);
        fileInfo.setFilePath(relativeFullName);
        fileInfo.setCreateTime(new Date(System.currentTimeMillis()));
        fileInfo.setCreateMemo(memo);
        fileInfosMapper.insertSelective(fileInfo);
        //Todo 调用dao接口保存fileInfo
        return fileInfo;
    }

    /**
     *保存文件
     * @param fileData 文件数据
     * @param filename 文件真实名称
     * @param fileExt 文件扩展名
     * @return 文件相对路径：保存成功时。
     *          null：保存失败时。
     */
    private String storeFile(byte[] fileData, String filename, String fileExt){
        String uniquenessName = generateFileName(filename, fileExt);
        String relativeFullName = generateRelativeFullName(uniquenessName);

        File dest = new File(rootDir + "/" + relativeFullName);
        //判断文件是否已经存在
        if (dest.exists()) {
            return "文件已存在";
        }
        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(dest));
            outputStream.write(fileData);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return relativeFullName;
    }

    /**
     * 生成文件全路径
     * @param uniquenessName 文件唯一名称
     * @return 按照特定日期格式组织的文件相对路径
     */
    private String generateRelativeFullName(String uniquenessName) {
        return generatePathByDate() + "/" + uniquenessName;
    }

    /**
     * 根据日期生成路径
     * @return 按照特定格式生成的日期路径
     */
    private String generatePathByDate() {
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }

    /**
     * 生成文件名
     * @param filename 文件真实名称，不含扩展名
     * @param fileExt 文件扩展名
     * @return 带真实名称和扩展名的文件唯一名称。
     */
    private String generateFileName(String filename, String fileExt) {
        return UUIDUtil.generate() + "_" + filename + fileExt;
    }

    @Override
    public boolean register(String id, String memo) {
        return false;
    }

    @Override
    public boolean deleteFile(String id) {
        return false;
    }

    @Override
    public FileInfos getFileInfo(String id) {
        return null;
    }

    @Override
    public byte[] readFile(String id) {
        return new byte[0];
    }
}