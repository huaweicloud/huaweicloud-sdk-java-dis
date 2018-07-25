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

package com.huaweicloud.dis.core.auth.signer.internal;

import com.huaweicloud.dis.core.Request;

/**
 * Parameters that are used for computing a signature for a request.
 */
public final class SignerRequestParams {

    /**
     * The request for which the signature needs to be computed.
     */
    private final Request<?> request;

    /**
     * The datetime in milliseconds for which the signature needs to be
     * computed.
     */
    private final long signingDateTimeMilli;

    /**
     * The scope of the signature.
     */
    private final String scope;

    /**
     * The region to be used for computing the signature.
     */
    private final String regionName;

    /**
     * The name of the service.
     */
    private final String serviceName;

    /**
     * UTC formatted version of the signing time stamp.
     */
    private final String formattedSigningDateTime;

    /**
     * UTC Formatted Signing date with time stamp stripped
     */
    private final String formattedSigningDate;

    /**
     * The signing algorithm to be used for computing the signature.
     */
    private final String signingAlgorithm;

    /**
     * Generates an instance of SignerRequestParams that holds the
     * parameters used for computing a signature for a request
     */
    public SignerRequestParams(Request<?> request, String regionNameOverride, String serviceNameOverride, String signingAlgorithm) {
        this(request, regionNameOverride, serviceNameOverride, signingAlgorithm , null);
    }
    
    /**
     * SignerRequestParams构造函数
     * <p>SignerRequestParams用于计算签名
     * 
     * @param request 请求对象
     * @param regionNameOverride 地域名
     * @param serviceNameOverride 服务名
     * @param signingAlgorithm 签名算法
     * @param signDate 签名时间
     */
    public SignerRequestParams(Request< ? > request, String regionNameOverride, String serviceNameOverride,
        String signingAlgorithm, String signDate) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (signingAlgorithm == null) {
            throw new IllegalArgumentException("Signing Algorithm cannot be null");
        }
        
        if(null == signDate) {
            this.signingDateTimeMilli = getSigningDate(request);
        } else {
            this.signingDateTimeMilli = getSigningDate(signDate);
        }
        
        this.request = request;
        this.formattedSigningDate = SignerUtils.formatDateStamp(signingDateTimeMilli);
        this.serviceName = serviceNameOverride != null ? serviceNameOverride : "";
        this.regionName = regionNameOverride != null ? regionNameOverride : "";
        this.scope = generateScope(formattedSigningDate, serviceName, regionName);
        this.formattedSigningDateTime = SignerUtils.formatTimestamp(signingDateTimeMilli);
        this.signingAlgorithm = signingAlgorithm;
    }

    /**
     * Returns the signing date from the request.
     */
    private final long getSigningDate(Request<?> request) {
        return System.currentTimeMillis() - request.getTimeOffset() * 1000;
    }
    
    /**
     * 根据signDate 日期字符串获取日期毫秒数
     * 
     * @param signDate 日期字符串
     * 
     * @return 获取SignDate对应毫秒数
     */
    private final long getSigningDate(String signDate) {
        return SignerUtils.parseMillis(signDate);
    }

    /**
     * Returns the scope to be used for the signing.
     */
    private String generateScope(String dateStamp, String serviceName, String regionName) {
        final StringBuilder scopeBuilder = new StringBuilder();
        return scopeBuilder.append(dateStamp).append("/").append(regionName)
                .append("/").append(serviceName).append("/")
                .append(SignerConstants.SDK_TERMINATOR).toString();
    }

    /**
     * Returns the request for which the signing needs to be done.
     */
    public Request<?> getRequest() {
        return request;
    }

    /**
     * Returns the scope of the signing.
     */
    public String getScope() {
        return scope;
    }

    /**
     * Returns the formatted date and time of the signing date in UTC zone.
     */
    public String getFormattedSigningDateTime() {
        return formattedSigningDateTime;
    }

    /**
     * Returns the signing date time in millis for which the signature needs to
     * be computed.
     */
    public long getSigningDateTimeMilli() {
        return signingDateTimeMilli;
    }

    /**
     * Returns the region name to be used while computing the signature.
     */
    public String getRegionName() {
        return regionName;
    }

    /**
     * Returns the Service name to be used while computing the signature.
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Returns the formatted date in UTC zone of the signing date.
     */
    public String getFormattedSigningDate() {
        return formattedSigningDate;
    }

    /**
     * Returns the signing algorithm used for computing the signature.
     */
    public String getSigningAlgorithm() {
        return signingAlgorithm;
    }
}