package com.dhht.service.seal.Impl;

import com.dhht.common.ImageGenerate;
import com.dhht.dao.*;
import com.dhht.model.*;
import com.dhht.model.pojo.MakedepartmentSimplePO;
import com.dhht.model.pojo.SealVerificationPO;
import com.dhht.model.pojo.SealWeChatDTO;
import com.dhht.service.employee.EmployeeService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.message.NotifyService;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.service.seal.SealCodeService;
import com.dhht.service.seal.WeChatSealService;
import com.dhht.service.tools.FileService;
import com.dhht.service.tools.SmsSendService;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.dhht.service.user.UserService;
import com.dhht.util.*;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service("weChatSealService")
@Transactional
public class WeChatSealServiceImp implements WeChatSealService {

    @Value("${sms.template.express}")
    private int express;

    @Value("${sms.template.getseal}")
    private int getseal;

    @Autowired
    private SealVerificationMapper sealVerificationMapper;

    @Autowired
    private SealAgentMapper sealAgentMapper;

    @Autowired
    private UseDepartmentService useDepartmentService;

    @Autowired
    private RecordDepartmentService recordDepartmentService;

    @Autowired
    private MakeDepartmentService makeDepartmentService;

    @Autowired
    private SealDao sealDao;

    @Autowired
    private UseDepartmentDao useDepartmentDao;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SealOperationRecordMapper sealOperationRecordMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private FileService fileService;

    @Autowired
    private CourierMapper courierMapper;

    @Autowired
    private MakeDepartmentSealPriceMapper makeDepartmentSealPriceMapper;

    @Autowired
    private SealPayOrderMapper sealPayOrderMapper;

    @Autowired
    private SmsSendService smsSendService;

    @Autowired
    private RecordDepartmentMapper recordDepartmentMapper;

    @Autowired
    private SealCodeService sealCodeService;



    /**
     * 资料更新列表
     * @param telphone
     * @return
     */
    @Override
    public List<SealVerificationPO> sealAndVerification(String telphone) {
        List<Seal> seals = sealDao.selectSealByTelphone(telphone);
        List<SealVerificationPO> sealVerificationPOS = new ArrayList<>();
        for(Seal seal:seals){
            SealVerificationPO sealVerificationPO =new SealVerificationPO();
            sealVerificationPO.setSeal(seal);
            String sealId = seal.getId();
            SealVerification sealVerification = sealVerificationMapper.selectBySealId(sealId);
            sealVerificationPO.setSealVerification(sealVerification);
            sealVerificationPOS.add(sealVerificationPO);
        }
        return sealVerificationPOS;
    }

    /**
     * 资料更新 根据id查找
     * @param id
     * @return
     */
    @Override
    public SealVerificationPO selectVerificationById(String id) {
        Seal seal = sealDao.selectByPrimaryKey(id);
        String agentId = seal.getAgentId();
        SealAgent sealAgent = sealAgentMapper.selectByPrimaryKey(agentId);
        List<SealAgent> list = new ArrayList<>();
        list.add(sealAgent);
        SealVerification sealVerification = sealVerificationMapper.selectBySealId(id);
        UseDepartment useDepartment = useDepartmentService.selectByCode(seal.getUseDepartmentCode());
        MakeDepartmentSimple makedepartmentSimple = makeDepartmentService.selectByDepartmentCode(seal.getMakeDepartmentCode());
        RecordDepartment recordDepartment = recordDepartmentService.selectByCode(seal.getMakeDepartmentCode());
        SealVerificationPO sealVerificationPO = new SealVerificationPO();
        sealVerificationPO.setSeal(seal);
        sealVerificationPO.setSealVerification(sealVerification);
        sealVerificationPO.setSealAgent(list);
        sealVerificationPO.setMakeDepartmentSimple(makedepartmentSimple);
        sealVerificationPO.setRecordDepartment(recordDepartment);
        sealVerificationPO.setUseDepartment(useDepartment);
        return sealVerificationPO;
    }

