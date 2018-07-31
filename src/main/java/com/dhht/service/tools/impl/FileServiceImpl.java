package com.dhht.service.tools.impl;

import com.dhht.common.CurrentUser;
import com.dhht.common.FastDFSClient;
import com.dhht.dao.FileMapper;
import com.dhht.model.FastDFSFile;
import com.dhht.model.FileInfo;
import com.dhht.model.User;
import com.dhht.service.tools.FileService;
import com.dhht.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Service("fileService")
public class FileServiceImpl implements FileService {

    @Autowired
    private FileMapper fileMapper;

    @Value("${trackerPort}")
    private String trackerPort;

    @Value("${trackerServer}")
    private String trackerServer;

    private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * 上传文件
     * @param request
     * @param file  文件
     * @return  返回的文件对象
     */
    @Override
    public FileInfo insertFile(HttpServletRequest request, MultipartFile file) {
        try {
            String path=saveFile(request,file);
            FileInfo file1=new FileInfo();
            file1.setId(UUIDUtil.generate());
            file1.setCreateTime(new Date(System.currentTimeMillis()));
            file1.setFileName(file.getOriginalFilename());
            file1.setFilePath(path);
            User user = CurrentUser.currentUser(request.getSession());
            file1.setOperationRecordId(user.getRealName());
            fileMapper.insert(file1);
            file1.setFilePath(trackerServer+trackerPort+path);
            return file1;
        }catch (Exception e) {
            logger.error("upload file failed",e);
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 删除文件服务文件和表
     * @param filePath
     * @return
     */
    @Override
    public boolean deleteFile(String filePath) {

        String groupName = filePath.substring(findNumber(filePath,"/",3)+1,findNumber(filePath,"/",4));
       String remoteFileName = filePath.substring(findNumber(filePath,"/",4)+1);
       try {
           FastDFSClient.deleteFile(groupName,remoteFileName);
           fileMapper.deleteByPrimaryPath(filePath);
           return true;
       }catch (Exception e) {
           logger.error("upload delete Exception!",e);
           return false;
       }
    }

    //找到这段字符串中某元素第几个出现的位置
    public  int findNumber(String str,String letter,int num){
        int i = 0;
        int m = 0;
        char c = new String(letter).charAt(0);
        char [] ch = str.toCharArray();
        for(int j=0; j<ch.length; j++){
            if(ch[j] == c){
                i++;
                if(i == num){
                    m = j;
                    break;
                }
            }
        }
        return m;
    }

    /**
     * 将文件保存在fastdfs文件服务器中
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public String saveFile(HttpServletRequest request,MultipartFile multipartFile) throws IOException {
        String[] fileAbsolutePath={};
        String fileName=multipartFile.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        byte[] file_buff = null;
        InputStream inputStream=multipartFile.getInputStream();
        if(inputStream!=null){
            int len1 = inputStream.available();
            file_buff = new byte[len1];
            inputStream.read(file_buff);
        }
        inputStream.close();
        FastDFSFile file = new FastDFSFile(fileName, file_buff, ext);
        try {
            fileAbsolutePath = FastDFSClient.upload(file);  //upload to fastdfs
        } catch (Exception e) {
            logger.error("upload file Exception!",e);
        }
        if (fileAbsolutePath==null) {
            logger.error("upload file failed,please upload again!");
        }
        //这是暴露出真实的文件服务器地址
        // FastDFSClient.getTrackerUrl()+fileAbsolutePath[0]+ "/"+
        String path=fileAbsolutePath[1];

        //这是反向代理的真实tomcat的地址
       // String path="http://"+request.getLocalName()+"/"+fileAbsolutePath[0]+ "/"+fileAbsolutePath[1];
        return path;
    }


    @Override
    public boolean insertLocal(FileInfo file) {
         if(fileMapper.insert(file)==1){
             return true;
         }else {
             return false;
         }
    }

    @Override
    public boolean deleteLocalFile(String filePath) {
        try {
            fileMapper.deleteByPrimaryPath(filePath);
            return true;
        }catch (Exception e) {
            logger.error("upload delete Exception!",e);
            return false;
        }

    }

    /**
     * 根据文件路径查询文件
     * @param path
     * @return
     */
    @Override
    public FileInfo selectByPath(String path) {
        return fileMapper.selectByPath(path);
    }
}
