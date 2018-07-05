package com.dhht.util;

import java.util.Random;

public class CodeUtil {

    public static String generate(){
        String code = null;
        Random random= new Random();
        for(int i=0;i<16;i++){
            Integer r= random.nextInt(10);
            code = code+r.toString();
        }
        return code;
    }
}
