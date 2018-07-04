package com.dhht.service.tools.impl;

import com.dhht.common.JsonObjectBO;
import com.dhht.dao.FileMapper;
import com.dhht.model.File;
import com.dhht.service.tools.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fileService")
public class FileServiceImpl implements FileService {

    @Autowired
    private FileMapper fileMapper;

    @Override
    public int insertFile(File file) {

      return fileMapper.insert(file);

    }
}
