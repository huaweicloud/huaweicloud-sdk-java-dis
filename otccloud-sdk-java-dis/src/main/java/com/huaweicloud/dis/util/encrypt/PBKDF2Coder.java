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

package com.otccloud.dis.util.encrypt;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * @author j00318840
 *         
 */
public class PBKDF2Coder
{
    
    private static final int iterations = 1000;
    
    // public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
    // String hexSalt = randomHex(8);
    // System.out.println(hexSalt);
    //
    // System.out.println(genHash("passwd", hexSalt));
    // }
    
    public static String randomHex(int bytes)
        throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[bytes];
        sr.nextBytes(salt);
        return toHex(salt);
    }
    
    public static String genHash(String data, String hexSalt, int length)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        char[] chars = data.toCharArray();
        byte[] salt = Hex.decodeHex(hexSalt.toCharArray());
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, length);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return toHex(hash);
    }
    
    public static String genHash(String data, String hexSalt)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return genHash(data, hexSalt, 64 * 16);
    }
    
    private static String toHex(byte[] array)
    {
        return Hex.encodeHexStr(array);
    }
}