package com.dhht;

import com.dhht.model.SealCode;
import com.dhht.service.seal.SealCodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private SealCodeService sealCodeService;

    @Test
    public void contextLoads(){
//        List<SealCode> sealCodes = sealCodeService.info("330000");
//        for (SealCode sealCode:sealCodes) {
//            System.out.println(sealCode.toString());
//        }
    }

}
