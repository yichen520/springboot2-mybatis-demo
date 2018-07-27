package com.dhht.service.seal.Impl;

import com.dhht.dao.MakedepartmentMapper;
import com.dhht.dao.SealDao;
import com.dhht.model.*;
import com.dhht.service.District.DistrictService;
import com.dhht.service.make.MakeDepartmentCuontService;
import com.dhht.service.seal.SealCuontService;
import com.dhht.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 2018/7/7 create by cy
 */
@Service(value = "sealCountService")
@Transactional
public class SealCountServiceImp implements SealCuontService {
    @Autowired
    private MakedepartmentMapper makedepartmentMapper;

    @Autowired
    private SealDao sealDao;

    @Autowired
    private DistrictService districtService;

    /**
     * 统计区域下所有的所有的制作单位.
     * @param districtId
     * @return
     */


    /**
     * 返回区域列表
     *
     * @param district
     * @return
     */
    public List<DistrictMenus> getDistrictList(String district) {
        List<DistrictMenus> list = districtService.selectOneDistrict(district);
        return list.get(0).getChildren();
    }

    /**
     * 统计每一栏的小计
     *
     * @param list
     * @return
     */
    public SealCount subtotal(List<SealCount> list) {

        int sealAdd = 0;
        int sealLoss = 0;
        int sealLogout = 0;

        for (SealCount counts : list) {
            sealAdd = sealAdd + counts.getNewSealNum();
            sealLoss = sealLoss + counts.getLossSealNum();
            sealLogout = sealLogout + counts.getLogoutSealNum();
        }

        SealCount count = new SealCount();
        count.setCountName("小计(" + list.get(0).getCountName() + ")");
        count.setSealType("");
        count.setNewSealNum(sealAdd);
        count.setLossSealNum(sealLoss);
        count.setLogoutSealNum(sealLogout);
        return count;
    }

    /**
     * 统计每一栏的总量
     *
     * @param list
     * @return
     */
    public List<SealCount> getSum(List<SealCount> list) {
        int sealAdd = 0;
        int sealLoss = 0;
        int sealLogout = 0;

        for (SealCount counts : list) {
            if (counts.getSealType().equals("")) {
                sealAdd = sealAdd + counts.getNewSealNum();
                sealLoss = sealLoss + counts.getLossSealNum();
                sealLogout = sealLogout + counts.getLogoutSealNum();
            }
        }


        SealCount count = new SealCount();
        count.setCountName("总计 ");
        count.setSealType("");
        count.setNewSealNum(sealAdd);
        count.setLossSealNum(sealLoss);
        count.setLogoutSealNum(sealLogout);
        list.add(count);
        return list;
    }


    /**
     * 根据status获取对应的num
     *
     * @param makeDepartmentCode
     * @param sealTypeCode
     * @param startTime
     * @param endTime
     * @return
     */
    public SealCount getStatus(String makeDepartmentCode, String sealTypeCode, String startTime, String endTime) {

        int newSealNum = sealDao.countAddSeal(makeDepartmentCode,sealTypeCode, startTime, endTime);
        int lossSealNum = sealDao.countLossSeal(makeDepartmentCode, sealTypeCode, startTime, endTime);
        int logoutSealNum = sealDao.countLogoutSeal(makeDepartmentCode, sealTypeCode, startTime, endTime);
        SealCount sealCount = new SealCount();
        sealCount.setNewSealNum(newSealNum);
        sealCount.setLossSealNum(lossSealNum);
        sealCount.setLogoutSealNum(logoutSealNum);
        return sealCount;
    }


