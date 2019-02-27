package com.dhht.service.seal.Impl;

import com.dhht.annotation.Sync;
import com.dhht.common.ImageGenerate;
import com.dhht.common.JsonObjectBO;
import com.dhht.dao.*;
import com.dhht.face.AFR;
import com.dhht.model.*;
import com.dhht.model.pojo.*;

import com.dhht.service.employee.EmployeeService;
import com.dhht.service.make.MakeDepartmentSealPriceService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.message.NotifyService;
import com.dhht.service.order.OrderService;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.service.seal.SealCodeService;
import com.dhht.service.seal.SealService;
import com.dhht.service.tools.FileService;
import com.dhht.service.tools.FileStoreService;
import com.dhht.service.tools.SmsSendService;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.dhht.service.user.UserLoginService;
import com.dhht.service.user.UserService;
import com.dhht.sync.SyncDataType;
import com.dhht.sync.SyncOperateType;
import com.dhht.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import dhht.idcard.trusted.identify.GuangRayIdentifier;
import dhht.idcard.trusted.identify.IdentifyResult;
import io.micrometer.core.instrument.Meter;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.poi.ddf.EscherSerializationListener;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import sun.management.Agent;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.dhht.service.user.impl.UserServiceImpl.createRandomVcode;

@Service("sealService")
@Transactional
public class SealServiceImpl implements SealService {

    @Autowired
    private SealDao sealDao;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SealAgentMapper sealAgentMapper;

    @Autowired
    private RecordDepartmentMapper recordDepartmentMapper;

    @Autowired
    private UseDepartmentDao useDepartmentDao;

    @Autowired
    private MakeDepartmentService makeDepartmentService;

    @Autowired
    private UseDepartmentService useDepartmentService;

    @Autowired
    private RecordDepartmentService recordDepartmentService;

    @Autowired
    private FaceCompareRecordMapper faceCompareRecordMapper;

    @Autowired
    private SealCodeService sealCodeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private SealOperationRecordMapper sealOperationRecordMapper;

    @Autowired
    private CourierMapper courierMapper;

    @Autowired
    private SealPayOrderMapper sealPayOrderMapper;

    @Autowired
    private RecipientsMapper recipientsMapper;

    @Autowired
    private  DistrictMapper districtMapper;

    @Autowired
    private SealVerificationMapper sealVerificationMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private SmsSendService smsSendService;

    @Value("${sms.template.insertUser}")
    private int userCode;

    //相似度参数
    @Value("${face.similarity}")
    private float similarity;

    @Resource(name = "localStoreFileService")
    private FileStoreService localStoreFileService;

    @Resource(name = "fastDFSStoreServiceImpl")
    private FileStoreService fastDFSStoreServiceImpl;


    @Value("${sms.template.express}")
    private int express;

    @Value("${sms.template.getseal}")
    private int getseal;

    @Value("${sms.template.redo}")
    private int redo;


    @Override
    public UseDepartment isrecord(String useDepartmentCode) {
        return useDepartmentDao.selectByCode(useDepartmentCode);
    }

    public    String getDistrictName(String districtId){
        String districtIds[] = StringUtil.DistrictUtil(districtId);
        String   districtName = null;
        if(districtIds[1].equals("00")&&districtIds[2].equals("00")){
            districtId = districtIds[0]+"0101";
            districtName =districtMapper.selectByDistrictId(districtId).getProvinceName();
        }else if(!districtIds[1].equals("00")&&districtIds[2].equals("00")){
            districtId = districtIds[0]+districtIds[1]+"01";
            District district = districtMapper.selectByDistrictId(districtId);
            districtName =district.getProvinceName()+"/"+district.getCityName();
        }else {
            District district = districtMapper.selectByDistrictId(districtId);
            districtName = district.getProvinceName()+"/"+district.getCityName()+"/"+district.getDistrictName();
        }
        return districtName;
    }
    /**
     * 6位简单密码
     *
     * @return
     */
    public static String createRandomCode(String districtId) {
        //印章编码
        String vcode = "";
        for (int i = 0; i < 8; i++) {
            vcode = vcode + (int) (Math.random() * 9);
        }
        String sealVcode = districtId + vcode;
        return sealVcode;
    }


    @Override
    public int insert(Seal seal) {
        //增加文件上传，操作记录和印章本身表
        return 1;
    }


