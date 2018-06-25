package com.dhht.service.user.impl;

import com.dhht.common.JsonObjectBO;
import com.dhht.dao.UserDao;
import com.dhht.model.User;
import com.dhht.service.user.UserLockingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "userLockingService")
@Transactional
public class UserLockingServiceImpl implements UserLockingService {


    @Autowired
    private UserDao userDao;

    /**
     * 主动加锁
     *
     * @param id
     * @return
     */
    @Override
    public JsonObjectBO activeLocking(String id) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user = userDao.findById(id);
        if (user.getIsLocked() == null || user.getIsLocked().equals("0") || !user.getIsLocked()) {
            Integer a = userDao.updateLock(id);
            if (a == 1) {
                return jsonObjectBO.ok("锁定成功");
            } else {
                return jsonObjectBO.error("锁定失败");
            }
        } else {
            return jsonObjectBO.error("该用户已经锁定,不能重复锁定");
        }
    }

    /**
     * 主动解锁
     *
     * @param
     * @return
     */
    @Override
    public JsonObjectBO activeUnlocking(String id) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user = userDao.findById(id);
        if (user.getIsLocked() == null || user.getIsLocked().equals("0") || !user.getIsLocked()) {
            return jsonObjectBO.error("该账号未加锁");
        } else {
            userDao.updateUnLock(id);
            return jsonObjectBO.ok("解锁成功");
        }
    }
}