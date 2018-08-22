package com.dhht.service.message.Impl;

import com.dhht.dao.NotifyMapper;
import com.dhht.dao.NotifyReceiveDetailMapper;
import com.dhht.model.*;
import com.dhht.service.tools.FileService;
import com.dhht.util.*;
import com.dhht.service.message.NotifyService;
import com.dhht.util.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * create by fye 2018/7/18
 */
@Service(value = "NotifyService")
@Transactional
public class NotifyServiceImp implements NotifyService {
    @Autowired
    private NotifyMapper notifyMapper;
    @Autowired
    private NotifyReceiveDetailMapper notifyReceiveDetailMapper;
    @Autowired
    private FileService fileService;

    private final static String NOTIFY_FILE_UPLOAD = "通知文件上传";
    /**
     * 写通知
     * @param notify
     * @param user
     * @return
     */
    @Override
    public int insertNotify(Notify notify, User user) {
        try{
            notify.setId(UUIDUtil.generate());
            notify.setSendUsername(user.getUserName());
            notify.setSendRealname(user.getRealName());
            notify.setNotifyContent(notify.getNotifyContent());
            notify.setNotifyTitle(notify.getNotifyTitle());
            notify.setCreateTime(DateUtil.getCurrentTime());
            notify.setDistrictId(user.getDistrictId());
            if(notifyMapper.insert(notify)==0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isFail;
            }
            for (String id:notify.getNotifyUser()){
                NotifyReceiveDetail notifyReceiveDetail = new NotifyReceiveDetail();
                notifyReceiveDetail.setId(UUIDUtil.generate());
                notifyReceiveDetail.setNofityId(notify.getId());
                notifyReceiveDetail.setReceiveUserId(id);
                if( notifyReceiveDetailMapper.insert(notifyReceiveDetail)==0) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ResultUtil.isFail;
                }
            }
            if(notify.getNotifyFileUrls()!=null){
                List<FileInfo> fieldIds = selectFileIds(notify.getNotifyFileUrls());
                registerNoticeFile(fieldIds);
            }
            return ResultUtil.isSuccess;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResultUtil.isException;
        }
    }

    /**
     * 撤回通知
     * @param id
     * @param result
     * @return
     */
    @Override
    public int recallNotify(String id, String result) {
        try {
            int n = notifyMapper.recallNotify(id, DateUtil.getCurrentTime(), result);
            int r = notifyReceiveDetailMapper.deleteByNotifyId(id);
            if (n == 1 && r != 0) {
                return ResultUtil.isSuccess;
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isFail;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResultUtil.isException;
        }
    }

    /**
     * 统计新消息条数
     * @param receiveUserId
     * @return
     */
    @Override
    public int countNewNotify(String receiveUserId) {
        return notifyReceiveDetailMapper.countNewNotify(receiveUserId);
    }

    /**
     * 展示详细的通知
     * @param receiveUserId
     * @return
     */
    @Override
    public PageInfo<Notify> selectNotifyDetail(String receiveUserId,int pageNum,int pageSize) {
        List<NotifyReceiveDetail> notifyIds = notifyReceiveDetailMapper.selectNotifyIdByUserId(receiveUserId);
        List<Notify> notifies =new ArrayList<>();
        PageHelper.startPage(pageNum,pageSize);
        if(notifyIds.size()==0){
            return new PageInfo<Notify>(notifies);
        }
        notifies = notifyMapper.selectNotifyDetail(notifyIds);
        for(Notify notify:notifies){
            if(notify.getNotifyFileUrls()!=null) {
                List<FileInfo> list = selectFileIds(notify.getNotifyFileUrls());
                notify.setFiles(list);
            }
        }
        try {
            int i = notifyReceiveDetailMapper.isRead(DateUtil.getCurrentTime(),receiveUserId);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            PageHelper.clearPage();
            return new PageInfo<>(notifies);
        }
    }

    /**
     * 管理员查看自己发出的通知
     * @param userName
     * @return
     */
    @Override
    public List<Notify> selectNotifyBySendUser(String userName) {
        List<Notify> notifies = notifyMapper.selectNotifyBySendUser(userName);
        for (Notify notify:notifies){
                if(notify.getNotifyFileUrls()!=null) {
                    List<FileInfo> list = selectFileIds(notify.getNotifyFileUrls());
                    notify.setFiles(list);
                }
                String result = notifyReadCount(notify.getId());
                if(notify.getIsRecall()) {
                    notify.setNotifyReadCount("已撤回！");
                }else {
                    notify.setNotifyReadCount(result);
                }
            }
        return notifies;
    }

    /**
     * 统计
     * @param notifyId
     * @return
     */
    @Override
    public String notifyReadCount(String notifyId) {
        Integer readCount = notifyReceiveDetailMapper.countReadById(notifyId);
        Integer allCount = notifyReceiveDetailMapper.countAllById(notifyId);
        String result = "已读："+readCount.toString()+"  未读："+ (allCount-readCount);
        return result;
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
                if(fileInfo!=null){
                    result.add(fileInfo);
                }
            }
        }
        return result;
    }

    /**
     * 注册通知文件
     * @param fileIds
     * @return
     */
    public boolean registerNoticeFile(List<FileInfo> fileIds){
        for(FileInfo fileInfo : fileIds){
            boolean result =  fileService.register(fileInfo.getId(),NOTIFY_FILE_UPLOAD);
            if(!result){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        return true;
    }

}
