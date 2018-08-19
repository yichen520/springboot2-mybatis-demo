package com.dhht.service.tools;

import com.dhht.model.FileInfo;
import com.dhht.model.pojo.FileInfoVO;

import java.util.List;

public interface FileService {
    /**
     * 文件创建方式：用户上传
     */
    int CREATE_TYPE_UPLOAD = 0;
    /**
     * 文件创建方式：系统生成
     */
    int CREATE_TYPE_SYSTEM_GENERATE = 1;
    /**
     * 保存文件
     * <p>文件存储后默认为未注册状态，可能会被垃圾清理任务清理，所以各业务调用此方法存储文件后，应及时对正式的文件进行注册。{@link #register(String, String)}</p>
     * @param fileData 文件数据
     * @param filename 文件名，不含后缀
     * @param fileExt 文件后缀
     * @param createMemo 存储说明。一般可以存储用户的一些基本信息，如id+用户名+姓名等。
     * @return 成功时，返回已存储文件的信息。
     */
    FileInfo save(byte[] fileData, String filename, String fileExt, String createMemo, int createType, String creatorId, String creatorName);

    /**
     * 注册文件
     * @param id 要注册的文件的id
     * @param memo 注册说明。一般存储业务相关的一些信息，如业务模块名+业务对象id等。
     * @return true 注册成功<br />
     *          false 注册失败
     */
    boolean register(String id, String memo);

    /**
     * 删除文件
     * @param id 要删除的文件id
     * @return true 删除成功<br />
     *          false 删除失败
     */
    boolean delete(String id);

    /**
     * 获取文件信息
     * @param id 要获取的文件的信息
     * @return null：当文件不存在时<br />

     */
    FileInfo getFileInfo(String id);

    /**
     * 查找文件信息
     * @param ids 以逗号分隔的文件id
     * @return
     */
    List<FileInfo> selectFileInfo(String ids);

    /**
     * 读取文件
     * @param id 要读取的文件的id
     * @return null：当文件不存在时<br />
     *     *    FileInfoVO：对应文件的信息，包含文件数据
     */
    FileInfoVO readFile(String id);
}
