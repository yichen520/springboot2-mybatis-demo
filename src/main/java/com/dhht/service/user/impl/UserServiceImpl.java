package com.dhht.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */
@Service(value = "userService")
@Transactional
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
     * 6位简单密码
     * @return
     */
    public static String createRandomVcode(){
        //密码
        String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int)(Math.random() * 9);
        }
        return vcode;
    }

    /**
     * 添加用户
     * @param users
     * @return
     */

    public JsonObjectBO addUser(Users users){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        if (users.getTelphone()==null){
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("请输入手机号");
            return jsonObjectBO;
        }
        Users users1 = userDao.findByTelphone(users.getTelphone());
        if (users1!=null){
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("该用户已经存在");
            return jsonObjectBO;
        }
        users.setId(UUIDUtil.generate());
        users.setUserName(users.getTelphone());
        String password = MD5Util.toMd5(createRandomVcode());
        users.setPassword(password);
        users.setRoleId("GLY");
        Integer a = userDao.addUser(users);
        if (a!=1){
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("添加失败");
            return jsonObjectBO;
        }else{
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("添加成功");
            return jsonObjectBO;
        }

    }
    /**
     * 修改用户
     * @param users
     * @return
     */
    @Override
    public JsonObjectBO Update(Users users) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        Users users2 = userDao.findById(users.getId());
        if (!users2.getTelphone().equals(users.getTelphone())) {
            Users users1 = userDao.findByTelphone(users.getTelphone());
            if (users1 != null) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("该用户已经存在");
                return jsonObjectBO;
            }
            users.setUserName(users.getTelphone());
            Integer a = userDao.update(users);
            if (a != 1) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("修改失败");
                return jsonObjectBO;
            } else {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("修改成功");
                return jsonObjectBO;
            }
        }else{
            users.setUserName(users.getTelphone());
            Integer a = userDao.update(users);
            if (a != 1) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("修改失败");
                return jsonObjectBO;
            } else {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("修改成功");
                return jsonObjectBO;
            }
        }


    }


    /**
     * 删除用户
     * @param id
     * @return
     */
    @Override
    public JsonObjectBO deleteuser(String id){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        Integer a = userDao.delete(id);
        if (a!=1){
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("删除失败");
            return jsonObjectBO;
        }else{
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("删除成功");
            return jsonObjectBO;
        }


    }


    /**
     * 查询全部用户
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public JsonObjectBO findAlluser(int pageNum, int pageSize) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(pageNum,pageSize);
        List<Users> suserList = userDao.findAllSuser();
        PageInfo<Users> result = new PageInfo<>(suserList);
        jsonObject.put("user",result);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        jsonObjectBO.setMessage("查询成功");
        return jsonObjectBO;
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


    /**
     * 查询列表 模糊查询
     * @param users
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public JsonObjectBO find(String realName,String roleId,String regionId,int pageNum, int pageSize) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(pageNum,pageSize);
        if(realName==null&&regionId==null&&roleId==null){
            List<Users> userList = userDao.findAllSuser();
            PageInfo<Users> result = new PageInfo<>(userList);
            jsonObject.put("user",result);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("查询成功");
        }else{
            List<Users> list = userDao.find(realName,regionId,roleId);
            PageInfo<Users> result = new PageInfo<>(list);
            jsonObject.put("user",result);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("查询成功");
        }
        return jsonObjectBO;
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
