package com.dhht.service.notice.Impl;


import com.dhht.dao.NoticeMapper;
import com.dhht.model.File;
import com.dhht.model.Notice;
import com.dhht.model.User;
import com.dhht.service.notice.NoticeService;
import com.dhht.service.tools.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import com.dhht.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * create by fyc 2018/7/16
 */
@Service(value = "noticeService")
@Transactional
public class NoticeServiceImp implements NoticeService{
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private FileService fileService;
    @Value("${notice.pageNum}")
    private Integer pageNum;

    /**
     * 新增公告
     * @param map
     * @param user
     * @return
     */
    @Override
    public int insert(Map map,User user) {
        Notice notice = new Notice();
        StringBuffer stringBuffer = new StringBuffer();
        try {
            notice.setId(UUIDUtil.generate());
            notice.setCreateTime(DateUtil.getCurrentTime());
            notice.setDistrictId(user.getDistrictId());
            notice.setSendRealname(user.getRealName());
            notice.setSendUsername(user.getUserName());
            notice.setNoticeContent((String) map.get("content"));
            notice.setNoticeTitle((String)map.get("title"));
            List<File> fileList = (List<File>) map.get("file");
            for (int i =0;i<fileList.size();i++) {
                String path = fileList.get(i).getFilePath();
                stringBuffer.append(path+";");
            }
            notice.setNoticeFileUrl(stringBuffer.toString());
            int i = noticeMapper.insert(notice);
            if (i == 1) {
                return ResultUtil.isSuccess;
            } else {
                return ResultUtil.isFail;
            }
        }catch (Exception e){
            return ResultUtil.isException;
        }
    }

    /**
     * 查询登入用户所发的公告
     * @param userName
     * @return
     */
    @Override
    public List<Notice> selectByUserName(String userName) {
        List<Notice> noticeList = noticeMapper.selectByUserName(userName);
        for (Notice notice:noticeList) {
            String paths[] = StringUtil.toStringArray(notice.getNoticeFileUrl());
            List<File> fileList = new ArrayList<>();
            for(int i = 0;i<paths.length;i++){
                fileList.add(fileService.selectByPath(paths[i]));
            }
            notice.setFiles(fileList);
        }
        return noticeList;
    }

    /**
     * 删除公告
     * @param id
     * @return
     */
    @Override
    public int delete(String id) {
        Notice notice = noticeMapper.selectById(id);
        String[] paths = StringUtil.toStringArray(notice.getNoticeFileUrl());
        int n = noticeMapper.delete(id);
        if(n!=1){
            return ResultUtil.isFail;
        }

       if(deleteFile(paths)==ResultUtil.isFail){
            return ResultUtil.isFail;
       }
        return ResultUtil.isSuccess;
    }

    /**
     * 更新公告
     * @param map
     * @return
     */
    @Override
    public int update(Map map) {
        Notice notice = noticeMapper.selectById((String) map.get("id"));
        StringBuffer stringBuffer = new StringBuffer();
        String paths[] = StringUtil.toStringArray(notice.getNoticeFileUrl());
        if(map.get("file")==null){
            notice.setNoticeTitle((String) map.get("content"));
            notice.setNoticeContent((String)map.get("title"));
          if(noticeMapper.update(notice)==1){
              return ResultUtil.isSuccess;
          }else {
              return ResultUtil.isFail;
          }
        }else {
            if(deleteFile(paths)==ResultUtil.isFail){
                return ResultUtil.isFail;
            }
            List<File> fileList = (List<File>) map.get("file");
            for (File file: fileList) {
                stringBuffer.append(file.getFilePath()+";");
            }
            notice.setNoticeTitle((String) map.get("content"));
            notice.setNoticeContent((String)map.get("title"));
            notice.setNoticeFileUrl(stringBuffer.toString());
            if(noticeMapper.update(notice)==1){
                return ResultUtil.isSuccess;
            }else {
                return ResultUtil.isFail;
            }
        }
    }

    /**
     * 在首页展示的公告
     * @return
     */
    @Override
    public List<Notice> selectByNum(String district) {
        String districtId = StringUtil.getDistrictId(district);
        return noticeMapper.selectNoticeByNum(pageNum,districtId);
    }

    /**
     * 删除文件表
     * @param paths
     * @return
     */
     public int deleteFile(String paths[]){
         for(int i = 0;i<paths.length;i++){
             if(!fileService.deleteFile(paths[i])){
                 TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                 return ResultUtil.isFail;
             }
         }
         return ResultUtil.isSuccess;
     }



}
