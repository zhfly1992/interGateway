/*
* File name: Des3Util.java								
*
* Purpose:
*
* Functions used and called:	
* Name			Purpose
* ...			...
*
* Additional Information:
*
* Development History:
* Revision No.	Author		Date
* 1.0			zhabing		2018年8月28日
* ...			...			...
*
***************************************************/

package com.run.big.data.center.util;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
* @Description:	采用des3的CBC算法
* @author: zhabing
* @version: 1.0, 2018年8月28日
*/

public class Des3Util {
	private static byte[] keyiv = { 1, 2, 3, 4, 5, 6, 7, 8 };
    /**
     * 加密
     */
    public static String des3EncodeCBC(String keyStr, String dataStr)
            throws Exception {
        byte[] key = Base64.getDecoder().decode(keyStr);
        //补位到24位
        byte[] tmp = new byte[24];
        if(key.length<tmp.length){
            System.arraycopy(key,0,tmp,0,key.length);
            key = tmp;
        }
        byte[] data = dataStr.getBytes("UTF-8");
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        String code = Base64.getEncoder().encodeToString(bOut);
        return code;
    }
    /**
     * 解密
     */
    public static String des3DecodeCBC(String keyStr, String codeCBC)
            throws Exception {
        byte[] key = Base64.getDecoder().decode(keyStr);
        //补位到24位
        byte[] tmp = new byte[24];
        if(key.length<tmp.length){
            System.arraycopy(key,0,tmp,0,key.length);
            key = tmp;
        }
        byte[] data = Base64.getDecoder().decode(codeCBC);
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);

        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        byte[] bOut = cipher.doFinal(data);
        String dataStr = new String(bOut, "UTF-8");
        return dataStr;

    }
}
