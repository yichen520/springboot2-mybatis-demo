package com.dhht.service.useDepartment.impl;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.dao.UseDepartmentDao;
import com.dhht.dao.UserDao;
import com.dhht.model.UseDepartment;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.dhht.util.StringUtil;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by imac_dhht on 2018/6/12.
 */
@Service(value = "useDepartmentService")
@Transactional
public class UseDepartmentImpl implements UseDepartmentService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UseDepartmentDao useDepartmentDao;


    /**
     * 使用单位添加
     * @param useDepartment
     * @return
     */
    @Override
    public JsonObjectBO insert(UseDepartment useDepartment) {
        String code = useDepartment.getCode();
        if(useDepartmentDao.selectByCode(code)!=null){
            return JsonObjectBO.error("该单位已经存在");
        }
        String Id = UUIDUtil.generate();
        useDepartment.setId(Id);
        useDepartment.setDepartmentStatus("0");//状态0为正常 1 为不正常  默认正常
        useDepartment.setIsDelete(false);
        useDepartment.setFlag(Id);
        useDepartment.setVersion(1);
        useDepartmentDao.insert(useDepartment);
        return JsonObjectBO.success("添加成功",null);


    }

    /**
     * 修改使用单位
     * @param useDepartment
     * @return
     */
    @Override
    public JsonObjectBO update(UseDepartment useDepartment) {
        String flag = useDepartment.getFlag();
        List<UseDepartment> list = useDepartmentDao.selectByFlag(flag);
        for(UseDepartment l:list){
            UseDepartment useDepartment1 = new UseDepartment();
            useDepartment1.setId(l.getId());
            useDepartment1.setIsDelete(true);
            useDepartmentDao.updateByPrimaryKey(useDepartment1);
        }
        UseDepartment useDepartment2 = useDepartmentDao.selectByCode(useDepartment.getCode());
        if(useDepartment2==null){
            useDepartment.setVersion(list.get(0).getVersion()+1);
            useDepartment.setFlag(list.get(0).getFlag());
            useDepartment.setIsDelete(false);
            useDepartmentDao.insert(useDepartment);
            return JsonObjectBO.success("修改成功",null);
        }else{
            return JsonObjectBO.error("该账号已经存在");
        }

    }

    /**
     * 查询全部
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public JsonObjectBO find(String code,String name,String districtId,String departmentStatus,int pageNum, int pageSize) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(pageNum, pageSize);
        if (code == null && districtId == null && name == null && departmentStatus==null) {
            List<UseDepartment> list = useDepartmentDao.findAllMake();
            PageInfo<UseDepartment> result = new PageInfo<>(list);
            jsonObject.put("useDepartment", result);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("查询成功");
        } else if(districtId != null){
            String districtIds[] = StringUtil.DistrictUtil(districtId);
            if(districtIds[1].equals("00")&&districtIds[2].equals("00")){
                List<UseDepartment> list = useDepartmentDao.find(code,districtIds[0],name,departmentStatus);
                PageInfo<UseDepartment> result = new PageInfo<>(list);
                jsonObject.put("useDepartment", result);
                jsonObjectBO.setData(jsonObject);
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("查询成功");
            }else if(!districtIds[1].equals("00")&&districtIds[2].equals("00")){
                List<UseDepartment> list = useDepartmentDao.find(code,districtIds[0],name,departmentStatus);
                PageInfo<UseDepartment> result = new PageInfo<>(list);
                jsonObject.put("useDepartment", result);
                jsonObjectBO.setData(jsonObject);
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("查询成功");
            }else if (!districtIds[1].equals("00")&&!districtIds[2].equals("00")){
                List<UseDepartment> list = useDepartmentDao.find(code,districtIds[0],name,departmentStatus);
                PageInfo<UseDepartment> result = new PageInfo<>(list);
                jsonObject.put("useDepartment", result);
                jsonObjectBO.setData(jsonObject);
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("查询成功");
            }
        }else {
            List<UseDepartment> list = useDepartmentDao.find(code,districtId,name,departmentStatus);
            PageInfo<UseDepartment> result = new PageInfo<>(list);
            jsonObject.put("user", result);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("查询成功");
        }
        return jsonObjectBO;
    }

    /**
     * 删除
     * @param useDepartment
     * @return
     */
    @Override
    public JsonObjectBO delete(UseDepartment useDepartment) {
        String flag = useDepartment.getFlag();
        int a = useDepartmentDao.delete(flag);
        if(a>0) {
            return JsonObjectBO.success("删除成功", null);
        }else{
            return JsonObjectBO.error("删除失败");
        }
    }

    @Override
    public JsonObjectBO item(UseDepartment useDepartment) {
        return null;
    }


}
