package com.dhht;

import com.dhht.common.JsonObjectBO;
import com.dhht.controller.UserController;
import com.dhht.dao.DistrictMapper;
import com.dhht.dao.UserDao;
import com.dhht.model.District;
import com.dhht.model.SysLog;
import com.dhht.model.UseDepartment;
import com.dhht.model.User;
import com.dhht.service.District.DistrictService;
import com.dhht.service.Log.LogService;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.dhht.service.user.UserPasswordService;
import com.dhht.service.user.UserService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
	private LogService logService;

	@Autowired
	private DistrictService districtService;

	@Autowired
	private DistrictMapper districtMapper;

	@Test
	public void contextLoads() {
//		UseDepartment useDepartment = new UseDepartment();
//		useDepartment.setId("22042e5dced44a3d83e17127c555a35f");
//		useDepartment.setCode("2");
//		useDepartment.setIsDelete(false);
//		useDepartment.setVersion(1);
//		useDepartment
	}

}
