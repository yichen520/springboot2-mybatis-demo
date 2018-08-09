package com.dhht.service.useDepartment.impl;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Sync;
import com.dhht.common.JsonObjectBO;
import com.dhht.dao.UseDepartmentDao;
import com.dhht.dao.UserDao;
import com.dhht.model.Makedepartment;
import com.dhht.model.SyncEntity;
import com.dhht.model.UseDepartment;
import com.dhht.model.User;
import com.dhht.service.District.DistrictService;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.dhht.sync.SyncDataType;
import com.dhht.sync.SyncOperateType;
import com.dhht.util.DateUtil;
import com.dhht.util.StringUtil;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private DistrictService districtService;


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
        useDepartment.setDepartmentStatus("01");//状态1为正常 00 为全部  02为注销
        useDepartment.setIsDelete(false);
        useDepartment.setFlag(Id);
        useDepartment.setVersion(0);
        useDepartment.setUpdateTime(new Date(System.currentTimeMillis()));
        int useDepartmentResult = useDepartmentDao.insert(useDepartment);
        if(useDepartmentResult<0){
            return JsonObjectBO.error("添加失败");
        }else{
            SyncEntity syncEntity =  ((UseDepartmentImpl) AopContext.currentProxy()).getSyncDate(useDepartment, SyncDataType.USERDEPARTMENT, SyncOperateType.SAVE);
            return JsonObjectBO.success("添加成功",null);
        }




    }

    /**
     * 修改使用单位
     * @param useDepartment
     * @return
     */
    @Override
    public JsonObjectBO update(UseDepartment useDepartment) {
            useDepartmentDao.deleteById(useDepartment.getId());
            UseDepartment useDepartment1 = useDepartmentDao.selectById(useDepartment.getId());
            if (useDepartment1 == null) {
                return JsonObjectBO.error("修改失败");
            } else {
                useDepartment.setVersion(useDepartment.getVersion() + 1);
                useDepartment.setIsDelete(false);
                useDepartment.setUpdateTime(DateUtil.getCurrentTime());
                useDepartment.setId(UUIDUtil.generate());
                useDepartment.setFlag(useDepartment1.getFlag());
                useDepartment.setDepartmentStatus(useDepartment1.getDepartmentStatus());
                int r = useDepartmentDao.insert(useDepartment);
                if (r == 1) {
                    SyncEntity syncEntity =  ((UseDepartmentImpl) AopContext.currentProxy()).getSyncDate(useDepartment, SyncDataType.USERDEPARTMENT, SyncOperateType.UPDATE);
                    return JsonObjectBO.success("修改成功", null);
                } else {
                    return JsonObjectBO.error("修改失败");
                }
            }

    }

    /**
     * 查询全部
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public JsonObjectBO find(String localDistrictId,String code,String name,String districtId,String departmentStatus,int pageNum, int pageSize) {
        List<UseDepartment> list = new ArrayList<>();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(pageNum, pageSize);
        if (code == null && districtId == null && name == null) {
            PageInfo<UseDepartment> result =selectByDistrict(localDistrictId,departmentStatus,pageNum,pageSize);
            jsonObject.put("useDepartment", result);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("查询成功");
            return jsonObjectBO;
        } else if(districtId != null){
            String districtIds = StringUtil.getDistrictId(districtId);
            list = useDepartmentDao.find(code,districtIds,name,departmentStatus);
        }
        else {
             list = useDepartmentDao.find(code,districtId,name,departmentStatus);
        }
        for (UseDepartment useDepartment:list) {
            useDepartment.setDistrictName(districtService.selectByDistrictId(useDepartment.getDistrictId()));
        }
        PageInfo<UseDepartment> result = new PageInfo<>(list);
        jsonObject.put("useDepartment", result);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        jsonObjectBO.setMessage("查询成功");
        return jsonObjectBO;
    }


    /**
     *根据区域查找用户
     * @param id
     * @param pageSize
     * @param pageNum
     * @return
     */
    public PageInfo<UseDepartment> selectByDistrict(String id,String departmentStatus,int pageNum, int pageSize) {
        List<UseDepartment> list = new ArrayList<UseDepartment>();
        PageHelper.startPage(pageNum, pageSize, false);
        String districtIds[] = StringUtil.DistrictUtil(id);
        if (districtIds[1].equals("00") && districtIds[2].equals("00")) {
            list = useDepartmentDao.find(null,districtIds[0],null,departmentStatus);
        } else if (!districtIds[1].equals("00") && districtIds[2].equals("00")) {
            list = useDepartmentDao.find(null,districtIds[0] + districtIds[1],null,departmentStatus);
        } else {
            list = useDepartmentDao.find(null,id,null,departmentStatus);
        }
        for (UseDepartment useDepartment:list) {
            useDepartment.setDistrictName(districtService.selectByDistrictId(useDepartment.getDistrictId()));
        }
        PageInfo<UseDepartment> result = new PageInfo(list);
        return result;
    }

    /**
     * 删除
     * @param useDepartment
     * @return
     */
    @Override
    public JsonObjectBO delete(UseDepartment useDepartment) {
        String id = useDepartment.getId();
        int a = useDepartmentDao.delete(id);
        if(a>0) {
            SyncEntity syncEntity =  ((UseDepartmentImpl) AopContext.currentProxy()).getSyncDate(useDepartment, SyncDataType.USERDEPARTMENT, SyncOperateType.DELETE);
            return JsonObjectBO.success("删除成功", null);
        }else{
            return JsonObjectBO.error("删除失败");
        }
    }

    /**
     * 查看历史
     * @param flag
     * @return
     */
    @Override
    public JsonObjectBO showHistory(String flag) {
        List<UseDepartment> list = useDepartmentDao.selectByFlag(flag);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("useDepartment",list);
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    /**
     * 查看详情
     * @param id
     * @return
     */
    @Override
    public UseDepartment selectDetailById(String id) {
        UseDepartment useDepartment = useDepartmentDao.selectDetailById(id);
        useDepartment.setDistrictName(districtService.selectByDistrictId(useDepartment.getDistrictId()));
        return useDepartment;
    }

    /**
     * 根据名字查询使用单位
     * @param useDepartmentName
     * @return
     */
    @Override
    public List<UseDepartment> selectUseDepartment(String useDepartmentName) {
        List<UseDepartment> useDepartment = useDepartmentDao.selectByName(useDepartmentName);
        return useDepartment;
    }
    

    /**
     * 数据同步
     * @param object
     * @param dataType
     * @param operateType
     * @return
     */
    @Sync()
    public SyncEntity getSyncDate(Object object, int dataType, int operateType){
        SyncEntity syncEntity = new SyncEntity();
        syncEntity.setObject(object);
        syncEntity.setDataType(dataType);
        syncEntity.setOperateType(operateType);
        return syncEntity;
    }

}
