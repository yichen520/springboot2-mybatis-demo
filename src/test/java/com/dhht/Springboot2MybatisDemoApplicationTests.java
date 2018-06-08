package com.dhht;

import com.dhht.model.Makedepartment;
import com.dhht.model.Resource;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.resource.Impl.ResourceImpl;
import com.dhht.service.resource.ResourceService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot2MybatisDemoApplicationTests {
    @Autowired
   private ResourceService resourceService ;
    @Autowired
   private MakeDepartmentService makeDepartmentService;

   @Test
	public void contextLoads() throws Exception{
		List<Resource> list  = new ArrayList<Resource>();
		list = resourceService.selectAllResource();
       for (Resource resource: list) {
           System.out.println(resource.getDescription());
           for (Resource r:resource.getChildren()) {
               System.out.println(r.getDescription());
               for (Resource ro:r.getChildren()) {
                   System.out.println(ro.getDescription());
               }
           }
       }


	}

	@Test
	public void MyTest() throws Exception{
		List<String> list = new ArrayList<String>();
		list.add("0c925bc4667842909c9967adef98de9a");
	}

}
