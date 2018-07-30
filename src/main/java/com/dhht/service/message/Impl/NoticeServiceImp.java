package com.dhht.service.message.Impl;


import com.dhht.dao.NoticeMapper;
import com.dhht.model.File;
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
            String paths[] = StringUtil.toStringArray(notice.getNoticeFileUrls());
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

        int n = noticeMapper.deleteById(id);
        if(n!=1){
            return ResultUtil.isFail;
        }
        if(notice.getNoticeFileUrls()!=null) {
            String[] paths = StringUtil.toStringArray(notice.getNoticeFileUrls());
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
        return noticeMapper.selectNoticeByNum(pageNum,cityId,provinceId);
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
        return noticeMapper.selectNoticeList(cityId,provinceId);
    }

    /**
     * 公告详情展示
     * @param id
     * @return
     */
    @Override
    public Notice selectNoticeDetail(String id) {
        Notice notice = noticeMapper.selectNoticeDetail(id);
        List<File> fileList = new ArrayList<>();
        if(notice.getNoticeFileUrls()!=null){
            String paths[] = StringUtil.toStringArray(notice.getNoticeFileUrls());
            for(int i=0;i<paths.length;i++){
                fileList.add(fileService.selectByPath(paths[i]));
            }
            notice.setFiles(fileList);
        }
        return notice;
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
