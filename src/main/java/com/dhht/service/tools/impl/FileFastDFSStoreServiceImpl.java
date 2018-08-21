package com.dhht.service.tools.impl;

import com.dhht.common.FastDFSClient;
import com.dhht.common.FastDFSFilename;
import com.dhht.model.FastDFSFile;
import com.dhht.service.tools.FileStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 文件存储的FastDFS方式实现
 * @author 赵兴龙
 */
@Service("fastDFSStoreServiceImpl")
public class FileFastDFSStoreServiceImpl implements FileStoreService {

    private static Logger logger = LoggerFactory.getLogger(FileFastDFSStoreServiceImpl.class);

    @Value("${trackerPort}")
    private String trackerPort;

    @Value("${trackerServer}")
    private String trackerServer;

    @Value("${trackerProtocol}")
    private String trackerProtocol;

    @Override
    public String store(byte[] fileData, String filename, String fileExt) {
        FastDFSFilename uploadResult = null;


        FastDFSFile file = new FastDFSFile(filename, fileData, fileExt);

        try {
            uploadResult = FastDFSClient.upload(file);
        } catch (Exception e) {
            logger.error("upload file Exception!",e);
        }
        if (uploadResult == null) {
            logger.error("upload file failed,please upload again!");
        }
        //这是暴露出真实的文件服务器地址
        // FastDFSClient.getTrackerUrl()+fileAbsolutePath[0]+ "/"+
        String path = uploadResult.getGroupName() + "/" + uploadResult.getFilename();

        return path;
    }

    @Override
    public boolean delete(String path) {
        FastDFSFilename filename = getGroupAndPath(path);
        try {
            FastDFSClient.deleteFile(filename.getGroupName(), filename.getFilename());
            return true;
        }catch (Exception e) {
            logger.error("upload delete Exception!",e);
            return false;
        }
    }

    @Override
    public byte[] readFile(String path) {
        FastDFSFilename filename = getGroupAndPath(path);
        return FastDFSClient.downFile(filename.getGroupName(), filename.getFilename());
    }

    @Override
    public String  getFullPath(String relativeFullName) {
        return trackerProtocol+trackerServer+trackerPort+relativeFullName;
    }

    /**
     * 将全路径生成fastdfs形式的分离路径
     * @param path
     * @return
     */
    private FastDFSFilename getGroupAndPath(String path) {
        FastDFSFilename filename = new FastDFSFilename();
        filename.setGroupName(path.substring(0, path.indexOf("/")));
        filename.setFilename(path.substring(path.indexOf("/") + 1));
        return filename;
    }

//    /**
//     * 找到这段字符串中某元素第几个出现的位置
//     */
//    private  int findNumber(String str,String letter,int num){
//        int i = 0;
//        int m = 0;
//        char c = new String(letter).charAt(0);
//        char [] ch = str.toCharArray();
//        for(int j=0; j<ch.length; j++){
//            if(ch[j] == c){
//                i++;
//                if(i == num){
//                    m = j;
//                    break;
//                }
//            }
//        }
//        return m;
//    }
}
