package com.dhht.util;

public class shilUtil {

        public shilUtil()
        {
        }

        private String HexString(int N)
        {
            return String.format("%1$08x", N);
        }

        //rnd  -2^31 ~ +2^31
        //key1 0~65535
        //key1 0~65535
        //key1 0~65535
        //key1 0~65535

        //·µ»ØÖµ -2^31 ~ +2^31
        public static int shiled(int rnd, int key1, int key2, int key3, int key4)
        {
            System.out.println(String.format("%1$08x", rnd));

            int x1 = rnd & 0xffff;
            System.out.println(String.format("%1$d", x1));
            System.out.println(String.format("%1$x", x1));

            System.out.println();
            int x2 = (rnd >> 16) & 0xffff;
            System.out.println(String.format("%1$d", x2));
            System.out.println(String.format("%1$x", x2));

            //
            int y1 = x1 ^ key2;
            int y11 = x2 ^ key1;

            y1 = y1 + y11;
            y1 = y1 % 65536;

            if (y1>32767)
            {
                y1 -= 32767;
            }

            y1 = y1 ^16;
            y1 = y1 % 65536;

            if(y1 > 32767)
            {
                y1 -= 32767;
            }

            y1 %= key4;
            y1 = y1 % 65536;

            int y;
            y = y1 ^key3;
            if(y>50000)
            {
                y = y - 50000 - 1;
            }
            y11 = x1 + key1;
            y11 = y11 % 65536;

            if(y11 > 32767)
            {
                y11 -= 32767;
            }

            int y22 = y11 % key3;
            y22 = y22 % 65536;

            y11 = key4 ^ x2;
            int y2;
            y2 = y22 ^y11;

            if(y2 > 50000)
            {
                y2 = y2 - 50000 -1;
            }
            y ^= y2;

            y2 = y2 % 65536;

            int ReTurnData;
            ReTurnData = y2;

            ReTurnData = ReTurnData << 16;
            ReTurnData = ReTurnData ^ y;
            return ReTurnData ;
        }

//        public static void main(String[] args){
//            Integer rand= (int)( 0x7FFFFFFF *(Math.random() * 2 - 1));
//            //long sss=new shil().shiled(rand, 50864, 48764, 55064, 36164);
//            long sss=new shilUtil().shiled(rand, 12345, 12345, 12345, 12345);
//            System.out.print(sss);
//        }

        public static int rand(){
            Integer rand= (int)( 0x7FFFFFFF *(Math.random() * 2 - 1));
            return rand;
        }
    }


