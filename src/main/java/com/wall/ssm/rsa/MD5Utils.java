package com.wall.ssm.rsa;

import org.springframework.util.DigestUtils;

public class MD5Utils {

    public static String MD5(String value) {

        return DigestUtils.md5DigestAsHex(value.getBytes());

    }

    public static String MD5BySaltvalue(String value) {
        String saltvalue = "27VYwUw1umM2egVh";//ToolUtils.getSalt("saltvalue");
        String newvalue = value + saltvalue;
        System.out.println("newvalue===========" + newvalue);
        byte[] newvalue_bytes = newvalue.getBytes();

        for (int i = 0; i < newvalue_bytes.length; i++) {
            System.out.print(newvalue_bytes[i] + " ");
        }
        return DigestUtils.md5DigestAsHex(newvalue.getBytes());
    }

}
