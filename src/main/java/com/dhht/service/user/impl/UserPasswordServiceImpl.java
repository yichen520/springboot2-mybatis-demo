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
    private int checkCode ;

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
    public int sendMessage(String phone, String code) {
        try {
                ArrayList<String> params = new ArrayList<String>();

                params.add(code);
                SMSCode smscode= smsCodeDao.getSms(phone);
                if(smscode==null){
                    smscode = new SMSCode();
                    smscode.setId(UUIDUtil.generate());
                    smscode.setLastTime(new Date().getTime());
                    smscode.setPhone(phone);
                    smscode.setSmscode(code);
                    smsCodeDao.save(smscode);
                }else{
                    smscode.setLastTime(new Date().getTime());
                    smscode.setSmscode(code);
                    smsCodeDao.update(smscode);
                }
                smsSendService.sendSingleMsgByTemplate(phone,checkCode,params);
                return ResultUtil.isSuccess;

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
        user.setPassword(MD5Util.toMd5(code));
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

    /**
     * 密码重置中
     * 获取验证码
     * @param phone
     * @return
     */
    @Override
    public int getCheckCode(String phone) {
        String code = createRandomVcode();
        if(sendMessage(phone,code)==1){
            return ResultUtil.isSuccess;
        }else{
            return ResultUtil.isFail;
        }
    }


    /**
     * 忘记密码后的
     * 重置密码
     */
    @Override
    public boolean resetPwd(String phone,String checkCode,String passWord){
        SMSCode code = smsCodeDao.getSMSCodeByPhone(phone);
        String smscode = code.getSmscode();
        if(smscode==checkCode){
            User user = userDao.findByTelphone(phone);
            String pwd = MD5Util.toMd5(passWord);
            user.setPassword(pwd);
            int a = userDao.update(user);
            if (a!=1){
                return false;
            }else {
                ArrayList<String> params = new ArrayList<String>();
                params.add(phone);
                params.add(passWord);
                smsSendService.sendSingleMsgByTemplate(user.getTelphone(),newPassword,params);
                return true;
            }

        }else {
            return false;
        }
    }

}
