package com.wall.ssm.rsa;

import java.util.ArrayList;

/**
 * @author twroten
 * @date 2019/11/27
 * @description
 **/
public class Edit {
    public static void main(String[] args) {
//        System.out.println("args = " + Arrays.deepToString(args));
//        System.out.println("Edit.main");
//        System.out.println("ParsInt");
//        System.out.println(true);

        String a = "三个字";
//        System.out.println("a = " + a);
//        int x = returnInt("1");
//        System.out.println("x = " + x);

        String arr[] = new String[]{"Tom", "Jerry", "Wroten"};
//        for (int i = 0; i < arr.length; i++) {
//            System.out.println("arr["+i+"] = " + arr[i]);
//        }
/*        for (String s : arr) {
            System.out.println("s = " + s);
        }*/
/*        for (int i = 0; i < arr.length; i++) {
            String s = arr[i];
            System.out.println("s = " + s);
        }*/
        ArrayList list = new ArrayList();
        list.add(123);
        list.add(456);
        list.add(789);
/*        for (Object o : list) {
            System.out.println("o = " + o);
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println("i = " + list.get(i));
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            System.out.println("i = " + list.get(i));
        }*/



    }

    public static String method(String args) {
        System.out.println("args = " + args);
        System.out.println("Edit.returnInt");
        ArrayList list = new ArrayList();
        list.add(123);
        list.add(456);
        list.add(789);
        if (list == null) {

        }
        if (list != null) {

        }
        if (list != null) {


        }
        if (list == null) {

        }
        return "1";
    }
    private static final Integer integer =new Integer("1");
}
