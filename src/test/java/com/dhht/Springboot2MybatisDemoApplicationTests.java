package com.dhht;

import com.dhht.common.JsonObjectBO;
import com.dhht.controller.UserController;
import com.dhht.dao.*;
import com.dhht.model.*;
import com.dhht.service.District.DistrictService;
import com.dhht.service.Log.LogService;
import com.dhht.service.seal.SealService;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.dhht.service.user.UserPasswordService;
import com.dhht.service.user.UserService;

import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot2MybatisDemoApplicationTests {
	@Autowired
	private UserService userService;

	@Autowired
	private UserDao suserDao;

	@Autowired
	private UserController suserController;

	@Autowired
	private UseDepartmentService useDepartmentService;

	@Autowired
	private UseDepartmentDao useDepartmentDao;

	@Autowired
	private LogService logService;

	@Autowired
	private DistrictService districtService;

	@Autowired
	private DistrictMapper districtMapper;

	@Autowired
	private SealDao sealDao;
	@Autowired
	private SealService sealService;

	@Autowired
	private UserPasswordService userPasswordService;
	@Autowired
	private UserController userController;
	@Autowired
	private MakedepartmentMapper makedepartmentMapper;
	@Test
	public void contextLoads() {

//		User user = new User();
//		user.setTelphone("18262880271");
//		String districtId = "330100";
		Seal seal = new Seal();
//		seal.setId("123123");
		seal.setId("2c23809bba1147959c4a358b7a248dc5");
//		seal.setSealName("天才崔杨1");
//		seal.setSealCode("123123123");
//		UseDepartment useDepartment = useDepartmentService.selectUseDepartment("天才崔杨test2");
//		seal.setUseDepartmentCode(useDepartment.getCode());
//		seal.setUseDepartmentName(useDepartment.getName());
//		seal.setSealTypeCode("01");
//		seal.setSealShapeCode("01");
//		seal.setSealSize(20.2);
//		SealOperationRecord sealOperationRecord = new SealOperationRecord();
//		sealOperationRecord.setOperatorName("天才崔杨");          //经办人
//		sealOperationRecord.setOperatorTelphone("18405816966");
//		sealOperationRecord.setOperatorCertificateCode("330682199942144124");
//		sealOperationRecord.setOperatorCrtificateType("01");
//		seal.setSealOperationRecord(sealOperationRecord);
//		String operatorName = "天才崔杨1";
//		String operatorTelphone ="18405816966";
//		String operatorCertificateCode = "02";
//		String operatorCrtificateType ="01";
//		String operatorPhoto = "123";
//		String idCardScanner ="123123";
//		String proxy = "123123";
//		seal.setSealMakeTypeCode("02");
//		seal.setSealCenterImage("02");
//		seal.setSealStatusCode("02");
//		seal.setMakeDepartmentCode("123244");
//		seal.setMakeDepartmentName("杭州第一雕刻店");


//		int a =sealService.sealRecord(seal,user,districtId,operatorTelphone,operatorName,operatorCertificateCode,operatorCrtificateType,operatorPhoto,idCardScanner,proxy);
//		System.out.print(a);
//		PageInfo<Seal> a =sealService.sealInfo("12345",null,null, "01", 1,1);
//		System.out.print(a);
//		String electronicSealURL = "123123";
//		String sealScannerURL ="123123";
//		User user = new User();
//     	user.setTelphone("15512341234");
//		SealGetPerson sealGetPerson = new SealGetPerson();
//		sealGetPerson.setIsSame(true);
//		String operatorPhoto = "123";
//		String proxy ="123";
//		String businessScanner ="123";
//		SealOperationRecord sealOperationRecord = new SealOperationRecord();
//		sealOperationRecord.setEmployeeId("1231231231123");
//		sealOperationRecord.setSealCode("33010028601167");
//		sealOperationRecord.setEmplyeeName("杨");
//
//		String recordCode = "11233333333";
//		int a = sealService.loss(user, seal,  operatorPhoto,   proxy , businessScanner, sealOperationRecord, recordCode);
//		System.out.print(a);
//		User user = new User();
//		user.setId("9be068d2ec904057bcef955ce54de214");
//		String phone ="18405816966";
////		int  a = userPasswordService.getCheckCode(phone);
//		userPasswordService.resetPwd(phone,"467776","cuiyang");
////		System.out.print(a);

//		List<DistrictMenus> list = new ArrayList<>();
//		DistrictMenus menus = new DistrictMenus();
//		menus.setDistrictId("330101");
//		list.add(menus);
//		DistrictMenus menus1 = new DistrictMenus();
//		menus1.setDistrictId("330102");
//		list.add(menus1);
//
//		List<SealDistrict> sealCounts = sealDao.countByDistrictId(list);
//		System.out.print(sealCounts.toString());
	}

}
