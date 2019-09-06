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

package com.huaweicloud.dis;


/**
 *
 */
public interface Constants
{
    /**
     * Http URI
     */
    String PATH = "/v1/data";

    String PUT_RECORDS_PATH = "/v2/records";

    String GET_RECORDS_PATH = "/v2/records";

    String GET_CURSORS_PATH = "/v2/cursors";

    String VERSION = "v2";
    
    String RECORDS_RESOURCES = "records";

    String CURSORS_RESOURCES = "cursors";

    String STREAMS_RESOURCES = "streams";

    String CHECKPOINT_RESOURCES = "checkpoints";

    String DATA_RESOURCES = "data";

    String SPLITSHARD_RESOURCES = "split";

    String MERGESHARDS_RESOURCES = "merge";

    int DEFAULT_THREAD_POOL_SIZE = 50;

    /********************************Data Signature********************************/

    String SERVICENAME = "dis";

    String SHARD_ID_PREFIX = "shardId";

    /**
     * HTTP响应码 401 : DIS用户认证失败
     */
    int HTTP_CODE_DIS_AUTHENTICATION_FAILED = 401;

    /**
     * HTTP响应码 400 : BAD_REQUEST
     */
    int HTTP_CODE_BAD_REQUEST = 400;

    /**
     * HTTP响应码 410 : GONE
     */
    int HTTP_CODE_GONE = 410;

    /**
     * HTTP响应码 413 : 请求体超大
     */
    int HTTP_CODE_REQUEST_ENTITY_TOO_LARGE = 413;

    /**
     * 错误码: 通道不存在
     */
    String ERROR_CODE_STREAM_NOT_EXISTS = "DIS.4301";

    /**
     * 错误码: 分区已过期
     */
    String ERROR_CODE_PARTITION_IS_EXPIRED = "DIS.4319";

    /**
     * 错误码: 分区不存在
     */
    String ERROR_CODE_PARTITION_NOT_EXISTS = "DIS.4302";

    /**
     * 错误码: 位移超过范围
     */
    String ERROR_CODE_SEQUENCE_NUMBER_OUT_OF_RANGE = "DIS.4224";

    /**
     * 错误码: 请求异常
     */
    String ERROR_CODE_REQUEST_ERROR = "DIS.4300";

    /**
     * 错误码: 超出流控
     */
    String ERROR_CODE_TRAFFIC_CONTROL_LIMIT = "DIS.4303";

    /**
     * 错误码: app已存在
     */
    String ERROR_CODE_APP_NAME_EXISTS = "DIS.4330";

    /**
     * 错误码: APP正在使用
     */
    String ERROR_CODE_APP_IN_USING = "DIS.4331";

    /**
     * 错误码: app不存在
     */
    String ERROR_CODE_APP_NAME_NOT_EXISTS = "DIS.4332";

    /**
     * 错误码: 超出APP配额上限
     */
    String ERROR_CODE_APP_QUOTA_EXCEEDED = "DIS.4329";

    /**
     * 错误码: 由于消费组处于rebalance状态，因此无法执行CheckPoint提交等操作
     */
    String ERROR_CODE_CONSUMER_GROUP_REBALANCE_IN_PROGRESS = "DIS.4701";

    /**
     * 错误码: 由于消费组已rebalanced，导致该消费者已被消费组剔除
     */
    String ERROR_CODE_CONSUMER_MEMBER_NOT_EXIST = "DIS.4702";

    /**
     * 错误码: 消费组客户端和服务端版本号不一致
     */
    String ERROR_CODE_CONSUMER_GROUP_ILLEGAL_GENERATION = "DIS.4703";

    /**
     * 错误描述: 时间戳异常 (对应错误码: {@link Constants#ERROR_CODE_REQUEST_ERROR})
     */
    String ERROR_INFO_TIMESTAMP_IS_EXPIRED = "timestamp is expired";

    /**
     * 错误描述: 位移超过范围 (对应错误码: {@link Constants#ERROR_CODE_SEQUENCE_NUMBER_OUT_OF_RANGE})
     */
    String ERROR_INFO_SEQUENCE_NUMBER_OUT_OF_RANGE = "Sequence_number out of range";

    /**
     * 错误描述: 通道名称不存在 (对应错误码: {@link Constants#ERROR_CODE_STREAM_NOT_EXISTS})
     */
    String ERROR_INFO_STREAM_NOT_EXISTS = "Stream does not exist";
    
    
    /******************************** Local Data Cache ********************************/
    /**
     * 缓存文件的前缀
     */
    String CACHE_FILE_PREFIX = "dis-cache-data-";
    
    /*
     * 缓存临时文件后缀
     */
    String CACHE_TMP_FILE_SUFFIX = ".tmp";
    
    /*
     * 缓存归档数据文件后缀
     */
    String CACHE_ARCHIVE_DATA_FILE_SUFFIX = ".data";
    
    /*
     * 缓存归档索引文件后缀
     */
    String CACHE_ARCHIVE_INDEX_FILE_SUFFIX = ".index";
    /****************************** Local Data Cache End ******************************/

    String COMPRESS_LZ4 = "lz4";

    String COMPRESS_SNAPPY = "snappy";

    String COMPRESS_ZSTD = "zstd";

    String COMPRESS_LZ4_CONTENT_LENGTH = "x-dis-lz4-content-length";

    String COMPRESS_ZSTD_CONTENT_LENGTH = "x-dis-zstd-content-length";

}
