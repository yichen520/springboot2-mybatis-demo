package com.dhht.util;

import java.util.Random;

public class CodeUtil {

    public static String generate(){

        Random random= new Random();
        String code = new Integer(random.nextInt(10)).toString() ;
        for(int i=0;i<15;i++){
            Integer r= random.nextInt(10);
            code = code+r.toString();
        }
        return code;
    }
}
