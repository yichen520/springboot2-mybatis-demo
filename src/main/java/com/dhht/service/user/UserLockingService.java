package com.dhht.service.user;

import com.dhht.common.JsonObjectBO;

/**
 * Created by 崔杨 on 2018/6/25.
 * 主动上锁和主动解锁
 */
public interface UserLockingService {
    int activeLocking(String id);

    int activeUnlocking(String id);
}
