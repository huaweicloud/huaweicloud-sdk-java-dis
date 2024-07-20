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

package com.huaweicloud.dis.util;

import com.huaweicloud.dis.Constants;
import com.huaweicloud.dis.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.regex.Pattern;

public class Utils
{
    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);
    
    private static final String UTF_8 = "UTF-8";
    
    public static byte[] encodingBytes(String value)
    {
        if (value == null)
        {
            return null;
        }
        byte[] bytes = null;
        try
        {
            bytes = value.getBytes(UTF_8);
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.error(e.getMessage(), e);
        }
        
        return bytes;
    }
    
    public static boolean isValidEndpoint(String endpoint)
    {
        String endpointPattern = "(?=.{1,255}$)^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+(:[1-9][0-9]{0,4})?([\\w-./?%&=]*)?$";
        
        Pattern pattern = Pattern.compile(endpointPattern);
        return pattern.matcher(endpoint).matches();
    }

    /**
     * change partitionId to shardId, e.g: 0 to shardId-0000000000
     * @param partitionId partitionId
     * @return shardId
     */
    public static String getShardIdFromPartitionId(String partitionId)
    {
        if (StringUtils.isNullOrEmpty(partitionId) || partitionId.startsWith(Constants.SHARD_ID_PREFIX))
        {
            return partitionId;
        }

        try
        {
            int id = Integer.parseInt(partitionId);
            if (id >= 0)
            {
                return String.format(Constants.SHARD_ID_PREFIX + "-%010d", Integer.parseInt(partitionId));
            }
        }
        catch (Exception ignored)
        {
        }
        return partitionId;
    }

    /**
     * Thread sleep time in millis
     * @param millis sleep time
     */
    public static void sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException ignored)
        {
        }
    }

    /**
     * 读取Java默认的TrustStore
     *
     * @return Default Java TrustStore
     * @throws KeyStoreException KeyStoreException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws CertificateException CertificateException
     * @throws IOException
     */
    public static KeyStore getDefaultTrustStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
    {
        String property = System.getProperty("javax.net.ssl.trustStore");
        final String CACERTS_PATH = property == null ? System.getProperties().getProperty("java.home") + File.separator + "lib" + File.separator + "security"
            + File.separator + "cacerts"
            : property;
        final String CACERTS_PASSWORD = System.getProperty("javax.net.ssl.trustStorePassword");

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream in = null;

        try
        {
            if (!StringUtils.isNullOrEmpty(CACERTS_PATH) && !StringUtils.isNullOrEmpty(CACERTS_PASSWORD))
            {
                in = new FileInputStream(CACERTS_PATH);
                trustStore.load(in, CACERTS_PASSWORD.toCharArray());
            }
        }
        finally
        {
            if (null != in)
            {
                in.close();
            }
        }

        return trustStore;
    }
}