    /**
     * 印章申请
     *
     * @param seals
     * @param user
     * @param districtId
     * @param agentTelphone
     * @param agentName
     * @param certificateNo
     * @param certificateType
     * @param agentPhotoId
     * @param idcardFrontId
     * @param idcardReverseId
     * @param proxyId
     * @param idCardPhotoId
     * @param confidence
     * @param fieldPhotoId
     * @return
     */
    @Override
    public int sealRecord(List<Seal> seals, User user, String useDepartmentCode, String districtId, String agentTelphone,
                          String agentName, String certificateNo, String certificateType,
                          String agentPhotoId, String idcardFrontId, String idcardReverseId, String proxyId, String idCardPhotoId, int confidence,
                          String fieldPhotoId, String entryType, String captcha) {
        try {
            String checkFace = UUIDUtil.generate();
            SMSCode smsCode = new SMSCode();
            smsCode.setPhone(agentTelphone);
            smsCode.setSmscode(captcha);
            JsonObjectBO jsonObjectBO = userLoginService.checkPhone(smsCode);
            //这里返回是code、要返回到JsonObjectBO
            if (jsonObjectBO.getCode() != 1) {
                return ResultUtil.isCodeError;
            }
            FaceCompareRecord faceCompareRecord = null;
            FaceCompareRecord TrustedIdentityAuthenticationResult = null;
            List<Seal> list = sealDao.selectByCodeAndType(useDepartmentCode,"05");
            List<Seal> list1 = sealDao.selectByCodeAndType(useDepartmentCode,"01");
            UseDepartment useDepartment = useDepartmentDao.selectByCode(useDepartmentCode);  //根据usedepartment查询对应的使用公司
            if (useDepartment == null) {
                return ResultUtil.isNoDepartment;
            }
            String phone = user.getTelphone();
            Employee employee = employeeService.selectByPhone(phone); //获取从业人员
            if (employee == null) {
                return ResultUtil.isNoEmployee;
            }
            MakeDepartmentSimple makedepartment = makeDepartmentService.selectByDepartmentCode(employee.getEmployeeDepartmentCode()); //获取制作单位
            RecordDepartment recordDepartment = recordDepartmentMapper.selectBydistrict(districtId);//获取备案单位
            if (makedepartment == null || recordDepartment == null) {
                return ResultUtil.isFail;
            }

            for (Seal seal : seals) {
                if (seal.getSealTypeCode().equals("05") && !seal.getSealReason().equals("03")) {
                    if (list.size() != 0) {
                        return ResultUtil.isHaveSeal;    //该公司的法务印章或者单位章已经存在
                    }

                } else if (seal.getSealTypeCode().equals("01") && !seal.getSealReason().equals("03")) {
                    if (list1.size() != 0) {
                        return ResultUtil.isHaveSeal;    //该公司的法务印章或者单位章已经存在
                    }
                }

            }


            //循环加入seal
            for (Seal seal : seals) {
//                Seal seal1 = new Seal();
//                String sealcode = SealSerialNum(districtId);
                String sealcode1 = sealCodeService.getMaxSealCode(districtId);
                System.out.println(sealcode1);
                String serial = "00000000";
                String sealcode = districtId + serial.substring(0, serial.length() - sealcode1.length()) + sealcode1;

                seal.setSealCode(sealcode);


                //印章信息

                String sealId = UUIDUtil.generate();
                seal.setId(sealId);
                seal.setSealName(useDepartment.getName());
                seal.setUseDepartmentCode(useDepartmentCode);
                seal.setUseDepartmentName(useDepartment.getName());
                seal.setSealStatusCode("03");
                seal.setIsRecord(true);
                seal.setRecordDate(DateUtil.getCurrentTime());
                seal.setIsCancel(false);
                seal.setCancelDate(DateUtil.getCurrentTime());
                seal.setIsMake(false);
                seal.setIsDeliver(false);
                seal.setIsLoss(false);
                seal.setIsPersonal(false);
                seal.setIsLogout(false);
                seal.setDistrictId(useDepartment.getDistrictId());
                seal.setMakeDepartmentCode(makedepartment.getDepartmentCode());
                seal.setMakeDepartmentName(makedepartment.getDepartmentName());
                seal.setRecordDepartmentCode(recordDepartment.getDepartmentCode());
                seal.setRecordDepartmentName(recordDepartment.getDepartmentName());
                seal.setIsRecord(true);
                seal.setRecordDate(DateUtil.getCurrentTime());
                seal.setIsUndertake(true);
                seal.setUndertakeDate(DateUtil.getCurrentTime());
                if (seal.getSealReason().equals("03")) {
                    String logoutId = UUIDUtil.generate();
                    if (seal.getSealTypeCode().equals("01")) {
                        Seal seal1 = sealDao.selectByTypeAndUseDepartmentCode(useDepartmentCode, null, "01");
                        if (seal1 != null) {
                            seal1.setLogoutPersonId(logoutId);
                            seal.setSealStatusCode("03");
                            logoutAbout(seal1, employee);
                            int logoutSeal = sealDao.logoutSeal(useDepartmentCode, "01");
                            //经办人信息
                            SealAgent sealAgent = new SealAgent();
                            sealAgent.setId(logoutId);
                            sealAgent.setName(agentName);
                            sealAgent.setTelphone(agentTelphone);
                            sealAgent.setAgentPhotoId(agentPhotoId);
                            sealAgent.setBusinessType("03");
                            sealAgent.setCertificateNo(certificateNo);
                            sealAgent.setCertificateType(certificateType);
                            sealAgent.setIdCardFrontId(idcardFrontId);
                            sealAgent.setEntryType(entryType);
                            sealAgent.setIdCardReverseId(idcardReverseId);
//                            sealAgent.setLoginTelPhone(agentTelphone);
                            if (entryType.equals("00")) {
                                sealAgent.setFaceCompareRecordId(checkFace);
                            }
                            sealAgent.setProxyId(proxyId);
                            sealAgent.setEntryType(entryType);
                            int sealAgentInsert = sealAgentMapper.insert(sealAgent);

                        }
                    } else if (seal.getSealTypeCode().equals("05")) {
                        Seal seal1 = sealDao.selectByTypeAndUseDepartmentCode(useDepartmentCode, null, "05");
                        if (seal1 != null) {
                            seal.setSealStatusCode("03");
                            logoutAbout(seal1, employee);
                            int logoutSeal = sealDao.logoutSeal(useDepartmentCode, "05");
                            //经办人信息
                            SealAgent sealAgent = new SealAgent();
                            sealAgent.setId(logoutId);
                            sealAgent.setName(agentName);
                            sealAgent.setTelphone(agentTelphone);
                            sealAgent.setAgentPhotoId(agentPhotoId);
                            sealAgent.setBusinessType("03");
                            sealAgent.setCertificateNo(certificateNo);
                            sealAgent.setCertificateType(certificateType);
                            sealAgent.setIdCardFrontId(idcardFrontId);
                            sealAgent.setEntryType(entryType);
                            sealAgent.setIdCardReverseId(idcardReverseId);
//                            sealAgent.setLoginTelPhone(agentTelphone);
                            if (entryType.equals("00")) {
                                sealAgent.setFaceCompareRecordId(checkFace);
                            }
                            sealAgent.setProxyId(proxyId);
                            sealAgent.setEntryType(entryType);
                            int sealAgentInsert = sealAgentMapper.insert(sealAgent);
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

                //操作记录
                String type1 = "00";
                for (int i = 0; i < 2; i++) {
                    SealOperationRecord sealOperationRecord = new SealOperationRecord();
                    sealOperationRecord.setId(UUIDUtil.generate());
                    sealOperationRecord.setSealId(sealId);
                    sealOperationRecord.setEmployeeId(employee.getEmployeeId());
                    sealOperationRecord.setEmployeeName(employee.getEmployeeName());
                    sealOperationRecord.setEmployeeCode(employee.getEmployeeCode());
                    sealOperationRecord.setOperateType(type1);
                    sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
                    int sealOperationRecordInsert = sealOperationRecordMapper.insertSelective(sealOperationRecord); //保存操作记录
                    type1 = type1.substring(0, 1) + "1";
                    if (sealOperationRecordInsert < 0) {
                        return ResultUtil.isFail;
                    }
                }




                if (!isLegalPerson(certificateNo, agentName, useDepartment.getCode())) { //判断不是法人
                    if (proxyId == null) {   //不是法人而且没有授权委托书
                        return ResultUtil.isNoProxy;
                    }
                }

                if (entryType.equals("00")) {
                    //认证合一记录
                    faceCompareRecord = faceCompareRecordResult(certificateNo, idCardPhotoId, confidence, fieldPhotoId, agentName);
                    if (faceCompareRecord == null) {
                        return ResultUtil.faceCompare;
                    }
                } else {
                    TrustedIdentityAuthenticationResult = TrustedIdentityAuthenticationResult(certificateNo, fieldPhotoId, agentName);
                    if (TrustedIdentityAuthenticationResult == null) {
                        return ResultUtil.faceCompare;
                    }
                }
                //经办人信息
                SealAgent sealAgent = new SealAgent();
                String saId = UUIDUtil.generate();
                sealAgent.setId(saId);
                sealAgent.setName(agentName);
                sealAgent.setTelphone(agentTelphone);
                sealAgent.setAgentPhotoId(agentPhotoId);
                sealAgent.setBusinessType("00");
                sealAgent.setCertificateNo(certificateNo);
                sealAgent.setCertificateType(certificateType);
                sealAgent.setIdCardFrontId(idcardFrontId);
                sealAgent.setEntryType(entryType);
                sealAgent.setIdCardReverseId(idcardReverseId);
//                sealAgent.setLoginTelPhone(agentTelphone);
                if (entryType.equals("00")) {
                    sealAgent.setFaceCompareRecordId(checkFace);
                }
                sealAgent.setProxyId(proxyId);
                sealAgent.setEntryType(entryType);
                int sealAgentInsert = sealAgentMapper.insert(sealAgent);


                seal.setAgentId(saId);
                seal.setIsUndertake(true);
                seal.setUndertakeDate(DateUtil.getCurrentTime());
                int sealInsert = sealDao.insertSelective(seal);
                SealPayOrder sealPayOrder = new SealPayOrder();

                sealPayOrder.setId(UUIDUtil.generate());
                sealPayOrder.setSealId(sealId);
                sealPayOrder.setPayDate(DateUtil.getCurrentTime());
                sealPayOrder.setPayWay("到店支付");
                sealPayOrder.setRefundStatus("-1");
                sealPayOrder.setPayAccout("价格到店商议");
                sealPayOrder.setExpressWay(false);
                sealPayOrder.setIsPay(true);
                orderService.insertOrder(sealPayOrder);


                //当增加经办人，操作信息和印章信息成功后，生成印模信息 存入数据库
                if (sealInsert > 0 && sealAgentInsert > 0) {
                    String sealType = chooseType(seal.getSealTypeCode());
                    Map<String, String> map = new HashMap<>();
                    map.put("useDepartment", seal.getUseDepartmentName());
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
                    FileInfo fileInfo = fileService.save(imageData, DateUtil.getCurrentTime() + seal.getUseDepartmentName() + sealType, "bmp", "", FileService.CREATE_TYPE_UPLOAD, user.getId(), user.getUserName());
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
                    FileInfo microfileInfo = fileService.save(microimageData, DateUtil.getCurrentTime() + seal.getUseDepartmentName() + sealType, "png", "", FileService.CREATE_TYPE_UPLOAD, user.getId(), user.getUserName());
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

//
//                   ImageGenerate imageGenerate = new ImageGenerate();

                    //二维数据
                    int[][] imgArr = imageGenerate.moulageData(map);
                    String imagArrLocalPath = FileUtil.saveArrayFile(imgArr);
                    File file1 = new File(imagArrLocalPath);
                    // InputStream inputStream1 = new FileInputStream(file1);
                    byte[] moulageDates = FileUtil.readInputStream(inputStream1);
                    FileInfo fileInfo1 = fileService.save(moulageDates, DateUtil.getCurrentTime() + seal.getUseDepartmentName() + sealType + "二维数据", "txt", "", FileService.CREATE_TYPE_UPLOAD, user.getId(), user.getUserName());
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
                } else {
                    return ResultUtil.isError;
                }
            }
            return ResultUtil.isSuccess;
        } catch (Exception e) {
            return ResultUtil.isException;
        }


    }


    /**
     * 印章主页面
     * 根据制作单位
     * @param useDepartmentName
     * @param useDepartmentCode
     * @param status
     * @return
     */
    @Override
    public PageInfo<Seal> sealInfo(User user, String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize,String sealType,String recordDepartmentName,String sealCode) {

        try {
            Seal seal = new Seal();
            seal.setSealTypeCode(sealType);
            seal.setRecordDepartmentName(recordDepartmentName);
            seal.setSealCode(sealCode);
            seal.setUseDepartmentCode(useDepartmentCode);
            seal.setUseDepartmentName(useDepartmentName);
            if (user.getRoleId().equals("BADW")) {
                String userName = user.getUserName();
                String telphone = userName.substring(4, 15);
                RecordDepartment recordDepartment = recordDepartmentService.selectByPhone(telphone);
                seal.setRecordDepartmentCode(recordDepartment.getDepartmentCode());

            } else {
                String telphone = user.getTelphone();
                if (telphone == null) {
                    return new PageInfo<>();
                }

                Employee employee = employeeService.selectByPhone(telphone);
                if (employee == null) {
                    return new PageInfo<>();
                }
                seal.setMakeDepartmentCode(employee.getEmployeeDepartmentCode());
            }
            List<Seal> list = new ArrayList<Seal>();
            PageHelper.startPage(pageNum, pageSize);

            list = chooseSealStatus(seal, status);

            PageInfo<Seal> result = new PageInfo<>(list);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new PageInfo<>();
        }
    }


    /**
     * 印模上传
     * @param user
     * @param id
     * @param sealedCardId
     * @param imageDataId
     * @return
     */
    @Override
    public int sealUpload(User user, String id, String sealedCardId, String imageDataId) {
        Seal seal1 = sealDao.selectByPrimaryKey(id);
        if(!seal1.getIsUndertake()){
            return ResultUtil.isFail;
        }
        seal1.setSealStatusCode("01");
        String operateType = "01";
        String sealCode = seal1.getSealCode();
        SealOperationRecord sealOperationRecord1 = sealDao.SelectByCodeAndFlag03(sealCode, operateType);  //查找印模上传的
        if (sealOperationRecord1 != null) {
            return ResultUtil.isFail;
        }
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);

        String useDepartmentCode = seal1.getUseDepartmentCode();
        UseDepartment useDepartment = useDepartmentService.selectByCode(useDepartmentCode);
        String useDepartmentTel = useDepartment.getLegalTelphone();
        String useDepartmentName = useDepartment.getLegalName();
        String name = useDepartment.getName();

        SealOperationRecord sealOperationRecord = new SealOperationRecord();
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setSealId(id);
        sealOperationRecord.setEmployeeCode(employee.getEmployeeCode());
        sealOperationRecord.setEmployeeId(employee.getEmployeeId());   //从业人员登记
        sealOperationRecord.setEmployeeName(employee.getEmployeeName());
        sealOperationRecord.setOperateType("02");
        sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
        int insertSealOperationRecord1 = sealOperationRecordMapper.insertSelective(sealOperationRecord);


        List<SealMaterial> sealMaterialLists = new ArrayList<>();  //批量存入数据库
        SealMaterial sealMaterial = new SealMaterial();
        sealMaterial.setId(UUIDUtil.generate());
        sealMaterial.setSealCode(sealCode);
        sealMaterial.setType("02");  //02为印鉴留存卡
        sealMaterial.setFilePath(sealedCardId);
        SealMaterial sealMaterial1 = new SealMaterial();
        sealMaterial1.setId(UUIDUtil.generate());
        sealMaterial1.setSealCode(sealCode);
        sealMaterial1.setType("03");  //03为印章图像数据
        sealMaterial1.setFilePath(imageDataId);
        sealMaterialLists.add(sealMaterial);
        sealMaterialLists.add(sealMaterial1);
        int insertSealMaterial = sealDao.insertSealMateriallist(sealMaterialLists);




        seal1.setIsMake(true);
        seal1.setMakeDate(DateUtil.getCurrentTime());

//        seal1.setDistrictId(seal1.getDistrictId());
        int updateByPrimaryKey1 = sealDao.updateByPrimaryKeySelective(seal1);
        if (insertSealOperationRecord1 < 0 || insertSealMaterial < 0 || updateByPrimaryKey1 < 0) {
            return ResultUtil.isFail;
        } else {
            //sealPayOrderMapper
            if (sealPayOrderMapper.selectBySealId(id).getExpressWay() == false ) {
                //查找经办人
                SealAgent sealAgent = sealAgentMapper.selectByPrimaryKey(seal1.getAgentId());
                ArrayList<String> params = new ArrayList<String>();
                params.add(name);
                params.add(ResultUtil.sealType(seal1.getSealTypeCode()));
                Boolean b = smsSendService.sendSingleMsgByTemplate(sealAgent.getTelphone(), getseal, params);

            } else {

            }
            return ResultUtil.isSuccess;
        }

    }

    /**
     * 印章个人化
     *
     * @param
     * @return
     */
    @Override
    public int sealPersonal(String id, User user) {
        Seal seal1 = sealDao.selectByPrimaryKey(id);
        if (seal1.getIsChipseal()) {
            seal1.setSealStatusCode("02");
            if (seal1.getIsLogout()) {
                return ResultUtil.isFail;
            }
            if (!seal1.getIsMake()) {
                return ResultUtil.isFail;
            }
            String operateType = "02";
            String sealCode = seal1.getSealCode();
            //根据用户找到对应的从业人员
            String telphone = user.getTelphone();
            Employee employee = employeeService.selectByPhone(telphone);
            SealOperationRecord sealOperationRecord1 = sealDao.SelectByCodeAndFlag03(id, operateType);
            if (sealOperationRecord1 != null) {
                return ResultUtil.isFail;
            } else {
                SealOperationRecord sealOperationRecord = new SealOperationRecord();
                sealOperationRecord.setId(UUIDUtil.generate());
                sealOperationRecord.setSealId(id);
                sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
                sealOperationRecord.setEmployeeId(employee.getEmployeeId());   //从业人员登记
                sealOperationRecord.setEmployeeName(employee.getEmployeeName());
                sealOperationRecord.setEmployeeCode(employee.getEmployeeCode());
                sealOperationRecord.setOperateType("03");
                int insertSealOperationRecord1 = sealOperationRecordMapper.insertSelective(sealOperationRecord);
                seal1.setIsPersonal(true);
                seal1.setPersonalDate(DateUtil.getCurrentTime());
                int updateByPrimaryKey1 = sealDao.updateByPrimaryKey(seal1);
                if (insertSealOperationRecord1 < 0 || updateByPrimaryKey1 < 0) {
                    return ResultUtil.isFail;
                } else {
                    SyncEntity syncEntity = ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealOperationRecord, SyncDataType.SEAL, SyncOperateType.PERSONAL);
                    SyncEntity syncEntity1 = ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(seal1, SyncDataType.SEAL, SyncOperateType.PERSONAL);
                    return ResultUtil.isSuccess;
                }
            }
        } else {
            return ResultUtil.isNoChipSeal;
        }
    }



    /**
     * 线下交付
     * @param user
     * @param
     * @param
     * @return
     */
    @Override
    public int deliver(User user, String id, String useDepartmentCode, String proxyId, String name,
                       String certificateType, String certificateNo, String agentTelphone, String agentPhotoId, String idcardFrontId, String idcardReverseId,
                       String entryType, int confidence, String fieldPhotoId, String idCardPhotoId) {
        Seal seal1 = sealDao.selectByPrimaryKey(id);

        FaceCompareRecord faceCompareRecordResult = null;
        FaceCompareRecord TrustedIdentityAuthenticationResult = null;

        if (seal1.getIsLogout()) {
            return ResultUtil.isLogout;
        }
        String sealCode = seal1.getSealCode();
        if (seal1.getIsLoss()) {
            return ResultUtil.isLoss;
        }

        UseDepartment useDepartment = useDepartmentDao.selectByCode(seal1.getUseDepartmentCode());  //根据usedepartment查询对应的使用公司
        if (useDepartment == null) {
            return ResultUtil.isNoDepartment;
        }
//        SealVerification sealVerification = sealVerificationMapper.selectBySealId(id);
//        if(!sealVerification.getVerifyTypeName().equals("1")){
//            return ResultUtil.isNoSealVerification;
//        }
        //印章操作
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        SealOperationRecord sealOperationRecord = new SealOperationRecord();
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setSealId(id);
        sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
        sealOperationRecord.setEmployeeId(employee.getEmployeeId());   //从业人员登记
        sealOperationRecord.setEmployeeName(employee.getEmployeeName());
        sealOperationRecord.setEmployeeCode(employee.getEmployeeCode());
        sealOperationRecord.setOperateType("04");
        int insertSealOperationRecord = sealOperationRecordMapper.insertSelective(sealOperationRecord);

        seal1.setIsDeliver(true);
        seal1.setDeliverDate(DateUtil.getCurrentTime());
        seal1.setIsEarlywarning(true);
        seal1.setEarlywarningDate(DateUtil.getCurrentTime());

        orderService.updateEvaluationStatus(id,true);  //订单更新 评价状态
        SealPayOrder sealOrder = orderService.selectBySealId(id);
        orderService.updatePayStatus(sealOrder.getPayWay(),sealOrder.getId(),sealOrder.getPayJsOrderId());  //订单更新支付状态
        RecordDepartment recordDepartment = recordDepartmentService.selectByCode(seal1.getRecordDepartmentCode());
        MakeDepartmentSimple makeDepartmentSimple = makeDepartmentService.selectByDepartmentCode(seal1.getMakeDepartmentCode());
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
        for (Employee employeeId : employees) {
            User user2 = userService.findByUserName("CYRY" + employeeId.getTelphone());
            if (user2 != null) {
                userId.add(user2.getId());
            }
        }
        notify.setNotifyUser(userId);
        notify.setNotifyContent("您的" + seal1.getSealName() + "编号" + seal1.getSealCode() + "的印章请尽快备案");
        int notifyResult = notifyService.insertNotify(notify, user1);
        if (notifyResult == ResultUtil.isFail) {
            return ResultUtil.isFail;
        }

//
        SealAgent sealAgent = new SealAgent();
        String saId = UUIDUtil.generate();
        if (!isLegalPerson(certificateNo, name, useDepartment.getCode())) { //判断不是法人
            if (proxyId == null) {   //不是法人而且没有授权委托书
                return ResultUtil.isNoProxy;
            }
        }

        if (entryType.equals("00")) {
            faceCompareRecordResult = faceCompareRecordResult(certificateNo, idCardPhotoId, confidence, fieldPhotoId, name);
            if (faceCompareRecordResult == null) {
                return ResultUtil.faceCompare;
            }
        } else {
            TrustedIdentityAuthenticationResult = TrustedIdentityAuthenticationResult(certificateNo, fieldPhotoId, name);
            if (TrustedIdentityAuthenticationResult == null) {
                return ResultUtil.faceCompare;
            }
        }
        sealAgent.setId(saId);
        sealAgent.setTelphone(agentTelphone);
        sealAgent.setAgentPhotoId(agentPhotoId);
        sealAgent.setCertificateNo(certificateNo);
        sealAgent.setCertificateType(certificateType);
        sealAgent.setIdCardFrontId(idcardFrontId);
        sealAgent.setIdCardReverseId(idcardReverseId);
        sealAgent.setProxyId(proxyId);
//        sealAgent.setLoginTelPhone(agentTelphone);
        sealAgent.setBusinessType("01");
        sealAgent.setName(name);
        if (entryType.equals("00")) {
            sealAgent.setFaceCompareRecordId(faceCompareRecordResult.getId());
        } else {
            sealAgent.setFaceCompareRecordId(TrustedIdentityAuthenticationResult.getId());
        }
        sealAgent.setEntryType(entryType);
        int sealAgentResult = sealAgentMapper.insert(sealAgent);
        seal1.setSealStatusCode("04");
        seal1.setGetterId(saId);
        int updateByPrimaryKey = sealDao.updateByPrimaryKeySelective(seal1);

        //更新支付状态
        SealPayOrder sealPayOrder = sealPayOrderMapper.selectBySealId(seal1.getId());
        if (sealPayOrder == null){
         return  ResultUtil.isFail;
        }
//        sealPayOrder.setIsPay(true);
//        sealPayOrderMapper.updateByPrimaryKeySelective(sealPayOrder);

        if (insertSealOperationRecord > 0 && updateByPrimaryKey > 0 && sealAgentResult > 0) {
//            SyncEntity syncEntity = ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealAgent, SyncDataType.SEAL, SyncOperateType.PERSONAL);
//            SyncEntity syncEntity1 = ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(seal1, SyncDataType.SEAL, SyncOperateType.PERSONAL);
//            SyncEntity syncEntity2 = ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealOperationRecord, SyncDataType.SEAL, SyncOperateType.PERSONAL);
            return ResultUtil.isSuccess;
        } else {
            return ResultUtil.isFail;
        }

    }

    /**
     *交付后备案
     * @param user   当前操作人（从业人员）
     * @param sealId  印章id
     * @return
     */
    @Override
    public int newsealRecord(User user, String sealId) {
        Seal seal = sealDao.selectByPrimaryKey(sealId);
        seal.setSealStatusCode("09");
        seal.setIsApply(true);
        seal.setApplyDate(DateUtil.getCurrentTime());
        seal.setIsEarlywarning(true);
        seal.setEarlywarningDate(DateUtil.getCurrentTime());
       int result = sealDao.updateByPrimaryKeySelective(seal);
        //印章操作
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        SealOperationRecord sealOperationRecord = new SealOperationRecord();
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setSealId(sealId);
        sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
        sealOperationRecord.setEmployeeId(employee.getEmployeeId());   //从业人员登记
        sealOperationRecord.setEmployeeName(employee.getEmployeeName());
        sealOperationRecord.setEmployeeCode(employee.getEmployeeCode());
        sealOperationRecord.setOperateType("05");
        int insertSealOperationRecord = sealOperationRecordMapper.insertSelective(sealOperationRecord);
        if (result == 1 && insertSealOperationRecord == 1) {
            return ResultUtil.isSuccess;
        } else {
            return ResultUtil.isFail;
        }
    }

    //承接
    @Override
    public int underTake(User user, String sealId) {
        Seal seal = sealDao.selectByPrimaryKey(sealId);

        seal.setIsUndertake(true);
        seal.setUndertakeDate(DateUtil.getCurrentTime());
        seal.setIsRecord(true);
        seal.setIsUpdate(false);
        seal.setUpdateDate(DateUtil.getCurrentTime());
        seal.setRecordDate(DateUtil.getCurrentTime());
        seal.setSealStatusCode("03");
        int result = sealDao.updateByPrimaryKeySelective(seal);
        //印章操作
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        int insertSealOperationRecord3= insertSealOperationRecord(employee,"11",sealId);
        int insertSealOperationRecord1= insertSealOperationRecord(employee,"00",sealId);
        int insertSealOperationRecord2 = insertSealOperationRecord(employee,"001",sealId);
        orderService.updateRefundStatus("-1",sealId);
        if (result == 1 && insertSealOperationRecord1 == ResultUtil.isSuccess &&insertSealOperationRecord2 == ResultUtil.isSuccess) {
            return ResultUtil.isSuccess;
        } else {
            return ResultUtil.isFail;
        }
    }

    public int insertSealOperationRecord(Employee employee,String type,String sealId){
        SealOperationRecord sealOperationRecord = new SealOperationRecord();
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setSealId(sealId);
        sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
        sealOperationRecord.setEmployeeId(employee.getEmployeeId());   //从业人员登记
        sealOperationRecord.setEmployeeName(employee.getEmployeeName());
        sealOperationRecord.setEmployeeCode(employee.getEmployeeCode());
        sealOperationRecord.setOperateType(type);
        int insertSealOperationRecord = sealOperationRecordMapper.insertSelective(sealOperationRecord);
        return insertSealOperationRecord;
    }

    /**
     * 制作单位退回
     * @param sealId
     * @param sealVerification
     * @return
     */
    @Override
    public int makeDepartmentUntread(User user ,String sealId, SealVerification sealVerification) {
        Seal seal = sealDao.selectByPrimaryKey(sealId);
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        int insertSealOperationRecord = insertSealOperationRecord(employee,"10",sealId);
        if(sealVerification.getRejectReason().equals("3")){  //退款
            orderService.updateRefundStatus("1",sealId);
            seal.setIsCancel(true);
            seal.setCancelDate(DateUtil.getCurrentTime());
            seal.setSealStatusCode("10"); //取消章
            sealVerification.setRejectReason("3");

        }else {    //资料问题
            seal.setIsUndertake(false);
            seal.setIsCancel(false);
            seal.setIsUpdate(true);
            seal.setUpdateDate(DateUtil.getCurrentTime());
            seal.setCancelDate(DateUtil.getCurrentTime());
            seal.setSealStatusCode("11");

            sealVerification.setRejectReason("1");
        }
        sealVerification.setIsVerification(true);
        sealVerification.setVerifyTypeName("-1");
        sealVerification.setSealId(sealId);
        sealVerification.setFlag("1");
        sealVerification.setVerificationDate(DateUtil.getCurrentTime());
        int updateSeal = sealDao.updateByPrimaryKeySelective(seal);
        int insertSealVerification = sealVerificationMapper.updateBySealIdAndFlag(sealVerification);
        if (updateSeal > 0 && insertSealVerification > 0&& insertSealOperationRecord>0) {
            return ResultUtil.isSuccess;
        } else {
            return ResultUtil.isFail;
        }

    }

    /**
     * 取消制作印章
     * @param Id
     * @return
     */
    @Override
    public int cancelSeal(String Id,WeChatUser weChatUser) {
        Seal seal = sealDao.selectByPrimaryKey(Id);
        seal.setIsCancel(true);
        seal.setSealStatusCode("10");
        seal.setCancelDate(DateUtil.getCurrentTime());
        int updateSeal = sealDao.updateByPrimaryKeySelective(seal);

        Employee employee = new Employee();
        employee.setEmployeeName(weChatUser.getName());
        employee.setEmployeeId(weChatUser.getCertificateNo());
        employee.setEmployeeCode(weChatUser.getTelphone());
        int insertSealOperationRecord = insertSealOperationRecord(employee,"10",Id);
        if(insertSealOperationRecord>0 && updateSeal>0){
            return ResultUtil.isSuccess;
        }else{
            return ResultUtil.isFail;
        }

    }


    /**
     * 挂失
     */
    @Override
    public int loss(User user, String id, String name, String agentPhotoId, String proxyId, String certificateNo, String certificateType,
                    String localDistrictId, String businesslicenseId, String idcardFrontId, String idcardReverseId, String agentTelphone, String entryType, String idCardPhotoId,
                    int confidence, String fieldPhotoId) {

        FaceCompareRecord faceCompareRecord = null;
        FaceCompareRecord TrustedIdentityAuthenticationResult = null;
        Seal seal1 = sealDao.selectByPrimaryKey(id);
        seal1.setSealStatusCode("05");
        if (seal1.getIsLoss()) {
            return ResultUtil.isFail;
        }
        if (name == null) {
            return ResultUtil.isNoName;
        }
        if (!isLegalPerson(certificateNo, name, seal1.getUseDepartmentCode())) { //判断不是法人
            if (proxyId == null) {   //不是法人而且没有授权委托书
                return ResultUtil.isNoProxy;
            }
        }
        String telphone = user.getTelphone();
        String sealCode = seal1.getSealCode();
        Employee employee = employeeService.selectByPhone(telphone);

        if (entryType.equals("00")) {
            faceCompareRecord = faceCompareRecordResult(certificateNo, idCardPhotoId, confidence, fieldPhotoId, name);
            if (faceCompareRecord == null) {
                return ResultUtil.faceCompare;
            }
        } else {
            TrustedIdentityAuthenticationResult = TrustedIdentityAuthenticationResult(certificateNo, fieldPhotoId, name);
            if (TrustedIdentityAuthenticationResult == null) {
                return ResultUtil.faceCompare;
            }
        }

        String saId = UUIDUtil.generate();
        SealAgent sealAgent = new SealAgent();
        sealAgent.setId(saId);
        sealAgent.setTelphone(agentTelphone);
        sealAgent.setAgentPhotoId(agentPhotoId);
//        sealAgent.setLoginTelPhone(agentTelphone);
        sealAgent.setCertificateNo(certificateNo);
        sealAgent.setCertificateType(certificateType);
        sealAgent.setIdCardFrontId(idcardFrontId);
        sealAgent.setIdCardReverseId(idcardReverseId);
        sealAgent.setProxyId(proxyId);
        sealAgent.setBusinessType("02");
        sealAgent.setEntryType(entryType);
        sealAgent.setName(name);
        if (entryType.equals("00")) {
            sealAgent.setFaceCompareRecordId(faceCompareRecord.getId());
        }
        sealAgent.setEntryType(entryType);
        int sealAgentResult = sealAgentMapper.insert(sealAgent);


        seal1.setIsLoss(true);
        seal1.setLossDate(DateUtil.getCurrentTime());
        seal1.setLossPersonId(saId);
        System.out.println(localDistrictId);
        RecordDepartment recordDepartment = recordDepartmentMapper.selectByDistrictIdVersion(localDistrictId);
        if (recordDepartment == null) {
            return ResultUtil.isFail;
        }
        seal1.setRecordDepartmentCode(recordDepartment.getDepartmentCode());
        seal1.setRecordDepartmentName(recordDepartment.getDepartmentName());
        int updateByPrimaryKey = sealDao.updateByPrimaryKeySelective(seal1);

        SealOperationRecord sealOperationRecord = new SealOperationRecord();
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setOperateType("06");
        sealOperationRecord.setEmployeeCode(employee.getEmployeeCode());
        sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
        sealOperationRecord.setEmployeeName(employee.getEmployeeName());
        sealOperationRecord.setEmployeeId(employee.getEmployeeId());
        sealOperationRecord.setSealId(id);
        int insertSealOperationRecord = sealOperationRecordMapper.insertSelective(sealOperationRecord);

        //营业执照
        SealMaterial sealMaterial = new SealMaterial();
        sealMaterial.setId(UUIDUtil.generate());
        sealMaterial.setType("01");
        sealMaterial.setSealCode(sealCode);
        sealMaterial.setFilePath(businesslicenseId);
        sealDao.insertSealMaterial(sealMaterial);


        if (sealAgentResult > 0 && updateByPrimaryKey > 0 && insertSealOperationRecord > 0 && sealAgentResult > 0) {
            SyncEntity syncEntity1 = ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealAgent, SyncDataType.SEAL, SyncOperateType.LOSS);
            SyncEntity syncEntity2 = ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealOperationRecord, SyncDataType.SEAL, SyncOperateType.LOSS);
            SyncEntity syncEntity3 = ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(seal1, SyncDataType.SEAL, SyncOperateType.LOSS);
            SyncEntity syncEntity4 = ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealMaterial, SyncDataType.SEAL, SyncOperateType.LOSS);
            return ResultUtil.isSuccess;
        } else {
            return ResultUtil.isFail;
        }
    }


    /**
     * 印章注销
     *
     * @param user
     */
    @Override
    public int logout(User user, String id, String name, String agentPhotoId, String proxyId, String certificateNo, String certificateType,
                      String localDistrictId, String businesslicenseId, String idcardFrontId, String idcardReverseId, String agentTelphone, String entryType, String idCardPhotoId,
                      int confidence, String fieldPhotoId) {
        SealAgent sealAgent;
        FaceCompareRecord faceCompareRecord = null;
        FaceCompareRecord TrustedIdentityAuthenticationResult = null;
        Seal seal1 = sealDao.selectByPrimaryKey(id);
        seal1.setSealStatusCode("06");

        if (seal1.getIsLogout()) {
            return ResultUtil.isFail;
        }
        if (name == null) {
            return ResultUtil.isNoName;
        }
        if (!isLegalPerson(certificateNo, name, seal1.getUseDepartmentCode())) { //判断不是法人
            if (proxyId == null) {   //不是法人而且没有授权委托书
                return ResultUtil.isNoProxy;
            }
        }
        String sealCode = seal1.getSealCode();
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);

        if (entryType.equals("00")) {
            faceCompareRecord = faceCompareRecordResult(certificateNo, idCardPhotoId, confidence, fieldPhotoId, name);
            if (faceCompareRecord == null) {
                return ResultUtil.faceCompare;
            }
        } else {
            TrustedIdentityAuthenticationResult = TrustedIdentityAuthenticationResult(certificateNo, fieldPhotoId, name);
            if (TrustedIdentityAuthenticationResult == null) {
                return ResultUtil.faceCompare;
            }
        }

        String saId = UUIDUtil.generate();
        sealAgent = new SealAgent();
        sealAgent.setId(saId);
        sealAgent.setTelphone(agentTelphone);
