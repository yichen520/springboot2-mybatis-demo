package com.dhht.service.ems;

import com.dhht.model.Ems;

import java.util.Map;

public interface EmsService {
    Map<String,Object> insertEms(Ems ems)throws Exception;
}
