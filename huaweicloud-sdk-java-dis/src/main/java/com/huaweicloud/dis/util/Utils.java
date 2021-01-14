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

import static com.huaweicloud.dis.Constants.CACHE_TIME_OUT;
import static com.huaweicloud.dis.Constants.STREAMINFO_EXPIRETIME;
import static com.huaweicloud.dis.Constants.STREAMINFO_REAL_EXPIRETIME;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huaweicloud.dis.Constants;
import com.huaweicloud.dis.DIS;
import com.huaweicloud.dis.DISClientBuilder;
import com.huaweicloud.dis.DISConfig;
import com.huaweicloud.dis.core.util.StringUtils;
import com.huaweicloud.dis.iface.stream.request.DescribeStreamRequest;
import com.huaweicloud.dis.iface.stream.request.Tag;
import com.huaweicloud.dis.iface.stream.response.DescribeStreamResult;
import com.huaweicloud.dis.util.cache.StreamInfoCacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        String endpointPattern = "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+(:[1-9][0-9]{0,4})?([\\w-./?%&=]*)?$";
        
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
     * 获取指定通道的企业项目编号
     * @param streamName 通道名称
     * @param disConfig 用户配置
     * @return
     */
    public static String getStreamEpsId(String streamName, DISConfig disConfig) {
        DescribeStreamResult describeStreamResult;
        Map<String, String> streamInfoMap = getResultInMemory(streamName);
        if (!isMemoryInfoNull(streamInfoMap) && !StreamInfoCacheManager.getAkUserInfoCacheInstance()
            .isMemoryExpired(streamInfoMap)) {
            String streamInfoJsonString = streamInfoMap.get(streamName);
            describeStreamResult = JSONObject.parseObject(streamInfoJsonString,
                DescribeStreamResult.class);
        } else {
            DIS dic = DISClientBuilder.standard()
                .withEndpoint(disConfig.getEndpoint())
                .withAk(disConfig.getAK())
                .withSk(disConfig.getSK())
                .withProjectId(disConfig.getProjectId())
                .withRegion(disConfig.getRegion())
                .build();

            DescribeStreamRequest describeStreamRequest = new DescribeStreamRequest();
            describeStreamRequest.setStreamName(streamName);
            describeStreamResult = dic.describeStream(describeStreamRequest);
            if (describeStreamRequest != null) {
                streamInfoMap = new HashMap<>();
                String streamInfoString = JSON.toJSONString(describeStreamResult);
                streamInfoMap.put(streamName, streamInfoString);
                // 时延5分钟
                streamInfoMap.put(STREAMINFO_EXPIRETIME,
                    Long.toString(System.currentTimeMillis() + CACHE_TIME_OUT * 60 * 1000));
                streamInfoMap.put(STREAMINFO_REAL_EXPIRETIME,
                    Long.toString(System.currentTimeMillis() + CACHE_TIME_OUT * 60 * 1000));
                putResultInMemory(streamName, streamInfoMap);
            }
        }
        if (describeStreamResult != null) {
            List<Tag> sysTags = describeStreamResult.getSysTags();
            String epsId = "";
            for (Tag tag : sysTags) {
                if (tag.getKey().equals("_sys_enterprise_project_id")) {
                    epsId = tag.getValue();
                    break;
                }
            }
            return epsId;
        } else {
            return null;
        }

    }

    /**
     * 逻辑缓存时间为 5min
     *
     * @param streamInfoKey
     * @param userInfo
     */
    private static void putResultInMemory(String streamInfoKey, Map<String, String> userInfo) {
        try {
            StreamInfoCacheManager.getAkUserInfoCacheInstance().put(streamInfoKey, userInfo);
        } catch (Exception e) {
            LOG.error("put userPolicyInfo to Memory error. {}", e.getMessage());
        }
    }

    private static Map<String, String> getResultInMemory(String streamInfoKey) {
        return (Map<String, String>) StreamInfoCacheManager.getAkUserInfoCacheInstance().get(streamInfoKey);
    }

    private static boolean isMemoryInfoNull(Map<String, String> result) {
        if (result == null || result.isEmpty()) {
            return true;
        }
        return false;
    }
}
