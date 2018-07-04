package com.dhht.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.dao.*;
import com.dhht.model.RecordPolice;
import com.dhht.model.User;
import com.dhht.service.tools.SmsSendService;
import com.dhht.service.user.UserPasswordService;
import com.dhht.util.MD5Util;
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

    private final static int SUCCESS =1;
    private final static int ERROR = -1;

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

    public JsonObjectBO insert(User user) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        if (user.getTelphone() == null) {
            jsonObjectBO.setCode(ERROR);
            jsonObjectBO.setMessage("请输入手机号");
            return jsonObjectBO;
        }
        User user1 = userDao.findByTelphone(user.getTelphone());
        if (user1 != null) {
            jsonObjectBO.setCode(ERROR);
            jsonObjectBO.setMessage("该用户已经存在");
            return jsonObjectBO;
        }
        user.setId(UUIDUtil.generate());
        user.setUserName(user.getTelphone());
        String code = createRandomVcode();
        String password = MD5Util.toMd5(code);
        user.setPassword(password);
        user.setRoleId("GLY");
        Integer a = userDao.addUser(user);
        ArrayList<String> params = new ArrayList<String>();
        params.add(code);
        smsSendService.sendSingleMsgByTemplate(user.getTelphone(),newPassword,params);
        if (a != 1) {
            jsonObjectBO.setCode(ERROR);
            jsonObjectBO.setMessage("添加失败");
            return jsonObjectBO;
        } else {
            jsonObjectBO.setCode(SUCCESS);
            jsonObjectBO.setMessage("添加成功");
            return jsonObjectBO;
        }

    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @Override
    public JsonObjectBO update(User user) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {

            User user2 = userDao.findById(user.getId());
            if (!user2.getTelphone().equals(user.getTelphone())) {
                User user1 = userDao.findByTelphone(user.getTelphone());
                if (user1 != null) {
                    jsonObjectBO.setCode(ERROR);
                    jsonObjectBO.setMessage("该用户已经存在");
                    return jsonObjectBO;
                }
                user.setUserName(user.getTelphone());
                Integer a = userDao.update(user);
                if (a != 1) {
                    jsonObjectBO.setCode(ERROR);
                    jsonObjectBO.setMessage("修改失败");
                    return jsonObjectBO;
                } else {
                    jsonObjectBO.setCode(SUCCESS);
                    jsonObjectBO.setMessage("修改成功");
                    ArrayList<String> params = new ArrayList<String>();
                    params.add(createRandomVcode());
                    smsSendService.sendSingleMsgByTemplate(user.getTelphone(),newPassword,params);
                    return jsonObjectBO;
                }
            } else {
                user.setUserName(user.getTelphone());
                Integer a = userDao.update(user);
                if (a != 1) {
                    jsonObjectBO.setCode(ERROR);
                    jsonObjectBO.setMessage("修改失败");
                } else {
                    jsonObjectBO.setCode(SUCCESS);
                    jsonObjectBO.setMessage("修改成功");
                    userPasswordService.sendMessage(user.getTelphone(), createRandomVcode());
                }
                return jsonObjectBO;
            }
        }catch (Exception e){
            jsonObjectBO.setMessage("出现异常");
            jsonObjectBO.setCode(-1);
            return jsonObjectBO;
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
        return userPasswordService.sendMessage(phone,code);
    }


    /**
     * 查询列表 模糊查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public JsonObjectBO find(User user,String realName, String roleId, String districtId, int pageNum, int pageSize) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(pageNum, pageSize);
        String localdistrictId = user.getDistrictId();
        if (realName == null && districtId == null && roleId == null) {
            PageInfo<User> result = selectByDistrict(localdistrictId,pageSize,pageNum);
            jsonObject.put("user", result);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(SUCCESS);
            jsonObjectBO.setMessage("查询成功");
        } else if(districtId != null){
            String districtIds[] = StringUtil.DistrictUtil(districtId);
            if(districtIds[1].equals("00")&&districtIds[2].equals("00")){
                List<User> list = userDao.find(realName,districtIds[0],roleId);
                PageInfo<User> result = new PageInfo<>(list);
                jsonObject.put("user", result);
                jsonObjectBO.setData(jsonObject);
                jsonObjectBO.setCode(SUCCESS);
                jsonObjectBO.setMessage("查询成功");
            }else if(!districtIds[1].equals("00")&&districtIds[2].equals("00")){
                List<User> list = userDao.find(realName,districtIds[0]+districtIds[1],roleId);
                PageInfo<User> result = new PageInfo<>(list);
                jsonObject.put("user", result);
                jsonObjectBO.setData(jsonObject);
                jsonObjectBO.setCode(SUCCESS);
                jsonObjectBO.setMessage("查询成功");
            }else if (!districtIds[1].equals("00")&&!districtIds[2].equals("00")){
                List<User> list = userDao.find(realName,districtId,roleId);
                PageInfo<User> result = new PageInfo<>(list);
                jsonObject.put("user", result);
                jsonObjectBO.setData(jsonObject);
                jsonObjectBO.setCode(SUCCESS);
                jsonObjectBO.setMessage("查询成功");
            }
            }else {
            List<User> list = userDao.find(realName,districtId,roleId);
            PageInfo<User> result = new PageInfo<>(list);
            jsonObject.put("user", result);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(SUCCESS);
            jsonObjectBO.setMessage("查询成功");
        }
        return jsonObjectBO;
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
        if(districtIds[1].equals("00")&&districtIds[2].equals("00")){
            list = userDao.selectByDistrict(districtIds[0]);
        }else if(!districtIds[1].equals("00")&&districtIds[2].equals("00")){
            list = userDao.selectByDistrict(districtIds[0]+districtIds[1]);
        }else {
            list = userDao.selectByDistrict(id);
        }
        PageHelper.startPage(pageNum,pageSize,false);
        PageInfo<User> result = new PageInfo(list);
        return result;
    }

    /**
     *根据电话删除用户
     * @param phone
     * @return
     */
    @Override
    public int deleteByTelphone(String phone) {
        return userDao.deleteByTelphone(phone);
    }


    @Override
    public JsonObjectBO delete(String id) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user = userDao.findById(id);
        String telphone = user.getTelphone();
        RecordPolice police = recordPoliceMapper.selectByTelphone(telphone);
        if (police==null){
            int a = userDao.delete(id);
            return jsonObjectBO.success("删除成功",null);
        }else{
            int a = userDao.delete(id);
            int b =recordPoliceMapper.deleteByTelphone(telphone);
            if (a==1&&b==1){
                return jsonObjectBO.success("删除成功",null);
            }else{
                return jsonObjectBO.error("删除失败");
            }
        }

    }


}
