package com.dhht.service.District.Impl;

import com.dhht.dao.DistrictMapper;
import com.dhht.model.District;
import com.dhht.model.DistrictMenus;
import com.dhht.model.Resource;
import com.dhht.service.District.DistrictService;
import com.dhht.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(value = "DistrictService")
@Transactional
public class DistrictServiceImp implements DistrictService{
    @Autowired
    private DistrictMapper districtMapper;


    @Override
    public List<DistrictMenus> selectAllDistrict() {
        List<DistrictMenus> list = findDistrictList(districtMapper.selectAllDistrict());
        List<DistrictMenus> districtMenus = findParent(list);
        setAllChildren(districtMenus,list);
        return districtMenus;
    }

    @Override
    public List<DistrictMenus> selectOneDistrict(String id) {
        List<DistrictMenus> list = findDistrictList(districtMapper.selectAllDistrict());
        List<DistrictMenus> districtMenus = findOneParent(list,id);
        setAllChildren(districtMenus,list);
        return districtMenus;
    }


    //生成菜单列表
    public List<DistrictMenus> findDistrictList(List<District> districtList){
        List<DistrictMenus> districtMenus = new ArrayList<DistrictMenus>();
        for (District district:districtList) {
            String DistrictIds[] = StringUtil.DistrictUtil(district.getProvinceId());
            if(isAdd(district.getProvinceId(),districtMenus)&&DistrictIds[1].equals("00")&&DistrictIds[2].equals("00")){
                districtMenus.add(new DistrictMenus(district.getProvinceId(),district.getProvinceName(),"00"));
            }
        }
        for (District district:districtList) {
            String DistrictIds[] = StringUtil.DistrictUtil(district.getCityId());
            if(isAdd(district.getCityId(),districtMenus)&&!DistrictIds[1].equals("00")&&DistrictIds[2].equals("00")){
                districtMenus.add(new DistrictMenus(district.getCityId(),district.getCityName(),DistrictIds[0]+"0000"));
            }
        }
        for (District district:districtList) {
            String DistrictIds[] = StringUtil.DistrictUtil(district.getDistrictId());
            if(!DistrictIds[1].equals("00")&&!DistrictIds[2].equals("00")){
                districtMenus.add(new DistrictMenus(district.getDistrictId(),district.getDistrictName(),DistrictIds[0]+DistrictIds[1]+"00"));
            }
        }
        return districtMenus;
    }

    //查找所有省级父节点
    public List<DistrictMenus> findParent(List<DistrictMenus> list){
        List<DistrictMenus> districtMenus = new ArrayList<DistrictMenus>();
        for (DistrictMenus d: list) {
            if(d.getParentId().equals("00")){
                districtMenus.add(d);
            }
        }
        return districtMenus;
    }

    //查找指定父节点
    public List<DistrictMenus> findOneParent(List<DistrictMenus> list,String id){
        List<DistrictMenus> districtMenus = new ArrayList<DistrictMenus>();
        for(DistrictMenus districtMenu:list){
            if (districtMenu.getDistrictId().toString().equals(id)){
                districtMenus.add(districtMenu);
            }
        }
        return districtMenus;
    }

    //查找地区子节点
    public void setAllChildren(List<DistrictMenus> parent,List<DistrictMenus> districtMenus){
        for (DistrictMenus districtMenu:parent) {
            List<DistrictMenus> list = findInList(districtMenus,districtMenu.getDistrictId().toString());
            if(list.size()>0){
                districtMenu.setChildren(list);
            }
            setAllChildren(list,districtMenus);
        }
    }


    //在给定集合中根据parentID查找数据
    public List<DistrictMenus> findInList(List<DistrictMenus> districtMenus,String id){
        List<DistrictMenus> list = new ArrayList<DistrictMenus>();
        for(DistrictMenus districtMenu:districtMenus){
            if(districtMenu.getParentId().equals(id)){
                list.add(districtMenu);
            }
        }
        return list;
    }


    //在往地区菜单列表添加时判断是否重复
    public boolean isAdd(int id, List<DistrictMenus> list){
        if(list.isEmpty()){
            return true;
        }
        for(DistrictMenus districtMenus:list){
            if(districtMenus.getDistrictId()==id){
                return false;
            }
        }
        return true;
    }
}
