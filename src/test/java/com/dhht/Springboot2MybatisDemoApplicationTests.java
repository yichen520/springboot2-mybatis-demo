package com.dhht;

import com.dhht.controller.UserController;
import com.dhht.controller.UserDepartmentController;
import com.dhht.dao.UserDao;

import com.dhht.model.UseDepartment;
import com.dhht.model.UserDepartment;
import com.dhht.model.Users;
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
	private UserService userService;

	@Autowired
	private UserDao suserDao;

	@Autowired
	private UserController suserController;

	@Autowired
	private UseDepartmentService useDepartmentService;



	@Test
	public void contextLoads() {


		Users users = new Users();
//		users.setRoleId("GLY");
//		users.setRegionId("110100");

		System.out.println(userService.find(null,null,null,1,2).toString());
	}

}
