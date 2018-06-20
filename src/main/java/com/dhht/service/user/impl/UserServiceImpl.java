package com.dhht.service.user.impl;

import com.dhht.dao.*;
import com.dhht.model.Users;
import com.dhht.util.MD5Util;
import com.dhht.util.StringUtil;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.dhht.model.UserDomain;
import com.dhht.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private DistrictMapper districtDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private MakedepartmentMapper makedepartmentMapper;

    /**
     * 添加用户
     * @param users
     * @return
     */
    @Override
    public int addUser(Users users) {
        users.setId(UUIDUtil.generate());
        if (users.getPassword()==null){
            users.setPassword(MD5Util.toMd5("123456"));
        }

//        users.setMakedepartment(makedepartmentMapper.findByNo(users.getUserName()));
        users.setPassword(MD5Util.toMd5(users.getPassword()));
        users.setRole(roleDao.getByNo(users.getRoleId()));
        users.setDistrict(districtDao.getByNo(users.getRegionId()));
        int user = userDao.addUser(users);

        return user;
    }

    /**
     * 修改用户
     * @param users
     * @return
     */
    @Override
    public int Update(Users users) {
        users.setDistrict(districtDao.getByNo(users.getRegionId()));
        return userDao.update(users);
    }


    /**
     * 删除用户
     * @param id
     * @return
     */
    @Override
    public int deleteSuser(String id){

        Integer delete = userDao.delete(id);
        return delete;

    }


    /**
     * 查询全部用户
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<Users> findAllSuser(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Users> suserList = userDao.findAllSuser();
        PageInfo<Users> result = new PageInfo<>(suserList);
        return result;
    }


    /**
     * 修改密码
     * @param id
     * @param password
     * @return
     */
    @Override
    public int changePwd(String id, String password) {
        String password1 = MD5Util.toMd5(password);
        return userDao.changePwd(id,password1);
    }


    @Override
    public int validateUserLoginOne(UserDomain userDomain){
        return userDao.validateUserLoginOne(userDomain);
    }
    @Override
    public int validateUserLoginTwo(UserDomain userDomain){
        return userDao.validateUserLoginTwo(userDomain);
    }
    @Override
    public int validateUserLoginThree(UserDomain userDomain){
        return userDao.validateUserLoginThree(userDomain);

    }

    @Override
    public Users validate(Users users){
        String userAccount = StringUtil.stringNullHandle(users.getUserName());
        String password = StringUtil.stringNullHandle(MD5Util.toMd5(users.getPassword()));
        Users user = usersMapper.validate(new UserDomain(userAccount,password));
        return user;
    }

    @Override
    public PageInfo<Users> selectByDistrict(Integer id,int pageSum,int pageNum) {
        List<Users> list = new ArrayList<Users>();
        String districtIds[] = StringUtil.DistrictUtil(id);
        if(districtIds[1].equals("00")&&districtIds[2].equals("00")){
            list = userDao.selectByDistrict(districtIds[0]);
        }else if(!districtIds[1].equals("00")&&districtIds[2].equals("00")){
            list = userDao.selectByDistrict(districtIds[0]+districtIds[1]);
        }else {
            list = userDao.selectByDistrict(id.toString());
        }
        PageHelper.startPage(pageSum,pageNum);
        PageInfo<Users> result = new PageInfo(list);
        return result;
    }

}