//        sealAgent.setLoginTelPhone(agentTelphone);
        sealAgent.setAgentPhotoId(agentPhotoId);
        sealAgent.setCertificateNo(certificateNo);
        sealAgent.setCertificateType(certificateType);
        sealAgent.setIdCardFrontId(idcardFrontId);
        sealAgent.setIdCardReverseId(idcardReverseId);
        sealAgent.setProxyId(proxyId);
        sealAgent.setBusinessType("03");
        sealAgent.setEntryType(entryType);
        sealAgent.setName(name);

        int sealAgentResult = sealAgentMapper.insert(sealAgent);
        //营业执照
        SealMaterial sealMaterial = new SealMaterial();
        sealMaterial.setId(UUIDUtil.generate());
        sealMaterial.setType("07");//注销的营业执照
        sealMaterial.setSealCode(sealCode);
        sealMaterial.setFilePath(businesslicenseId);
        sealDao.insertSealMaterial(sealMaterial);

        seal1.setIsLogout(true);
        seal1.setLogoutDate(DateUtil.getCurrentTime());
        seal1.setLogoutPersonId(saId);


        int updateByPrimaryKey = sealDao.updateByPrimaryKeySelective(seal1);
        SealOperationRecord sealOperationRecord = new SealOperationRecord();
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setOperateType("07");
        sealOperationRecord.setEmployeeCode(employee.getEmployeeCode());
        sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
        sealOperationRecord.setEmployeeName(employee.getEmployeeName());
        sealOperationRecord.setEmployeeId(employee.getEmployeeId());
        sealOperationRecord.setSealId(id);
        int insertSealOperationRecord = sealOperationRecordMapper.insertSelective(sealOperationRecord);

        if (sealAgentResult > 0 && insertSealOperationRecord > 0 && updateByPrimaryKey > 0 && sealAgentResult > 0) {
            SyncEntity syncEntity2 = ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealAgent, SyncDataType.SEAL, SyncOperateType.LOGOUT);
            SyncEntity syncEntity3 = ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealOperationRecord, SyncDataType.SEAL, SyncOperateType.LOGOUT);
            SyncEntity syncEntity4 = ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(seal1, SyncDataType.SEAL, SyncOperateType.LOGOUT);
            SyncEntity syncEntity = ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealMaterial, SyncDataType.SEAL, SyncOperateType.LOGOUT);
            return ResultUtil.isSuccess;
        } else {
            return ResultUtil.isFail;
        }
    }


    /**
     * 查看详情
     */
    @Override
    public SealVO selectDetailById(String id) {
        Seal seal = sealDao.selectByPrimaryKey(id);
        String useDepartmentCode = seal.getUseDepartmentCode();
        String makeDepartmentCode = seal.getMakeDepartmentCode();
        UseDepartment useDepartment = useDepartmentService.selectByCode(useDepartmentCode);
        Makedepartment makedepartment = makeDepartmentService.selectByCode(makeDepartmentCode);

        List<SealAgent> sealAgents = new ArrayList<>();
        SealVO sealVo = new SealVO();
        sealVo.setSeal(seal);
        String sealCode = seal.getSealCode();
        String agentId = seal.getAgentId();
        SealAgent sealAgent = sealDao.selectSealAgentById(agentId);
        sealAgents.add(sealAgent);
        if(seal.getIsDeliver()){    //交付经办人
            SealAgent sealAgent1 = sealAgentMapper.selectSealAgentByGetterId(seal.getGetterId());
            if(sealAgent1!=null) {
                sealAgents.add(sealAgent1);
            }
            if (!seal.getIsLogout() && seal.getIsLoss()) {  //只有挂失但是没有注销
                String lossId = seal.getLossPersonId();
                SealAgent sealAgent2 = sealAgentMapper.selectByPrimaryKey(lossId);  //挂失经办人
                if(sealAgent2!=null) {
                    sealAgents.add(sealAgent2);
                }
            }else if(seal.getIsLogout()){
                SealAgent sealAgent2 = sealAgentMapper.selectSealAgentByGetterId(seal.getLossPersonId());//挂失经办人
                SealAgent sealAgent3 = sealAgentMapper.selectByPrimaryKey(seal.getLogoutPersonId());//注销经办人
                System.out.println(sealAgent3.getId());
                if(sealAgent2!=null) {
                    sealAgents.add(sealAgent2);
                }
                if(sealAgent3!=null) {
                    sealAgents.add(sealAgent3);
                }
            }
        }else if(!seal.getIsDeliver() && seal.getIsLogout()&& !seal.getIsLoss()){  //遗失补课
            SealAgent sealAgent2 = sealAgentMapper.selectSealAgentByGetterId(seal.getLossPersonId());//挂失经办人
            SealAgent sealAgent3 = sealAgentMapper.selectByPrimaryKey(seal.getLogoutPersonId());//注销经办人
            System.out.println(sealAgent3.getId());
            if(sealAgent2!=null) {
                sealAgents.add(sealAgent2);
            }
            if(sealAgent3!=null) {
                sealAgents.add(sealAgent3);
            }
        }

        sealVo.setSealAgents(sealAgents);
        sealVo.setOperationPhoto(sealAgent.getAgentPhotoId());
        sealVo.setPositiveIdCardScanner(sealAgent.getIdCardFrontId());
        sealVo.setReverseIdCardScanner(sealAgent.getIdCardReverseId());
        sealVo.setProxy(sealAgent.getProxyId());
        sealVo.setMakeDepartment(makedepartment);
        sealVo.setUseDepartment(useDepartment);
        sealVo.setSealOperationRecords(sealOperationRecordMapper.selectSealOperationRecord(id,null));
//        SealOperationRecord sealOperationRecord = sealDao.selectOperationRecordByCode(id);   //操作记录
//        SealMaterial sealMaterial = sealDao.selectSealMaterial(sealCode,"04");
        SealMaterial microsealMaterial = sealDao.selectSealMaterial(sealCode, "06");
        if(seal.getIsCancel()){
            SealVerification sealVerification = sealVerificationMapper.selectBySealIdAndReason(id,"3");
            sealVo.setSealVerifications(sealVerification);
        }
        if (microsealMaterial == null) {
            sealVo.setMicromoulageImageId("");
        } else {
            sealVo.setMicromoulageImageId(microsealMaterial.getFilePath());
        }
        SealMaterial lossBusinessLicense = sealDao.selectSealMaterial(sealCode,"01");
        if(lossBusinessLicense!=null){
            sealVo.setLossBusinessLicense(lossBusinessLicense.getFilePath());
        }
        SealMaterial LogoutBussinessLicense = sealDao.selectSealMaterial(sealCode,"07");
        if(LogoutBussinessLicense!=null){
            sealVo.setLogoutBussinessLicense(LogoutBussinessLicense.getFilePath());
        }
        SealMaterial sealCardImage = sealDao.selectSealMaterial(sealCode,"02");
        if(sealCardImage!=null){
            sealVo.setSealCardImage(sealCardImage.getFilePath());
        }
        SealMaterial sealCard = sealDao.selectSealMaterial(sealCode,"03");
        if(sealCard!=null){
            sealVo.setSealCard(sealCard.getFilePath());
        }
//        sealVo.setSealOperationRecord(sealOperationRecord);
        return sealVo;
    }


    /**
     * 印章查询
     *
     * @param useDepartmentName
     * @param useDepartmentCode
     * @param status
     * @return
     */
    @Override
    public PageInfo<Seal> seal(User user, String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize) {


        String id = user.getDistrictId();
        String districtIds[] = StringUtil.DistrictUtil(id);
        String districtId = null;
        if (districtIds[1].equals("00") && districtIds[2].equals("00")) {
            districtId = districtIds[0];
        } else if (!districtIds[1].equals("00") && districtIds[2].equals("00")) {
            districtId = districtIds[0] + districtIds[1];
        } else {
            districtId = id;
        }


        Seal seal = new Seal();
        seal.setUseDepartmentCode(useDepartmentCode);
        seal.setUseDepartmentName(useDepartmentName);
        if(user.getRoleId().equals("ADMIN")) {
            seal.setDistrictId(districtId);
        }
        if(user.getRoleId().equals("ZZDW")){
            String name = user.getRealName();
            String code = makeDepartmentService.makeDepartmentCode(name);
            seal.setMakeDepartmentCode(code);
        }
        if(user.getRoleId().equals("BADW")){
            String telphone = user.getTelphone();
            RecordDepartment recordDepartment = recordDepartmentService.selectByPhone(telphone);
            String code = recordDepartment.getDepartmentCode();
            String BadwDistrictId = recordDepartment.getDepartmentAddress();
            seal.setDistrictId(BadwDistrictId);
            seal.setRecordDepartmentCode(code);
            List<Seal> list = new ArrayList<Seal>();
            PageHelper.startPage(pageNum, pageSize);
            list = selectSealByBADW(seal, status);
            PageInfo<Seal> result = new PageInfo<>(list);
            return result;
        }
        if(user.getRoleId().equals("CYRY")){
            String telphone = user.getTelphone();
            if (telphone == null) {
                return new PageInfo<>();
            }

            Employee employee = employeeService.selectByPhone(telphone);
            seal.setMakeDepartmentCode(employee.getEmployeeDepartmentCode());
        }
        List<Seal> list = new ArrayList<Seal>();
        PageHelper.startPage(pageNum, pageSize);
        list = chooseSealStatus(seal, status);
        PageInfo<Seal> result = new PageInfo<>(list);
        return result;
    }


    /**
     *
     * 制作单位查找seal
     * @param user
     * @param useDepartmentName
     * @param useDepartmentCode
     * @param status
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<Seal> mdSeal(User user, String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize) {


        String name = user.getRealName();
        String code = makeDepartmentService.makeDepartmentCode(name);


        Seal seal = new Seal();
        seal.setMakeDepartmentCode(code);
        List<Seal> list = new ArrayList<Seal>();
        PageHelper.startPage(pageNum, pageSize);
        list = chooseSealStatus(seal, status);
        PageInfo<Seal> result = new PageInfo<>(list);
        return result;
    }

    //印章核验
    @Override
    public int verifySeal(User user,String id, String rejectReason, String rejectRemark, String verify_type_name) {
        Seal seal = sealDao.selectByPrimaryKey(id);
        String makeDepartmentCode = seal.getMakeDepartmentCode();
        MakeDepartmentSimple makeDepartmentSimple = makeDepartmentService.selectByDepartmentCode(makeDepartmentCode);
        SealVerification sealVerification = sealVerificationMapper.selectBySealIdAndFlag        (id,"2");
        String telphone = user.getTelphone();
        RecordDepartment recordDepartment = recordDepartmentService.selectByPhone(telphone);
//        Employee employee = employeeService.selectByPhone(telphone);

        sealVerification.setRejectReason(rejectReason);
        sealVerification.setRejectRemark(rejectRemark);
        sealVerification.setVerifyTypeName(verify_type_name);
        sealVerification.setVerificationDate(DateUtil.getCurrentTime());
        sealVerification.setFlag("2");
        int updateVerifySeal = sealVerificationMapper.updateByPrimaryKeySelective(sealVerification);

        SealOperationRecord sealOperationRecord = new SealOperationRecord();
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setOperateType("06");  //核验的操作人
        sealOperationRecord.setEmployeeCode(recordDepartment.getDepartmentCode());
        sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
        sealOperationRecord.setEmployeeName(recordDepartment.getDepartmentName());
        sealOperationRecord.setEmployeeId(recordDepartment.getCertificateNo());
        sealOperationRecord.setSealId(id);
        int insertSealOperationRecord = sealOperationRecordMapper.insertSelective(sealOperationRecord);
        if(updateVerifySeal<0||insertSealOperationRecord<0){
            return ResultUtil.isFail;
        }else{
            if(rejectRemark.equals("2")){ //如果印章问题  直接给制作单位发短信
                String telphone1 = makeDepartmentSimple.getTelphone();
                ArrayList<String> params = new ArrayList<>();
                params.add(makeDepartmentSimple.getLegalName());
                params.add(seal.getUseDepartmentName());
                params.add(chooseType(seal.getSealTypeCode())+seal.getSealCode());
                smsSendService.sendSingleMsgByTemplate(telphone1, redo, params);
            }
            return ResultUtil.isSuccess;
        }

    }

    @Override
    public List<Seal> waitUndertake(String makeDepartmentCode) {
        List<Seal> seals = sealDao.allUndertakeSeal(makeDepartmentCode);
        return seals;
    }

    @Override
    public List<Seal> selectSealByDistrictId(String districtId) {
        String districtId1 = districtId.substring(0, 4);
        List<Seal> seals = sealDao.selectLikeDistrictId1(districtId1);

        return seals;
    }


    @Override
    public PageInfo<Seal> Infoseal(User user, String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize) {
        String districtId = user.getDistrictId().substring(0, 2);


        Seal seal = new Seal();
        seal.setUseDepartmentCode(useDepartmentCode);
        seal.setUseDepartmentName(useDepartmentName);
        seal.setDistrictId(districtId);
        List<Seal> list = new ArrayList<Seal>();
        PageHelper.startPage(pageNum, pageSize);
        list = chooseSealStatus(seal, status);
        PageInfo<Seal> result = new PageInfo<>(list);
        return result;
    }

    /**
     * 图片下载
     *
     * @param id
     * @return
     */
    @Override
    public FileInfoVO download(String id) {
        try {

            Seal seal = sealDao.selectByPrimaryKey(id);
            String sealCode = seal.getSealCode();
            SealMaterial sealMaterial = sealDao.selectSealMaterial(sealCode, "04");
            String moulageImageId = sealMaterial.getFilePath();
            FileInfo fileInfo = fileService.readFile(moulageImageId);
            String path = fileInfo.getFilePath();
            String Url = fastDFSStoreServiceImpl.getFullPath(path);
            URL url = new URL(Url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3 * 1000);
            // conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            InputStream inputStream = conn.getInputStream();
            byte[] fileDate = FileUtil.readInputStream(inputStream);
            FileInfoVO fileInfoVO = new FileInfoVO(fileInfo);
            fileInfoVO.setFileData(fileDate);
            return fileInfoVO;
        } catch (IOException ioe) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 是否是法人
     *
     * @param certificateNo
     * @param name
     * @param useDepartmentCode
     * @return
     */
    @Override
    public boolean isLegalPerson(String certificateNo, String name, String useDepartmentCode) {
        UseDepartment useDepartment = useDepartmentDao.selectByCode(useDepartmentCode);
        if (certificateNo.equals(useDepartment.getLegalId()) && name.equals(useDepartment.getLegalName())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Seal selectLastSeal() {
        Seal seal = sealDao.selectLastSeal();
        return seal;
    }

    /**
     * 人证合一
     *
     * @param idCardPhotoId
     * @param fieldPhotoId
     * @return
     */
    @Override
    //@DeleteTempFile
    public FaceCompareResult faceCompare(String idCardPhotoId, String fieldPhotoId) {
        FileInfo idCardFileInfo = fileService.getFileInfo(idCardPhotoId);
        FileInfo fieldFileInfo = fileService.getFileInfo(fieldPhotoId);
        if (fieldFileInfo == null || idCardFileInfo == null) {
            return null;
        }
        String idCardUrl = fastDFSStoreServiceImpl.getFullPath(idCardFileInfo.getFilePath());
        String fieldUrl = fastDFSStoreServiceImpl.getFullPath(fieldFileInfo.getFilePath());

        String idCardPath = downLoadPicture(idCardUrl);
        String fieldPath = downLoadPicture(fieldUrl);
        if (idCardPath == null || fieldPath == null) {
            return null;
        }
        Integer a = (int) AFR.compareImage(idCardPath, fieldPath);

        FaceCompareResult confidence = new FaceCompareResult();
        confidence.setFieldPhotoId(fieldPhotoId);
        confidence.setIdCardPhotoId(idCardPhotoId);
        confidence.setIdCardPhotoPath(idCardPath);
        confidence.setFieldPhotoPath(fieldPath);
        confidence.setSimilarity(a);

        if (a <= similarity) {
            confidence.setIsPass(false);
        } else {
            confidence.setIsPass(true);
        }
        return confidence;
    }


    /**
     * 比对图片下载，传入文件的url
     *
     * @param fileUrl
     * @return
     */
    public String downLoadPicture(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3 * 1000);
            // conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            InputStream inputStream = conn.getInputStream();
            byte[] fileDate = FileUtil.readInputStream(inputStream);
            String relativePath = localStoreFileService.store(fileDate, "", ".png");
            String absolutePath = localStoreFileService.getFullPath(relativePath);
            return absolutePath;
        } catch (IOException ioe) {
            return "";
        } catch (Exception e) {
            return "";
        }
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

    //用于备案单位印章查询
    public List<Seal> selectSealByBADW(Seal seal, String status) {
        List<Seal> list = new ArrayList<Seal>();
        if (status.equals("03")) {  //已备案
            seal.setIsCancel(false);
            seal.setIsRecord(true);
            seal.setIsDeliver(false);
            seal.setIsMake(false);
            seal.setIsPersonal(false);
            seal.setIsLogout(false);
            seal.setIsLoss(false);
            seal.setIsCancel(false);
        } else if (status.equals("01")) {  //已制作
            seal.setIsCancel(false);
            seal.setIsRecord(true);
            seal.setIsDeliver(false);
            seal.setIsMake(true);
            seal.setIsPersonal(false);
            seal.setIsLogout(false);
            seal.setIsLoss(false);
            seal.setIsCancel(false);
        } else if (status.equals("02")) {  //已个人化
            seal.setIsCancel(false);
            seal.setIsRecord(true);
            seal.setIsDeliver(false);
            seal.setIsMake(true);
            seal.setIsPersonal(true);
            seal.setIsLogout(false);
            seal.setIsLoss(false);
            seal.setIsCancel(false);
        } else if (status.equals("00")) {    //未交付
            seal.setIsCancel(false);
            seal.setIsRecord(true);
            seal.setIsDeliver(false);
            seal.setIsLogout(false);
            seal.setIsLoss(false);
            seal.setIsCancel(false);
        } else if (status.equals("04")) {    //已交付
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setIsDeliver(true);
            seal.setIsCancel(false);
            seal.setIsApply(false);
            seal.setIsLogout(false);
            seal.setIsLoss(false);
            seal.setIsUndertake(true);
        } else if (status.equals("05")) {  //已经挂失
            seal.setIsCancel(false);
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setIsDeliver(true);
            seal.setIsLoss(true);
            seal.setIsCancel(false);
        } else if (status.equals("06")) {   //已注销
            seal.setIsCancel(false);
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setIsDeliver(true);
            seal.setIsLogout(true);
            seal.setIsCancel(false);
        }else if (status.equals("08")) {   //待承接
            seal.setIsCancel(false);
            seal.setIsApply(false);
            seal.setIsDeliver(false);
            seal.setIsRecord(false);
            seal.setIsMake(false);
            seal.setIsUndertake(false);
            seal.setIsCancel(false);
            seal.setIsUpdate(false);
        }else if(status.equals("07")){ //待交付
            list=sealDao.selectWaitdeliveredByBADW(seal);
            return list;
        }else if (status.equals("09")) {   //已备案
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setIsDeliver(true);
            seal.setIsUndertake(true);
            seal.setIsApply(true);
            seal.setIsCancel(false);
            list = sealDao.selectIsApply(seal);
            return list;
        }else if(status.equals("10")){
            seal.setIsCancel(true);
        }else if(status.equals("11")){ //资料更新
            seal.setIsCancel(false);
            seal.setIsRecord(false);
            seal.setIsUpdate(true);
            seal.setIsUndertake(false);
        }
        list = sealDao.selectSealByBADW(seal);
        return list;
    }

    //用于印章管理
    public List<Seal> chooseSealStatus(Seal seal, String status) {
        List<Seal> list = new ArrayList<Seal>();
        if (status.equals("03")) {  //已申请
            list = sealDao.selectIsRecord(seal);
        } else if (status.equals("01")) {  //已制作
            list = sealDao.selectIsMake(seal);
            for (Seal seal1 :list){
                SealPayOrder sealPayOrder =sealPayOrderMapper.selectBySealId(seal1.getId());
                if( sealPayOrder.getCourierId()!=null &&  !sealPayOrder.getCourierId().isEmpty()){
                    //  Recipients recipients =recipientsMapper.selectByPrimaryKey(courierMapper.selectByPrimaryKey(sealPayOrder.getCourierId()).getRecipientsId());

                    Recipients recipients =   recipientsMapper.selectByPrimaryKey(courierMapper.selectBySealId(seal1.getId()).getRecipientsId());
                    DeliveryExpressInfo deliveryExpressInfo = new DeliveryExpressInfo();
                    deliveryExpressInfo.setReceiveName(recipients.getRecipientsName());
                    deliveryExpressInfo.setReceivephone(recipients.getRecipientsTelphone());
                    deliveryExpressInfo.setReceivedistrictId(recipients.getDistrictId());
                    deliveryExpressInfo.setReceiveAddressDetail(recipients.getRecipientsAddress());
                    deliveryExpressInfo.setReceivedistrictName(recipients.getDistrictName());
                    MakeDepartmentSimple makedepartment = makeDepartmentService.selectByDepartmentCode(seal1.getMakeDepartmentCode());
                    if(makedepartment == null){
                        deliveryExpressInfo.setSendName("未找到刻制单位信息");
                    }else {
                        deliveryExpressInfo.setSendName(makedepartment.getDepartmentName());
                        deliveryExpressInfo.setSenddistrictId(makedepartment.getDepartmentAddress());
                        deliveryExpressInfo.setSenddistrictName(getDistrictName(makedepartment.getDepartmentAddress()));
                        deliveryExpressInfo.setSendTelphone(makedepartment.getTelphone());
                        deliveryExpressInfo.setSendAddressDetail(makedepartment.getDepartmentAddressDetail());
                    }
                    //增加经办人信息
                    SealAgent sealAgent = sealAgentMapper.selectByPrimaryKey(seal1.getAgentId());
                    if (sealAgent == null){
                        deliveryExpressInfo.setAgentName("未找到经办人信息");
                    }else {

                        deliveryExpressInfo.setAgentName(sealAgent.getName());
                        deliveryExpressInfo.setAgentidcard(sealAgent.getCertificateNo());
                        deliveryExpressInfo.setAgentphone(sealAgent.getTelphone());
                        deliveryExpressInfo.setAgentidcardFrontImage(sealAgent.getIdCardFrontId());
                        deliveryExpressInfo.setAgentidcardFrontReverseImage(sealAgent.getIdCardReverseId());
                        deliveryExpressInfo.setAgentPhotoImage(sealAgent.getAgentPhotoId());
                        deliveryExpressInfo.setAgentproxyImage(sealAgent.getProxyId());
//                        deliveryExpressInfo.setLoginTelphone(sealAgent.getLoginTelPhone());
                    }

                    seal1.setDeliveryExpressInfo(deliveryExpressInfo);
                }
            }
        } else if (status.equals("02")) {  //已个人化
            list = sealDao.selectPersonal(seal);
        } else if (status.equals("00")) {    //未交付
            list = sealDao.selectUndelivered(seal);
        } else if (status.equals("04")) {    //已交付
            seal.setSealStatusCode("04");
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setIsDeliver(true);
            seal.setIsCancel(false);
            list = sealDao.selectIsDeliver(seal);
        } else if (status.equals("05")) {  //已经挂失
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setIsDeliver(true);
            seal.setIsLoss(true);
            seal.setIsCancel(false);
            list = sealDao.selectIsLoss(seal);
        } else if (status.equals("06")) {   //已注销
            seal.setIsRecord(true);
//            seal.setIsMake(true);
//            seal.setIsDeliver(true);
            seal.setIsLogout(true);
            seal.setIsCancel(false);
            list = sealDao.selectIsLogout(seal);
        }
        else if (status.equals("08")) {   //待承接
            seal.setIsApply(false);
            seal.setIsDeliver(false);
            seal.setIsRecord(false);
            seal.setIsMake(false);
            seal.setIsUndertake(false);
            seal.setIsCancel(false);
            seal.setIsUpdate(false);
            list = sealDao.selectByCodeAndName(seal);
        }else if (status.equals("09")) {   //已备案
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setIsDeliver(true);
            seal.setIsUndertake(true);
            seal.setIsApply(true);
            seal.setIsCancel(false);
            list = sealDao.selectIsApply(seal);
        }else if(status.equals("07")){ //待交付
            list=sealDao.selectWaitDeliver(seal);
        }else if(status.equals("10")){  //已退回
            seal.setIsCancel(true);
            list=sealDao.selectByCodeAndName(seal);
        }else if(status.equals("11")){//资料更新
            seal.setIsCancel(false);
            seal.setIsRecord(false);
            seal.setIsUpdate(true);
            seal.setIsUndertake(false);
            list=sealDao.selectByCodeAndName(seal);
        }
        //后续可以同时归并到selectByCodeAndName（）中，由于时间紧暂未归并
        return list;
    }

    /**
     * 认证合一信息记录
     */
    public FaceCompareRecord faceCompareRecordResult(String certificateNo, String idCardPhotoId, int confidence, String fieldPhotoId, String agentName) {
        FaceCompareRecord faceCompareRecord = new FaceCompareRecord();
        String checkFace = UUIDUtil.generate();
        faceCompareRecord.setId(checkFace);
        faceCompareRecord.setCertificateNo(certificateNo);
        faceCompareRecord.setCertificatePhotoId(idCardPhotoId);
        faceCompareRecord.setConfidence(confidence);
        faceCompareRecord.setFacePhotoId(fieldPhotoId);
        faceCompareRecord.setFiledPhotoId(fieldPhotoId);
        faceCompareRecord.setName(agentName);
        faceCompareRecord.setRecordTime(DateUtil.getCurrentTime());
        int faceCompareRecordInsert = faceCompareRecordMapper.insert(faceCompareRecord);
        if (faceCompareRecordInsert > 0) {
            return faceCompareRecord;
        } else {
            return null;
        }
    }

    /**
     * 可信身份认证
     */
    public FaceCompareRecord TrustedIdentityAuthenticationResult(String certificateNo, String fieldPhotoId, String agentName) {
        FaceCompareRecord TrustedIdentityAuthenticationResult = new FaceCompareRecord();
        String checkFace = UUIDUtil.generate();
        TrustedIdentityAuthenticationResult.setId(checkFace);
        TrustedIdentityAuthenticationResult.setCertificateNo(certificateNo);
        TrustedIdentityAuthenticationResult.setFiledPhotoId(fieldPhotoId);
        TrustedIdentityAuthenticationResult.setName(agentName);
        TrustedIdentityAuthenticationResult.setRecordTime(DateUtil.getCurrentTime());
        int faceCompareRecordInsert = faceCompareRecordMapper.insert(TrustedIdentityAuthenticationResult);
        if (faceCompareRecordInsert > 0) {
            return TrustedIdentityAuthenticationResult;
        } else {
            return null;
        }
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


    public String testNum(int num) {
        StringBuilder str = new StringBuilder();//定义变长字符串
        Random random = new Random();
        for (int i = 0; i < num; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }



    @Override
    public List<Seal> portalSealInfo(String useDepartmentName, String useDepartmentCode, String status, String sealType, String recordDepartmentName, String sealCode) {

        try {
            Seal seal = new Seal();
            seal.setSealTypeCode(sealType);
            seal.setRecordDepartmentName(recordDepartmentName);
            seal.setSealCode(sealCode);
            seal.setUseDepartmentCode(useDepartmentCode);
            seal.setUseDepartmentName(useDepartmentName);
            List<Seal> list = new ArrayList<Seal>();
            list = chooseSealStatus(seal, status);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 印章检测
     *
     * @param sealCode
     * @param useDepartmentCode
     * @return
     */
    @Override
    public int checkSealCode(String sealCode, String useDepartmentCode, String sealTypeCode) {
        List<Seal> seals = sealDao.selectByTypeAndUseDepartmentCode2(useDepartmentCode, sealTypeCode);
        if (seals.size() == 0) {
            return ResultUtil.isFail;
        }
        for (Seal seal : seals) {
            if (seal.getSealCode().equals(sealCode)) {
                return ResultUtil.isSuccess;
            }
        }
        return ResultUtil.isFail;
    }

    /**
     * 注销相关操作
     *
     * @param seal
     * @param employee
     * @return
     */
    public int logoutAbout(Seal seal,Employee employee){



        String agentId = seal.getAgentId();
        SealAgent sealAgent = sealAgentMapper.selectByPrimaryKey(agentId);
        sealAgent.setId(UUIDUtil.generate());
        sealAgent.setBusinessType("03");
        int sealAgentInsert = sealAgentMapper.insertSelective(sealAgent);
        SealOperationRecord sealOperationRecord = new SealOperationRecord();
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setSealId(seal.getId());
        sealOperationRecord.setEmployeeId(employee.getEmployeeId());
        sealOperationRecord.setEmployeeName(employee.getEmployeeName());
        sealOperationRecord.setEmployeeCode(employee.getEmployeeCode());
        sealOperationRecord.setOperateType("07");
        sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
        int sealOperationRecordInsert = sealOperationRecordMapper.insertSelective(sealOperationRecord);
        int sealUpdate = sealDao.updateByPrimaryKeySelective(seal);
        if(sealAgentInsert<0&&sealOperationRecordInsert<0 &&sealUpdate<0){
            return ResultUtil.isFail;
        }else{
            return ResultUtil.isSuccess;
        }

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


}





