package com.wall.ssm.rsa;

import org.apache.commons.codec.binary.Base64;

import net.sf.json.JSONObject;

public class TestDemo {

	public static void main(String[] args) {
		//生成公、私密钥方法
		RSAUtils.generateKeyPair();//调用一次生成即可
		
		//加密解密方法
		JSONObject json = new JSONObject();
		json.put("key", "明文：RSAencrypt");
		//base64编码+RSA加密
		byte[] rsa_string = RSAUtils.encryptByPublicByteBase64(json.toString(),null,"");//""用的是本地公钥
		//第二次base64编码
		String base = Base64.encodeBase64String(rsa_string);
		System.out.println("base = " + base);
		//生成MD5
		String md5 = MD5Utils.MD5BySaltvalue(base);
		System.out.println("md5 = " + md5);

		/****************http-body=base*****************/
		
		/****************接收*****************/
		//MD5验签
		System.out.println("base======"+base);
		String recv_md5 = MD5Utils.MD5BySaltvalue(base);
		System.out.println("recv_md5 = " + recv_md5);
		System.out.println(MD5Utils.MD5BySaltvalue(base).equals(md5));
		//第一次base64解码
		byte[] d_base = Base64.decodeBase64(base);
		//RSA解密+第二次base64解码
		String d_rsa = RSAUtils.decryptByPrivateByteBase64(d_base,null);//用本地私钥
		
		System.out.println(d_rsa);
	}
}