    /***
     * 根据status和districtId获取对应的num
     *
     * @param Status
     * @param
     * @param sealTypeCode
     * @param startTime
     * @param endTime
     * @return
     */
    public SealCount getStatusAndDistrictId(List<String> Status, String id, String sealTypeCode, String startTime, String endTime) {

        int newSealNum = 0;
        int lossSealNum = 0;
        int logoutSealNum = 0;
        if(Status!=null){
        if (Status.contains("01") && Status.contains("05") && Status.contains("06")) { //只查询 已经制作 已经注销和已经挂失的
            newSealNum = sealDao.countAddSealByDistrictId(id, sealTypeCode, startTime, endTime);
            lossSealNum = sealDao.countLossSealByDistrictId(id, sealTypeCode, startTime, endTime);
            logoutSealNum = sealDao.countLogoutSealByDistrictId(id, sealTypeCode, startTime, endTime);
        } else if (Status.contains("05") && Status.contains("06")) {
            lossSealNum = sealDao.countLossSealByDistrictId(id, sealTypeCode, startTime, endTime);
            logoutSealNum = sealDao.countLogoutSealByDistrictId(id, sealTypeCode, startTime, endTime);
        } else if (Status.contains("01") && Status.contains("06")) {
            newSealNum = sealDao.countAddSealByDistrictId(id, sealTypeCode, startTime, endTime);
            logoutSealNum = sealDao.countLogoutSealByDistrictId(id, sealTypeCode, startTime, endTime);
        } else if (Status.contains("01") && Status.contains("05")) {
            newSealNum = sealDao.countAddSealByDistrictId(id, sealTypeCode, startTime, endTime);
            lossSealNum = sealDao.countLossSealByDistrictId(id, sealTypeCode, startTime, endTime);
        } else if (Status.contains("01")) {
            newSealNum = sealDao.countAddSealByDistrictId(id, sealTypeCode, startTime, endTime);
        } else if (Status.contains("05")) {
            lossSealNum = sealDao.countLossSealByDistrictId(id, sealTypeCode, startTime, endTime);
        } else{
            logoutSealNum = sealDao.countLogoutSealByDistrictId(id, sealTypeCode, startTime, endTime);
        }
        }else {
            newSealNum = sealDao.countAddSealByDistrictId(id, sealTypeCode, startTime, endTime);
            lossSealNum = sealDao.countLossSealByDistrictId(id, sealTypeCode, startTime, endTime);
            logoutSealNum = sealDao.countLogoutSealByDistrictId(id, sealTypeCode, startTime, endTime);
        }
        SealCount sealCount = new SealCount();
        sealCount.setNewSealNum(newSealNum);
        sealCount.setLossSealNum(lossSealNum);
        sealCount.setLogoutSealNum(logoutSealNum);
        return sealCount;

    }

    /**
     * 获取制作单位
     * @param districtIds
     * @return
     */
    public List<String> getMakeDepartmentCode(List<String> districtIds){
        List<String> makeDepartmentCode = new ArrayList<>();
        for(String id:districtIds){                //遍历传入的districtId
            String districtId1[] = StringUtil.DistrictUtil(id);
            String districtId = null;
            if(districtId1[1].equals("00")&&districtId1[2].equals("00")){   //省
                districtId = districtId1[0];
                List<String> a =sealDao.selectLikeDistrictId(districtId);
                makeDepartmentCode.addAll(a);

            }else if(!districtId1[1].equals("00")&&districtId1[2].equals("00")){
                districtId = districtId1[0]+districtId1[1];
                List<String> a =sealDao.selectLikeDistrictId(districtId);
                makeDepartmentCode.addAll(a);
            }else {
                districtId = id;
                List<String> a =sealDao.selectLikeDistrictId(districtId);
                makeDepartmentCode.addAll(a);
            }
        }
        return makeDepartmentCode;
    }

    /**
     * 根据制作单位
     *

     * @param districtIds
     * @param sealTypeCodes

     * @param startTime
     * @param endTime
     * @return
     */

