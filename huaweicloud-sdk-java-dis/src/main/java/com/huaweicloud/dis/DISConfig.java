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

import com.huaweicloud.dis.core.ClientParams;
import com.huaweicloud.dis.core.auth.credentials.BasicCredentials;
import com.huaweicloud.dis.core.auth.credentials.Credentials;
import com.huaweicloud.dis.core.util.StringUtils;
import com.huaweicloud.dis.http.Protocol;
import com.huaweicloud.dis.util.compress.CompressionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DISConfig extends Properties implements ClientParams {
    private static final Logger LOG = LoggerFactory.getLogger(DISConfig.class);

    /**
     * 默认配置文件名
     */
    private static final String FILE_NAME = "dis.properties";

    private static final String DEFAULT_VALUE_REGION_ID = null;
    private static final String DEFAULT_VALUE_ENDPOINT = null;
    private static final String DEFAULT_VALUE_PROJECT_ID = null;

    private static final int DEFAULT_VALUE_CONNECTION_TIMEOUT = 30;
    private static final int DEFAULT_VALUE_SOCKET_TIMEOUT = 60;
    private static final int DEFAULT_VALUE_MAX_PER_ROUTE = 100;
    private static final int DEFAULT_VALUE_MAX_TOTAL = 500;
    private static final boolean DEFAULT_VALUE_IS_DEFAULT_TRUSTED_JKS_ENABLED = false;
    private static final boolean DEFAULT_VALUE_IS_DEFAULT_DATA_ENCRYPT_ENABLED = false;
    private static final boolean DEFAULT_VALUE_DATA_COMPRESS_ENABLED = false;
    private static final boolean DEFAULT_VALUE_DATA_CACHE_ENABLED = false;
    private static final String DEFAULT_VALUE_DATA_CACHE_DIR = "/data/dis";
    private static final int DEFAULT_VALUE_DATA_CACHE_DIR_MAX_SIZE = 2048;
    private static final int DEFAULT_VALUE_DATA_CACHE_ARCHIVE_MAX_SIZE = 512;
    private static final int DEFAULT_VALUE_DATA_CACHE_ARCHIVE_LIFE_CYCLE = 60;

    private static final BodySerializeType DEFAULT_VALUE_BODY_SERIALIZE_TYPE = BodySerializeType.json;
    private static final boolean DEFAULT_VALUE_ORDER_BY_PARTITION = false;
    private static final int DEFAULT_VALUE_METADATA_TIMEOUT_MS = 600000;

    private static final int DEFAULT_NIO_IO_THREADS = Runtime.getRuntime().availableProcessors();

    public static final String PROPERTY_REGION_ID = "region";
    public static final String PROPERTY_ENDPOINT = "endpoint";
    public static final String PROPERTY_MANAGER_ENDPOINT = "manager.endpoint";
    public static final String PROPERTY_PROJECT_ID = "projectId";
    public static final String GROUP_ID = "group.id";

    public static final String PROPERTY_CONNECTION_TIMEOUT = "CONNECTION_TIME_OUT";
    public static final String PROPERTY_SOCKET_TIMEOUT = "SOCKET_TIME_OUT";
    public static final String PROPERTY_MAX_PER_ROUTE = "DEFAULT_MAX_PER_ROUTE";
    public static final String PROPERTY_MAX_TOTAL = "DEFAULT_MAX_TOTAL";
    public static final String PROPERTY_PROXY_HOST = "PROXY_HOST";
    public static final String PROPERTY_PROXY_PORT = "PROXY_PORT";
    public static final int DEFAULT_PORT_PROXY_PORT = 80;
    public static final String PROPERTY_PROXY_PROTOCOL = "PROXY_PROTOCOL";
    public static final String DEFAULT_PROPERTY_PROXY_PROTOCOL = "http";
    public static final String PROPERTY_PROXY_USERNAME = "PROXY_USERNAME";
    public static final String PROPERTY_PROXY_PASSWORD = "PROXY_PASSWORD";
    public static final String PROPERTY_PROXY_WORKSTATION = "PROXY_WORKSTATION";
    public static final String PROPERTY_PROXY_DOMAIN = "PROXY_DOMAIN";
    public static final String PROPERTY_NON_PROXY_HOSTS = "NON_PROXY_HOSTS";

    public static final String PROPERTY_AK = "ak";
    public static final String PROPERTY_SK = "sk";
    public static final String PROPERTY_DATA_PASSWORD = "data.password";
    public static final String PROPERTY_IS_DEFAULT_TRUSTED_JKS_ENABLED = "IS_DEFAULT_TRUSTED_JKS_ENABLED";

    public static final String PROPERTY_IS_DEFAULT_DATA_ENCRYPT_ENABLED = "data.encrypt.enabled";

    public static final String PROPERTY_DATA_COMPRESS_ENABLED = "data.compress.enabled";

    public static final String PROPERTY_DATA_CACHE_ENABLED = "data.cache.enabled";

    public static final String PROPERTY_DATA_CACHE_DIR = "data.cache.dir";

    public static final String PROPERTY_DATA_CACHE_DISK_MAX_SIZE = "data.cache.disk.max.size";

    public static final String PROPERTY_DATA_CACHE_ARCHIVE_MAX_SIZE = "data.cache.archive.max.size";

    public static final String PROPERTY_DATA_CACHE_ARCHIVE_LIFE_CYCLE = "data.cache.archive.life.cycle";

    public static final String PROPERTY_BODY_SERIALIZE_TYPE = "body.serialize.type";

    public static final String PROPERTY_CONFIG_PROVIDER_CLASS = "config.provider.class";

    public static final String PROPERTY_SECURITY_TOKEN = "security.token";

    public static final String PROPERTY_PRODUCER_EXCEPTION_RETRIES = "exception.retries";

    public static final String PROPERTY_PRODUCER_RECORDS_RETRIES = "records.retries";

    public static final String PROPERTY_PRODUCER_LINGER_MS = "linger.ms";

    public static final String PROPERTY_PRODUCER_BUFFER_MEMORY = "buffer.memory";

    public static final String PROPERTY_PRODUCER_BUFFER_COUNT = "buffer.count";

    public static final String PROPERTY_PRODUCER_BLOCK_ON_BUFFER_FULL = "block.on.buffer.full";

    public static final String PROPERTY_PRODUCER_MAX_BLOCK_MS = "max.block.ms";

    public static final String PROPERTY_PRODUCER_BATCH_SIZE = "batch.size";

    public static final String PROPERTY_PRODUCER_BATCH_COUNT = "batch.count";

    public static final String PROPERTY_BACK_OFF_MAX_INTERVAL_MS = "backoff.max.interval.ms";

    public static final String PROPERTY_MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION = "max.in.flight.requests.per.connection";

    public static final String PROPERTY_PRODUCER_RECORDS_RETRIABLE_ERROR_CODE = "records.retriable.error.code";

    public static final String PROPERTY_PRODUCER_EXCEPTION_RETRIABLE_ERROR_CODE = "exception.retriable.error.code";

    public static final String PROPERTY_NIO_IO_THREADS = "nio.io.threads";

    public static final String PROPERTY_ORDER_BY_PARTITION = "order.by.partition";

    public static final String PROPERTY_METADATA_TIMEOUT_MS = "metadata.timeout.ms";

    public static final String PROPERTY_BODY_COMPRESS_ENABLED = "body.compress.enabled";

    public static final String PROPERTY_BODY_COMPRESS_TYPE = "body.compress.type";

    public static final String PROPERTY_AUTH_TOKEN = "auth.token";

    public static final String PROPERTY_AUTH_TYPE = "auth.type";

    public String[] producerRecordsRetriableErrorCode;

    public String[] producerExceptionRetriableErrorCode;

    private Credentials credentials;

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public boolean getIsDefaultTrustedJksEnabled() {
        return getBoolean(PROPERTY_IS_DEFAULT_TRUSTED_JKS_ENABLED, DEFAULT_VALUE_IS_DEFAULT_TRUSTED_JKS_ENABLED);
    }

    public boolean getIsDefaultDataEncryptEnabled() {
        return getBoolean(PROPERTY_IS_DEFAULT_DATA_ENCRYPT_ENABLED, DEFAULT_VALUE_IS_DEFAULT_DATA_ENCRYPT_ENABLED);
    }

    public boolean isDataCompressEnabled() {
        return getBoolean(PROPERTY_DATA_COMPRESS_ENABLED, DEFAULT_VALUE_DATA_COMPRESS_ENABLED);
    }

    public boolean isDataCacheEnabled() {
        return getBoolean(PROPERTY_DATA_CACHE_ENABLED, DEFAULT_VALUE_DATA_CACHE_ENABLED);
    }

    public String getDataCacheDir() {
        return get(PROPERTY_DATA_CACHE_DIR, DEFAULT_VALUE_DATA_CACHE_DIR);
    }

    public int getDataCacheDiskMaxSize() {
        return getInt(PROPERTY_DATA_CACHE_DISK_MAX_SIZE, DEFAULT_VALUE_DATA_CACHE_DIR_MAX_SIZE);
    }

    public int getDataCacheArchiveMaxSize() {
        return getInt(PROPERTY_DATA_CACHE_ARCHIVE_MAX_SIZE, DEFAULT_VALUE_DATA_CACHE_ARCHIVE_MAX_SIZE);
    }

    public int getDataCacheArchiveLifeCycle() {
        return getInt(PROPERTY_DATA_CACHE_ARCHIVE_LIFE_CYCLE, DEFAULT_VALUE_DATA_CACHE_ARCHIVE_LIFE_CYCLE);
    }

    public BodySerializeType getBodySerializeType() {
        String tmp = get(PROPERTY_BODY_SERIALIZE_TYPE, "");
        if (tmp == null || "".equals(tmp.trim())) {
            return DEFAULT_VALUE_BODY_SERIALIZE_TYPE;
        }
        BodySerializeType res = null;
        try {
            res = BodySerializeType.valueOf(tmp.trim());
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage(), e);
        }

        return res == null ? DEFAULT_VALUE_BODY_SERIALIZE_TYPE : res;
    }


    public String getAK() {
        return get(PROPERTY_AK, null);
    }

    public String getSK() {
        return get(PROPERTY_SK, null);
    }

    public String getDataPassword() {
        return get(PROPERTY_DATA_PASSWORD, null);
    }

    public String getRegion() {
        return get(PROPERTY_REGION_ID, null);
    }

    public int getConnectionTimeOut() {
        return getInt(PROPERTY_CONNECTION_TIMEOUT, DEFAULT_VALUE_CONNECTION_TIMEOUT) * 1000;
    }

    public String getAuthToken() {
        return get(PROPERTY_AUTH_TOKEN, null);
    }

    public String getAuthType() {
        return get(PROPERTY_AUTH_TYPE, null);
    }

    public int getSocketTimeOut() {
        return getInt(PROPERTY_SOCKET_TIMEOUT, DEFAULT_VALUE_SOCKET_TIMEOUT) * 1000;
    }

    public int getMaxPerRoute() {
        return getInt(PROPERTY_MAX_PER_ROUTE, DEFAULT_VALUE_MAX_PER_ROUTE);
    }

    public int getMaxTotal() {
        return getInt(PROPERTY_MAX_TOTAL, DEFAULT_VALUE_MAX_TOTAL);
    }

    public String getProxyHost() {
        return get(PROPERTY_PROXY_HOST, null);
    }

    public int getProxyPort() {
        return getInt(PROPERTY_PROXY_PORT, DEFAULT_PORT_PROXY_PORT);
    }

    public Protocol getProxyProtocol() {
        String proxyProtocol = get(PROPERTY_PROXY_PROTOCOL, DEFAULT_PROPERTY_PROXY_PROTOCOL);
        Protocol protocol = null;
        try {
            protocol = Protocol.valueOf(proxyProtocol);
        } catch (IllegalArgumentException e) {
            protocol = Protocol.HTTP;
        }
        return protocol;
    }

    public boolean isProxyEnabled() {
        return getProxyHost() != null && getProxyPort() > 0;
    }

    public String getProxyUsername() {
        return get(PROPERTY_PROXY_USERNAME, null);
    }

    public String getProxyPassword() {
        return get(PROPERTY_PROXY_PASSWORD, null);
    }

    public String getProxyWorkstation() {
        return get(PROPERTY_PROXY_WORKSTATION, null);
    }

    public String getProxyDomain() {
        return get(PROPERTY_PROXY_DOMAIN, null);
    }

    public String getNonProxyHosts() {
        return get(PROPERTY_NON_PROXY_HOSTS, null);
    }

    public boolean isAuthenticatedProxy() {
        return getProxyUsername() != null && getProxyPassword() != null;
    }

    public String getRegionId() {
        return get(PROPERTY_REGION_ID, DEFAULT_VALUE_REGION_ID);
    }

    public String getSecurityToken() {
        return get(PROPERTY_SECURITY_TOKEN, null);
    }

    /**
     * @return 接口异常重试次数
     */
    public int getExceptionRetries() {
        int exceptionRetry = getInt(PROPERTY_PRODUCER_EXCEPTION_RETRIES, 20);
        if (exceptionRetry < 0) {
            return Integer.MAX_VALUE;
        }
        return exceptionRetry;
    }

    /**
     * @return 记录上传重试次数
     */
    public int getRecordsRetries() {
        int recordsRetry = getInt(PROPERTY_PRODUCER_RECORDS_RETRIES, 20);
        if (recordsRetry < 0) {
            return Integer.MAX_VALUE;
        }
        return recordsRetry;
    }

    /**
     * @return 记录重试的错误码集合(多个之间以, 分隔)
     */
    public String[] getRecordsRetriesErrorCode() {
        return producerRecordsRetriableErrorCode;
    }

    /**
     * @return 发生4XX错误时，需要重试的错误码集合(多个之间以,分隔)
     */
    public String[] getExceptionRetriesErrorCode() {
        return producerExceptionRetriableErrorCode;
    }

    public int getNIOIOThreads() {
        return getInt(PROPERTY_NIO_IO_THREADS, DEFAULT_NIO_IO_THREADS);
    }

    public boolean isOrderByPartition() {
        return getBoolean(PROPERTY_ORDER_BY_PARTITION, DEFAULT_VALUE_ORDER_BY_PARTITION);
    }

    public long getMetadataTimeoutMs() {
        return (long) getInt(PROPERTY_METADATA_TIMEOUT_MS, DEFAULT_VALUE_METADATA_TIMEOUT_MS);
    }

    /**
     * @return 批量发送延迟时间
     */
    public long getLingerMs() {
        long lingerMs = Long.valueOf(get(PROPERTY_PRODUCER_LINGER_MS, "50"));
        return lingerMs < 0 ? 0 : lingerMs;
    }

    /**
     * @return 一个批次的总大小限制(B)
     */
    public long getBatchSize() {
        return Long.valueOf(get(PROPERTY_PRODUCER_BATCH_SIZE, String.valueOf(1024 * 1024)));
    }

    /**
     * @return 一个批次的总条数限制
     */
    public int getBatchCount() {
        return getInt(PROPERTY_PRODUCER_BATCH_COUNT, 1000);
    }

    /**
     * @return 总缓存的内存大小(B)
     */
    public long getBufferMemory() {
        return Long.valueOf(get(PROPERTY_PRODUCER_BUFFER_MEMORY, String.valueOf(32 * 1024 * 1024)));
    }

    /**
     * @return 总缓存的批次数量限制
     */
    public int getBufferCount() {
        return getInt(PROPERTY_PRODUCER_BUFFER_COUNT, 5000);
    }

    /**
     * 当发送缓冲区满，是否一直阻塞
     * 如为true，表示一直阻塞，max.block.ms会修改为Long.MAX_VALUE；如为false，则根据max.block.ms的时间阻塞，超过时间抛出异常
     *
     * @return {@code true} 阻塞 {@code false} 不阻塞
     */
    public boolean isBlockOnBufferFull() {
        return getBoolean(PROPERTY_PRODUCER_BLOCK_ON_BUFFER_FULL, false);
    }

    /**
     * @return Buffer满时的阻塞时间(ms)
     */
    public long getMaxBlockMs() {
        if (isBlockOnBufferFull()) {
            return Long.MAX_VALUE;
        } else {
            return Long.valueOf(get(PROPERTY_PRODUCER_MAX_BLOCK_MS, String.valueOf(60 * 1000)));
        }
    }

    /**
     * @return 单次backoff最长等待时间
     */
    public long getBackOffMaxIntervalMs() {
        return Long.valueOf(get(PROPERTY_BACK_OFF_MAX_INTERVAL_MS, String.valueOf(30 * 1000)));
    }

    public int getMaxInFlightRequestsPerConnection() {
        int maxConnection = getInt(PROPERTY_MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, Constants.DEFAULT_THREAD_POOL_SIZE);
        if (maxConnection <= 0) {
            return Constants.DEFAULT_THREAD_POOL_SIZE;
        }
        return maxConnection;
    }

    public String getEndpoint() {
        String endpoint = get(PROPERTY_ENDPOINT, DEFAULT_VALUE_ENDPOINT);
        if (endpoint != null && !endpoint.trim().isEmpty()) {
            return endpoint;
        }

        //根据区域，域名，端口拼接
        String endpointFormat = "https://dis.%s.%s";
        return String.format(endpointFormat, getRegion(), "myhuaweicloud.com");
    }

    public String getManagerEndpoint() {
        String managerEndpoint = get(PROPERTY_MANAGER_ENDPOINT, null);
        if (managerEndpoint == null || managerEndpoint.trim().isEmpty()) {
            return getEndpoint();
        }
        return managerEndpoint;
    }

    public String getProjectId() {
        return get(PROPERTY_PROJECT_ID, DEFAULT_VALUE_PROJECT_ID);
    }

    private void load(String fileName) throws IOException {
        InputStream inputStream = null;
        try {
            ClassLoader classLoader = DISConfig.class.getClassLoader();
            if (null != classLoader) {
                inputStream = classLoader.getResourceAsStream(fileName);
                LOG.debug("get from classLoader");
                if (inputStream == null) {
                    throw new FileNotFoundException("config file " + fileName + " not exist.");
                }
            }

            if (null == inputStream && this.getClass() != null) {
                inputStream = this.getClass().getResourceAsStream(fileName);
                LOG.debug("get from class");
            }

            if (null == inputStream && fileName.startsWith("/") && null != classLoader) {
                inputStream = classLoader.getResourceAsStream("." + fileName);
                LOG.debug("get from ./");
            }

            if (null == inputStream && fileName.startsWith("/") && null != classLoader) {
                inputStream = classLoader.getResourceAsStream(fileName.substring(1));
                LOG.debug("get from no /");
            }

            if (null == inputStream) {
                ClassLoader.getSystemResourceAsStream(fileName);
                LOG.debug("get from ClassLoader");
            }

            if (null == inputStream) {
                LOG.debug("getResourceAsStream() returns null.");
                return;
            }

            this.load(inputStream);
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }

    public String get(String propName, String defaultValue) {
        if (this.containsKey(propName)) {
            String value = this.getProperty(propName);
            if (value != null) {
                return value.trim();
            }
        }
        return defaultValue;
    }

    public int getInt(String propName, int defaultValue) {
        String value = get(propName, null);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                LOG.error(e.getMessage(), e);
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public boolean getBoolean(String propName, boolean defaultValue) {
        String value = get(propName, null);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }

    public static DISConfig buildDefaultConfig() {
        return buildConfig(FILE_NAME);
    }

    public static DISConfig buildConfig(String configFile) {
        DISConfig disConfig = new DISConfig();

        boolean configFileDefined = configFile != null && !configFile.trim().isEmpty();
        boolean needLoadFromDefault = false;

        if (configFileDefined) {
            try {
                disConfig.load(configFile);
            } catch (IOException e) {
                if (e instanceof FileNotFoundException) {
                    LOG.trace("load config from file {} failed. {}", configFile, e.getMessage());
                } else {
                    LOG.warn("load config from file {} failed. {}", configFile, e.getMessage());
                }
                needLoadFromDefault = true;
            }
        }

        if (!configFileDefined || needLoadFromDefault) {
            try {
                disConfig.load(FILE_NAME);
            } catch (IOException e) {
                if (e instanceof FileNotFoundException) {
                    LOG.trace("load config from default file {} failed. {}", FILE_NAME, e.getMessage());
                } else {
                    LOG.warn("load config from default file {} failed. {}", FILE_NAME, e.getMessage());
                }
            }
        }

        // 默认只对流控与服务端错误重试
        String recordsRetriableErrorCode = disConfig.get(PROPERTY_PRODUCER_RECORDS_RETRIABLE_ERROR_CODE, "DIS.4303,DIS.5");
        if (StringUtils.isNullOrEmpty(recordsRetriableErrorCode)) {
            disConfig.producerRecordsRetriableErrorCode = new String[0];
        } else {
            String[] items = recordsRetriableErrorCode.split(",");
            for (int i = 0; i < items.length; i++) {
                items[i] = items[i].trim();
            }
            disConfig.producerRecordsRetriableErrorCode = items;
        }

        // 默认情况下，发生4XX错误都不进行重试
        String exceptionRetriableErrorCode = disConfig.get(PROPERTY_PRODUCER_EXCEPTION_RETRIABLE_ERROR_CODE, "");
        if (StringUtils.isNullOrEmpty(exceptionRetriableErrorCode)) {
            disConfig.producerExceptionRetriableErrorCode = new String[0];
        } else {
            String[] items = exceptionRetriableErrorCode.split(",");
            for (int i = 0; i < items.length; i++) {
                items[i] = items[i].trim();
            }
            disConfig.producerExceptionRetriableErrorCode = items;
        }

        return disConfig;
    }

    public static DISConfig buildConfig(DISConfig disConfig) {
        DISConfig fileConfig = buildDefaultConfig();

        fileConfig.putAll(disConfig);

        return fileConfig;
    }


    public DISConfig setAK(String ak) {
        return set(PROPERTY_AK, ak);
    }

    public DISConfig setSK(String sk) {
        return set(PROPERTY_SK, sk);
    }

    public DISConfig setAuthToken() {
        return set(PROPERTY_AUTH_TOKEN, null);
    }

    public DISConfig setAuthType() {
        return set(PROPERTY_AUTH_TYPE, null);
    }

    public DISConfig setRegion(String region) {
        return set(PROPERTY_REGION_ID, region);
    }

    public DISConfig setProjectId(String projectId) {
        return set(PROPERTY_PROJECT_ID, projectId);
    }

    public DISConfig setEndpoint(String endpoint) {
        return set(PROPERTY_ENDPOINT, endpoint);
    }

    public DISConfig setDataEncryptEnabled(boolean dataEncryptEnabled) {
        return set(PROPERTY_IS_DEFAULT_DATA_ENCRYPT_ENABLED, String.valueOf(dataEncryptEnabled));
    }

    public DISConfig setDefaultClientCertAuthEnabled(boolean defaultClientCertAuthEnabled) {
        return set(PROPERTY_IS_DEFAULT_TRUSTED_JKS_ENABLED, String.valueOf(defaultClientCertAuthEnabled));
    }

    public DISConfig setDataCompressEnabled(boolean dataCompressEnabled) {
        return set(PROPERTY_DATA_COMPRESS_ENABLED, String.valueOf(dataCompressEnabled));
    }

    public DISConfig setBodyCompressEnabled(boolean bodyCompressEnabled) {
        return set(PROPERTY_BODY_COMPRESS_ENABLED, String.valueOf(bodyCompressEnabled));
    }

    public DISConfig setBodyCompressType(CompressionType compressionType) {
        return set(PROPERTY_BODY_COMPRESS_TYPE, compressionType.toString());
    }

    public DISConfig setDataCacheEnabled(boolean dataCacheEnabled) {
        return set(PROPERTY_DATA_CACHE_ENABLED, String.valueOf(dataCacheEnabled));
    }

    public DISConfig setDataCacheDir(String dataCacheDir) {
        return set(PROPERTY_DATA_CACHE_DIR, String.valueOf(dataCacheDir));
    }

    public DISConfig setDataCacheDiskMaxSize(String dataCacheDiskMaxSize) {
        return set(PROPERTY_DATA_CACHE_DISK_MAX_SIZE, String.valueOf(dataCacheDiskMaxSize));
    }

    public DISConfig setDataCacheArchiveMaxSize(String dataCacheArchiveMaxSize) {
        return set(PROPERTY_DATA_CACHE_ARCHIVE_MAX_SIZE, String.valueOf(dataCacheArchiveMaxSize));
    }

    public DISConfig setDataCacheArchiveLifeCycle(String dataCacheArchiveLifeCycle) {
        return set(PROPERTY_DATA_CACHE_ARCHIVE_LIFE_CYCLE, String.valueOf(dataCacheArchiveLifeCycle));
    }

    public DISConfig setBodySerializeType(BodySerializeType bodySerializeType) {
        return set(PROPERTY_BODY_SERIALIZE_TYPE, String.valueOf(bodySerializeType));
    }

    public DISConfig setSecurityToken(String securityToken) {
        return set(PROPERTY_SECURITY_TOKEN, securityToken);
    }

    public DISConfig setRecordsRetries(int retries) {
        return set(PROPERTY_PRODUCER_RECORDS_RETRIES, String.valueOf(retries));
    }

    public DISConfig setNIOIOThreads(int ioThreads) {
        return set(PROPERTY_NIO_IO_THREADS, String.valueOf(ioThreads));
    }

    public DISConfig setProxyHost(String proxyHost) {
        return set(PROPERTY_PROXY_HOST, proxyHost);
    }

    public DISConfig setProxyPort(String proxyPort) {
        return set(PROPERTY_PROXY_PORT, proxyPort);
    }

    public DISConfig setProxyProtocol(Protocol proxyProtocol) {
        return set(PROPERTY_PROXY_PROTOCOL, proxyProtocol.toString());
    }

    public DISConfig setProxyUsername(String proxyUsername) {
        return set(PROPERTY_PROXY_USERNAME, proxyUsername);
    }

    public DISConfig setProxyPassword(String proxyPassword) {
        return set(PROPERTY_PROXY_PASSWORD, proxyPassword);
    }

    public DISConfig setProxyWorkstation(String proxyPassword) {
        return set(PROPERTY_PROXY_WORKSTATION, proxyPassword);
    }

    public DISConfig setProxyDomain(String proxyDomain) {
        return set(PROPERTY_PROXY_DOMAIN, proxyDomain);
    }

    public DISConfig setNonProxyHosts(String nonProxyHosts) {
        return set(PROPERTY_NON_PROXY_HOSTS, nonProxyHosts);
    }

    public DISConfig set(String key, String value) {
        this.put(key, value);
        return this;
    }

    public static enum BodySerializeType {
        json, protobuf
    }

    public String getGroupId() {
        return get(GROUP_ID, null);
    }


    @Override
    public Credentials getCredential() {
        return new BasicCredentials(this.getAK(), this.getSK());
    }
}
