package com.dhht.service.user.impl;

import com.dhht.common.JsonObjectBO;
import com.dhht.dao.SMSCodeDao;
import com.dhht.dao.UserDao;
import com.dhht.model.SMSCode;
import com.dhht.model.UseDepartment;
import com.dhht.model.User;
import com.dhht.service.tools.SmsSendService;
import com.dhht.service.user.UserPasswordService;
import com.dhht.service.user.UserService;
import com.dhht.sms.SmsSingleSender;
import com.dhht.sms.SmsSingleSenderResult;
import com.dhht.util.MD5Util;
import com.dhht.util.ResultUtil;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smutil.SM3Util;

import java.util.ArrayList;
import java.util.Date;

@Service(value = "userPasswordService")
@Transactional
public class UserPasswordServiceImpl implements UserPasswordService{

    @Autowired
    private SMSCodeDao smsCodeDao;

    @Autowired
    private SmsSendService smsSendService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    //新用户
    @Value("${sms.template.insertUser}")
    private int userCode ;


    //新密码
    @Value("${sms.template.newPassword}")
    private int newPassword ;


    //验证码
    @Value("${sms.template.resetPasswordCode}")
    private int checkCode1 ;

    @Value("${sms.template.agentCheck}")
    private int agentCheck ;

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
     * 发送验证码的短信
     * 把验证码放入数据库 方便做比对
     * @param phone
     * @param code
     * @return
     */
    @Override
    public int sendMessage(String phone, String code,int smsmesscode) {
        try {
                ArrayList<String> params = new ArrayList<String>();
                params.add(code);

                SMSCode smscode= smsCodeDao.getSms(phone);
                if(smscode==null){
                    smscode = new SMSCode();
                    smscode.setId(UUIDUtil.generate());
                    smscode.setLastTime(System.currentTimeMillis());
                    smscode.setPhone(phone);
                    smscode.setSmscode(code);
                    smsCodeDao.save(smscode);
                }else{
                    smscode.setLastTime(System.currentTimeMillis());
                    smscode.setSmscode(code);
                    smsCodeDao.update(smscode);
                }
                boolean a =smsSendService.sendSingleMsgByTemplate(phone,smsmesscode,params);
                if(a){
                    return ResultUtil.isSuccess;
                }else{
                    return ResultUtil.isFail;
                }

        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.isFail;
        }
    }


    /**
     * 管理员重置密码
     *
     * @param id
     * @param
     * @return
     */
    @Override
    public boolean adminResetPwd(String id) {
        String code = createRandomVcode();
        User user = userDao.findById(id);
        user.setPassword(SM3Util.doSM3(code));
//        user.setPassword(code);
        String userName = user.getUserName();
        String phone = user.getTelphone();
        userDao.update(user);
        ArrayList<String> params = new ArrayList<String>();
        params.add(userName);
        params.add(code);
        if(smsSendService.sendSingleMsgByTemplate(user.getTelphone(),newPassword,params)){
            return true;
        }else{
            return false;
        }
    }


    /*
     * 经办人获取验证码
     * @return
     */
    @Override
    public int getCheckCode(String telphone) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String code = createRandomVcode();
        int a = sendMessage(telphone,code,checkCode1);
        if(a<0){
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("验证码获取失败");
            return ResultUtil.isFail;
        }else{
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("验证码获取成功");
            return ResultUtil.isSuccess;
        }
    }


    /**
     * 忘记密码后的
     * 重置密码
     */
    @Override
    public boolean resetPwd(String username,String checkCode,String password){
        User user = userDao.findByUserName(username);
        String phone = user.getTelphone();
        SMSCode code = smsCodeDao.getSMSCodeByPhone(phone);
        String smscode = code.getSmscode();
        if(smscode.equals(checkCode)){
            String pwd = SM3Util.doSM3(password);
            user.setPassword(pwd);
            int a = userDao.update(user);
            if (a!=1){
                return false;
            }else {
                ArrayList<String> params = new ArrayList<String>();
                params.add(username);
                params.add(password);
                smsSendService.sendSingleMsgByTemplate(user.getTelphone(),newPassword,params);
                return true;
            }

        }else {
            return false;
        }
    }



    /**
     * app上的重置密码
     */
    @Override
    public boolean appResetPwd(String id, String newPassWord) {
        User user = userDao.findById(id);
        String userName = user.getUserName();
        String pwd = SM3Util.doSM3(newPassWord);
        String phone = user.getTelphone();
        user.setPassword(pwd);
        int a = userDao.update(user);
        if (a!=1){
            return false;
        }else {
            ArrayList<String> params = new ArrayList<String>();
            params.add(userName);
            params.add(newPassWord);
            smsSendService.sendSingleMsgByTemplate(user.getTelphone(),newPassword,params);
            return true;
        }
    }

    /**
     *登入后修改修改密码
     * @param username
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @Override
    public int changePwd(String username,String oldPassword, String newPassword) {
        User user = userDao.findByUserName(username);
        if(SM3Util.verify(oldPassword,user.getPassword())){
            user.setPassword(SM3Util.doSM3(newPassword));
            user.setChangedPwd(true);
            int updatePwd = userDao.update(user);
            if(updatePwd<0){
                return ResultUtil.isFail;
            }else{
                return ResultUtil.isSuccess;
            }
        }else{
            return ResultUtil.isNoTrue;
        }

    }

    @Override
    public int getCheckAgentCode(String telphone) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String code = createRandomVcode();
        int a = sendMessage(telphone, code, agentCheck);
        if (a < 0) {
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("验证码获取失败");
            return ResultUtil.isFail;
        } else {
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("验证码获取成功");
            return ResultUtil.isSuccess;
        }
    }
}