    /**
     * 线上快递交付
     * @param user
     * @param
     * @return
     */
    @Override
    public int expressdeliver(User user, Seal seal1) {
        Seal seal= sealDao.selectByPrimaryKey(seal1.getId());
        if (seal.getIsLogout()) {
            return ResultUtil.isLogout;
        }
        if (seal.getIsLoss()) {
            return ResultUtil.isLoss;
        }
        UseDepartment useDepartment = useDepartmentDao.selectByCode(seal.getUseDepartmentCode());  //根据usedepartment查询对应的使用公司
        if (useDepartment == null) {
            return ResultUtil.isNoDepartment;
        }
        //印章操作
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        SealOperationRecord sealOperationRecord = new SealOperationRecord();
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setSealId(seal.getId());
        sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
        sealOperationRecord.setEmployeeId(employee.getEmployeeId());   //从业人员登记
        sealOperationRecord.setEmployeeName(employee.getEmployeeName());
        sealOperationRecord.setEmployeeCode(employee.getEmployeeCode());
        sealOperationRecord.setOperateType("04");
        int insertSealOperationRecord = sealOperationRecordMapper.insertSelective(sealOperationRecord);

        seal.setIsDeliver(true);
        seal.setDeliverDate(DateUtil.getCurrentTime());
        seal.setIsEarlywarning(true);
        seal.setEarlywarningDate(DateUtil.getCurrentTime());

        RecordDepartment recordDepartment = recordDepartmentService.selectByCode(seal.getRecordDepartmentCode());
        MakeDepartmentSimple makeDepartmentSimple = makeDepartmentService.selectByDepartmentCode(seal.getMakeDepartmentCode());
        User user1 = new User();
        user1.setId(UUIDUtil.generate());
        user1.setTelphone(recordDepartment.getTelphone());
        user1.setUserName(recordDepartment.getDepartmentName());
        user1.setRealName(recordDepartment.getDepartmentName());
        user1.setDistrictId(recordDepartment.getDepartmentAddress());
        Notify notify = new Notify();
        notify.setId(UUIDUtil.generate());
        notify.setNotifyTitle("备案预警");
        List<Employee> employees = employeeService.selectAllByDepartmentCode(makeDepartmentSimple.getDepartmentCode());
        List<String> userId = new ArrayList<>();
        for(Employee employeeId:employees){
            User user2 = userService.findByUserName("CYRY"+employeeId.getTelphone());
            if(user2!=null) {
                userId.add(user2.getId());
            }
        }
        notify.setNotifyUser(userId);
        notify.setNotifyContent("您的"+seal.getSealName()+"编号"+seal.getSealCode()+"的印章请尽快备案");
        int notifyResult = notifyService.insertNotify(notify,user1);
        if(notifyResult==ResultUtil.isFail){
            return ResultUtil.isFail;
        }

        SealAgent sealAgent = new SealAgent();
        String saId = UUIDUtil.generate();
        sealAgent.setId(saId);
        seal.setDeliveryExpressInfo(seal1.getDeliveryExpressInfo());
        if (seal.getDeliveryExpressInfo().getAgentphone()==null){

        }else{
            sealAgent.setTelphone(seal.getDeliveryExpressInfo().getAgentphone());
            sealAgent.setAgentPhotoId(seal.getDeliveryExpressInfo().getAgentPhotoImage());
            sealAgent.setCertificateNo(seal.getDeliveryExpressInfo().getAgentidcard());
            sealAgent.setCertificateType("111");
            sealAgent.setIdCardFrontId(seal.getDeliveryExpressInfo().getAgentidcardFrontImage());
            sealAgent.setIdCardReverseId(seal.getDeliveryExpressInfo().getAgentidcardFrontReverseImage());
            sealAgent.setProxyId(seal.getDeliveryExpressInfo().getAgentproxyImage());
        }

        sealAgent.setBusinessType("01");
        sealAgent.setName(seal.getDeliveryExpressInfo().getAgentName());

        int sealAgentResult = sealAgentMapper.insert(sealAgent);
        seal.setSealStatusCode("04");
        seal.setGetterId(saId);
        int updateByPrimaryKey = sealDao.updateByPrimaryKeySelective(seal);

        //更新支付状态
        SealPayOrder sealPayOrder = sealPayOrderMapper.selectBySealId(seal.getId());
        if (sealPayOrder == null){
            return  ResultUtil.isFail;
        }
        sealPayOrder.setIsPay(true);
        sealPayOrderMapper.updateByPrimaryKeySelective(sealPayOrder);
        if (insertSealOperationRecord > 0 && updateByPrimaryKey > 0 && sealAgentResult > 0) {
            ArrayList<String> params = new ArrayList<String>();
            params.add(seal.getUseDepartmentName());
            params.add(ResultUtil.sealType(seal.getSealTypeCode()));
            Boolean b = smsSendService.sendSingleMsgByTemplate(sealAgent.getTelphone(), express, params);
            return ResultUtil.isSuccess;
        } else {
            return ResultUtil.isFail;
        }
    }

