package com.dhht.service.tools.impl;

import com.dhht.dao.FileMapper;
import com.dhht.model.FileInfo;
import com.dhht.model.pojo.FileInfoVO;
import com.dhht.service.tools.FileService;
import com.dhht.service.tools.FileStoreService;
import com.dhht.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 文件管理
 *
 * @author 赵兴龙
 */
@Service
public class FileServiceImpl implements FileService {
    /**
     * 本地存储根目录
     */
    @Value("${file.local.root}")
    private String rootDir;

    @Autowired
    private FileMapper fileMapper;

    @Resource(name="localStoreFileService")
    private FileStoreService fileStoreService;

    private static final Logger logger = LoggerFactory.getLogger(FileLocalStoreServiceImpl.class);

    @Override
    public FileInfo save(byte[] fileData, String filename, String fileExt, String createMemo, int createType, String creatorId, String creatorName) {
        String relativeFullName = fileStoreService.store(fileData, filename, fileExt);
        if(relativeFullName == null) {
            return null;
        }
        return saveFileInfo(filename, fileExt, relativeFullName, createMemo, createType, creatorId, creatorName);
    }

    /**
     * 保存文件信息
     * @param filename 文件真实名称
     * @param fileExt 文件扩展名
     * @param relativeFullName 文件相对路径
     * @param memo 备注
     * @return FileInfo 保存成功时。
     *
     */
    private FileInfo saveFileInfo(String filename, String fileExt, String relativeFullName, String memo, int createType, String creatorId, String creatorName) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(UUIDUtil.generate());
        fileInfo.setFileName(filename);
        fileInfo.setFileExt(fileExt);
        fileInfo.setFilePath(relativeFullName);
        fileInfo.setCreateTime(new Date(System.currentTimeMillis()));
        fileInfo.setCreateMemo(memo);
        fileInfo.setCreateType(createType);
        fileInfo.setCreatorId(creatorId);
        fileInfo.setCreatorName(creatorName);
        fileInfo.setRegister(false);
        fileMapper.insert(fileInfo);
        return fileInfo;
    }

    @Override
    public boolean register(String id, String memo) {
        FileInfo fileInfo = getFileInfo(id);
        fileInfo.setRegister(true);
        fileInfo.setCreateMemo(memo);
        fileInfo.setRegisterTime(new Date());
        fileMapper.updateByPrimaryKey(fileInfo);
        return true;
    }

    @Override
    public boolean delete(String id) {
        FileInfo fileInfo = getFileInfo(id);
        fileStoreService.delete(fileInfo.getFilePath());
        fileMapper.deleteByPrimaryKey(id);
        return true;
    }

    @Override
    public FileInfo getFileInfo(String id) {
        FileInfo fileInfo = fileMapper.getById(id);
//        fileInfo.getFilePath();
//        fileInfo.getId();
        return fileInfo;
    }

    @Override
    public List<FileInfo> selectFileInfo(String ids) {
        return fileMapper.selectByIds(ids.split(","));
    }

    @Override
    public FileInfoVO readFile(String id) {
            FileInfo fileInfo = getFileInfo(id);
            byte[] fileData = fileStoreService.readFile(fileInfo.getFilePath());
            FileInfoVO fileInfoVO = new FileInfoVO(fileInfo);
            fileInfoVO.setFileData(fileData);
            return fileInfoVO;
    }


}
