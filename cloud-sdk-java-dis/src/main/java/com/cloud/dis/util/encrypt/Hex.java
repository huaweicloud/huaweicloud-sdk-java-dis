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

package com.cloud.dis.util.encrypt;

/**
 * reference apache commons <a href="http://commons.apache.org/codec/">http://commons.apache.org/codec/</a>
 * 
 * @author Aub
 *         
 */
public class Hex
{
    
    /**
     * 用于建立十六进制字符的输出的小写字符数组
     */
    private static final char[] DIGITS_LOWER =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        
    /**
     * 用于建立十六进制字符的输出的大写字符数组
     */
    private static final char[] DIGITS_UPPER =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        
    /**
     * 将字节数组转换为十六进制字符数组
     * 
     * @param data byte[]
     * @return 十六进制char[]
     */
    public static char[] encodeHex(byte[] data)
    {
        boolean istoLowerCase = true;
        return encodeHex(data, istoLowerCase);
    }
    
    /**
     * 将字节数组转换为十六进制字符数组
     * 
     * @param data byte[]
     * @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
     * @return 十六进制char[]
     */
    public static char[] encodeHex(byte[] data, boolean toLowerCase)
    {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }
    
    /**
     * 将字节数组转换为十六进制字符数组
     * 
     * @param data byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制char[]
     */
    protected static char[] encodeHex(byte[] data, char[] toDigits)
    {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++)
        {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }
    
    /**
     * 将字节数组转换为十六进制字符串
     * 
     * @param data byte[]
     * @return 十六进制String
     */
    public static String encodeHexStr(byte[] data)
    {
        boolean istoLowerCase = true;
        return encodeHexStr(data, istoLowerCase);
    }
    
    /**
     * 将字节数组转换为十六进制字符串
     * 
     * @param data byte[]
     * @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
     * @return 十六进制String
     */
    public static String encodeHexStr(byte[] data, boolean toLowerCase)
    {
        return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }
    
    /**
     * 将字节数组转换为十六进制字符串
     * 
     * @param data byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制String
     */
    protected static String encodeHexStr(byte[] data, char[] toDigits)
    {
        return new String(encodeHex(data, toDigits));
    }
    
    /**
     * 将十六进制字符数组转换为字节数组
     * 
     * @param data 十六进制char[]
     * @return byte[]
     * @throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
     */
    public static byte[] decodeHex(char[] data)
    {
        
        int len = data.length;
        
        if ((len & 0x01) != 0)
        {
            throw new RuntimeException("Odd number of characters.");
        }
        
        byte[] out = new byte[len >> 1];
        
        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++)
        {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte)(f & 0xFF);
        }
        
        return out;
    }
    
    /**
     * 将十六进制字符转换成一个整数
     * 
     * @param ch 十六进制char
     * @param index 十六进制字符在字符数组中的位置
     * @return 一个整数
     * @throws RuntimeException 当ch不是一个合法的十六进制字符时，抛出运行时异常
     */
    protected static int toDigit(char ch, int index)
    {
        int digit = Character.digit(ch, 16);
        if (digit == -1)
        {
            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }
    
    // public static void main(String[] args) {
    // String srcStr = "待转换字符串";
    // String encodeStr = encodeHexStr(srcStr.getBytes());
    // String decodeStr = new String(decodeHex(encodeStr.toCharArray()));
    // System.out.println("转换前：" + srcStr);
    // System.out.println("转换后：" + encodeStr);
    // System.out.println("还原后：" + decodeStr);
    // }
    
}