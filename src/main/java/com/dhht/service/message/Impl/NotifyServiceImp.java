package com.dhht.service.message.Impl;

import com.dhht.dao.NotifyMapper;
import com.dhht.dao.NotifyReceiveDetailMapper;
import com.dhht.model.*;
import com.dhht.service.tools.FileService;
import com.dhht.service.user.UserService;
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

import java.io.File;
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

    @Value("${trackerPort}")
    private String trackerPort;

    @Value("${trackerServer}")
    private String trackerServer;

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
                //notifyReceiveDetail.setReceiveUserName(userSimple.getUserName());
                notifyReceiveDetail.setReceiveUserId(id);
                if( notifyReceiveDetailMapper.insert(notifyReceiveDetail)==0) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ResultUtil.isFail;
                }
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
                notify.setFiles(selectFileByPath(notify.getNotifyFileUrls()));
            }
        }
        try {
            int i = notifyReceiveDetailMapper.isRead(DateUtil.getCurrentTime(),receiveUserId);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
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
                    notify.setFiles(selectFileByPath(notify.getNotifyFileUrls()));
                }
                String result = notifyReadCount(notify.getId());
                if(notify.getIsRecall()) {
                    notify.setNotifyReadCount("已撤回！");
                }else {
                    notify.setNotifyReadCount(notifyReadCount(notify.getId()));
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
     * 查找文件列表
     * @param path
     * @return
     */
    public List<FileInfo> selectFileByPath(String path){
        String paths[] = StringUtil.toStringArray(path);
        List<FileInfo> fileList = new ArrayList<>();
        for(int i=0;i<paths.length;i++){
            FileInfo file = fileService.selectByPath(paths[i]);
            file.setFilePath("http://"+trackerServer+":"+trackerPort+"group1/"+file.getFilePath());
            fileList.add(file);
        }
        return fileList;
    }
}
