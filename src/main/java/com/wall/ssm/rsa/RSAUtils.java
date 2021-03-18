package com.wall.ssm.rsa;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

import net.sf.json.JSONObject;

public class RSAUtils {

	/** 算法名称 */
	private static final String ALGORITHM =  "RSA";
	/** 默认密钥大小 */
	private static final int KEY_SIZE = 1024;
	/** 用来指定保存密钥对的文件名和存储的名称 */
	private static final String PUBLIC_KEY_NAME = "publicKey";
	private static final String PRIVATE_KEY_NAME = "privateKey";
	private static final String PUBLIC_FILENAME = "public.properties";
	private static final String PRIVATE_FILENAME = "private.properties";

	/** 密钥对生成器 */
	private static KeyPairGenerator keyPairGenerator = null;
	private static KeyFactory keyFactory = null;
	/** 缓存的密钥对 */
	private static KeyPair keyPair = null;
	
	/** 初始化密钥工厂 */
	static{
		try {
			keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
			keyFactory = KeyFactory.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			
		}
	}
	/** 私有构造器 */
	private RSAUtils(){}

	
	/**
	 * 生成密钥对 将密钥分别用Base64编码保存到#public.key#和#private.key#文件中
	 * 保存的默认名称分别为publicKey和privateKey
	 */
	public static synchronized void generateKeyPair() {
		try {
			keyPairGenerator.initialize(KEY_SIZE,
					new SecureRandom(UUID.randomUUID().toString().replaceAll("-", "").getBytes()));
			keyPair = keyPairGenerator.generateKeyPair();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
		String publicKeyString = Base64.encodeBase64String(rsaPublicKey.getEncoded());
		String privateKeyString = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
		storeKey(publicKeyString, PUBLIC_KEY_NAME, PUBLIC_FILENAME);
		storeKey(privateKeyString, PRIVATE_KEY_NAME, PRIVATE_FILENAME);
	}
	
	/**
	 * 将指定的密钥字符串保存到文件中,如果找不到文件，就创建
	 * @param keyString
	 *            密钥的Base64编码字符串（值）
	 * @param keyName
	 *            保存在文件中的名称（键）
	 * @param fileName
	 *            目标文件名
	 */
	private static void storeKey(String keyString, String keyName, String fileName) {
		Properties properties = new Properties();
		String path = null;
		try {
			path = RSAUtils.class.getClassLoader().getResource(fileName).toString();
			path = path.substring(path.indexOf(":") + 1);
		} catch (NullPointerException e) {
			String classPath = RSAUtils.class.getClassLoader().getResource("").toString();
			String prefix = classPath.substring(classPath.indexOf(":") + 1);
			String suffix = fileName;
			File file = new File(prefix + suffix);
			try {
				file.createNewFile();
				path = file.getAbsolutePath();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		try (OutputStream out = new FileOutputStream(path)) {
			properties.setProperty(keyName, keyString);
			properties.store(out, "There is " + keyName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取密钥字符串
	 * @param keyName
	 *            需要获取的密钥名
	 * @param fileName
	 *            密钥所在文件
	 * @return Base64编码的密钥字符串
	 */
	private static String getKeyString(String keyName, String fileName) {
		try (InputStream in = new BufferedInputStream (new FileInputStream(System.getProperty("user.dir")+File.separator+"properties"+File.separator+fileName))) {
			Properties properties = new Properties();
			properties.load(in);
			return properties.getProperty(keyName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从文件获取RSA公钥
	 * @return RSA公钥
	 * @throws InvalidKeySpecException
	 */
	public static RSAPublicKey getPublicKey(String end) {
		try {
			byte[] keyBytes = Base64.decodeBase64(getKeyString(PUBLIC_KEY_NAME, "public"+end+".properties"));
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
			return (RSAPublicKey) keyFactory.generatePublic(x509EncodedKeySpec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 从文件获取RSA私钥
	 * @return RSA私钥
	 * @throws InvalidKeySpecException
	 */
	public static RSAPrivateKey getPrivateKey() {
		try {
			byte[] keyBytes = Base64.decodeBase64(getKeyString(PRIVATE_KEY_NAME, PRIVATE_FILENAME));
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
			return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * RSA公钥加密
	 * @param content
	 *            等待加密的数据
	 * @param publicKey
	 *            RSA 公钥 if null then getPublicKey()
	 * @return 加密后的密文(16进制的字符串)
	 */
	public static String encryptByPublic(byte[] content, PublicKey publicKey, String end) {
		if (publicKey == null) {
			publicKey = getPublicKey(end);
		}
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8 - 11;
			byte[][] arrays = splitBytes(content, splitLength);
			StringBuffer stringBuffer = new StringBuffer();
			for (byte[] array : arrays) {
				stringBuffer.append(bytesToHexString(cipher.doFinal(array)));
			}
			return stringBuffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] encryptByPublicByte(byte[] content, PublicKey publicKey, String end) {
		if (publicKey == null) {
			publicKey = getPublicKey(end);
		}
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8 - 11;
			byte[][] arrays = splitBytes(content, splitLength);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buff;
			for (byte[] array : arrays) {
				buff = cipher.doFinal(array);
				out.write(buff, 0, buff.length);
			}
			byte[] resultDatas = out.toByteArray();
			out.close();
			return resultDatas;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] encryptByPublicByteBase64(String text, PublicKey publicKey, String end) {
		if (publicKey == null) {
			publicKey = getPublicKey(end);
		}
		try {
			byte[] content = Base64.encodeBase64(text.getBytes());
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8 - 11;
			byte[][] arrays = splitBytes(content, splitLength);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buff;
			for (byte[] array : arrays) {
				buff = cipher.doFinal(array);
				out.write(buff, 0, buff.length);
			}
			byte[] resultDatas = out.toByteArray();
			out.close();
			return resultDatas;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String encryptByPublicBase64(String text, PublicKey publicKey, String end) {
		if (publicKey == null) {
			publicKey = getPublicKey(end);
		}
		try {
			byte[] content = Base64.encodeBase64(text.getBytes());
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8 - 11;
			byte[][] arrays = splitBytes(content, splitLength);
			StringBuffer stringBuffer = new StringBuffer();
			for (byte[] array : arrays) {
				stringBuffer.append(bytesToHexString(cipher.doFinal(array)));
			}
			return stringBuffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * RSA私钥加密
	 * @param content
	 *            等待加密的数据
	 * @param privateKey
	 *            RSA 私钥 if null then getPrivateKey()
	 * @return 加密后的密文(16进制的字符串)
	 */
	public static String encryptByPrivate(byte[] content, PrivateKey privateKey) {
		if (privateKey == null) {
			privateKey = getPrivateKey();
		}
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8 - 11;
			byte[][] arrays = splitBytes(content, splitLength);
			StringBuffer stringBuffer = new StringBuffer();
			for (byte[] array : arrays) {
				stringBuffer.append(bytesToHexString(cipher.doFinal(array)));
			}
			return stringBuffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String encryptByPrivateBase64(byte[] content, PrivateKey privateKey) {
		if (privateKey == null) {
			privateKey = getPrivateKey();
		}
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8 - 11;
			byte[][] arrays = splitBytes(Base64.encodeBase64(content), splitLength);
			StringBuffer stringBuffer = new StringBuffer();
			for (byte[] array : arrays) {
				stringBuffer.append(bytesToHexString(cipher.doFinal(array)));
			}
			return stringBuffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * RSA公钥解密
	 * @param content
	 *            等待解密的数据
	 * @param publicKey
	 *            RSA 公钥 if null then getPublicKey()
	 * @return 解密后的明文
	 */
	public static String decryptByPublic(String content, PublicKey publicKey) {
		if (publicKey == null) {
			publicKey = getPublicKey("");
		}
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8;
			byte[] contentBytes = hexStringToBytes(content);
			byte[][] arrays = splitBytes(contentBytes, splitLength);
			StringBuffer stringBuffer = new StringBuffer();
			for (byte[] array : arrays) {
				stringBuffer.append(new String(cipher.doFinal(array)));
			}
			return new String(stringBuffer.toString());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decryptByPublicBase64(String content, PublicKey publicKey, String end) {
		if (publicKey == null) {
			publicKey = getPublicKey(end);
		}
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8;
			byte[] contentBytes = hexStringToBytes(content);
			byte[][] arrays = splitBytes(contentBytes, splitLength);
			StringBuffer stringBuffer = new StringBuffer();
			for (byte[] array : arrays) {
				stringBuffer.append(new String(cipher.doFinal(array)));
			}
			return new String(Base64.decodeBase64(stringBuffer.toString()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * RSA私钥解密
	 * @param content
	 *            等待解密的数据
	 * @param privateKey
	 *            RSA 私钥 if null then getPrivateKey()
	 * @return 解密后的明文
	 */
	public static String decryptByPrivate(String content, PrivateKey privateKey) {
		if (privateKey == null) {
			privateKey = getPrivateKey();
		}
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8;
			byte[] contentBytes = hexStringToBytes(content);
			byte[][] arrays = splitBytes(contentBytes, splitLength);
			StringBuffer stringBuffer = new StringBuffer();
			for (byte[] array : arrays) {
				stringBuffer.append(new String(cipher.doFinal(array)));
			}
			return new String(stringBuffer.toString());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decryptByPrivateByte(byte[] content, PrivateKey privateKey) {
		if (privateKey == null) {
			privateKey = getPrivateKey();
		}
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8;
			byte[] contentBytes = content;
			byte[][] arrays = splitBytes(contentBytes, splitLength);
			StringBuffer stringBuffer = new StringBuffer();
			for (byte[] array : arrays) {
				stringBuffer.append(new String(cipher.doFinal(array)));
			}
			return new String(stringBuffer.toString());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decryptByPrivateByteBase64(byte[] content, PrivateKey privateKey) {
		if (privateKey == null) {
			privateKey = getPrivateKey();
		}
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8;
			byte[] contentBytes = content;
			byte[][] arrays = splitBytes(contentBytes, splitLength);
			StringBuffer stringBuffer = new StringBuffer();
			for (byte[] array : arrays) {
				stringBuffer.append(new String(cipher.doFinal(array)));
			}
			return new String(Base64.decodeBase64(stringBuffer.toString()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decryptByPrivateBase64(String content, PrivateKey privateKey) {
		if (privateKey == null) {
			privateKey = getPrivateKey();
		}
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8;
			byte[] contentBytes = hexStringToBytes(content);
			byte[][] arrays = splitBytes(contentBytes, splitLength);
			StringBuffer stringBuffer = new StringBuffer();
			for (byte[] array : arrays) {
				stringBuffer.append(new String(cipher.doFinal(array)));
			}
			return new String(Base64.decodeBase64(stringBuffer.toString()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * 根据限定的每组字节长度，将字节数组分组
	 * @param bytes
	 *            等待分组的字节组
	 * @param splitLength
	 *            每组长度
	 * @return 分组后的字节组
	 */
	public static byte[][] splitBytes(byte[] bytes, int splitLength) {
		// bytes与splitLength的余数
		int remainder = bytes.length % splitLength;
		// 数据拆分后的组数，余数不为0时加1
		int quotient = remainder != 0 ? bytes.length / splitLength + 1 : bytes.length / splitLength;
		byte[][] arrays = new byte[quotient][];
		byte[] array = null;
		for (int i = 0; i < quotient; i++) {
			// 如果是最后一组（quotient-1）,同时余数不等于0，就将最后一组设置为remainder的长度
			if (i == quotient - 1 && remainder != 0) {
				array = new byte[remainder];
				System.arraycopy(bytes, i * splitLength, array, 0, remainder);
			} else {
				array = new byte[splitLength];
				System.arraycopy(bytes, i * splitLength, array, 0, splitLength);
			}
			arrays[i] = array;
		}
		return arrays;
	}

	/**
	 * 将字节数组转换成16进制字符串
	 * @param bytes
	 *            即将转换的数据
	 * @return 16进制字符串
	 */
	public static String bytesToHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer(bytes.length);
		String temp = null;
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(0xFF & bytes[i]);
			if (temp.length() < 2) {
				sb.append(0);
			}
			sb.append(temp);
		}
		return sb.toString();
	}

	/**
	 * 将16进制字符串转换成字节数组
	 * @param hex
	 *            16进制字符串
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hex) {
		int len = (hex.length() / 2);
		hex = hex.toUpperCase();
		byte[] result = new byte[len];
		char[] chars = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(chars[pos]) << 4 | toByte(chars[pos + 1]));
		}
		return result;
	}

	/**
	 * 将char转换为byte
	 * @param c
	 * char
	 * @return byte
	 */
	private static byte toByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}


	public static void main(String[] args) {
  		generateKeyPair();//生成密钥
  		System.err.println(getKeyString(PUBLIC_KEY_NAME, PUBLIC_FILENAME));
		JSONObject obj = new JSONObject();
		String s = "测试一下";
		obj.put("key", s);
//		String c1 = RSAUtils.encryptByPublic(s.getBytes(),null);
//		String m1 = RSAUtils.decryptByPrivate(c1,null);
		String c2 = RSAUtils.encryptByPrivate(obj.toString().getBytes(),null);
		String m2 = RSAUtils.decryptByPublic(c2,null);
//		System.out.println(m1);
		System.out.println(m2);

	}
	
}
