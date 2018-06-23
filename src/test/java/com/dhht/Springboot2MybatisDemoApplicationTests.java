package com.dhht;

import com.dhht.controller.UserController;
import com.dhht.dao.UserDao;

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



	@Test
	public void contextLoads() {


	}

}
