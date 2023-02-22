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

package io.github.dis.util.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import io.github.dis.Constants;
import io.github.dis.DIS;
import io.github.dis.DISClientBuilder;
import io.github.dis.DISConfig;
import io.github.dis.iface.data.request.PutRecordsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.dis.util.IOUtils;
import io.github.dis.util.JsonUtils;

/**
 * 缓存重发线程
 *
 */
public class CacheResenderThread extends Thread
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtils.class); 
    
    private DISConfig disConfig;
    
    private static final int BUFFER_SIZE = 5 * 1024 * 1024; // 缓存读取大小：5M
    
    public CacheResenderThread(String name, DISConfig disConfig)
    {
        setDaemon(true);
        setName("Cache-ResenderThread-" + name);
        
        this.disConfig = disConfig;
    }
    
    @Override
    public void run()
    {
        
        // 归档缓存数据重传
        while (true)
        {
            LOGGER.info("BEGIN resend cache data.");
            
            // 重发缓存数据文件之前，先执行归档操作
            CacheManager cacheManager = CacheManager.getInstance(disConfig);
            cacheManager.archive();
            
            try
            {
                resend();
            }
            catch (IOException ioException)
            {
                LOGGER.error("Failed to resend cache data.", ioException);
            }
            
            LOGGER.info("END resend cache data.");
            
            try
            {
                // 10分钟一次数据重发
//                Thread.sleep(10 * 60 * 1000);
                Thread.sleep(2 * 60 * 1000);
            }
            catch (InterruptedException e)
            {
                LOGGER.error("Thread interrupt.", e);
            }
        }
    }
    
    private void resend() throws IOException
    {
        String dataCacheDir = this.disConfig.getDataCacheDir();
        File dataCacheDirFile = new File(dataCacheDir);
        if (!dataCacheDirFile.exists() || !dataCacheDirFile.isDirectory())
        {
            LOGGER.debug("Data Cache dir doesn't exist, continue.");
            return ;
        }
        
        File[] files = dataCacheDirFile.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            if (isArchiveFile(files[i]))
            {
                doResend(files[i], getIndexFile(files[i]));
            }
        }
    }
    
    /**
     * 重发缓存数据
     * 
     * @param dataFile 缓存数据文件
     * @param indexFile 缓存索引文件
     * @throws IOException
     */
    private void doResend(File dataFile, File indexFile) throws IOException
    {
        DIS dic = DISClientBuilder.standard()
            .withEndpoint(disConfig.getEndpoint())
            .withAk(disConfig.getAK())
            .withSk(disConfig.getSK())
            .withProjectId(disConfig.getProjectId())
            .withRegion(disConfig.getRegion())
            .build();
        
        String indexFileContents = getIndexFileContents(indexFile);
        String[] indexArray = indexFileContents.split(",");
        int resendIndex = Integer.parseInt(indexArray[0]);
        int totalIndex = Integer.parseInt(indexArray[1]);
        if (resendIndex == totalIndex)
        {
            // 该缓存文件已完成重发，清理归档文件
            clearArchiveFile(dataFile, indexFile);
        }
        else
        {
            InputStreamReader read = null;
            BufferedReader bufferedReader = null;
            
            try
            {
                read = new InputStreamReader(new FileInputStream(dataFile), "UTF-8"); // 考虑到编码格式
                bufferedReader = new BufferedReader(read, BUFFER_SIZE);
                String lineTxt = null;
                
                while ((lineTxt = bufferedReader.readLine()) != null)
                {
                    // 重发成功，则更新索引文件；重发失败，则认为网络未恢复，终止本次重发。
                    LOGGER.info("lineTxt: {}", lineTxt);
                    PutRecordsRequest putRecordsRequest = JsonUtils.jsonToObj(lineTxt, PutRecordsRequest.class);
                    try
                    {
                        dic.putRecords(putRecordsRequest);
                    }
                    catch (Exception e)
                    {
                        LOGGER.error("Failed to resend records, archive data filename: {}, break.", dataFile.getName(), e);
                        break;
                    }
                    
                    // 更新索引文件
                    resendIndex++;
                    updateIndexFile(resendIndex, totalIndex, indexFile);
                }
            }
            catch (IOException e)
            {
                throw e;
            }
            finally
            {
                bufferedReader.close();
                read.close();
            }
            
            if (resendIndex == totalIndex)
            {
                // 该缓存文件已完成重发，清理归档文件
                clearArchiveFile(dataFile, indexFile);
            }
        }
        
    }
    
    /**
     * 该文件是否为归档的缓存数据文件
     * @param file 缓存目录下的文件对象
     * @return {@code true} 是归档的缓存数据文件
     *          {@code false} 不是归档的缓存数据文件
     */
    private boolean isArchiveFile(File file)
    {
        String fileName = file.getName();
        if (fileName.contains(Constants.CACHE_ARCHIVE_DATA_FILE_SUFFIX))
        {
            return true;
        }
        return false;
    }
    
    private File getIndexFile(File dataFile) throws IOException
    {
        String dataFilePath = dataFile.getCanonicalPath();
        String indexFilePath = dataFilePath.replace(Constants.CACHE_ARCHIVE_DATA_FILE_SUFFIX, Constants.CACHE_ARCHIVE_INDEX_FILE_SUFFIX);
        
        return new File(indexFilePath);
    }
    
    private String getIndexFileContents(File indexFile) throws IOException
    {
        String lineTxt = null;
        InputStreamReader read = null;
        BufferedReader bufferedReader = null;
        
        try
        {
            read = new InputStreamReader(new FileInputStream(indexFile), "UTF-8"); // 考虑到编码格式
            bufferedReader = new BufferedReader(read);
            
            while ((lineTxt = bufferedReader.readLine()) != null)
            {
                break;
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally 
        {
            read.close();
            bufferedReader.close();
        }
        
        return lineTxt;
    }
    
    private void updateIndexFile(int resendIndex, int totalIndex, File indexFile) throws IOException
    {
        String newIndex = resendIndex + "," + totalIndex;
        IOUtils.writeToFile(newIndex, indexFile.getCanonicalPath());
    }
    
    private void clearArchiveFile(File dataFile, File indexFile)
    {
        dataFile.delete();
        indexFile.delete();
        LOGGER.debug("Clean-up work has been completed, archive data filename: {}.", dataFile.getName());
    }
}
