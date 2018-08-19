package com.dhht.service.message.Impl;


import com.dhht.dao.NoticeMapper;
import com.dhht.model.FileInfo;
import com.dhht.model.Notice;
import com.dhht.model.NoticeSimple;
import com.dhht.model.User;
import com.dhht.service.message.NoticeService;
import com.dhht.service.tools.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import com.dhht.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

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
//    @Value("${notice.pageNum}")
//    private Integer pageNum;
    @Value("${trackerPort}")
    private String trackerPort;

    @Value("${trackerServer}")
    private String trackerServer;

    /**
     * 新增公告
     * @param notice
     * @param user
     * @return
     */
    @Override
    public int insert(Notice notice,User user) {
        notice.setId(UUIDUtil.generate());
        notice.setCreateTime(DateUtil.getCurrentTime());
        notice.setDistrictId(user.getDistrictId());
        notice.setSendRealname(user.getRealName());
        notice.setSendUsername(user.getUserName());
        int i = noticeMapper.insert(notice);
        if (i == 1) {
            return ResultUtil.isSuccess;
        } else {
            return ResultUtil.isFail;
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
            if (notice.getNoticeFileUrl()!=null) {
                List<FileInfo> fileList = fileService.selectFileInfo(notice.getNoticeFileUrl());
                if (fileList.size() > 0) {
                    notice.setFiles(fileList);
                }
            }
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

        int n = noticeMapper.deleteById(id);
        if(n!=1){
            return ResultUtil.isFail;
        }
        if(notice.getNoticeFileUrl()!=null) {
            String[] paths = StringUtil.toStringArray(notice.getNoticeFileUrl());
            if (deleteFile(paths) == ResultUtil.isFail) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isFail;
            }
            return ResultUtil.isSuccess;
        }
        return ResultUtil.isSuccess;
    }

    /**
     * 更新公告
     * @param notice
     * @return
     */
    @Override
    public int update(Notice notice) {
       Notice oldDate = noticeMapper.selectById(notice.getId());
       notice.setSendUsername(oldDate.getSendUsername());
       notice.setSendRealname(oldDate.getSendRealname());
       notice.setCreateTime(oldDate.getCreateTime());
       notice.setDistrictId(oldDate.getDistrictId());
       int i = noticeMapper.update(notice);
       if(i==1){
           return ResultUtil.isSuccess;
       }else {
           return ResultUtil.isFail;
       }
    }

    /**
     * 在首页展示的公告
     * @return
     */
    @Override
    public List<NoticeSimple> selectByNum(String district) {
        String districtIds[] = StringUtil.DistrictUtil(district);
        String cityId = districtIds[0]+districtIds[1]+"00";
        String provinceId = districtIds[0]+"00"+"00";
        String districtId = districtIds[0]+districtIds[1]+districtIds[2];
        return noticeMapper.selectNoticeByNum(cityId,provinceId,districtId);
    }

    /**
     * 公告列表
     * @param district
     * @return
     */
    @Override
    public List<NoticeSimple> selectNoticeList(String district) {
        String districtIds[] = StringUtil.DistrictUtil(district);
        String cityId = districtIds[0]+districtIds[1]+"00";
        String provinceId = districtIds[0]+"00"+"00";
        String districtId = districtIds[0]+districtIds[1]+districtIds[2];
        return noticeMapper.selectNoticeList(cityId,provinceId,districtId);
    }

    /**
     * 公告详情展示
     * @param id
     * @return
     */
    @Override
    public Notice selectNoticeDetail(String id) {
        Notice notice = noticeMapper.selectNoticeDetail(id);
        List<FileInfo> fileList = new ArrayList<>();
        if(notice.getNoticeFileUrl()!=null){
            fileList = fileService.selectFileInfo(notice.getNoticeFileUrl());
            notice.setFiles(fileList);
        }
        return notice;
    }

    /**
     * 删除文件表
     * @param fileIds
     * @return
     */
     public int deleteFile(String[] fileIds){
         for(int i = 0;i<fileIds.length;i++){
             if(!fileService.delete(fileIds[i])){
                 TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                 return ResultUtil.isFail;
             }
         }
         return ResultUtil.isSuccess;
     }

    /**
     * 查找文件列表
     * @param noticeFileUrls
     * @return
     */
     public List<FileInfo> selectFileInfo(String noticeFileUrls) {
         String[] fileIds = StringUtil.toStringArray(noticeFileUrls);
         List<FileInfo> fileList = new ArrayList<>();
         if (fileIds.length>0) {
             for (int i = 0; i < fileIds.length; i++) {
                 FileInfo fileInfo = fileService.getFileInfo(fileIds[i]);
                 if(fileInfo!=null) {
                     fileInfo.setFilePath("http://" + trackerServer + ":" + trackerPort + "group1/" + fileInfo.getFilePath());
                     fileList.add(fileInfo);
                 }
             }
         }
         return fileList;
     }
}
