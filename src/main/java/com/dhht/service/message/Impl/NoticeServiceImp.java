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

    private static final String NOTICE_FILE_UPLOAD = "公告文件上传";

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
        int n = noticeMapper.insert(notice);
        if (n > 0) {
            if(notice.getNoticeFileUrl()!=null) {
                List<FileInfo> fileInfos = selectFileIds(notice.getNoticeFileUrl());
                boolean result = registerNoticeFile(fileInfos);
                if(!result){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ResultUtil.isError;
                }
            }
            return ResultUtil.isSuccess;
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
                List<FileInfo> fileList = selectFileIds(notice.getNoticeFileUrl());
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
        if(n<1){
            return ResultUtil.isFail;
        }
        if(notice.getNoticeFileUrl()!=null) {
            List<FileInfo> list = selectFileIds(notice.getNoticeFileUrl());
            for (FileInfo fileInfo:list) {
                boolean result = fileService.delete(fileInfo.getId());
                if(!result){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ResultUtil.isFail;
                }
            }
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
        if (i == 1) {
            if (notice.getNoticeFileUrl()!= null) {
                List<FileInfo> fileInfos = selectFileIds(notice.getNoticeFileUrl());
                boolean result = registerNoticeFile(fileInfos);
                if(result){}else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ResultUtil.isError;
                }
            }
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
        if(notice.getNoticeFileUrl()!=null){
            List<FileInfo> fileList = selectFileIds(notice.getNoticeFileUrl());
            notice.setFiles(fileList);
        }
        return notice;
    }


    /**
     * 处理文件ID的方法
     * @param fileId
     * @return
     */
     public List<FileInfo> selectFileIds(String fileId){
         List<FileInfo> result = new ArrayList<>();
         if(fileId!=null){
             String[] fileIds = StringUtil.toStringArray(fileId);
             for(int i=0;i<fileIds.length;i++){
                 FileInfo fileInfo = fileService.getFileInfo(fileIds[i]);
                 if(fileInfo!=null) {
                     result.add(fileInfo);
                 }
             }
         }
         return result;
     }

    /**
     * 注册公告文件
     * @param fileInfos
     * @return
     */
     public boolean registerNoticeFile(List<FileInfo> fileInfos){
         for(FileInfo fileInfo : fileInfos){
            boolean result =  fileService.register(fileInfo.getId(),NOTICE_FILE_UPLOAD);
            if(!result){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
         }
         return true;
     }
}
