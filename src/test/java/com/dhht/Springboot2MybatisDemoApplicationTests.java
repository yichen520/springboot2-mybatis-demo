package com.dhht;

import com.dhht.controller.UserController;
import com.dhht.dao.UserDao;
import com.dhht.service.user.UserService;
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

	@Test
	public void contextLoads() {
//		Users users = new Users();
//		users.setId("123");
//		users.setUserName("123412");
//		users.setRealName("test032");
//		users.setRoleId("CJYH");
//		users.setRegionId("120102");
//		suserService.addUser(users);
//		suserController.addSuser(users);
//		suserDao.delete("00265c419a8347ee80b346b7d545f183");
//		List<Users> users = suserDao.findAllSuser();
//		System.out.print(users.toString());
//		JsonObjectBO jsonObjectBO = suserController.findAllSuser(1,2);
//		System.out.println(jsonObjectBO);
//        suserController.updateSuser(users);
//		suserController.deleteSuser("314e7ebeeca44c33a926b53bd19af2bd");
//		suserService.Update(users);
//		suserDao.update(users);
//        List rs = suserDao.findAllSuser();
//		System.out.println(rs.toString());
		suserController.changePwd("08a1f9788b984f8a9e234e206dd0df75","messi");
	}

}
