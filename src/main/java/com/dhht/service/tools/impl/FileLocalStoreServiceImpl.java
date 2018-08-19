package com.dhht.service.tools.impl;

import com.dhht.dao.FileMapper;
import com.dhht.service.tools.FileStoreService;
import com.dhht.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 本地存储的本地磁盘存储实现
 * @author 赵兴龙
 */
@Service("localStoreFileService")
public class FileLocalStoreServiceImpl implements FileStoreService,InitializingBean {
    /**
     * 本地存储根目录
     */
    @Value("${file.local.root}")
    private String rootDir;

    @Autowired
    private FileMapper fileMapper;

    private static final Logger logger = LoggerFactory.getLogger(FileLocalStoreServiceImpl.class);

    /**
     *保存文件
     * @param fileData 文件数据
     * @param filename 文件真实名称
     * @param fileExt 文件扩展名
     * @return 文件相对路径：保存成功时。
     *          null：保存失败时。
     */
    @Override
    public String store(byte[] fileData, String filename, String fileExt){
        String uniquenessName = generateFileName(fileExt);
        String relativeFullName = generateRelativeFullName(uniquenessName);

        File dest = new File(getFullPath(relativeFullName));
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
     * @param fileExt 文件扩展名
     * @return 带真实名称和扩展名的文件唯一名称。
     */
    private String generateFileName(String fileExt) {
        return UUIDUtil.generate() + "." + fileExt;
    }

    @Override
    public boolean delete(String path) {
        new File(getFullPath(path)).delete();
        return true;
    }

    /**
     * 获取文件绝对路径
     * @param relativeFullName
     * @return
     */
    private String getFullPath(String relativeFullName) {
        return rootDir + "/" + relativeFullName;
    }

    @Override
    public byte[] readFile(String path) {
        File file = new File(getFullPath(path));
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            logger.info("file:" + file.getName() + ". too big to process!");
            return null;
        }
        FileInputStream fi = null;
        byte[] buffer = null;

        try {
            fi = new FileInputStream(file);
            buffer = new byte[(int) fileSize];
            int offset = 0;
            int numRead = 0;
            while (offset < buffer.length
                    && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numRead;
            }
            // 确保所有数据均被读取
            if (offset != buffer.length) {
                logger.error("Could not completely read file: " + file.getName());
            }
        }catch (IOException e) {
            logger.error(e.toString());
        }finally {
            try {
                fi.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer;
    }

    /**
     * 如果根目录不存在则进行创建
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        File root = new File(rootDir);
        if(!root.exists()){
            root.mkdirs();
        }
    }
}