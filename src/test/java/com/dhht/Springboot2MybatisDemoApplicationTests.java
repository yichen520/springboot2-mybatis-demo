package com.dhht;

import com.dhht.util.face.demo.AFRTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot2MybatisDemoApplicationTests {

	@Test
	public void contextLoads() {
		String file1 = "D://picture/3.jpg";
		String file2= "D://picture/4.jpg";
		AFRTest afrTest = new AFRTest();
		float f = afrTest.compareImage(file1,file2);
	}

}
