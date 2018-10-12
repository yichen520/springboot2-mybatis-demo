package com.dhht.service.useDepartment.impl;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Sync;
import com.dhht.common.JsonObjectBO;
import com.dhht.dao.OperatorRecordDetailMapper;
import com.dhht.dao.OperatorRecordMapper;
import com.dhht.dao.UseDepartmentDao;
import com.dhht.model.*;
import com.dhht.service.District.DistrictService;
import com.dhht.service.tools.FileService;
import com.dhht.service.tools.HistoryService;
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
    private UseDepartmentDao useDepartmentDao;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private FileService fileService;

    private final String IDCARD_FRONT_FILE = "使用单位法人身份证正面照片";
    private final String IDCARD_REVERSE_FILE = "使用单位法人身份证反面照片";
    private final String BUSINESS_LICENSE_FILE = "使用单位营业执照扫面件";


    /**
     * 使用单位添加
     * @param useDepartment
     * @return
     */
    @Override
    public JsonObjectBO insert(UseDepartment useDepartment,User updateUser) {
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
        useDepartment = (UseDepartment)StringUtil.deleteSpace(useDepartment);
        int useDepartmentResult = useDepartmentDao.insert(useDepartment);
        boolean f = registerFile(useDepartment);
        boolean operateResult = historyService.insertOperateRecord(updateUser,useDepartment.getFlag(),useDepartment.getId(),"userDepartment",SyncOperateType.SAVE,UUIDUtil.generate());
        if(useDepartmentResult<0&&!operateResult&&!f){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
    public JsonObjectBO update(UseDepartment useDepartment,User updateUser) {
            useDepartmentDao.deleteById(useDepartment.getId());
            UseDepartment oldUseDepartment = useDepartmentDao.selectById(useDepartment.getId());
            useDepartment = (UseDepartment)StringUtil.deleteSpace(useDepartment);
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
                String operateUUid = UUIDUtil.generate();
                String[] ignore = new String[]{"id","departmentAddress","isDelete","version","operator","updateTime","specialBusinessLicenceScanning"};
                int r = useDepartmentDao.insert(useDepartment);
                boolean f = registerFile(useDepartment);
                boolean operateResult = historyService.insertOperateRecord(updateUser,useDepartment.getFlag(),useDepartment.getId(),"userDepartment",SyncOperateType.UPDATE,operateUUid);
                boolean operateDetailResult = historyService.insertUpdateRecord(useDepartment,oldUseDepartment,operateUUid,ignore);
                if (r == 1&&operateDetailResult&&operateResult&&f) {
                    SyncEntity syncEntity =  ((UseDepartmentImpl) AopContext.currentProxy()).getSyncDate(useDepartment, SyncDataType.USERDEPARTMENT, SyncOperateType.UPDATE);
                    return JsonObjectBO.success("修改成功", null);
                } else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
        int pagetotal = pageNum * pageSize;
        if (code == null && districtId == null && name == null) {
            PageInfo<UseDepartment> result =selectByDistrict(localDistrictId,departmentStatus,pageNum,pageSize);
            jsonObject.put("useDepartment", result);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("查询成功");
            return jsonObjectBO;
        } else if(districtId != null){
            String districtIds = StringUtil.getDistrictId(districtId);
            list = useDepartmentDao.find(code,districtIds,name,departmentStatus,pagetotal,pageSize);
        }
        else {
             list = useDepartmentDao.find(code,districtId,name,departmentStatus,pagetotal,pageSize);
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
        String districtIds[] = StringUtil.DistrictUtil(id);

        int pagestart = pageNum * pageSize;
        if (districtIds[1].equals("00") && districtIds[2].equals("00")) {

            list = useDepartmentDao.find(null,districtIds[0],null,departmentStatus,pagestart,pageSize);
        } else if (!districtIds[1].equals("00") && districtIds[2].equals("00")) {
            list = useDepartmentDao.find(null,districtIds[0] + districtIds[1],null,departmentStatus,pagestart,pageSize);
        } else {
            list = useDepartmentDao.find(null,id,null,departmentStatus,pagestart,pageSize);
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
    public JsonObjectBO delete(UseDepartment useDepartment,User updateUser) {
        String id = useDepartment.getId();
        int d = useDepartmentDao.deleteById(id);
        useDepartment = useDepartmentDao.selectById(id);
        useDepartment.setId(UUIDUtil.generate());
        useDepartment.setDepartmentStatus("02");
        useDepartment.setEndDate(DateUtil.getCurrentTime());
        useDepartment.setVersion(useDepartment.getVersion()+1);
        int a = useDepartmentDao.delete(useDepartment);
        boolean operateResult = historyService.insertOperateRecord(updateUser,useDepartment.getFlag(),useDepartment.getId(),"userDepartment",SyncOperateType.DELETE,UUIDUtil.generate());
        if(a>0&&operateResult&&d>0) {
            SyncEntity syncEntity =  ((UseDepartmentImpl) AopContext.currentProxy()).getSyncDate(useDepartment, SyncDataType.USERDEPARTMENT, SyncOperateType.DELETE);
            return JsonObjectBO.success("删除成功", null);
        }else{
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
        List<OperatorRecord> list = historyService.showUpdteHistory(flag,SyncDataType.USERDEPARTMENT);
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
        return setDistrictName(useDepartment);
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
     * 文件注册
     * @param useDepartment
     */
    public boolean registerFile(UseDepartment useDepartment){
        boolean f1 =fileService.register(useDepartment.getIdCardFrontId(),IDCARD_FRONT_FILE);
        boolean f2 =fileService.register(useDepartment.getIdCardReverseId(),IDCARD_REVERSE_FILE);
        boolean f3 = fileService.register(useDepartment.getBusinessLicenseUrl(),BUSINESS_LICENSE_FILE);
        if(f1&&f2&&f3){
            return true;
        }
        return false;
    }
    @Override
    public UseDepartment selectByCode(String useDepartmentCode){
        UseDepartment useDepartment = useDepartmentDao.selectByCode(useDepartmentCode);
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
