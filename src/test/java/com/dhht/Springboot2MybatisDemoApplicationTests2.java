package com.dhht;

import com.dhht.controller.UserController;

import com.dhht.dao.UserDao;

import com.dhht.model.Users;

import com.dhht.service.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot2MybatisDemoApplicationTests2 {
	@Autowired
	private UserService suserService;

	@Autowired
	private UserDao suserDao;

	@Autowired
	private UserController suserController;

	@Test
	public void contextLoads() {
//		Users users = new Users();
//		users.setId("d3213c1e44944c269d3f7ac53172d9ff");
//		users.setUserName("test123456");
//		users.setRealName("test");
//		users.setRoleId("CJYH");
//		users.setRegionId("120105");
//		suserService.addUser(users);
//		suserController.addSuser(users);
//		suserDao.delete("00265c419a8347ee80b346b7d545f183");
//		List<Users> users = suserDao.findAllSuser();
//		System.out.print(users.toString());
//		JsonObjectBO jsonObjectBO = suserController.findAllSuser(1,2);
//		System.out.println(jsonObjectBO);
//        suserController.updateSuser(users);
//		suserController.deleteSuser("d3213c1e44944c269d3f7ac53172d9ff");
//		suserService.Update(users);
//		suserDao.update(users);
//      List rs = suserDao.findAllSuser();
//		System.out.println(rs.toString());
//		suserController.changePwd("cc2b31b7e1794e929e54a322bc6b30f7","messi123");
	}

}
