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
     * 添加用户
     *
     * @param user
     * @return
     */

    public int insert(User user) {
        User user1 = userDao.findByTelphone(user.getTelphone());
        if (user1 != null) {
            return ResultUtil.isHave;                  //该用户不存在
        }
        user.setId(UUIDUtil.generate());
        user.setUserName(user.getTelphone());
        String code = createRandomVcode();
        String password = MD5Util.toMd5(code);
        user.setPassword(password);
        user.setRoleId("GLY");
        Integer a = userDao.addUser(user);
        userPasswordService.sendMessage(user.getTelphone(), code);
        if (a != 1) {
            return ResultUtil.isFail;                //插入失败
        } else {
            return ResultUtil.isSuccess;                //插入成功
        }

    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @Override
    public int update(User user) {
        try {

            User user2 = userDao.findById(user.getId());
            if (!user2.getTelphone().equals(user.getTelphone())) {
                User user1 = userDao.findByTelphone(user.getTelphone());
                if (user1 != null) {
                    return ResultUtil.isHave;              //该用户已经存在
                }
                user.setUserName(user.getTelphone());
                Integer a = userDao.update(user);
                if (a != 1) {
                    return ResultUtil.isFail;             //修改失败
                } else {
                    userPasswordService.sendMessage(user.getTelphone(), createRandomVcode());
                    return ResultUtil.isSuccess;             //修改成功
                }
            } else {
                user.setUserName(user.getTelphone());
                Integer a = userDao.update(user);
                if (a != 1) {
                    return ResultUtil.isFail;             //修改失败
                } else {
                    userPasswordService.sendMessage(user.getTelphone(), createRandomVcode());
                    return ResultUtil.isSuccess;          //修改成功
                }
            }
        } catch (Exception e) {
//            jsonObjectBO.setMessage("出现异常");
//            jsonObjectBO.setCode(-1);
            return ResultUtil.isException;                   //出现异常
        }

    }


    /**
     * 管理员修改密码
     *
     * @param id
     * @param
     * @return
     */
    @Override
    public JsonObjectBO changePwd(String id) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String code = createRandomVcode();
        User user = userDao.findById(id);
        user.setPassword(MD5Util.toMd5(code));
        String phone = user.getTelphone();
        return userPasswordService.sendMessage(phone, code);
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
        if (realName == null && districtId == null && roleId == null) {
            PageInfo<User> result = selectByDistrict(localdistrictId, pageSize, pageNum);
            return result;
        } else if (districtId != null) {
            String districtIds[] = StringUtil.DistrictUtil(districtId);
            if (districtIds[1].equals("00") && districtIds[2].equals("00")) {
                list = userDao.find(realName, districtIds[0], roleId);
            } else if (!districtIds[1].equals("00") && districtIds[2].equals("00")) {
                list = userDao.find(realName, districtIds[0] + districtIds[1], roleId);
            } else if (!districtIds[1].equals("00") && !districtIds[2].equals("00")) {
                list = userDao.find(realName, districtId, roleId);
            }
        } else {
            list = userDao.find(realName, districtId, roleId);

        }

        PageInfo<User> result = new PageInfo<>(list);
        return result;
    }


    /**
     * 根据区域查找用户
     *
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
     * 根据电话删除用户
     *
     * @param phone
     * @return
     */
    @Override
    public int deleteByTelphone(String phone) {
        return userDao.deleteByTelphone(phone);
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