    @Override
    public List<SealCount> countByDepartment(List<String>  districtIds, List<String> sealTypeCodes, String startTime, String endTime) {
        int newSealNum = 0;
        int lossSealNum = 0;
        int logoutSealNum = 0;
        List<SealCount> counts = new ArrayList<>();
        List<String> makeDepartmentCodes = getMakeDepartmentCode(districtIds);

        for (String makeDepartmentCode : makeDepartmentCodes) { //根据传入的code进行遍历
            List<SealCount> count = new ArrayList<>();
            String countName = makedepartmentMapper.selectByDepartmentCode(makeDepartmentCode).getDepartmentName(); //更加code查找name
//            sealCount.setCountName(countName);  //存入count对象
            List<Seal> seals = sealDao.selectByMakeDepartmentCode(makeDepartmentCode); //根据code查找seal中的所有印章
            Set<String> set = new HashSet<>();
            for (Seal seal : seals) {
                set.add(seal.getSealTypeCode());
            }
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                String sealTypeCode = iterator.next();  //每个seal中的所有的sealtypecode的集合
                if (sealTypeCodes.size() != 0) {
                    for (String sealTypeCode1 : sealTypeCodes) {
                        if (sealTypeCode.equals(sealTypeCode1)) {
                            String sealType = "";
                            switch (sealTypeCode) {
                                case "01":
                                    sealType = "法定名称章";
                                    break;
                                case "02":
                                    sealType = "财务专用章";
                                    break;
                                case "03":
                                    sealType = "发票专用章";
                                    break;
                                case "04":
                                    sealType = "合同专用章";
                                    break;
                                case "05":
                                    sealType = "法人代表专用章";
                                    break;
                                case "06":
                                    sealType = "公章";
                                    break;
                                case "07":
                                    sealType = "内设机构章";
                                    break;
                                case "08":
                                    sealType = "分支机构章";
                                    break;
                                case "99":
                                    sealType = "其他类型章";
                                    break;
                            }

                            SealCount Num = getStatus( makeDepartmentCode, sealTypeCode, startTime, endTime);
                            newSealNum = Num.getNewSealNum();
                            lossSealNum = Num.getLossSealNum();
                            logoutSealNum = Num.getLogoutSealNum();
                            if (newSealNum != 0 || lossSealNum != 0 || logoutSealNum != 0) {
                                SealCount sealCount = new SealCount(countName, sealType, newSealNum, lossSealNum, logoutSealNum);
                                count.add(sealCount);
                            }
                        }
                    }
                } else { //当前端输入当type不存在当时候
                    String sealType = "";
                    switch (sealTypeCode) {

                        case "01":
                            sealType = "法定名称章";
                            break;
                        case "02":
                            sealType = "财务专用章";
                            break;
                        case "03":
                            sealType = "发票专用章";
                            break;
                        case "04":
                            sealType = "合同专用章";
                            break;
                        case "05":
                            sealType = "法人代表专用章";
                            break;
                        case "06":
                            sealType = "公章";
                            break;
                        case "07":
                            sealType = "内设机构章";
                            break;
                        case "08":
                            sealType = "分支机构章";
                            break;
                        case "99":
                            sealType = "其他类型章";
                            break;
                    }
                    SealCount Num = getStatus( makeDepartmentCode, sealTypeCode, startTime, endTime);
                    newSealNum = Num.getNewSealNum();
                    lossSealNum = Num.getLossSealNum();
                    logoutSealNum = Num.getLogoutSealNum();
                    if (newSealNum != 0 || lossSealNum != 0 || logoutSealNum != 0) {
                        SealCount sealCount = new SealCount(countName, sealType, newSealNum, lossSealNum, logoutSealNum);
                        count.add(sealCount);
                    }
                }
            }
            count.add(subtotal(count));//把小计放入队列
            counts.addAll(count);

        }
        return getSum(counts);
    }

    /**
     * 根据区域查找印章
     *
     * @param districtIds
     * @param sealTypeCodes
     * @param Status
     * @param startTime
     * @param endTime
     * @return
     */
