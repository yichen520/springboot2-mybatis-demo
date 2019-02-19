package com.dhht.service.make.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Sync;
import com.dhht.dao.*;
import com.dhht.model.*;
import com.dhht.model.pojo.CommonHistoryVO;
import com.dhht.model.pojo.MakedepartmentSimplePO;
import com.dhht.model.pojo.SealDTO;
import com.dhht.model.pojo.SealVO;
import com.dhht.service.employee.EmployeeService;

import com.dhht.service.make.MakeDepartmentAttacthInfoService;
import com.dhht.service.tools.FileService;
import com.dhht.service.tools.HistoryService;
import com.dhht.service.user.UserService;
import com.dhht.sync.SyncDataType;
import com.dhht.sync.SyncOperateType;
import com.dhht.util.*;
import com.dhht.service.make.MakeDepartmentService;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 2018/7/2 create by fyc
 */
@Service(value = "makeDepartmentService")
@Transactional
public class MakeDepartmentServiceImpl implements MakeDepartmentService {

    @Autowired
    private MakedepartmentMapper makedepartmentMapper;

    @Autowired
    private ExamineRecordDetailMapper examineRecordDetailMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private FileService fileService;

    @Autowired
    private SealDao sealDao;

    @Autowired
    private SealAgentMapper sealAgentMapper;

    @Autowired
    private DistrictMapper districtMapper;

    @Autowired
    private MakeDepartmentAttacthInfoService makeDepartmentAttacthInfoService;

    private final String IDCARD_FRONT_FILE = "制作单位法人身份证正面照片";
    private final String IDCARD_REVERSE_FILE = "制作单位法人身份证反面照片";
    private final String SPECIAL_LICENSE_FILE = "制作单位特种行业许可证扫面件";
    private final String BUSINESS_LICENSE_FILE = "制作单位营业执照扫面件";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

    /**
     * 展示制作单位列表
     *
     * @param districtId
     * @param name
     * @param status
     * @return
     */
    @Override
    public List<MakeDepartmentSimple> selectInfo(String districtId, String name, String status) {
        String did = StringUtil.getDistrictId(districtId);
        List<MakeDepartmentSimple> list = makedepartmentMapper.selectInfo(did, status, name);
        return list;
    }

    /**
     * 查询区域下所有的制作单位
     *
     * @param districtId
     * @return
     */
    @Override
    public List<MakeDepartmentSimple> selectAllInfo(String districtId) {
        String did = StringUtil.getDistrictId(districtId);
        List<MakeDepartmentSimple> list = makedepartmentMapper.selectAllInfo(did);
        return list;
    }


    /**
     * 根据Id查询详细的制作单位资料
     *
     * @param id
     * @return
     */
    @Override
    public Makedepartment selectDetailById(String id) {
        Makedepartment makedepartment = makedepartmentMapper.selectDetailById(id);
        return makedepartment;
    }

