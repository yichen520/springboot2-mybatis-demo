package com.dhht.service.tools;

import com.dhht.common.JsonObjectBO;
import com.dhht.model.File;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public interface FileService {
   File insertFile(HttpServletRequest request,MultipartFile file);

   boolean deleteFile(String filePath);

   boolean insertLocal(File file);

   boolean deleteLocalFile(String filePath);

}
