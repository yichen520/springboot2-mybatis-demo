package com.dhht.service.notice;

import com.dhht.model.Notice;
import com.dhht.model.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface NoticeService {
    int insert(Map map, User user);

    List<Notice> selectByUserName(String userName);

    int delete(String id);

    int update(Map map);

    List<Notice> selectByNum(String district);
}
