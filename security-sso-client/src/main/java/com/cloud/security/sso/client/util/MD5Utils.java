package com.cloud.security.sso.client.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    /**
     * 用MD5算法进行加密
     *
     * @param str 需要加密的字符串
     * @return MD5加密后的结果
     */
    public static String md5(String str) {
        return encode(str);
    }

    private static String encode(String str) {
        MessageDigest md;
        String dstr = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            dstr = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return dstr;
    }

}
