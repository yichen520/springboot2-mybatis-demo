package com.dhht;

import com.dhht.controller.UserController;
import com.dhht.dao.DistrictMapper;
import com.dhht.dao.UserDao;
import com.dhht.model.District;
import com.dhht.model.SysLog;
import com.dhht.model.User;
import com.dhht.service.District.DistrictService;
import com.dhht.service.Log.LogService;
import com.dhht.service.user.UserService;
import com.dhht.service.userDepartment.UseDepartmentService;
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
//		districtService.insert("990110","990100","天才崔杨");
//		District district = new District();
//		district.setCityId("331100");
//		System.out.print(districtMapper.findByDistrictId(district).toString());
//		districtService.delete("990110");
		User user = new User();
		System.out.print(userService.delete("6ea9727b513646b3b2387cec1200fb01"));
	}

}
