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
import com.dhht.service.user.WeChatUserService;
import com.dhht.sync.SyncDataType;
import com.dhht.sync.SyncOperateType;
import com.dhht.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @Autowired
    private WeChatUserService weChatUserService;
    @Value("${sms.template.business}")
    private int business;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final String IDCARD_FRONT_FILE = "使用单位法人身份证正面照片";
    private final String IDCARD_REVERSE_FILE = "使用单位法人身份证反面照片";
    private final String BUSINESS_LICENSE_FILE = "使用单位营业执照扫面件";


    /**
     * 使用单位添加
     *
     * @param useDepartment
     * @return
     */
    @Override
    public JsonObjectBO insert(UseDepartment useDepartment, User updateUser) {
        String code = useDepartment.getCode();
        if (useDepartmentDao.selectByCode(code) != null) {
            return JsonObjectBO.error("该单位已经存在");
        }
        String Id = UUIDUtil.generate();
        useDepartment.setId(Id);
        useDepartment.setDepartmentStatus("01");//状态1为正常 00 为全部  02为注销
        useDepartment.setIsDelete(false);
        useDepartment.setFlag(Id);
        useDepartment.setVersion(0);
        useDepartment.setUpdateTime(new Date(System.currentTimeMillis()));
        useDepartment = (UseDepartment) StringUtil.deleteSpace(useDepartment);
        int useDepartmentResult = useDepartmentDao.insert(useDepartment);
        // boolean f = registerFile(useDepartment);
        boolean operateResult = historyService.insertOperateRecord(updateUser, useDepartment.getFlag(), useDepartment.getId(), "userDepartment", SyncOperateType.SAVE, UUIDUtil.generate());
        if (useDepartmentResult < 0 && !operateResult) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonObjectBO.error("添加失败");
        } else {
            if (useDepartment.getManagerPhone() != null && useDepartment.getManagerPhone() != "") {
                System.out.println(useDepartment.getManagerPhone());
                weChatUserService.sendMessage(useDepartment.getManagerPhone(), business);
            }
            return JsonObjectBO.success("添加成功", null);
        }
    }

    /**
     * 修改使用单位
     *
     * @param useDepartment
     * @return
     */
    @Override
    public JsonObjectBO update(UseDepartment useDepartment, User updateUser) {
        useDepartmentDao.deleteById(useDepartment.getId());
        UseDepartment oldUseDepartment = useDepartmentDao.selectById(useDepartment.getId());
        useDepartment = (UseDepartment) StringUtil.deleteSpace(useDepartment);
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
            String[] ignore = new String[]{"id", "departmentAddress", "isDelete", "version", "operator", "updateTime", "specialBusinessLicenceScanning", "managerPhone", "managerName"};
            int r = useDepartmentDao.insert(useDepartment);
            //先不做文件注册，推送
            // boolean f = registerFile(useDepartment);
            boolean operateResult = historyService.insertOperateRecord(updateUser, useDepartment.getFlag(), useDepartment.getId(), "userDepartment", SyncOperateType.UPDATE, operateUUid);
            boolean operateDetailResult = historyService.insertUpdateRecord(useDepartment, oldUseDepartment, operateUUid, ignore);
            if (r == 1 && operateDetailResult && operateResult) {
                SyncEntity syncEntity = ((UseDepartmentImpl) AopContext.currentProxy()).getSyncDate(useDepartment, SyncDataType.USERDEPARTMENT, SyncOperateType.UPDATE);
                return JsonObjectBO.success("修改成功", null);
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return JsonObjectBO.error("修改失败");
            }
        }
    }

    @Override
    public int updateFromWeChatAPP(UseDepartment useDepartment, WeChatUser updateUser) {
        User user = new User();
        user.setRealName(updateUser.getName());
        user.setId(updateUser.getId());
        updateUser.setName(useDepartment.getLegalName());
        updateUser.setCompanyName(useDepartment.getName());
        updateUser.setTelphone(useDepartment.getLegalTelphone());
        useDepartmentDao.deleteById(useDepartment.getId());
        UseDepartment oldUseDepartment = useDepartmentDao.selectById(useDepartment.getId());
        UseDepartment newUseDepartment = (UseDepartment) oldUseDepartment.clone();
        useDepartment = (UseDepartment) StringUtil.deleteSpace(useDepartment);
        if (oldUseDepartment == null) {
            return ResultUtil.isError;
        } else {
            newUseDepartment.setVersion(useDepartment.getVersion() + 1);
            newUseDepartment.setIsDelete(false);
            newUseDepartment.setUpdateTime(DateUtil.getCurrentTime());
            newUseDepartment.setName(useDepartment.getName());
            newUseDepartment.setLegalName(useDepartment.getLegalName());
            newUseDepartment.setTelphone(useDepartment.getTelphone());
            newUseDepartment.setAddress(useDepartment.getAddress());
            newUseDepartment.setPostalCode(useDepartment.getPostalCode());
            String uuid = UUIDUtil.generate();
            newUseDepartment.setId(uuid);
            newUseDepartment.setFlag(oldUseDepartment.getFlag());
            newUseDepartment.setDepartmentStatus(oldUseDepartment.getDepartmentStatus());
            String operateUUid = UUIDUtil.generate();
            String[] ignore = new String[]{"id", "departmentAddress", "isDelete", "version", "operator", "updateTime", "specialBusinessLicenceScanning", "managerPhone", "managerName"};
            int r = useDepartmentDao.insert(newUseDepartment);
            int u = weChatUserService.updateWeChatUserInfo(updateUser);
            boolean operateResult = historyService.insertOperateRecord(user, newUseDepartment.getFlag(), newUseDepartment.getId(), "userDepartment", SyncOperateType.UPDATE, operateUUid);
            boolean operateDetailResult = historyService.insertUpdateRecord(newUseDepartment, oldUseDepartment, operateUUid, ignore);
            if (u > 0 && r == 1 && operateDetailResult && operateResult) {
                SyncEntity syncEntity = ((UseDepartmentImpl) AopContext.currentProxy()).getSyncDate(useDepartment, SyncDataType.USERDEPARTMENT, SyncOperateType.UPDATE);
                return ResultUtil.isSuccess;
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isFail;
            }
        }
    }


    /**
     * 查询全部
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public JsonObjectBO find(String localDistrictId, String code, String name, String districtId, String departmentStatus, int pageNum, int pageSize) {
        List<UseDepartment> list = new ArrayList<>();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        // PageHelper.startPage(pageNum, pageSize);
        int pagetotal = (pageNum - 1) * pageSize;
        if (code == null && districtId == null && name == null) {
            PageInfo<UseDepartment> result = selectByDistrict(localDistrictId, departmentStatus, pagetotal, pageSize);
            int total = useDepartmentDao.findcount(code, districtId, name, departmentStatus);
            result.setPageNum(pageNum);
            result.setPageSize(pageSize);
            result.setTotal(total);
            jsonObject.put("useDepartment", result);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("查询成功");
            return jsonObjectBO;
        } else if (districtId != null) {
            String districtIds = StringUtil.getDistrictId(districtId);
            list = useDepartmentDao.find(code, districtIds, name, departmentStatus, pagetotal, pageSize);
        } else {
            list = useDepartmentDao.find(code, districtId, name, departmentStatus, pagetotal, pageSize);
        }
        list = setDistrictName(list);
        PageInfo<UseDepartment> result = new PageInfo<>(list);
        int total = useDepartmentDao.findcount(code, districtId, name, departmentStatus);
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        jsonObject.put("useDepartment", result);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        jsonObjectBO.setMessage("查询成功");
        return jsonObjectBO;
    }

    /**
     * 根据区域查找用户
     *
     * @param id
     * @param pageSize
     * @param pageNum
     * @return
     */
    public PageInfo<UseDepartment> selectByDistrict(String id, String departmentStatus, int pageNum, int pageSize) {
        List<UseDepartment> list = new ArrayList<UseDepartment>();
        String districtIds[] = StringUtil.DistrictUtil(id);

        int pagestart = pageNum * pageSize;
        if (districtIds[1].equals("00") && districtIds[2].equals("00")) {

            list = useDepartmentDao.find(null, districtIds[0], null, departmentStatus, pagestart, pageSize);
        } else if (!districtIds[1].equals("00") && districtIds[2].equals("00")) {
            list = useDepartmentDao.find(null, districtIds[0] + districtIds[1], null, departmentStatus, pagestart, pageSize);
        } else {
            list = useDepartmentDao.find(null, id, null, departmentStatus, pagestart, pageSize);
        }
        for (UseDepartment useDepartment : list) {
            useDepartment.setDistrictName(districtService.selectByDistrictId(useDepartment.getDistrictId()));
        }
        PageInfo<UseDepartment> result = new PageInfo(list);
        return result;
    }

    /**
     * 删除
     *
     * @param useDepartment
     * @return
     */
    @Override
    public JsonObjectBO delete(UseDepartment useDepartment, User updateUser) {
        String id = useDepartment.getId();
        int d = useDepartmentDao.deleteById(id);
        useDepartment = useDepartmentDao.selectById(id);
        useDepartment.setId(UUIDUtil.generate());
        useDepartment.setDepartmentStatus("02");
        useDepartment.setEndDate(DateUtil.getCurrentTime());
        useDepartment.setVersion(useDepartment.getVersion() + 1);
        int a = useDepartmentDao.delete(useDepartment);
        boolean operateResult = historyService.insertOperateRecord(updateUser, useDepartment.getFlag(), useDepartment.getId(), "userDepartment", SyncOperateType.DELETE, UUIDUtil.generate());
        if (a > 0 && operateResult && d > 0) {
            SyncEntity syncEntity = ((UseDepartmentImpl) AopContext.currentProxy()).getSyncDate(useDepartment, SyncDataType.USERDEPARTMENT, SyncOperateType.DELETE);
            return JsonObjectBO.success("删除成功", null);
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonObjectBO.error("删除失败");
        }
    }

    /**
     * 查看历史
     *
     * @param flag
     * @return
     */
    @Override
    public JsonObjectBO showHistory(String flag) {
        List<OperatorRecord> list = historyService.showUpdteHistory(flag, SyncDataType.USERDEPARTMENT);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("useDepartment", list);
        return JsonObjectBO.success("查询成功", jsonObject);
    }

    /**
     * 查看详情
     *
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
     *
     * @param useDepartmentName
     * @return
     */
    @Override
    public List<UseDepartment> selectUseDepartment(String useDepartmentName) {
        List<UseDepartment> useDepartments = useDepartmentDao.selectByName(useDepartmentName);
        for (UseDepartment useDepartment : useDepartments) {
            try {
                useDepartment.setFoundDateFormat(simpleDateFormat.format(useDepartment.getFoundDate()));
            } catch (Exception e) {
                continue;
            }
        }
        return setDistrictName(useDepartments);
    }

    /**
     * 设置区域名字
     *
     * @param list
     * @return
     */
    public List<UseDepartment> setDistrictName(List<UseDepartment> list) {
        for (UseDepartment useDepartment : list) {
            if (useDepartment.getDistrictId() != null) {
                String districtName = districtService.selectByDistrictId(useDepartment.getDistrictId());
                useDepartment.setDistrictName(districtName);
            }
        }
        return list;
    }

    public UseDepartment setDistrictName(UseDepartment useDepartment) {
        String districtName = districtService.selectByDistrictId(useDepartment.getDistrictId());
        useDepartment.setDistrictName(districtName);
        return useDepartment;
    }

    /**
     * 文件注册
     *
     * @param useDepartment
     */
    public boolean registerFile(UseDepartment useDepartment) {
        boolean f1 = fileService.register(useDepartment.getIdCardFrontId(), IDCARD_FRONT_FILE);
        boolean f2 = fileService.register(useDepartment.getIdCardReverseId(), IDCARD_REVERSE_FILE);
        boolean f3 = fileService.register(useDepartment.getBusinessLicenseUrl(), BUSINESS_LICENSE_FILE);
        if (f1 && f2 && f3) {
            return true;
        }
        return false;
    }

    @Override
    public UseDepartment selectByCode(String useDepartmentCode) {
        UseDepartment useDepartment = useDepartmentDao.selectByCode(useDepartmentCode);
        return setDistrictName(useDepartment);
    }

    @Override
    public UseDepartment selectActiveUseDepartmentByCode(String useDepartmentCode) {
        UseDepartment useDepartment = useDepartmentDao.selectActiveUseDepartmentByCode(useDepartmentCode);
        return setDistrictName(useDepartment);
    }

    /**
     * 根据name和code查找单位
     *
     * @param code
     * @return
     */
    @Override
    public List<UseDepartment> selectByNameAndCode(String code) {
        List<UseDepartment> list = useDepartmentDao.selectByNameAndCode(code);
        return list;
    }

    /**
     * 用户绑定使用单位
     *
     * @param id
     * @param weChatUser
     * @return
     */
    @Override
    public int binding(String id, WeChatUser weChatUser) {
        UseDepartment useDepartment = useDepartmentDao.selectById(id);
        weChatUser.setCompanyName(useDepartment.getName());
        weChatUser.setCompany(useDepartment.getFlag());
        return weChatUserService.updateWeChatUser(weChatUser, weChatUser.getId());

    }

    /**
     * 用户解绑单位
     *
     * @param weChatUser
     * @return
     */
    @Override
    public int relieveBinding(WeChatUser weChatUser) {
        weChatUser.setCompanyName(null);
        weChatUser.setCompany(null);
        return weChatUserService.updateWeChatUser(weChatUser, weChatUser.getId());
    }

    @Override
    public UseDepartment selectByFlag(String flag) {
        UseDepartment useDepartment = useDepartmentDao.selectByFlag(flag);
        useDepartment.setDistrictName(districtService.selectByDistrictId(useDepartment.getDistrictId()));
        return useDepartment;
    }

    /**
     * 数据同步
     *
     * @param object
     * @param dataType
     * @param operateType
     * @return
     */
    @Sync()
    public SyncEntity getSyncDate(Object object, int dataType, int operateType) {
        SyncEntity syncEntity = new SyncEntity();
        syncEntity.setObject(object);
        syncEntity.setDataType(dataType);
        syncEntity.setOperateType(operateType);
        return syncEntity;
    }

}
