package com.dhht.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.dao.*;
import com.dhht.model.RecordPolice;
import com.dhht.model.User;
import com.dhht.service.tools.SmsSendService;
import com.dhht.service.user.UserPasswordService;
import com.dhht.util.MD5Util;
import com.dhht.util.ResultUtil;
import com.dhht.util.StringUtil;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.dhht.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

/**
 * Created by 崔杨 on 2017/8/16.
 */
@Service(value = "userService")
@Transactional
public class UserServiceImpl implements UserService {


    @Autowired
    private UserDao userDao;


    @Autowired
    private SmsSendService smsSendService;

    @Autowired
    private RecordPoliceMapper recordPoliceMapper;

    @Autowired
    private UserPasswordService userPasswordService;

    @Value("${sms.template.newPassword}")
    private int newPassword ;
    @Value("${sms.template.insertUser}")
    private int userCode ;
    @Value("${sms.template.newUserName}")
    private int newUserName ;
    /**
     * 6位简单密码
     *
     * @return
     */
    public static String createRandomVcode() {
        //密码
        String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int) (Math.random() * 9);
        }
        return vcode;
    }

    /**
     * 增加用户
     * @param telphone
     * @param roleId
     * @param realName
     * @param districtId
     * @return
     */
    public int insert(String telphone,String roleId,String realName,String districtId){
        String userName = roleId+telphone;
        if(findByUserName(userName)!=null){
            return ResultUtil.isHave;
        }
        User user = new User();
        user.setId(UUIDUtil.generate());
        String code = createRandomVcode();
        String password = MD5Util.toMd5(code);
        user.setUserName(userName);
        user.setTelphone(telphone);
        user.setRoleId(roleId);
        user.setPassword(password);
        user.setDistrictId(districtId);
        user.setRealName(realName);
        Integer a = userDao.addUser(user);
        ArrayList<String> params = new ArrayList<String>();
        params.add(user.getUserName());
        params.add(code);
        Boolean b = smsSendService.sendSingleMsgByTemplate(telphone,userCode,params);
        if(b){
            return ResultUtil.isSend;
        }else if(!b){
            return ResultUtil.isNoSend;
        }
        else if (a != 1) {
            return ResultUtil.isFail;                //插入失败
        } else {
            return ResultUtil.isSuccess;                //插入成功
        }

    }



    /**
     * 修改用户
     * @param oldTelPhone
     * @param telPhone
     * @param roleId
     * @param realName
     * @param districtId
     * @return
     */
    @Override
    public int update(String oldTelPhone,String telPhone,String roleId,String realName,String districtId){
        try {
            String oldUserName = roleId + oldTelPhone;
            String userName = roleId + telPhone;
            User userSave = findByUserName(oldUserName);
            userSave.setDistrictId(districtId);
            userSave.setRealName(realName);
            userSave.setTelphone(telPhone);
            userSave.setUserName(userName);
            userSave.setRoleId(roleId);
            ArrayList<String> params = new ArrayList<>();
            if (!oldTelPhone.equals(telPhone)) {
                User user = findByUserName(userName);
                if (user != null) {
                    return ResultUtil.isHave;//用户已经存在
                }

                Integer a = userDao.update(userSave);
                if (a < 0) {
                    return ResultUtil.isFail;
                } else {
                    params.add(oldUserName);
                    params.add(userName);
                    smsSendService.sendSingleMsgByTemplate(userSave.getTelphone(), newUserName, params);
                    return ResultUtil.isSuccess;
                }

            } else {
                Integer a = userDao.update(userSave);
                if (a < 0) {
                    return ResultUtil.isFail;
                } else {
                    return ResultUtil.isSuccess;
                }
            }
         }catch (Exception e){
            return ResultUtil.isException;
        }
    }






    /**
     * 查询列表 模糊查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<User> find(User user, String realName, String roleId, String districtId, int pageNum, int pageSize) {
        List<User> list = new ArrayList<User>();
        PageHelper.startPage(pageNum, pageSize);
        String localdistrictId = user.getDistrictId();
        String role = user.getRoleId();
        if (realName == null && districtId == null && roleId == null) {
            PageInfo<User> result = selectByDistrict(role,localdistrictId, pageSize, pageNum);
            return result;
        } else if (districtId != null) {
            String districtIds[] = StringUtil.DistrictUtil(districtId);
            if (districtIds[1].equals("00") && districtIds[2].equals("00")) {
                list = userDao.find(realName, districtIds[0], roleId,role);
            } else if (!districtIds[1].equals("00") && districtIds[2].equals("00")) {
                list = userDao.find(realName, districtIds[0] + districtIds[1], roleId,role);
            } else if (!districtIds[1].equals("00") && !districtIds[2].equals("00")) {
                list = userDao.find(realName, districtId, roleId,role);
            }
        } else {
            String localdistrictIds[] = StringUtil.DistrictUtil(localdistrictId);
            if (localdistrictIds[1].equals("00") && localdistrictIds[2].equals("00")) {
                list = userDao.find(realName, localdistrictIds[0], roleId,role);
            } else if (!localdistrictIds[1].equals("00") && localdistrictIds[2].equals("00")) {
                list = userDao.find(realName, localdistrictIds[0] + localdistrictIds[1], roleId,role);
            }

        }

        PageInfo<User> result = new PageInfo<>(list);
        return result;
    }


    /**
     * 根据区域查找用户
     * @param id
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<User> selectByDistrict(String id, int pageSize, int pageNum) {
        List<User> list = new ArrayList<User>();
        String districtIds[] = StringUtil.DistrictUtil(id);
        if (districtIds[1].equals("00") && districtIds[2].equals("00")) {
            list = userDao.selectByDistrict(districtIds[0]);
        } else if (!districtIds[1].equals("00") && districtIds[2].equals("00")) {
            list = userDao.selectByDistrict(districtIds[0] + districtIds[1]);
        } else {
            list = userDao.selectByDistrict(id);
        }
        PageHelper.startPage(pageNum, pageSize, false);
        PageInfo<User> result = new PageInfo(list);
        return result;
    }

    /**
     *根据区域查找用户
     * @param role
     * @param id
     * @param pageSize
     * @param pageNum
     * @return
     */
    public PageInfo<User> selectByDistrict(String role,String id, int pageSize, int pageNum) {
        List<User> list = new ArrayList<User>();
        String districtIds[] = StringUtil.DistrictUtil(id);
        if (districtIds[1].equals("00") && districtIds[2].equals("00")) {
            list = userDao.selectByDistrict1(districtIds[0],role);
        } else if (!districtIds[1].equals("00") && districtIds[2].equals("00")) {
            list = userDao.selectByDistrict1(districtIds[0] + districtIds[1],role);
        } else {
            list = userDao.selectByDistrict1(id,role);
        }
        PageHelper.startPage(pageNum, pageSize, false);
        PageInfo<User> result = new PageInfo(list);
        return result;
    }

    /**
     * 根据userName删除
     * @param roleId
     * @param telphone
     * @return
     */
    @Override
    public int deleteByUserName(String roleId, String telphone) {
        int i= userDao.deleteByUserName(roleId+telphone);
        return i;
    }

    @Override
    public User findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }

    @Override
    public User findById(String Id){
        return userDao.findById(Id);
    }

    @Override
    public int delete(String id) {
        int a = userDao.delete(id);
        if (a != 1) {
            return ResultUtil.isFail;
        } else {
            return ResultUtil.isSuccess;
        }
    }
}