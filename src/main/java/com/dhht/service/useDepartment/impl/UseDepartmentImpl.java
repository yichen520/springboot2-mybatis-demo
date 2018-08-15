package com.dhht.service.useDepartment.impl;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Sync;
import com.dhht.common.JsonObjectBO;
import com.dhht.dao.OperatorRecordDetailMapper;
import com.dhht.dao.OperatorRecordMapper;
import com.dhht.dao.UseDepartmentDao;
import com.dhht.dao.UserDao;
import com.dhht.model.*;
import com.dhht.service.District.DistrictService;
import com.dhht.service.tools.ShowHistoryService;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.dhht.sync.SyncDataType;
import com.dhht.sync.SyncOperateType;
import com.dhht.util.CompareFieldsUtil;
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

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by imac_dhht on 2018/6/12.
 */
@Service(value = "useDepartmentService")
@Transactional
public class UseDepartmentImpl implements UseDepartmentService {

    @Autowired
    private OperatorRecordMapper operatorRecordMapper;
    @Autowired
    private OperatorRecordDetailMapper operatorRecordDetailMapper;
    @Autowired
    private UseDepartmentDao useDepartmentDao;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private ShowHistoryService showHistoryService;


    /**
     * 使用单位添加
     * @param useDepartment
     * @return
     */
    @Override
    public JsonObjectBO insert(UseDepartment useDepartment,HttpServletRequest httpServletRequest) {
        String code = useDepartment.getCode();
        if(useDepartmentDao.selectByCode(code)!=null){
            return JsonObjectBO.error("该单位已经存在");
        }
        User user1 = (User)httpServletRequest.getSession().getAttribute("user");
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
            OperatorRecord operatorRecord = new OperatorRecord();
            operatorRecord.setFlag(Id);
            operatorRecord.setId(UUIDUtil.generate());
            operatorRecord.setOperateUserId(user1.getId());
            operatorRecord.setOperateUserRealname(user1.getRealName());
            operatorRecord.setOperateEntityId(Id);
            operatorRecord.setOperateEntityName("UseDepartment");
            operatorRecord.setOperateType(SyncOperateType.SAVE);
            operatorRecord.setOperateTypeName(SyncOperateType.getOperateTypeName(SyncOperateType.SAVE));
            operatorRecord.setOperateTime(new Date(System.currentTimeMillis()));
            operatorRecordMapper.insert(operatorRecord);
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
    public JsonObjectBO update(UseDepartment useDepartment,HttpServletRequest httpServletRequest) {
            useDepartmentDao.deleteById(useDepartment.getId());
            UseDepartment oldUseDepartment = useDepartmentDao.selectById(useDepartment.getId());
            if (oldUseDepartment == null) {
                return JsonObjectBO.error("修改失败");
            } else {
                useDepartment.setVersion(useDepartment.getVersion() + 1);
                useDepartment.setIsDelete(false);
                useDepartment.setUpdateTime(DateUtil.getCurrentTime());
                String uuid = UUIDUtil.generate();
                useDepartment.setId(uuid);
                useDepartment.setFlag(oldUseDepartment.getFlag());
                useDepartment.setDepartmentStatus(oldUseDepartment.getDepartmentStatus());
                int r = useDepartmentDao.insert(useDepartment);
                //增加操作记录
                User user = (User)httpServletRequest.getSession().getAttribute("user");
                OperatorRecord operatorRecord = new OperatorRecord();
                String operatorRecordId = UUIDUtil.generate();
                operatorRecord.setFlag(oldUseDepartment.getFlag());
                operatorRecord.setId(operatorRecordId);
                operatorRecord.setOperateUserId(user.getId());
                operatorRecord.setOperateUserRealname(user.getRealName());
                operatorRecord.setOperateEntityId(uuid);
                operatorRecord.setOperateEntityName("UseDepartment");
                operatorRecord.setOperateType(SyncOperateType.UPDATE);
                operatorRecord.setOperateTypeName(SyncOperateType.getOperateTypeName(SyncOperateType.UPDATE));
                operatorRecord.setOperateTime(new Date(System.currentTimeMillis()));
                int o =  operatorRecordMapper.insert(operatorRecord);
                OperatorRecordDetail operatorRecordDetail = new OperatorRecordDetail();
                //和上一次作比较  然后比较不同
                UseDepartment newUseDepartment =useDepartmentDao.selectById(uuid);
                Map<String, List<Object>> compareResult = CompareFieldsUtil.compareFields(oldUseDepartment, newUseDepartment, new String[]{"id","departmentAddress","isDelete","version","operator","updateTime"});
                Set<String> keySet = compareResult.keySet();
                if(keySet.size()==0){
                    operatorRecordDetail.setId(UUIDUtil.generate());
                    operatorRecordDetail.setEntityOperateRecordId(operatorRecordId);
                    operatorRecordDetail.setPropertyName("nothing");
                    int od= operatorRecordDetailMapper.insert(operatorRecordDetail);
                    if(o<0){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return JsonObjectBO.error("修改失败");
                    }
                }
                for(String key : keySet){
                    List<Object> list = compareResult.get(key);
                    operatorRecordDetail.setId(UUIDUtil.generate());
                    operatorRecordDetail.setEntityOperateRecordId(operatorRecordId);
                    if(list.get(1)==null||list.get(1)==""){
                        operatorRecordDetail.setNewValue("");
                    }else {
                        operatorRecordDetail.setNewValue(list.get(1).toString());
                    }
                    if(list.get(0)==null||list.get(0)==""){
                        operatorRecordDetail.setOldValue("");
                    }else {
                        operatorRecordDetail.setOldValue(list.get(0).toString());
                    }
                    operatorRecordDetail.setPropertyName(key);
                    int od = operatorRecordDetailMapper.insert(operatorRecordDetail);
                    if(od<1){
                        return JsonObjectBO.error("修改失败");
                    }
                }
                if (r == 1&&o>0) {
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
        list = setDistrictName(list);
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
        List<OperatorRecord> list = showHistoryService.showUpdteHistory(flag,SyncDataType.USERDEPARTMENT);
//        for(OperatorRecord operatorRecord : list){
//            if(operatorRecord.getOperatorRecordDetails()!=null) {
//                for (OperatorRecordDetail operatorRecordDetail : operatorRecord.getOperatorRecordDetails()) {
//                    if (operatorRecordDetail.getPropertyName().equals("districtId")) {
//                        String oldDistrictName = districtService.selectByDistrictId(operatorRecordDetail.getOldValue());
//                        String newDistrictName = districtService.selectByDistrictId(operatorRecordDetail.getNewValue());
//                        operatorRecordDetail.setOldValue(oldDistrictName);
//                        operatorRecordDetail.setNewValue(newDistrictName);
//                    }
//                }
//            }
//        }
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
     * 设置区域名字
     * @param list
     * @return
     */
    public List<UseDepartment> setDistrictName(List<UseDepartment> list){
        for(UseDepartment useDepartment:list){
            if(useDepartment.getDistrictId()!=null){
                String districtName = districtService.selectByDistrictId(useDepartment.getDistrictId());
                useDepartment.setDistrictName(districtName);
            }
        }
        return list;
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