//    @Override
//    public List<SealCount> countByDistrictId(List<String> districtIds, List<String> sealTypeCodes, List<String> Status, String startTime, String endTime) {
//        int newSealNum = 0;
//        int lossSealNum = 0;
//        int logoutSealNum = 0;
//        List<SealCount> counts = new ArrayList<>();
//        for (String districtId : districtIds) {
//            List<SealCount> count = new ArrayList<>();
//            List<DistrictMenus> list = districtService.selectOneDistrict(districtId);
//            List<DistrictMenus> districtIdList = new ArrayList<>();
//            String isProvince = list.get(0).getDistrictId();
//            String isProvinces[] = StringUtil.DistrictUtil(isProvince);
//            if(isProvinces[1].equals("00")&&isProvinces[2].equals("00")){
//                list = list.get(0).getChildren();
//            }
//            if (list.get(0).getChildren() == null) {
//                districtIdList = list;
//            } else {
//                districtIdList = list.get(0).getChildren();
//            }
//            for (DistrictMenus districtMenus : districtIdList) {
//                String id =  StringUtil.getDistrictId(districtMenus.getDistrictId());
//                List<Seal> seals = sealDao.selectByDistrictId(id);
//                Set<String> set = new HashSet<>();
//                for (Seal seal : seals) {
//                    set.add(seal.getSealTypeCode());
//                }
//                Iterator<String> iterator = set.iterator();
//                while (iterator.hasNext()) {
//                    String sealTypeCode = iterator.next();
//                    if (sealTypeCodes.size() != 0) {
//                        for (String sealTypeCode1 : sealTypeCodes) {
//                            if (sealTypeCode.equals(sealTypeCode)) {
//                                String sealType = "";
//                                switch (sealTypeCode) {
//                                    case "01":
//                                        sealType = "法定名称章";
//                                        break;
//                                    case "02":
//                                        sealType = "财务专用章";
//                                        break;
//                                    case "03":
//                                        sealType = "发票专用章";
//                                        break;
//                                    case "04":
//                                        sealType = "合同专用章";
//                                        break;
//                                    case "05":
//                                        sealType = "法人代表专用章";
//                                        break;
//                                    case "06":
//                                        sealType = "公章";
//                                        break;
//                                    case "07":
//                                        sealType = "内设机构章";
//                                        break;
//                                    case "08":
//                                        sealType = "分支机构章";
//                                        break;
//                                    case "99":
//                                        sealType = "其他类型章";
//                                        break;
//                                }
//
//                                SealCount Num = getStatusAndDistrictId(Status, id, sealTypeCode1, startTime, endTime);
//                                newSealNum = Num.getNewSealNum();
//                                lossSealNum = Num.getLossSealNum();
//                                logoutSealNum = Num.getLogoutSealNum();
//                                if (newSealNum != 0 || lossSealNum != 0 || logoutSealNum != 0) {
//                                    count.add(new SealCount(districtMenus.getDistrictName(), sealType, newSealNum, lossSealNum, logoutSealNum));
//                                }
//                            }
//                        }
//                    }else{ //当前端输入当type不存在当时候
//                            String sealType = "";
//                            switch (sealTypeCode) {
//
//                                case "01":
//                                    sealType = "法定名称章";
//                                    break;
//                                case "02":
//                                    sealType = "财务专用章";
//                                    break;
//                                case "03":
//                                    sealType = "发票专用章";
//                                    break;
//                                case "04":
//                                    sealType = "合同专用章";
//                                    break;
//                                case "05":
//                                    sealType = "法人代表专用章";
//                                    break;
//                                case "06":
//                                    sealType = "公章";
//                                    break;
//                                case "07":
//                                    sealType = "内设机构章";
//                                    break;
//                                case "08":
//                                    sealType = "分支机构章";
//                                    break;
//                                case "99":
//                                    sealType = "其他类型章";
//                                    break;
//                            }
//
//                            SealCount Num = getStatusAndDistrictId(Status, id, sealTypeCode, startTime, endTime);
//                            newSealNum = Num.getNewSealNum();
//                            lossSealNum = Num.getLossSealNum();
//                            logoutSealNum = Num.getLogoutSealNum();
//                            if (newSealNum != 0 || lossSealNum != 0 || logoutSealNum != 0) {
//                                count.add(new SealCount(districtMenus.getDistrictName(), sealType, newSealNum, lossSealNum, logoutSealNum));
//                            }
//                        }
//                    }
//
//
//                }
//            if(count.size()!=0){
//            count.add(subtotal(count));//把小计放入队列
//            counts.addAll(count);
//            }
//
//            }
//        return getSum(counts);
//
//    }
//
    @Override
    public List<SealCount> countByDistrictId(List<String> districtIds, List<String> sealTypeCodes, List<String> Status, String startTime, String endTime) {
        int newSealNum = 0;
        int lossSealNum = 0;
        int logoutSealNum = 0;
        List<SealCount> counts = new ArrayList<>();

            List<SealCount> count = new ArrayList<>();
            List<DistrictMenus> districtIdList = districtService.selectDistrictByArray(districtIds);
            for (DistrictMenus districtMenus : districtIdList) {
                String id =  StringUtil.getDistrictId(districtMenus.getDistrictId());
                List<Seal> seals = sealDao.selectByDistrictId(id);
                Set<String> set = new HashSet<>();
                for (Seal seal : seals) {
                    set.add(seal.getSealTypeCode());
                }
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    String sealTypeCode = iterator.next();
                    if (sealTypeCodes.size() != 0) {
                        for (String sealTypeCode1 : sealTypeCodes) {
                            if (sealTypeCode.equals(sealTypeCode)) {
                                String sealType = "";
                                switch (sealTypeCode) {
                                    case "01":
                                        sealType = "法定名称章";
                                        break;
                                    case "02":
                                        sealType = "财务专用章";
                                        break;
                                    case "03":
                                        sealType = "发票专用章";
                                        break;
                                    case "04":
                                        sealType = "合同专用章";
                                        break;
                                    case "05":
                                        sealType = "法人代表专用章";
                                        break;
                                    case "06":
                                        sealType = "公章";
                                        break;
                                    case "07":
                                        sealType = "内设机构章";
                                        break;
                                    case "08":
                                        sealType = "分支机构章";
                                        break;
                                    case "99":
                                        sealType = "其他类型章";
                                        break;
                                }

                                SealCount Num = getStatusAndDistrictId(Status, id, sealTypeCode1, startTime, endTime);
                                newSealNum = Num.getNewSealNum();
                                lossSealNum = Num.getLossSealNum();
                                logoutSealNum = Num.getLogoutSealNum();
                                if (newSealNum != 0 || lossSealNum != 0 || logoutSealNum != 0) {
                                    count.add(new SealCount(districtMenus.getDistrictName(), sealType, newSealNum, lossSealNum, logoutSealNum));
                                }
                            }
                        }
                    }else{ //当前端输入当type不存在当时候
                        String sealType = "";
                        switch (sealTypeCode) {

                            case "01":
                                sealType = "法定名称章";
                                break;
                            case "02":
                                sealType = "财务专用章";
                                break;
                            case "03":
                                sealType = "发票专用章";
                                break;
                            case "04":
                                sealType = "合同专用章";
                                break;
                            case "05":
                                sealType = "法人代表专用章";
                                break;
                            case "06":
                                sealType = "公章";
                                break;
                            case "07":
                                sealType = "内设机构章";
                                break;
                            case "08":
                                sealType = "分支机构章";
                                break;
                            case "99":
                                sealType = "其他类型章";
                                break;
                        }

                        SealCount Num = getStatusAndDistrictId(Status, id, sealTypeCode, startTime, endTime);
                        newSealNum = Num.getNewSealNum();
                        lossSealNum = Num.getLossSealNum();
                        logoutSealNum = Num.getLogoutSealNum();
                        if (newSealNum != 0 || lossSealNum != 0 || logoutSealNum != 0) {
                            count.add(new SealCount(districtMenus.getDistrictName(), sealType, newSealNum, lossSealNum, logoutSealNum));
                        }
                    }
                }


            }
            if(count.size()!=0){
                count.add(subtotal(count));//把小计放入队列
                counts.addAll(count);
            }
        return getSum(counts);
        }

