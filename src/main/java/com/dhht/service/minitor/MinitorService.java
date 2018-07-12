package com.dhht.service.minitor;

import com.dhht.model.Minitor;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface MinitorService {

    boolean add(Minitor minitor);

    List<Minitor> info(Integer minitor);

    boolean delete(String id);

    boolean update(Minitor minitor);

}
