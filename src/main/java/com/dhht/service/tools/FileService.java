package com.dhht.service.tools;

import com.dhht.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface FileService {
   FileInfo insertFile(HttpServletRequest request, MultipartFile file);

   boolean deleteFile(String filePath);

   boolean insertLocal(FileInfo file);

   boolean deleteLocalFile(String filePath);

   FileInfo selectByPath(String path);

}
