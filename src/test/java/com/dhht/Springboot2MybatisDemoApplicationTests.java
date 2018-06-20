package com.dhht;

import com.dhht.controller.UserController;
import com.dhht.controller.UserDepartmentController;
import com.dhht.dao.UserDao;

import com.dhht.model.UseDepartment;
import com.dhht.model.UserDepartment;
import com.dhht.service.user.UserService;
import com.dhht.service.userDepartment.UseDepartmentService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot2MybatisDemoApplicationTests {
	@Autowired
	private UserService suserService;

	@Autowired
	private UserDao suserDao;

	@Autowired
	private UserController suserController;

	@Autowired
	private UseDepartmentService useDepartmentService;



	@Test
	public void contextLoads() {

//		UseDepartment useDepartment = new UseDepartment();
//		useDepartment.setId("91e79dfa737e4715a900939be80ec849");
//		useDepartment.setAddress("中国山东");
//		useDepartment.setCode("234");
//		useDepartment.setDepartmentCertificate("111");
//		useDepartment.setDepartmentCertificateType("11");
//		useDepartment.setDepartmentStatus("123");
//		useDepartment.setDistrictId("123");
//		useDepartment.setEnglishAhhr("123");
//		useDepartment.setEnglishName("dhht");
//		useDepartment.setLegalCountry("123");
//		useDepartment.setName("dhht");
//		useDepartment.setStatus(2);
//		useDepartment.setLegalName("cy123");
//		useDepartment.setLegalId("123123");
//		useDepartment.setLegalIdType("123123");
//		useDepartment.setTelphone("1231231231");
//		useDepartment.setStatus(1);
//		useDepartment.setIsDelete(false);
		System.out.println(useDepartmentService.findAllMakeBySize(1,2).toString());
	}

}
