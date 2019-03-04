package com.dhht.service.ems.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhht.common.Cache;
import com.dhht.dao.*;
import com.dhht.model.*;
import com.dhht.service.ems.EmsService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.seal.SealService;
import com.dhht.service.tools.FileService;
import com.dhht.util.DateUtil;
import com.dhht.util.ResultUtil;
import com.dhht.util.StringUtil;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service(value = "emsService")
public class EmsServiceImp implements EmsService {

    @Autowired
    private FileService fileService;

    @Autowired
    private SealService sealService;

    @Autowired
    private RecipientsMapper recipientsMapper;

    @Autowired
    private CourierMapper courierMapper;

    @Autowired
    private SealPayOrderMapper sealPayOrderMapper;

    @Autowired
    private MakeDepartmentService makeDepartmentService;

    @Autowired
    private DistrictMapper districtMapper;

   @Autowired
    private SealDao sealDao;

    @Value("${file.local.ems}")
    private String filePath;
    @Override
    public Map<String,Object> insertEms(Ems ems) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(DateUtil.getCurrentTime());
        Map<String,Object> map = new HashMap<>();
        try {
            if (ems == null) {
                map.put("status", "error");
                map.put("message","数据不存在");
                return map;
            }
            JSONObject jsonObject = new JSONObject();
            Cache.put("ems", ems);
            String emsJson = JSON.toJSONString(ems);
            String fileId = byteOutStream(ems.getAddresser(),filePath, emsJson);
            map.put("status", "ok");
            map.put("message","上传成功");
            map.put("fileId",fileId);
            map.put("fileName",date+ems.getAddresser());
            return map;
        }catch (IOException e) {
            e.printStackTrace();
            map.put("status", "error");
            map.put("message","出现异常");
            return map;
        }

    }

    /**
     * 下载
     * @param id
     * @param
     * @return
     */
    @Override
    public Map<String, Object> downLoad(@RequestBody String id) {
        Map<String, Object> map = new HashMap<>();
        try {
            Seal seal1 = sealDao.selectByPrimaryKey(id);
            SealPayOrder sealPayOrder = sealPayOrderMapper.selectBySealId(seal1.getId());
            if (sealPayOrder.getCourierId() != null && !sealPayOrder.getCourierId().isEmpty()) {
                //  Recipients recipients =recipientsMapper.selectByPrimaryKey(courierMapper.selectByPrimaryKey(sealPayOrder.getCourierId()).getRecipientsId());

                Recipients recipients = recipientsMapper.selectByPrimaryKey(courierMapper.selectBySealId(seal1.getId()).getRecipientsId());
                DeliveryExpressInfo deliveryExpressInfo = new DeliveryExpressInfo();
                deliveryExpressInfo.setReceiveName(recipients.getRecipientsName());
                deliveryExpressInfo.setReceivephone(recipients.getRecipientsTelphone());
                deliveryExpressInfo.setReceivedistrictId(recipients.getDistrictId());
                deliveryExpressInfo.setReceiveAddressDetail(recipients.getRecipientsAddress());
                deliveryExpressInfo.setReceivedistrictName(recipients.getDistrictName());
                MakeDepartmentSimple makedepartment = makeDepartmentService.selectByDepartmentCode(seal1.getMakeDepartmentCode());
                if (makedepartment == null) {
                    deliveryExpressInfo.setSendName("未找到刻制单位信息");
                } else {
                    deliveryExpressInfo.setSendName(makedepartment.getDepartmentName());
                    deliveryExpressInfo.setSenddistrictId(makedepartment.getDepartmentAddress());
                    deliveryExpressInfo.setSenddistrictName(getDistrictName(makedepartment.getDepartmentAddress()));
                    deliveryExpressInfo.setSendTelphone(makedepartment.getTelphone());
                    deliveryExpressInfo.setSendAddressDetail(makedepartment.getDepartmentAddressDetail());
                }
                //增加经办人信息
                // seal1.setDeliveryExpressInfo(deliveryExpressInfo);
                Ems ems = new Ems();
                ems.setSender(deliveryExpressInfo.getSendName());
                ems.setSenderTelPhone(deliveryExpressInfo.getSendTelphone());
                ems.setSenderAddress(deliveryExpressInfo.getSendAddressDetail());
                ems.setSenderDistrictId(deliveryExpressInfo.getSenddistrictId());
                ems.setAddresser(deliveryExpressInfo.getReceiveName());
                ems.setAddresseeDistrictId(deliveryExpressInfo.getReceivedistrictId());
                ems.setAddresseeTelPhone(deliveryExpressInfo.getReceivephone());
                ems.setAddresseeAddress(deliveryExpressInfo.getReceiveAddressDetail());
                map = insertEms(ems);

            }else{
                map.put("status", "error");
                map.put("message","数据不存在");
            }
        }catch(Exception e){
                e.printStackTrace();
            }
        return map;

    }

    public  String  byteOutStream(String sender,String filePath,String jsonString) throws Exception {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(DateUtil.getCurrentTime());
        //1:使用File类创建一个要操作的文件路径
        File file = new File(filePath+"ems/"+date+sender+".txt");
        if(!file.getParentFile().exists()){ //如果文件的目录不存在
            file.getParentFile().mkdirs(); //创建目录

        }

        //2: 实例化对象
        OutputStream output = new FileOutputStream(file);

        //3: 准备好实现内容的输出
//        String msg = object.toString();
        byte data[] = jsonString.getBytes();
        output.write(data);
        output.close();
        FileInfo fileInfo = fileService.save(data, DateUtil.getCurrentTime() + "ems", "txt", "", FileService.CREATE_TYPE_UPLOAD,UUIDUtil.generate(),"ems");
        String fileId = fileInfo.getId();
        return fileId;

    }

        public  String getDistrictName(String districtId){
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
}
