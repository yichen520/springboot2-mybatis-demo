package com.dhht;

import com.dhht.common.ImageGenerate;
import com.dhht.dao.RecordPoliceMapper;
import com.dhht.dao.SealDao;
import com.dhht.dao.UserDao;
import com.dhht.service.District.DistrictService;
import com.dhht.service.employee.EmployeeCountService;
import com.dhht.service.make.MakeDepartmentCuontService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.message.NoticeService;
import com.dhht.service.message.NotifyService;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.service.user.RoleService;
import com.dhht.service.user.UserService;
import com.dhht.util.FileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTest {
    @Autowired
    private DistrictService districtService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;


    @Autowired
    private RecordPoliceMapper recordPoliceMapper;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RecordDepartmentService recordDepartmentService;

    @Autowired
    private MakeDepartmentService makeDepartmentService;
    @Autowired
    private MakeDepartmentCuontService makeDepartmentCuontService;

    @Autowired
    private EmployeeCountService employeeCountService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private SealDao sealDao;
//

    @Test
    public void Test1() {
        ImageGenerate imageGenerate = new ImageGenerate();
        Map map = new HashMap();
        map.put("useDepartment","北京东华宏泰股份有限公司");
        map.put("sealType","合同专用章");
        map.put("sealCode","3301081025358963");
        map.put("centerImage","★");
        int imgArr[][] = imageGenerate.moulageData(map);
        String id = FileUtil.saveArrayFile(imgArr,"1",1,"1","1");
        System.out.println(id);

    }


//
//    @Test
//    public void Test1() {
//        List<DistrictMenus> list = districtService.selectMakeDepartmentMenus("330100");
//        for (DistrictMenus d1 : list) {
//            System.out.println(d1.toString());
//        }
//    }
//
//    @Test
//    public void Test3() {
//        Notice notice = noticeService.selectNoticeDetail("fa097054569b4809843ca7a47ad794f8");
//        System.out.println(notice.toString());
//    }
//
//    @Test
//    public void Test4() {
//        System.out.println(notifyService.countNewNotify("00265c419a8347ee80b346b7d545f183"));
//    }
//
//    @Test
//    public void Test5() {
//        List<DistrictMenus> list = new ArrayList<>();
//        DistrictMenus districtMenus = new DistrictMenus();
//        districtMenus.setDistrictId("330100");
//        list.add(districtMenus);
//        DistrictMenus a = new DistrictMenus();
//        a.setDistrictId("330200");
//        list.add(a);
//        List b = sealDao.countByDistrictId(list);
//        System.out.println(b.toString());
//
//    }
//}
//
//
//  /*  public void Test1(){
//        List<Users> list = userService.selectByDistrict(110107);
//        for (Users users:list) {
//            System.out.println(users.toString());
//        }
//    }*/
}