    /**
     * 添加制作单位
     *
     * @param makedepartment
     * @return
     */
    @Override
    public int insert(Makedepartment makedepartment, User updateUser) {
        boolean f = true;
        if (isInsert(makedepartment)) {
            return ResultUtil.isHaveCode;
        }
        makedepartment.setId(UUIDUtil.generate());
        makedepartment.setVersionTime(DateUtil.getCurrentTime());
        makedepartment.setFlag(UUIDUtil.generate());
        makedepartment.setVersion(1);
        makedepartment.setRegisterTime(DateUtil.getCurrentTime());
        makedepartment = (Makedepartment) StringUtil.deleteSpace(makedepartment);
        boolean o = historyService.insertOperateRecord(updateUser, makedepartment.getFlag(), makedepartment.getId(), "makDepartment", SyncOperateType.SAVE, UUIDUtil.generate());
        int m = makedepartmentMapper.insert(makedepartment);
        if (makedepartment.getBusinessLicenseUrl() != null) {
            f = registerFile(makedepartment);
        }
        int u = userService.insert(makedepartment.getLegalTelphone(), "ZZDW", makedepartment.getDepartmentName(), makedepartment.getDepartmentAddress());
        if (f && u == ResultUtil.isSend && o && m > 0) {
            SyncEntity syncEntity = ((MakeDepartmentServiceImpl) AopContext.currentProxy()).getSyncData(makedepartment, SyncDataType.MAKEDEPARTMENT, SyncOperateType.SAVE);
            return ResultUtil.isSuccess;
        } else if (u == ResultUtil.isHave) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.isHave;
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.isError;
        }
    }

    /**
     * 更新制作单位
     *
     * @param makedepartment
     * @return
     */
    @Override
    public int update(Makedepartment makedepartment, User updateUser) {
        try {
            Makedepartment oldDate = makedepartmentMapper.selectDetailById(makedepartment.getId());
            List<Employee> employees = employeeService.selectByDepartmentCode(oldDate.getDepartmentCode());
            int d = makedepartmentMapper.deleteHistoryByID(oldDate.getId());
            if (d == 0) {
                return 5;
            }
            makedepartment.setId(UUIDUtil.generate());
            makedepartment.setVersionTime(DateUtil.getCurrentTime());
            makedepartment.setFlag(makedepartment.getFlag());
            makedepartment.setVersion(makedepartment.getVersion() + 1);
            makedepartment.setRegisterTime(oldDate.getRegisterTime());
            makedepartment = (Makedepartment) StringUtil.deleteSpace(makedepartment);
            if (isInsert(makedepartment)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isHaveCode;
            }
            String operateUUid = UUIDUtil.generate();
            String ignore[] = new String[]{"id", "departmentStatus", "deleteStatus", "version", "flag", "versionTime", "registerTime"};
            boolean o = historyService.insertOperateRecord(updateUser, makedepartment.getFlag(), makedepartment.getId(), "makDepartment", SyncOperateType.UPDATE, operateUUid);
            boolean od = historyService.insertUpdateRecord(makedepartment, oldDate, operateUUid, ignore);
            int m = makedepartmentMapper.insert(makedepartment);
            boolean f = registerFile(makedepartment);
            int e = setEmployeeByDepartment(employees, makedepartment, updateUser, 2);
            int u = userService.update(oldDate.getLegalTelphone(), makedepartment.getLegalTelphone(), "ZZDW", makedepartment.getDepartmentName(), makedepartment.getDepartmentAddress());
            if (f && u == ResultUtil.isSuccess && o && od && m > 0 && e > 0) {
                SyncEntity syncEntity = ((MakeDepartmentServiceImpl) AopContext.currentProxy()).getSyncData(makedepartment, SyncDataType.MAKEDEPARTMENT, SyncOperateType.UPDATE);
                return ResultUtil.isSuccess;
            } else if (u == 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isHave;
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isError;
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.isException;
        }
    }

    /**
     * 删除制作单位
     *
     * @param id
     * @return
     */
    @Override
    public int deleteById(String id, User updateUser) {
        Makedepartment makedepartment = makedepartmentMapper.selectDetailById(id);
        if (makedepartmentMapper.deleteHistoryByID(id) == 0) {
            return ResultUtil.isError;
        }
        List<Employee> employees = employeeService.selectByDepartmentCode(makedepartment.getDepartmentCode());
        makedepartment.setVersion(makedepartment.getVersion() + 1);
        makedepartment.setVersionTime(DateUtil.getCurrentTime());
        User user = userService.findByUserName("ZZDW" + makedepartment.getLegalTelphone());
        if (user == null) {
            makedepartment.setId(UUIDUtil.generate());
            int m = makedepartmentMapper.deleteById(makedepartment);
            int e = setEmployeeByDepartment(employees, makedepartment, updateUser, 1);
            boolean o = historyService.insertOperateRecord(updateUser, makedepartment.getFlag(), makedepartment.getId(), "makDepartment", SyncOperateType.DELETE, UUIDUtil.generate());
            if (m == 1 && e == 2 && o) {
                SyncEntity syncEntity = ((MakeDepartmentServiceImpl) AopContext.currentProxy()).getSyncData(makedepartment, SyncDataType.MAKEDEPARTMENT, SyncOperateType.DELETE);
                return ResultUtil.isSuccess;
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isError;
            }
        } else {
            int u = userService.delete(user.getId());
            makedepartment.setId(UUIDUtil.generate());
            int m = makedepartmentMapper.deleteById(makedepartment);
            int e = setEmployeeByDepartment(employees, makedepartment, updateUser, 1);
            boolean o = historyService.insertOperateRecord(updateUser, makedepartment.getFlag(), makedepartment.getId(), "makDepartment", SyncOperateType.DELETE, UUIDUtil.generate());
            if (u == ResultUtil.isSuccess && m == 1 && e == ResultUtil.isSuccess && o) {
                SyncEntity syncEntity = ((MakeDepartmentServiceImpl) AopContext.currentProxy()).getSyncData(makedepartment, SyncDataType.MAKEDEPARTMENT, SyncOperateType.DELETE);
                return ResultUtil.isSuccess;
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isFail;
            }
        }
    }

    /**
     * 按照制作单位查询印章
     *
     * @param user
     * @return
     */
    @Override
    public List<Seal> selectSeal(User user) {
        String telPhone = user.getTelphone();
        MakeDepartmentSimple makedepartment = makedepartmentMapper.selectByLegalTephone(telPhone);
        String makeDepartmentCode = makedepartment.getDepartmentCode();
        List<Seal> seals = sealDao.selectByMakeDepartmentCodeAndIsMake(makeDepartmentCode);
        return seals;
    }

    /**
     * 根据id查找印章的详情
     *
     * @param id
     * @return
     */
    @Override
    public SealVO sealDetails(String id) {
        Seal seal = sealDao.selectByPrimaryKey(id);
        String anentId = seal.getAgentId();
        SealOperationRecord sealOperationRecord = sealDao.selectOperationRecordByCode(id);
        List<SealAgent> sealAgents = new ArrayList<>();
        SealAgent sealAgent = sealAgentMapper.selectByPrimaryKey(anentId);
        if (seal.getIsDeliver()) {    //交付经办人
            SealAgent sealAgent1 = sealAgentMapper.selectSealAgentByGetterId(seal.getGetterId());
            sealAgents.add(sealAgent1);
            if (!seal.getIsLogout() && seal.getIsLoss()) {  //只有挂失但是没有注销
                String lossId = seal.getLossPersonId();
                SealAgent sealAgent2 = sealAgentMapper.selectByPrimaryKey(lossId);  //挂失经办人
                sealAgents.add(sealAgent2);
            } else if (seal.getIsLogout()) {
                SealAgent sealAgent2 = sealAgentMapper.selectSealAgentByGetterId(seal.getLossPersonId());//挂失经办人
                SealAgent sealAgent3 = sealAgentMapper.selectByPrimaryKey(seal.getLogoutPersonId());//注销经办人
                sealAgents.add(sealAgent2);
                sealAgents.add(sealAgent3);
            }
        }
        sealAgents.add(sealAgent);
        SealVO sealVO = new SealVO();
        sealVO.setSeal(seal);
        sealVO.setSealAgents(sealAgents);
        List<SealOperationRecord> list = new ArrayList<>();
        list.add(sealOperationRecord);
        sealVO.setSealOperationRecords(list);
        return sealVO;
    }

    /**
     * 查询历史
     *
     * @param flag
     * @return
     */
    @Override
    public List<Makedepartment> selectHistory(String flag) {
        List<Makedepartment> makedepartments = makedepartmentMapper.selectByFlag(flag);
        return makedepartments;
    }

    /**
     * 模糊查找制作单位简略信息
     *
     * @param districtId
     * @param departmentName
     * @return
     */
    @Override
    public List<MakeDepartmentSimple> selectSimpleByDepartmentName(String districtId, String departmentName) {
        districtId = StringUtil.getDistrictId(districtId);
        return makedepartmentMapper.selectSimpleByName(departmentName, districtId);
    }

    /**
     * 根据法人电话获取制作单位
     *
     * @param phone
     * @return
     */
    @Override
    public MakeDepartmentSimple selectByLegalTephone(String phone) {
        return makedepartmentMapper.selectByLegalTephone(phone);
    }

    /**
     * 根据制作单位的编号查询
     *
     * @param code
     * @return
     */
    @Override
    public MakeDepartmentSimple selectByDepartmentCode(String code) {
        return makedepartmentMapper.selectByDepartmentCode(code);
    }


    /**
     * 根据法人手机号获取备案单位的编号
     *
     * @param phone
     * @return
     */
    @Override
    public String selectCodeByLegalTelphone(String phone) {
        return makedepartmentMapper.selectCodeByLegalTelphone(phone);
    }

    /**
     * 处罚查询
     *
     * @param makeDepartmentName
     * @param startTime
     * @param endTime
     * @param districtId
     * @return
     */
    @Override
    public List<Makedepartment> selectPunish(String makeDepartmentName, String startTime, String endTime, String districtId, String localDistrictId) {
        List<Makedepartment> list = new ArrayList<>();
        if (makeDepartmentName == null && districtId == null && startTime == null && endTime == null) {
            list = makedepartmentMapper.selectByNameAndTimeAndDistrict(makeDepartmentName, startTime, endTime, localDistrictId);
            return list;
        } else if (districtId != null) {
            String districtIds[] = StringUtil.DistrictUtil(districtId);
            if (districtIds[1].equals("00") && districtIds[2].equals("00")) {
                list = selectByTime(makeDepartmentName, startTime, endTime, districtIds[0]);
            } else if (!districtIds[1].equals("00") && districtIds[2].equals("00")) {
                list = selectByTime(makeDepartmentName, startTime, endTime, districtIds[0] + districtIds[1]);
            } else if (!districtIds[1].equals("00") && !districtIds[2].equals("00")) {
                list = selectByTime(makeDepartmentName, startTime, endTime, districtId);
            }
        } else {
            String localdistrictIds[] = StringUtil.DistrictUtil(localDistrictId);
            if (localdistrictIds[1].equals("00") && localdistrictIds[2].equals("00")) {
                list = selectByTime(makeDepartmentName, startTime, endTime, localdistrictIds[0]);
            } else if (!localdistrictIds[1].equals("00") && localdistrictIds[2].equals("00")) {
                list = selectByTime(makeDepartmentName, startTime, endTime, localdistrictIds[0] + localdistrictIds[1]);
            }

        }
        return list;
    }

    /**
     * 对时间做的判断
     *
     * @param makeDepartmentName
     * @param startTime
     * @param endTime
     * @param districtId
     * @return
     */
    public List<Makedepartment> selectByTime(String makeDepartmentName, String startTime, String endTime, String districtId) {
        List<Makedepartment> list = new ArrayList<>();
        //如果时间段为空
        if ((startTime == null || "".equals(startTime))
                && (endTime == null || "".equals(endTime))) {
            list = makedepartmentMapper.selectByNameAndTimeAndDistrict(makeDepartmentName, startTime, endTime, districtId);
        }
        //只有开始时间没有结束时间
        else if ((startTime != null || !"".equals(startTime)) && (endTime == null || "".equals(endTime))) {
            String end = simpleDateFormat.format(new Date());
            list = makedepartmentMapper.selectByNameAndTimeAndDistrict(makeDepartmentName, startTime, end, districtId);
        } else {
            list = makedepartmentMapper.selectByNameAndTimeAndDistrict(makeDepartmentName, startTime, endTime, districtId);
        }
        return list;
    }

    /**
     * 判断是否有重复Code
     *
     * @param makedepartment
     * @return
     */
    public boolean isInsert(Makedepartment makedepartment) {
        List<MakeDepartmentSimple> list = makedepartmentMapper.selectByCode(makedepartment);
        if (list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 对制作单位操作时同时操作从业人员
     *
     * @param employees
     * @return
     */
    public int setEmployeeByDepartment(List<Employee> employees, Makedepartment makedepartment, User user, int type) {
        switch (type) {
            case 1:
                for (Employee emp : employees) {
                    int e = employeeService.deleteEmployee(emp.getId(), user);
                    if (e < 1) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ResultUtil.isFail;
                    }
                }
                return ResultUtil.isSuccess;
            case 2:
                for (Employee emp : employees) {
                    emp.setDistrictId(makedepartment.getDepartmentAddress());
                    int e = employeeService.updateMakeDepartment(emp.getId(), emp.getDistrictId());
                    if (e < 1) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ResultUtil.isFail;
                    }
                }
                return ResultUtil.isSuccess;
        }
        return ResultUtil.isError;
    }


    /**
     * 检查详情
     *
     * @param id
     * @return
     */
    @Override
    public List<ExamineRecordDetail> selectExamineDetailByID(String id) {
        return examineRecordDetailMapper.selectExamineDetailByID(id);
    }

    @Override
    public Makedepartment selectByCode(String departmentCode) {
        Makedepartment makedepartment = makedepartmentMapper.selectByCode1(departmentCode);
        return makedepartment;
    }

    /**
     * 文件注册
     *
     * @param makedepartment
     */
    public boolean registerFile(Makedepartment makedepartment) {
        boolean f1 = fileService.register(makedepartment.getIdCardFrontId(), IDCARD_FRONT_FILE);
        boolean f2 = fileService.register(makedepartment.getIdCardReverseId(), IDCARD_REVERSE_FILE);
        boolean f3 = fileService.register(makedepartment.getSpecialLicenseUrl(), SPECIAL_LICENSE_FILE);
        boolean f4 = fileService.register(makedepartment.getBusinessLicenseUrl(), BUSINESS_LICENSE_FILE);
        if (f1 && f2 && f3 && f4) {
            return true;
        }
        return false;
    }

    @Override
    public String makeDepartmentCode(String name) {
        Makedepartment makedepartment = makedepartmentMapper.selectDetailByName(name);
        return makedepartment.getDepartmentCode();
    }

    @Override
    public Makedepartment selectByAllName(String name){
        Makedepartment makedepartment = makedepartmentMapper.selectByAllName(name);
        return makedepartment;
    }

    /**
     * 数据同步
     *
     * @return
     */
    @Sync()
    public SyncEntity getSyncData(Object object, int dataType, int operateType) {
        SyncEntity syncEntity = new SyncEntity();
        syncEntity.setObject(object);
        syncEntity.setDataType(dataType);
        syncEntity.setOperateType(operateType);
        return syncEntity;
    }

    /**
     * @param map 区域
     * @return
     */
    @Override
    public List<Makedepartment> makeDepartmentSort(Map map) {
        String type = (String) map.get("type");
        String districtId = (String) map.get("districtId");
        districtId = StringUtil.getDistrictId(districtId);
        if (type.equals("0")) {
            return makedepartmentMapper.makeDepartmentSort(districtId);
        } else {
            return makedepartmentMapper.makeDepartmentSortBySealNum(districtId);
        }

    }


    @Override
    public List<MakedepartmentSimplePO> selectMakedePartment(MakedepartmentSimplePO makedepartmentSimplePO) {
        List<MakedepartmentSimplePO> makedepartmentSimplePOS = new ArrayList<>();
        if (makedepartmentSimplePO.getCityName()!=null && makedepartmentSimplePO.getCityName()!="") {
            District district = districtMapper.selectDistrictByCityName(makedepartmentSimplePO.getCityName());
            makedepartmentSimplePO.setDepartmentAddress(district.getCityId().substring(0, 4));

        }
        String userLongitude = makedepartmentSimplePO.getUserLongitude();
        String userLatitude = makedepartmentSimplePO.getUserLatitude();
        //综合排序
        if (makedepartmentSimplePO.getType().equals("1")) {
            ///评价排序
            makedepartmentSimplePOS = makedepartmentMapper.selectMakedePartmentByEvaluate(makedepartmentSimplePO);
         for(int i = 0;i < makedepartmentSimplePOS.size();i++){
                MakedepartmentSimplePO makedepartmentSimplePO1 = makedepartmentSimplePOS.get(i);
                int total = sealDao.countSealByMonthAndMakeDepartment(makedepartmentSimplePO1.getDepartmentCode());
                makedepartmentSimplePO1.setTotal(total);
            }
            sortByDistance(makedepartmentSimplePOS,false,userLongitude,userLatitude);
        } else if (makedepartmentSimplePO.getType().equals("2")) {
            //
            makedepartmentSimplePOS = makedepartmentMapper.selectMakedePartmentByEvaluate(makedepartmentSimplePO);
            for(int i = 0;i < makedepartmentSimplePOS.size();i++){
                MakedepartmentSimplePO makedepartmentSimplePO1 = makedepartmentSimplePOS.get(i);
                int total = sealDao.countSealByMonthAndMakeDepartment(makedepartmentSimplePO1.getDepartmentCode());
                makedepartmentSimplePO1.setTotal(total);
            }

            Collections.sort(makedepartmentSimplePOS, new Comparator<MakedepartmentSimplePO>() {
                @Override
                public int compare(MakedepartmentSimplePO makedepartmentSimplePO1, MakedepartmentSimplePO makedepartmentSimplePO2) {
                    int i = (int) (makedepartmentSimplePO2.getTotal() - makedepartmentSimplePO1.getTotal());
                    if (i == 0) {
                        return (int) (makedepartmentSimplePO1.getTotal() - makedepartmentSimplePO2.getTotal());
                    }
                    return i;
                }
            });
            sortByDistance(makedepartmentSimplePOS,false,userLongitude,userLatitude);
        } else {
            //综合排序
            makedepartmentSimplePOS = makedepartmentMapper.selectMakedePartmentByEvaluate(makedepartmentSimplePO);
            for(int i = 0;i < makedepartmentSimplePOS.size();i++){
                MakedepartmentSimplePO makedepartmentSimplePO1 = makedepartmentSimplePOS.get(i);
                int total = sealDao.countSealByMonthAndMakeDepartment(makedepartmentSimplePO1.getDepartmentCode());
                makedepartmentSimplePO1.setTotal(total);
            }
            sortByDistance(makedepartmentSimplePOS,true,userLongitude,userLatitude);
        }

        return makedepartmentSimplePOS;
    }

    @Override
    public List<MakedepartmentSimplePO> selectMakedePartmentByRegionId(MakedepartmentSimplePO makedepartmentSimplePO) {
        return null;
    }

    @Override
    public MakedepartmentSimplePO selectMakedepartmentSimplePODetailById(String id) {
        return makedepartmentMapper.selectMakedepartmentSimplePODetailById(id);
    }

    /**
     * 根据距离并确定是否要排序
     * @param makedepartmentSimplePOS
     * @return
     */
    public List<MakedepartmentSimplePO> sortByDistance(List<MakedepartmentSimplePO> makedepartmentSimplePOS,boolean isSortByDistance,String userLongitude, String userLatitude) {
        for (MakedepartmentSimplePO makedepartmentSimplePO : makedepartmentSimplePOS) {
            MakeDepartmentAttachInfo makeDepartmentAttachInfo = makeDepartmentAttacthInfoService.selectByMakeDepartmentFlag(makedepartmentSimplePO.getFlag());
            if (makeDepartmentAttachInfo != null) {
                Double[] distance = getDistance(userLongitude,userLatitude,makeDepartmentAttachInfo.getLongitude(),makeDepartmentAttachInfo.getLatitude());
                makedepartmentSimplePO.setDistance(distance[0]);
            }
        }
        if(isSortByDistance) {
            Collections.sort(makedepartmentSimplePOS, new Comparator<MakedepartmentSimplePO>() {
                @Override
                public int compare(MakedepartmentSimplePO makedepartmentSimplePO1, MakedepartmentSimplePO makedepartmentSimplePO2) {
                    int i = (int) (makedepartmentSimplePO1.getDistance() - makedepartmentSimplePO2.getDistance());
                    if (i == 0) {
                        return (int) (makedepartmentSimplePO2.getDistance() - makedepartmentSimplePO1.getDistance());
                    }
                    return i;
                }
            });
        }
        return makedepartmentSimplePOS;
    }

    /**
     * 向腾讯接口获取距离
     * @param userLongitude
     * @param userLatitude
     * @param makeLongitude
     * @param makeLatitude
     * @return
     */
    public Double[] getDistance(String userLongitude, String userLatitude, String makeLongitude, String makeLatitude) {
        Double[] distances = new Double[1];
        String key = "MD4BZ-G4LLD-NAE4J-PCB6D-QUEAT-4RFZT";
        //请求路径
        String getURL = "http://apis.map.qq.com/ws/distance/v1/";
        //计算方式：driving（驾车）、walking（步行）默认：driving
        String mode = "walking";
        //返回格式：支持JSON/JSONP，默认JSON (非必填项)
        //String output = "";
        //JSONP方式回调函数  (非必填项)
        //String callback = "";
        //请求路径
        String from = userLatitude+","+userLongitude ;
        String to =   makeLatitude+","+ makeLongitude;
        String urlString = getURL + "?mode=" + mode + "&from=" + from + "&to=" + to + "&key=" + key;
        //接收腾讯回传的地址解析结果
        String result = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            // 腾讯地图使用GET
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            // 获取地址解析结果
            while ((line = in.readLine()) != null) {
                result += line + "\n";
               // System.out.println(result);
            }
            in.close();
        } catch (Exception e) {
            e.getMessage();
        }
        // 转JSON格式
        JSONObject jsonObject = JSONObject.parseObject(result).getJSONObject("result");
        //elements是[](数组格式)所以使用JSONArray获取
        JSONArray adInfo = jsonObject.getJSONArray("elements");
        //for数组
        for (int j = 0; j < adInfo.size(); j++) {
            JSONObject jsonObject2 = adInfo.getJSONObject(j);
            //获取距离(获取到的是m为单位)
            String distance = jsonObject2.getString("distance");
            double distanceD = Double.valueOf(distance);
            DecimalFormat df = new DecimalFormat("#.00");
            //转化为km
            distanceD = distanceD / 1000;
            distances[j] = Double.valueOf(df.format(distanceD));
        }
        return distances;
    }

    @Override
    public Makedepartment getCompany(String company) {
        return makedepartmentMapper.selectDetailById(company);
    }

//    @Override
//    public List<Makedepartment> selectByMakeDepartmentName(String departmentName,String departmentCode) {
//        List<Makedepartment> makedepartments = makedepartmentMapper.selectByMakeDepartmentName(departmentName,departmentCode);
//        return makedepartments;
//    }
}
