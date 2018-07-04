package com.dhht.service.user.impl;

import com.dhht.common.JsonObjectBO;
import com.dhht.dao.UserDao;
import com.dhht.model.User;
import com.dhht.service.user.UserLockingService;
import com.dhht.util.ResultUtil;
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
    public int activeLocking(String id) {
        User user = userDao.findById(id);
        if (user.getIsLocked() == null || user.getIsLocked().equals("0") || !user.getIsLocked()) {
            Integer a = userDao.updateLock(id);
            if (a == 1) {
                return ResultUtil.isSuccess;
            } else {
                return ResultUtil.isFail;
            }
        } else {
            return ResultUtil.islock;
        }
    }

    /**
     * 主动解锁
     *
     * @param
     * @return
     */
    @Override
    public int activeUnlocking(String id) {
        User user = userDao.findById(id);
        if (user.getIsLocked() == null || user.getIsLocked().equals("0") || !user.getIsLocked()) {
            return ResultUtil.isUnlock;
        } else {
            userDao.updateUnLock(id);
            return ResultUtil.isSuccess;
        }
    }
}