//    @Override
//    public List<SealCount> countByDepartment(List<String> districtIds, List<String> sealTypeCodes, List<String> Status, String startTime, String endTime) {
//        int newSealNum = 0;
//        int lossSealNum = 0;
//        int logoutSealNum = 0;
//        List<DistrictMenus> list = districtService.selectDistrictByArray(districtIds);
//        List<DistrictMenus> list1 = list.get(0).getChildren();
//        List<SealCount> sealCounts = new ArrayList<>();
//        for (DistrictMenus districtMenus : list1) {  //遍历每个区
//            String districtId = districtMenus.getDistrictId();
//            List<Seal> sealtList = sealDao.selectByDistrictId(districtId);  //该区下所有的印章
//            if (sealtList.size() != 0) {
//                List<SealCount> counts = new ArrayList<>();
//                for (Seal seal : sealtList) {
//                    String makeDepartmentCode = seal.getMakeDepartmentCode();
//                    String makeDepartmentName = seal.getMakeDepartmentName();
//                    String sealTypeCode = seal.getSealTypeCode();
//                    for (String sealTypeCode1 : sealTypeCodes) {
//                        if (sealTypeCode.equals(sealTypeCode1)) {
//                            String sealType = "";
//                            switch (sealTypeCode) {
//                                case "01":
//                                    sealType = "法定名称章";
//                                    break;
//                                case "02":
//                                    sealType = "财务专用章";
//                                    break;
//                                case "03":
//                                    sealType = "发票专用章";
//                                    break;
//                                case "04":
//                                    sealType = "合同专用章";
//                                    break;
//                                case "05":
//                                    sealType = "法人代表专用章";
//                                    break;
//                                case "06":
//                                    sealType = "公章";
//                                    break;
//                                case "07":
//                                    sealType = "内设机构章";
//                                    break;
//                                case "08":
//                                    sealType = "分支机构章";
//                                    break;
//                                case "99":
//                                    sealType = "其他类型章";
//                                    break;
//                            }
//                            if (counts.size() == 0) {
//                                newSealNum = sealDao.countAddSeal(makeDepartmentCode, sealTypeCode, startTime, endTime);
//                                lossSealNum = sealDao.countLossSeal(makeDepartmentCode, sealTypeCode, startTime, endTime);
//                                logoutSealNum = sealDao.countLogoutSeal(makeDepartmentCode, sealTypeCode, startTime, endTime);
//                                if (newSealNum != 0 || lossSealNum != 0 || logoutSealNum != 0) {
//                                    counts.add(new SealCount(makeDepartmentName, sealType, newSealNum, lossSealNum, logoutSealNum));
//                                }
//                            } else {
//                                for (SealCount count : counts) {
//                                    if (!makeDepartmentName.equals(count.getCountName()) && !sealType.equals(count.getSealType())) {
//                                        newSealNum = sealDao.countAddSeal(makeDepartmentCode, sealTypeCode, startTime, endTime);
//                                        lossSealNum = sealDao.countLossSeal(makeDepartmentCode, sealTypeCode, startTime, endTime);
//                                        logoutSealNum = sealDao.countLogoutSeal(makeDepartmentCode, sealTypeCode, startTime, endTime);
//                                        if (newSealNum != 0 || lossSealNum != 0 || logoutSealNum != 0) {
//                                            counts.add(new SealCount(makeDepartmentName, sealType, newSealNum, lossSealNum, logoutSealNum));
//                                        }
//                                    }
//                                }
//                            }
//
//                        }
//
//
//                    }
//                    if (counts.size() != 0) {
//                        counts.add(subtotal(counts));//把小计放入队列
//                        sealCounts.addAll(counts);
//                    }
//
//                }
//
//
//            }
//
//        }
//        return getSum(sealCounts);
//    }

}



