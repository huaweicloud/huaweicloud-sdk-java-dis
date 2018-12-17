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

package com.huaweicloud.dis.util.cache;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huaweicloud.dis.DISConfig;
import com.huaweicloud.dis.exception.DISClientException;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequest;
import com.huaweicloud.dis.util.JsonUtils;

/**
 * <p>
 * 本地缓存管理
 * </p>
 *
 */
public class CacheManager implements ICacheManager<PutRecordsRequest>
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);
    
    private static final String CACHE_FILE_PREFIX = "dis-cache-data-";
    
    /*
     * 缓存临时文件后缀
     */
    private static final String CACHE_TMP_FILE_SUFFIX = ".tmp";
    
    /*
     * 缓存归档数据文件后缀
     */
    private static final String CACHE_ARCHIVE_DATA_FILE_SUFFIX = ".data";
    
    /*
     * 缓存归档索引文件后缀
     */
    private static final String CACHE_ARCHIVE_INDEX_FILE_SUFFIX = ".index";
    
    private static CacheManager instance;
    
    private DISConfig disConfig;
    
    private File dataTmpFile; // 临时缓存数据文件对象
    
    private String dataTmpFileName; // 临时缓存数据文件名
    
    private long tmpFileCreateTime; // 临时缓存文件创建时间
    
    private CacheManager()
    {
    }
    
    private CacheManager(DISConfig disConfig)
    {
        this.disConfig = disConfig;
    }
    
    public static synchronized CacheManager getInstance(DISConfig disConfig)
    {
        if (instance == null)
        {
            instance = new CacheManager(disConfig);
            instance.init();
        }
        return instance;
    }
    
    @Override
    public void putToCache(PutRecordsRequest putRecordsRequest)
    {
        LOGGER.info("Put records to cache, record size: {}, cache dir: {}.",
            putRecordsRequest.getRecords().size(),
            disConfig.getDataCacheDir());
        String data = JsonUtils.objToJson(putRecordsRequest);
        
        synchronized (this)
        {
            if (needToArchive(data))
            {
                // 缓存临时文件归档
                LOGGER.debug("Need to archive cache tmp file, filename: '{}'.", dataTmpFileName);
                archive();
            }
            
            if (hasEnoughSpace(data))
            {
                writeToFile(data);
            }
            else
            {
                LOGGER.error("Put to cache failed, cache space is not enough, configured max dir size: {}.",
                    disConfig.getDataCacheDiskMaxSize());
            }
        }
    }
    
    @Override
    public boolean hasEnoughSpace(String data)
    {
        long dataSize = getDataSize(data);
        if ((dataSize + FileUtils.sizeOfDirectory(new File(getCacheDir()))) / 1024 / 1024 > disConfig.getDataCacheDiskMaxSize())
        {
            return false;
        }
        
        return true;
    }
    
    private boolean needToArchive(String data)
    {
        long dataSize = getDataSize(data);
        if (((dataSize + FileUtils.sizeOf(dataTmpFile)) / 1024 / 1024 > disConfig.getDataCacheArchiveMaxSize())
            || (System.currentTimeMillis() - tmpFileCreateTime > disConfig.getDataCacheArchiveLifeCycle() * 1000))
        {
            return true;
        }
        
        return false;
    }
    
    private long getDataSize(String data)
    {
        long dataSize = 0;
        try
        {
            dataSize = data.getBytes("UTF-8").length;
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("Failed to calculate data size.");
            throw new DISClientException(e);
        }
        
        return dataSize;
    }
    
    /**
     * 临时缓存文件归档
     */
    private void archive()
    {
        if (dataTmpFile == null || !dataTmpFile.exists())
        {
            LOGGER.error("Tmp cache file not exist, filename: '{}'.", dataTmpFileName);
            return ;
        }
        
        String archiveDataFilename = dataTmpFileName.replace(CACHE_TMP_FILE_SUFFIX, "") + CACHE_ARCHIVE_DATA_FILE_SUFFIX;
        String archiveIndexFilename = dataTmpFileName.replace(CACHE_TMP_FILE_SUFFIX, "") + CACHE_ARCHIVE_INDEX_FILE_SUFFIX;
        File archiveDataFile = new File(archiveDataFilename);
        File archiveIndexFile = new File(archiveIndexFilename);
        try
        {
            FileUtils.moveFile(dataTmpFile, archiveDataFile);
            archiveIndexFile.createNewFile();
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to create archive files.", e);
            archiveDataFile.deleteOnExit();
            archiveIndexFile.deleteOnExit();
            return ;
        }
        
        // 重置缓存临时文件
        reset();
        init();
    }
    
    /**
     * 初始化
     */
    private void init()
    {
        Long timestamp = System.currentTimeMillis();
        String cacheTmpDataFileName = CACHE_FILE_PREFIX + timestamp + CACHE_TMP_FILE_SUFFIX;
        
        LOGGER.debug("Cache tmp data file name: '{}'.", cacheTmpDataFileName);
        if (dataTmpFile == null || !dataTmpFile.exists())
        {
            try
            {
                // 生成缓存数据文件和缓存索引文件
                dataTmpFileName = getCacheDir() + File.separator + cacheTmpDataFileName;
                dataTmpFile = new File(dataTmpFileName);
                dataTmpFile.createNewFile();
                tmpFileCreateTime = System.currentTimeMillis();
            }
            catch (IOException e)
            {
                LOGGER.error("Failed to create cache tmp file.", e);
                throw new DISClientException(e);
            }
        }
    }
    
    /**
     * 重置
     */
    private void reset()
    {
        dataTmpFile = null;
        dataTmpFileName = null;
    }
    
    /**
     * 获取配置的缓存目录路径
     * @return 存放缓存文件的目录
     * @throws IOException
     */
    private String getCacheDir()
    {
        String dataCacheDir = disConfig.getDataCacheDir();
        File file = new File(dataCacheDir);
        if (!file.exists())
        {
            file.mkdirs();
        }
        
        try
        {
            return file.getCanonicalPath();
        }
        catch (IOException e)
        {
            LOGGER.error("Invalid cache dir: {}.", disConfig.getDataCacheDir());
            throw new DISClientException(e);
        }
    }
    
    /**
     * 写入缓存文件
     * @param data 待写入缓存文件的数据
     */
    private void writeToFile(String data)
    {
        try
        {
            // 追加写 
            FileWriter writer = new FileWriter(dataTmpFileName, true);
            writer.write(data + System.getProperty("line.separator"));
            writer.close();
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to write cache file.");
            throw new DISClientException(e);
        }
    }
    
}
