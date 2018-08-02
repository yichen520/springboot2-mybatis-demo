package com.dhht.service.tools;

import com.dhht.model.FileInfo;

/**
 * 文件管理<br />
 * 用于统一处理系统文件的上传和下载。
 * @author ZhaoXingLong
 */
public interface FileService {
   /**
    * 保存文件
    * <p>文件存储后默认为未注册状态，可能会被垃圾清理任务清理，所以各业务调用此方法存储文件后，应及时对正式的文件进行注册。{@link #register(String, String)}</p>
    * @param fileData 文件数据
    * @param filename 文件名，不含后缀
    * @param fileExt 文件后缀
    * @param memo 存储说明。一般可以存储用户的一些基本信息，如id+用户名+姓名等。
    * @return 成功时，返回已存储文件的信息。
    * @see FileInfo
    */
   FileInfo storeFile(byte[] fileData, String filename, String fileExt, String memo);

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
   boolean deleteFile(String id);

   /**
    * 获取文件信息
    * @param id 要获取的文件的信息
    * @return null：当文件不存在时<br />
    *          FileInfo：对应文件的信息
    *  @see FileInfo
    */
   FileInfo getFileInfo(String id);

   /**
    * 读取文件
    * @param id 要读取的文件的id
    * @return null：当文件不存在时<br />
    *     *    byte[]：对应文件的数据
    */
   byte[] readFile(String id);
}
