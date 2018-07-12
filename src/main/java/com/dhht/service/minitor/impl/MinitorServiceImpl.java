package com.dhht.service.minitor.impl;

import com.dhht.dao.MinitorMapper;
import com.dhht.model.Minitor;
import com.dhht.service.minitor.MinitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("minitorService")
public class MinitorServiceImpl implements MinitorService {

    @Autowired
    private MinitorMapper minitorMapper;

    @Override
    public List<Minitor> info(Integer minitor) {
      return   minitorMapper.selectByMinitor(minitor);
    }

    @Override
    public boolean add(Minitor minitor) {

        if (minitorMapper.insert(minitor)== 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean delete(String id) {

        if (minitorMapper.deleteByPrimaryKey(id)== 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean update(Minitor minitor) {
        if (minitorMapper.updateByPrimaryKeySelective(minitor)== 1){
            return true;
        }else {
            return false;
        }
    }
}
