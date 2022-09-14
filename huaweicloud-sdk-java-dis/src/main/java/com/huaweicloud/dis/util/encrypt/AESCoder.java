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

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * <p>
 * AES Coder
 * </p>
 * <p>
 * secret key length: 128bit, default: 128 bit
 * </p>
 * <p>
 * mode: ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128
 * </p>
 * <p>
 * padding: Nopadding/PKCS5Padding/ISO10126Padding/
 * </p>
 *
 * @author Aub
 *
 */
public class AESCoder
{
    
    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "AES";

    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/GCM/NoPadding";

    private static final int GCM_TAG_LENGTH = 128;
    
    /**
     * 转换密钥
     *
     * @param key 二进制密钥
     * @return 密钥
     */
    private static Key toKey(byte[] key)
    {
        // 生成密钥
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }
    
    /**
     * 加密
     *
     * @param data 待加密数据
     * @param key 二进制密钥
     * @param ivbs 加密向量
     * @return byte[] 加密数据
     * @throws BadPaddingException BadPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeyException InvalidKeyException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     */
    public static byte[] encrypt(byte[] data, byte[] key, byte[] ivbs)
        throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
        BadPaddingException, InvalidAlgorithmParameterException
    {
        return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM, ivbs);
    }
    
    /**
     * 加密
     *
     * @param data 待加密数据
     * @param keybs 密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @param ivbs 加密向量
     * @return byte[] 加密数据
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeyException InvalidKeyException
     * @throws BadPaddingException BadPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     */
    public static byte[] encrypt(byte[] data, byte[] keybs, String cipherAlgorithm, byte[] ivbs)
        throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
        BadPaddingException, InvalidAlgorithmParameterException
    {
        Key key = toKey(keybs);
        // 实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);

        GCMParameterSpec iv = new GCMParameterSpec(GCM_TAG_LENGTH, ivbs);
        // 使用密钥初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        
        // 执行操作
        return cipher.doFinal(data);
    }
    
    /**
     * 解密
     *
     * @param data 待解密数据
     * @param key 二进制密钥
     * @param ivbs 加密向量
     * @return byte[] 解密数据
     * @throws BadPaddingException BadPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeyException InvalidKeyException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     */
    public static byte[] decrypt(byte[] data, byte[] key, byte[] ivbs)
        throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
        BadPaddingException, InvalidAlgorithmParameterException
    {
        return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM, ivbs);
    }
    
    /**
     * 解密
     *
     * @param data 待解密数据
     * @param keybs 密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @param ivbs 加密向量
     * @return byte[] 解密数据
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeyException InvalidKeyException
     * @throws BadPaddingException BadPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     */
    public static byte[] decrypt(byte[] data, byte[] keybs, String cipherAlgorithm, byte[] ivbs)
        throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
        BadPaddingException, InvalidAlgorithmParameterException
    {
        Key key = toKey(keybs);
        // 实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        // 使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, ivbs));
        // 执行操作
        return cipher.doFinal(data);
    }
    
}