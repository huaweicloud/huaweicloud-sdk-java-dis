/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huaweicloud.dis.util.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 加密工具类
 * 
 * @author j00318840
 */
public class EncryptUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(EncryptUtils.class);
    
    /**
     * 生成一定位数的安全随机数
     * 
     * @param bytes 随机数位数
     * @return 随机数进行 Hex小写编码的结果
     * @throws NoSuchAlgorithmException no algorithm found
     */
    public static String randomHex(int bytes)
        throws NoSuchAlgorithmException
    {
        return PBKDF2Coder.randomHex(bytes);
    }
    
    /**
     * 使用PBKDF2进行不可逆hash转换
     * 
     * @param data 待转换的文本
     * @param hexSalt 小写hex编码的盐值
     * @return 转换后的并经过hex小写编码的结果,HASH长度默认64*8
     * @throws NoSuchAlgorithmException no algorithm found
     * @throws InvalidKeySpecException invalid key spec
     */
    public static String PBKDF2encode(String data, String hexSalt)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return PBKDF2Coder.genHash(data, hexSalt);
    }
    
    /**
     * 使用PBKDF2进行不可逆hash转换
     * 
     * @param data 待转换的文本
     * @param hexSalt 小写hex编码的盐值
     * @param length HASH结果长度
     * @return 转换后的并经过hex小写编码的结果
     * @throws NoSuchAlgorithmException no algorithm found
     * @throws InvalidKeySpecException invalid key spec
     */
    public static String PBKDF2encode(String data, String hexSalt, int length)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return PBKDF2Coder.genHash(data, hexSalt, length);
    }
    
    /**
     * 生成AES 128位key,并做hex小写编码
     * 
     * @return key hex
     * @throws NoSuchAlgorithmException no algorithm found
     */
    public static String initAES128Key()
        throws NoSuchAlgorithmException
    {
        byte[] key = AESCoder.initSecretKey();
        return Hex.encodeHexStr(key);
    }
    
    /**
     * 对称加密文本，返回经hex小写编码的结果
     * 
     * @param data 待加密文本
     * @param hexKey 小写hex编码的key
     * @param hexIv 小写hex编码的IV
     * @return 加密结果
     * @throws InvalidKeyException InvalidKeyException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     */
    public static String encryptAES128(byte[] data, String hexKey, String hexIv)
        throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
        BadPaddingException, InvalidAlgorithmParameterException
    {
        byte[] key = Hex.decodeHex(hexKey.toCharArray());
        return encryptAES128(data, key, hexIv);
    }
    
    /**
     * 对称加密文本，返回经hex小写编码的结果
     * 
     * @param data 待加密文本
     * @param key key
     * @param hexIv 小写hex编码的IV
     * @return 加密结果
     * @throws InvalidKeyException InvalidKeyException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     */
    public static String encryptAES128(byte[] data, byte[] key, String hexIv)
        throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
        BadPaddingException, InvalidAlgorithmParameterException
    {
        byte[] encryptData = AESCoder.encrypt(data, key, Hex.decodeHex(hexIv.toCharArray()));
        return Hex.encodeHexStr(encryptData);

    }
    
    /**
     * 将hex格式的文本进行对称解密
     * 
     * @param hexData 待加密文本
     * @param hexKey 小写hex编码的key
     * @param hexIv 小写hex编码的IV
     * @return 解密结果
     * @throws InvalidKeyException InvalidKeyException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     */
    public static String decryptAES128(String hexData, String hexKey, String hexIv)
        throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
        BadPaddingException, InvalidAlgorithmParameterException
    {
        byte[] key = Hex.decodeHex(hexKey.toCharArray());
        return decryptAES128(hexData, key, hexIv);
    }
    
    /**
     * 将hex格式的文本进行对称解密
     * 
     * @param hexData 待加密文本
     * @param key 小写hex编码的key
     * @param hexIv 小写hex编码的IV
     * @return 解密结果
     * @throws InvalidKeyException InvalidKeyException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     */
    public static String decryptAES128(String hexData, byte[] key, String hexIv)
        throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
        BadPaddingException, InvalidAlgorithmParameterException
    {
        try
        {
            byte[] data = Hex.decodeHex(hexData.toCharArray());
            byte[] iv = Hex.decodeHex(hexIv.toCharArray());
            byte[] res = AESCoder.decrypt(data, key, iv);
            return new String(res, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.error("---------------UnsupportedEncoding----------------");
        }
        return null;
    }
    
    private static final String SPLIT_STR = "::";
    
    public static String gen(String[] srcKeys, byte[] pwd)
        throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException,
        IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException{
        // 分片算法，建议使用XOR
        StringBuffer srcKey = new StringBuffer();
        for (String tmp : srcKeys)
        {
            srcKey.append(tmp);
        }
        
        String hexSalt = EncryptUtils.randomHex(8);
        String key = EncryptUtils.PBKDF2encode(srcKey.toString(), hexSalt, 128);
        String hexIv = EncryptUtils.randomHex(16);
        String sK = EncryptUtils.encryptAES128(pwd, key, hexIv);
        
        String res = hexSalt + SPLIT_STR + hexIv + SPLIT_STR + sK;
        try
        {
            return Hex.encodeHexStr(res.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.error("---------------UnsupportedEncoding----------------");
        }
        return null;
    }
    
    /**
     * 根秘钥加密
     * 
     * @param srcKeys 秘钥分片
     * @param pwd 待加密数据
     * @return 加密结果
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     * @throws InvalidKeyException InvalidKeyException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static String gen(String[] srcKeys, String pwd)
        throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException,
        IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException
    {
        return gen(srcKeys, pwd.getBytes("UTF-8"));
    }
    
    /**
     * 根秘钥解密
     * 
     * @param srcKeys 秘钥分片
     * @param ser 待解密数据
     * @return 解密结果
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     * @throws InvalidKeyException InvalidKeyException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     */
    public static String dec(String[] srcKeys, String ser)
        throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException,
        IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException
    {
        // 分片算法，建议使用XOR
        StringBuffer srcKey = new StringBuffer();
        for (String tmp : srcKeys)
        {
            srcKey.append(tmp);
        }
        
        String s = "";
        try
        {
            s = new String(Hex.decodeHex(ser.toCharArray()), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.error("---------------UnsupportedEncoding----------------");
        }
        
        String[] tmps = s.split(SPLIT_STR);
        String key = EncryptUtils.PBKDF2encode(srcKey.toString(), tmps[0], 128);
        String iv = tmps[1];
        String jkspwd = EncryptUtils.decryptAES128(tmps[2], key, iv);
        return jkspwd;
    }
     
}