    /**
     * 公章变更
     *
     * @param
     * @return
     */
    @Override
    public int cachetChange(String sealId,String sealAgentId, WeChatUser weChatUser) {
        try {
            Seal seal1 = sealDao.selectByPrimaryKey(sealId);
            if (seal1 != null) {
                seal1.setLogoutPersonId(sealAgentId);
                seal1.setIsLogout(true);
                seal1.setLogoutDate(DateUtil.getCurrentTime());

                SealOperationRecord sealOperationRecord = new SealOperationRecord();
                sealOperationRecord.setId(UUIDUtil.generate());
                sealOperationRecord.setSealId(seal1.getId());
                sealOperationRecord.setEmployeeId(weChatUser.getId());  //小程序端的变更操作人就是小程序的登录用户
                sealOperationRecord.setEmployeeName(weChatUser.getName());
//                sealOperationRecord.setEmployeeCode();
                sealOperationRecord.setOperateType("07");
                sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
                int sealOperationRecordInsert = sealOperationRecordMapper.insertSelective(sealOperationRecord);
                int sealUpdate = sealDao.updateByPrimaryKeySelective(seal1);
                if(sealOperationRecordInsert<0 && sealUpdate<0){
                    return ResultUtil.isError;
                }else{
                    return ResultUtil.isSuccess;
                }
            }else{
                return ResultUtil.isNoSeal;
            }

        }catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.isException;
        }
    }


    @Override
    public int sealWeChatRecord(WeChatUser user, SealWeChatDTO sealDTO, String payOrderId) {
        try {
            List<Seal> list = sealDao.selectByCodeAndType(sealDTO.getUseDepartmentCode());
            UseDepartment useDepartment = useDepartmentDao.selectByCode(sealDTO.getUseDepartmentCode());
            MakeDepartmentSimple makedepartment = makeDepartmentService.selectByDepartmentCode(sealDTO.getMakedepartmentCode());
            RecordDepartment recordDepartment = recordDepartmentMapper.selectBydistrict(makedepartment.getDepartmentAddress());

            for (Seal seal : list) {
                if (seal.getSealTypeCode().equals(sealDTO.getSeal().getSealTypeCode()) && (sealDTO.getSeal().getSealTypeCode().equals("05") || sealDTO.getSeal().getSealTypeCode().equals("01"))) {
                    return ResultUtil.isHaveSeal;
                }
            }
            Seal seal = sealDTO.getSeal();
            String sealcode1 = sealCodeService.getMaxSealCode(makedepartment.getDepartmentAddress());
            String serial = "00000000";
            String sealcode = makedepartment.getDepartmentAddress() + serial.substring(0, serial.length() - sealcode1.length()) + sealcode1;
            seal.setSealCode(sealcode);
            //印章信息
            String sealId = UUIDUtil.generate();
            seal.setId(sealId);
            String sealType = chooseType(seal.getSealTypeCode());
            seal.setSealName(useDepartment.getName() + sealType);
            seal.setUseDepartmentCode(sealDTO.getUseDepartmentCode());
            seal.setUseDepartmentName(useDepartment.getName());
            seal.setSealStatusCode("03");
            seal.setIsRecord(true);
            seal.setRecordDate(DateUtil.getCurrentTime());
            seal.setIsMake(false);
            seal.setIsDeliver(false);
            seal.setIsLoss(false);
            seal.setIsPersonal(false);
            seal.setIsLogout(false);
            seal.setDistrictId(useDepartment.getDistrictId());
            seal.setMakeDepartmentCode(makedepartment.getDepartmentCode());
            seal.setMakeDepartmentName(makedepartment.getDepartmentName());
            seal.setApplySource(1);

            if (recordDepartment != null) {
                seal.setRecordDepartmentCode(recordDepartment.getDepartmentCode());
                seal.setRecordDepartmentName(recordDepartment.getDepartmentName());
            }

            seal.setIsRecord(true);
            seal.setRecordDate(DateUtil.getCurrentTime());
            seal.setIsUndertake(true);
            seal.setUndertakeDate(DateUtil.getCurrentTime());
            if(seal.getSealTypeCode().equals("01")){
                Seal seal2 = sealDao.selectByTypeAndUseDepartmentCode(sealDTO.getUseDepartmentCode(),null,"01");
                if(seal2!=null){
                    return ResultUtil.isHaveSeal;
                }
            }
            if(seal.getSealTypeCode().equals("05")){
                Seal seal2 = sealDao.selectByTypeAndUseDepartmentCode(sealDTO.getUseDepartmentCode(),null,"05");
                if(seal2!=null){
                    return ResultUtil.isHaveSeal;
                }
            }
            if (seal.getSealReason().equals("03")) {
                if (seal.getSealTypeCode().equals("01")) {
                    Seal seal1 = sealDao.selectByTypeAndUseDepartmentCode(seal.getUseDepartmentCode(),null,"01");
                    if (seal1 != null) {
                        int logoutSeal = sealDao.logoutSeal(seal.getUseDepartmentCode(),"01");
                    }
                    else {
                        return ResultUtil.isNoSeal;
                    }
                }
            }

            //核验记录
            SealVerification sealVerification = new SealVerification();
            sealVerification.setId(UUIDUtil.generate());
            sealVerification.setSealId(sealId);
            sealVerification.setIsVerification(false);
            sealVerification.setVerifyTypeName("0");
            sealVerificationMapper.insertSelective(sealVerification);

            //申请操作记录
            String type1 = "00";
            SealOperationRecord sealOperationRecord = new SealOperationRecord();
            sealOperationRecord.setId(UUIDUtil.generate());
            sealOperationRecord.setSealId(sealId);
            sealOperationRecord.setOperateType(type1);
            sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
            sealOperationRecord.setEmployeeName(user.getName());
            int sealOperationRecordInsert = sealOperationRecordMapper.insertSelective(sealOperationRecord); //保存操作记录
            if (sealOperationRecordInsert < 0) {
                return ResultUtil.isFail;
            }

            //自动承接操作记录
            String type2 = "01";
            SealOperationRecord sealOperationRecord2 = new SealOperationRecord();
            sealOperationRecord2.setId(UUIDUtil.generate());
            sealOperationRecord2.setSealId(sealId);
            sealOperationRecord2.setOperateType(type2);
            sealOperationRecord2.setOperateTime(DateUtil.getCurrentTime());
            sealOperationRecord2.setEmployeeName(makedepartment.getDepartmentName());
            int sealOperationRecordInsert2 = sealOperationRecordMapper.insertSelective(sealOperationRecord2); //保存操作记录
            if (sealOperationRecordInsert2< 0) {
                return ResultUtil.isFail;
            }

            //经办人信息

            String saId = sealDTO.getSealAgent().getId();

            seal.setAgentId(sealDTO.getSealAgent().getId());
            seal.setIsUndertake(true);
            seal.setUndertakeDate(DateUtil.getCurrentTime());
            int sealInsert = sealDao.insertSelective(seal);


            //插入快递
            SealPayOrder sealPayOrder = sealDTO.getSealPayOrder();
            Courier courier = sealDTO.getCourier();
            if (courier.getRecipientsId() != null && !courier.getRecipientsId().isEmpty()) {
                String courierId = UUIDUtil.generate();
                courier.setId(courierId);
                courier.setSealId(sealId);
                courier.setCourierNo(testNum(13));
                courier.setCourierType("EMS");
                courierMapper.insertSelective(courier);
                sealPayOrder.setCourierId(courierId);
            }
            //插入到订单中
//
            sealPayOrder.setId(payOrderId);
            sealPayOrder.setSealId(sealId);
            sealPayOrder.setPayDate(DateUtil.getCurrentTime());
            sealPayOrderMapper.insertSelective(sealPayOrder);

            //当增加经办人，操作信息和印章信息成功后，生成印模信息 存入数据库
            if (sealInsert > 0 ) {

                Map<String, String> map = new HashMap<>();
                map.put("useDepartment", useDepartment.getName());
                map.put("sealType", sealType);
                map.put("sealCode", sealcode);
                String centerImageNum = seal.getSealCenterImage();
                String centerImage = "";
                switch (centerImageNum) {
                    case "01":
                        centerImage = "★";
                        break;
                    case "02":
                        centerImage = "▲";
                        break;
                    case "03":
                        centerImage = "★";
                        break;
                }
                map.put("centerImage", centerImage);
                //印模图像
                String localPath = new ImageGenerate().seal(map);
                File file = new File(localPath);
                BufferedImage image = ImageUtil.getBufferedImage(localPath);
                ImageGenerate imageGenerate = new ImageGenerate();

                imageGenerate.saveGridImage(new File(localPath), image); //设置dpi

                BufferedImage image1 = ImageUtil.getBufferedImage(localPath);
                ImageIO.write(image1, "bmp", new File(localPath));
                File file2 = new File(localPath);
                InputStream inputStream = new FileInputStream(file2);
                byte[] imageData = FileUtil.readInputStream(inputStream);
                FileInfo fileInfo = fileService.save(imageData, DateUtil.getCurrentTime() + useDepartment.getName() + sealType, "bmp", "", FileService.CREATE_TYPE_UPLOAD, user.getId(), user.getTelphone());
                String moulageImageId = fileInfo.getId();
                //增加微缩图
                String fileName = file.getAbsolutePath();
                String caselsh = fileName.substring(0, fileName.lastIndexOf("."));//前缀
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);//后缀

                String path = caselsh + "_micro." + suffix;
//                    ImageUtil.convert(localPath);  透明化
                Thumbnails.of(localPath).scale(0.3f).toFile(path);

                File microfile = new File(path);
                InputStream inputStream1 = new FileInputStream(microfile);

                byte[] microimageData = FileUtil.readInputStream(inputStream1);
                FileInfo microfileInfo = fileService.save(microimageData, DateUtil.getCurrentTime() + seal.getUseDepartmentName() + sealType, "png", "", FileService.CREATE_TYPE_UPLOAD, user.getId(), user.getTelphone());
                String micromoulageImageId = microfileInfo.getId();
//                    Thumbnails.of(filePathName).scale(1f).outputQuality(scale).outputFormat("jpg")


                //把印模信息材料存入数据库
                SealMaterial sealMaterial = new SealMaterial();
                sealMaterial.setId(UUIDUtil.generate());
                sealMaterial.setSealCode(sealcode);
                sealMaterial.setType("04");
                sealMaterial.setFilePath(moulageImageId);
                int sealMaterialInsert = sealDao.insertSealMaterial(sealMaterial);

                SealMaterial microsealMaterial = new SealMaterial();
                microsealMaterial.setId(UUIDUtil.generate());
                microsealMaterial.setSealCode(sealcode);
                microsealMaterial.setType("06");
                microsealMaterial.setFilePath(micromoulageImageId);
                sealDao.insertSealMaterial(microsealMaterial);


                //二维数据
                int[][] imgArr = imageGenerate.moulageData(map);
                String imagArrLocalPath = FileUtil.saveArrayFile(imgArr);
                File file1 = new File(imagArrLocalPath);
                // InputStream inputStream1 = new FileInputStream(file1);
                byte[] moulageDates = FileUtil.readInputStream(inputStream1);
                FileInfo fileInfo1 = fileService.save(moulageDates, DateUtil.getCurrentTime() + seal.getUseDepartmentName() + sealType + "二维数据", "txt", "", FileService.CREATE_TYPE_UPLOAD, user.getId(), user.getTelphone());
                String moulageId = fileInfo1.getId();
                SealMaterial sealMaterial1 = new SealMaterial();
                sealMaterial1.setId(UUIDUtil.generate());
                sealMaterial1.setSealCode(sealcode);
                sealMaterial1.setType("05");
                sealMaterial1.setFilePath(moulageId);
                int sealMaterialInsert1 = sealDao.insertSealMaterial(sealMaterial1);

                if (sealMaterialInsert < 0 && sealMaterialInsert1 < 0) {
                    return ResultUtil.isError;
                }
            }
            return ResultUtil.isSuccess;
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.isException;
        }
    }

    @Override
    public MakeDepartmentSealPrice sealPrice(Map map) {
        String makeDepartmentFlag = (String) map.get("makeDepartmentFlag");
        String sealType = (String) map.get("sealType");
        MakeDepartmentSealPrice makeDepartmentSealPrice = new MakeDepartmentSealPrice(sealType, makeDepartmentFlag);
        return makeDepartmentSealPriceMapper.selectBySealTypeAndMakeDepartment(makeDepartmentSealPrice);
    }

    @Override
    public List<Seal> sealProgress(Map map) {
        String telphone =(String)map.get("telphone");
        return sealDao.selectSealByTelphone(telphone);
    }

    @Override
    public List<Seal> portalSealInfoByCode(String code) {
        List<Seal> seals = sealDao.selectByCodeAndType(code);
        return seals;
    }

    @Override
    public List<Seal> sealListForWeChat(String telphone) {
        List<Seal> sealList = sealDao.sealList(telphone);
        return sealList;
    }

    /**
     * 小程序端的公章核验
     * @param sealCode
     * @param useDepartmentCode
     * @param sealTypeCode
     * @return
     */
    @Override
    public Map<String,Object> weChatcheckSealCode(String sealCode, String useDepartmentCode, String sealTypeCode) {
        Map<String,Object> map = new HashMap<>();
        List<Seal> seals = sealDao.selectByTypeAndUseDepartmentCode2(useDepartmentCode,sealTypeCode);
        UseDepartment useDepartment = useDepartmentService.selectByCode(useDepartmentCode);
        if(seals.size()==0){
            map.put("status", "error");
            map.put("message","数据不存在");
            return map;
        }
        for(Seal seal:seals){
            if(seal.getSealCode().equals(sealCode)){
                map.put("status", "ok");
                map.put("message","查询成功");
                map.put("seal",seal);
                map.put("useDepartment",useDepartment);
                return map;
            }
        }
        map.put("status", "error");
        map.put("message","数据不存在");
        return map;
    }

    /**
     * 资料更新查询
     * @param sealId
     * @return
     */
    @Override
    public SealVerificationPO isSealVerification(String sealId){
        SealVerification sealVerification = sealVerificationMapper.selectBySealId(sealId);
        Seal seal = sealDao.selectByPrimaryKey(sealId);
        SealVerificationPO sealVerificationPO = new SealVerificationPO();
        sealVerificationPO.setSeal(seal);
        sealVerificationPO.setSealVerification(sealVerification);
        List<SealAgent> list = new ArrayList<>();
        if(seal.getAgentId()!=null){
            SealAgent sealAgent = sealAgentMapper.selectByPrimaryKey(seal.getAgentId());
            list.add(sealAgent);
        }
        if(seal.getGetterId()!=null){
            SealAgent sealAgent = sealAgentMapper.selectByPrimaryKey(seal.getGetterId());
            list.add(sealAgent);
        }
        if(seal.getLossPersonId()!=null){
            SealAgent sealAgent = sealAgentMapper.selectByPrimaryKey(seal.getLossPersonId());
            list.add(sealAgent);
        }
        if(seal.getLogoutPersonId()!=null){
            SealAgent sealAgent = sealAgentMapper.selectByPrimaryKey(seal.getLogoutPersonId());
            list.add(sealAgent);
        }
        sealVerificationPO.setSealAgent(list);
        return sealVerificationPO;
    }


    @Override
    public int updatePay(SealPayOrder sealPayOrder) {
        return sealPayOrderMapper.updateByPrimaryKeySelective(sealPayOrder);
    }

    /**
     * 选择章
     *
     * @param num
     * @return
     */
    public String chooseType(String num) {
        String sealType = "";
        switch (num) {
            case "01":
                sealType = "单位专用章";
                break;
            case "02":
                sealType = "财务专用章";
                break;
            case "03":
                sealType = "税务专用章";
                break;
            case "04":
                sealType = "合同专用章";
                break;
            case "05":
                sealType = "法人代表人名章";
                break;
            case "99":
                sealType = "其他类型印章";
                break;
        }
        return sealType;
    }


    public String testNum(int num) {
        StringBuilder str = new StringBuilder();//定义变长字符串
        Random random = new Random();
        for (int i = 0; i < num; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    /**
     * 判断是否重复
     * @param useDepartmentCode
     * @param seal
     * @return
     */
    public int isHaveSeal(String useDepartmentCode,Seal seal){
        if(seal.getSealTypeCode().equals("01")) {
            Seal seal1 = sealDao.selectByTypeAndUseDepartmentCode(useDepartmentCode,null,"01");
            if(seal1!=null){
                return ResultUtil.isHaveSeal;
            }
        }
        if(seal.getSealTypeCode().equals("05")){
            Seal seal1 = sealDao.selectByTypeAndUseDepartmentCode(useDepartmentCode,null,"05");
            if(seal1!=null){
                return ResultUtil.isHaveSeal;
            }
        }

        return ResultUtil.isSuccess;
    }

    /**
     * 资料更新
     * @param weChatUser
     * @param id
     * @param sealAgent
     * @return
     */
    public int dateUpdate(WeChatUser weChatUser,String id,SealAgent sealAgent){
        Seal seal = sealDao.selectByPrimaryKey(id);
        String agentId = seal.getAgentId();
        sealAgent.setId(agentId);
        int updateAgent = sealAgentMapper.updateByPrimaryKeySelective(sealAgent);
        int updateASeal = sealDao.updateByPrimaryKeySelective(seal);
        SealOperationRecord sealOperationRecord = new SealOperationRecord();
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setEmployeeName(weChatUser.getName());
        sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
        sealOperationRecord.setOperateType("09");
        sealOperationRecord.setSealId(seal.getId());
        int insertOperationRecord = sealOperationRecordMapper.insertSelective(sealOperationRecord);
        SealVerification sealVerification = sealVerificationMapper.selectBySealId(id);
        sealVerification.setIsReuploadData(true);
        int updateVerification = sealVerificationMapper.updateByPrimaryKeySelective(sealVerification);
        if(updateAgent>0&&updateASeal>0&&insertOperationRecord>0){
            return ResultUtil.isSuccess;
        }else {
            return ResultUtil.isFail;
        }
    }



}
