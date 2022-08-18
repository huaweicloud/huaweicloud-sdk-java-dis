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

package com.otccloud.dis.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.otccloud.dis.core.util.StringUtils;
import com.otccloud.dis.exception.DISClientException;

/**
 * IO Utils
 */
public class IOUtils
{
    
    private static final Logger LOG = LoggerFactory.getLogger(IOUtils.class);
    
    // Max file size: 128M
    private static final long MAX_FILE_LENGTH = 128 * 1024 * 1024;
    
    // Max file name length: 128
    private static int MAX_FILE_NAME_LENGTH = 128;
    
    // 流控单元：1000ms
    final static int STATISTICAL_UNIT = 1000;
    
    /**
     * 以字节方式读取文件，每次读取执行回调函数 {@link IOHandler}
     * 
     * @param filePath 文件路径
     * @param readBytesPerTime 每次读取字节数
     * @param bandwidth 流通道带宽，单位为M
     * @param handler 回调函数
     * @see IOHandler
     */
    public static void readFileByBytes(String filePath, int readBytesPerTime, int bandwidth, IOHandler<ByteBuffer> handler)
    {
        File file = new File(filePath);
        checkFileLength(file);
        
        InputStream in = null;
        
        try
        {
            byte[] tempbytes = new byte[readBytesPerTime];
            int byteRead = 0;
            int index = 0;
            long fileLength = file.length();
            int fileFlag = (int)(fileLength % readBytesPerTime);
            int maxIndex = (int)(fileLength / readBytesPerTime);
            in = new FileInputStream(file);
            
            HashMap<Long, Integer> trafficInUnitMap = new HashMap<>(); // 流控单元内流量
            
            while ((byteRead = in.read(tempbytes)) != -1)
            {
                LOG.debug("Byte reads: " + byteRead);
                if (byteRead < readBytesPerTime || (fileFlag == 0 && index == (maxIndex - 1)))
                {
                    // 文件末尾
                    byte[] lastTempBytes = Arrays.copyOf(tempbytes, byteRead);
                    handler.doLastIO(ByteBuffer.wrap(lastTempBytes), index);
                    trafficInUnitMap.clear();
                }
                else
                {
                    long timestamp = System.currentTimeMillis();
                    long currentUnit = timestamp / STATISTICAL_UNIT;
                    
                    Integer trafficInUnit = trafficInUnitMap.get(currentUnit);
                    if (trafficInUnit == null)
                    {
                        trafficInUnit = readBytesPerTime;
                        trafficInUnitMap.put(currentUnit, trafficInUnit);
                    }
                    else
                    {
                        trafficInUnit += readBytesPerTime;
                        if (trafficInUnit > bandwidth * 1024 * 1024 * 0.8)
                        {
                            // 流控单元内流量超过80%带宽，线程休眠至下个流控单元
                            int sleepTime = (int)((currentUnit + 1) * STATISTICAL_UNIT - timestamp);
                            try
                            {
                                LOG.debug("Exceed Traffic Control, sleep for {} ms.", sleepTime);
                                Thread.sleep(sleepTime);
                            }
                            catch (InterruptedException e)
                            {
                                LOG.error(e.getMessage(), e);
                            }
                            
                            trafficInUnit = readBytesPerTime;
                            trafficInUnitMap.put(currentUnit + 1, trafficInUnit);
                        }
                        else
                        {
                            trafficInUnitMap.put(currentUnit, trafficInUnit);
                        }
                    }
                    handler.doInIO(ByteBuffer.wrap(tempbytes), index);
                    index++;
                }
            }
        }
        catch (IOException exception)
        {
            LOG.error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e1)
                {
                    LOG.error("Fail to close InputStream.", e1);
                }
            }
        }
    }
    
    public static void checkFileLength(File file)
    {
        if (!file.exists())
        {
            throw new RuntimeException("File not found.");
        }
        if (file.length() > MAX_FILE_LENGTH)
        {
            throw new RuntimeException("Files bigger than 128M is not allowed now.");
        }
        
        if (file.length() == 0)
        {
            throw new RuntimeException("Empty file.");
        }
    }
    
	/**
	 * 
	 * <p>
	 * 检查文件名是否合法
	 * </p>
	 * <p>
	 * 校验规则：允许使用反斜杠分割多级目录和文件，目录和文件名称不能包含特殊字符
	 * </p>
	 * 
	 * @param fileName
	 *            文件名
	 * @return {@code true} 文件名合法 {@code false} 文件名不合法
	 */
    public static boolean isValidFileName(String fileName)
    {
        // 文件名校验
        if (fileName == null || fileName.length() > MAX_FILE_NAME_LENGTH)
        {
            return false;
        }
        String[] paths = fileName.split("/");
        for (int i = 0; i < paths.length - 1; i++)
        {
            if (StringUtils.isNullOrEmpty(paths[i]) || paths[i].matches("^\\..*$|^.*[\"*<>?|/:\\\\]+.*$|.*\\.$"))
            {
                return false;
            }
        }
        return !StringUtils.isNullOrEmpty(paths[paths.length - 1])
            && !paths[paths.length - 1].matches("^.*[\"*<>?|/:\\\\]+.*$");
    }
    
    public static interface IOHandler<DATATYPE>
    {
        
        /**
         * 文件读取过程中需要执行的回调操作
         * 
         * @param readType 读取类型
         * @param index 本次读取索引
         */
        void doInIO(DATATYPE readType, int index);
        
        /**
         * 文件最后一次读取需要执行的回调操作
         * 
         * @param readType 读取类型
         * @param index 本次读取索引
         */
        void doLastIO(DATATYPE readType, int index);
    }
    
    /**
     * 追加写入文件
     * @param data 待写入缓存文件的数据
     * @param file 文件路径
     */
    public static void appendToFile(String data, String file)
    {
        FileWriter writer = null;
        try
        {
            // 追加写 
            writer = new FileWriter(file, true);
            writer.write(data + System.getProperty("line.separator"));
        }
        catch (IOException e)
        {
            LOG.error("Failed to append to file.");
            throw new DISClientException(e);
        }
        finally
        {
            if (writer != null)
            {
                try
                {
                    writer.close();
                }
                catch (IOException e)
                {
                    LOG.error("Failed to close writer.", e);
                }
            }
        }
    }
    
    /**
     * 写入文件，文件存在则覆盖
     * @param data 待写入缓存文件的数据
     * @param file 文件路径
     */
    public static void writeToFile(String data, String file)
    {
        BufferedWriter out = null;
        try
        {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            out.write(data);
        }
        catch (Exception e)
        {
            LOG.error("Failed to write to file.");
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                LOG.error("Failed to close writer.", e);
            }
        }
    }
    
}
