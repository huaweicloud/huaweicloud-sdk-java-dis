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

package com.huaweicloud.dis.core.auth.signer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

import com.huaweicloud.dis.core.ClientException;
import com.huaweicloud.dis.core.Request;
import com.huaweicloud.dis.core.WebServiceRequest;
import com.huaweicloud.dis.core.auth.credentials.Credentials;
import com.huaweicloud.dis.core.auth.signer.internal.SignerConstants;
import com.huaweicloud.dis.core.auth.signer.internal.SignerKey;
import com.huaweicloud.dis.core.auth.signer.internal.SignerRequestParams;
import com.huaweicloud.dis.core.auth.signer.internal.SignerUtils;
import com.huaweicloud.dis.core.internal.FIFOCache;
import com.huaweicloud.dis.core.util.BinaryUtils;
import com.huaweicloud.dis.core.util.DateUtils;
import com.huaweicloud.dis.core.util.HttpUtils;
import com.huaweicloud.dis.core.util.StringUtils;

/**
 * Signer implementation that signs requests with the signing protocol.
 */
public class DefaultSigner extends AbstractSigner implements ServiceSigner, RegionSigner, Presigner, VerifySigner {
    
    private static final int SIGNER_CACHE_MAX_SIZE = 300;
    
    private static final String LINUX_NEW_LINE="\n";
    
    private static final FIFOCache<SignerKey> signerCache = new FIFOCache<SignerKey>(SIGNER_CACHE_MAX_SIZE);
    
    /**
     * Service name override for use when the endpoint can't be used to
     * determine the service name.
     */
    protected String serviceName;
    
    /**
     * Region name override for use when the endpoint can't be used to determine
     * the region name.
     */
    protected String regionName;
    
    /**
     * Whether double url-encode the resource path when constructing the
     * canonical request. By default, we enable double url-encoding.
     * <p/>
     * services that want to suppress this, they should use new
     * DefaultSigner(false).
     */
    protected boolean doubleUrlEncode;
    
    /**
     * Construct a new signer instance. By default, enable double
     * url-encoding.
     */
    public DefaultSigner() {
        this(true);
    }
    
    /**
     * Construct a new signer instance.
     *
     * @param doubleUrlEncoding Whether double url-encode the resource path when constructing
     *                          the canonical request.
     */
    public DefaultSigner(boolean doubleUrlEncoding) {
        this.doubleUrlEncode = doubleUrlEncoding;
    }
    
    /**
     * Sets the service name that this signer should use when calculating
     * request signatures. This can almost always be determined directly from
     * the request's end point, so you shouldn't need this method, but it's
     * provided for the edge case where the information is not in the endpoint.
     *
     * @param serviceName The service name to use when calculating signatures in this
     *                    signer.
     */
    @Override
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    /**
     * Returns the service name that is used when calculating the signature.
     */
    public String getServiceName() {
        return serviceName;
    }
    
    /**
     * Sets the region name that this signer should use when calculating request
     * signatures. This can almost always be determined directly from the
     * request's end point, so you shouldn't need this method, but it's provided
     * for the edge case where the information is not in the endpoint.
     *
     * @param regionName The region name to use when calculating signatures in this
     *                   signer.
     */
    @Override
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
    
    /**
     * Returns the region name that is used when calculating the signature.
     */
    public String getRegionName() {
        return regionName;
    }
    
    @Override
    public void sign(Request< ? > request, Credentials credentials) {
        Credentials sanitizedCredentials = sanitizeCredentials(credentials);
        
        final SignerRequestParams signerParams =
            new SignerRequestParams(request, regionName, serviceName, SignerConstants.SDK_SIGNING_ALGORITHM);
        addHostHeader(request);
        request.addHeader(SignerConstants.X_SDK_DATE, signerParams.getFormattedSigningDateTime());
        
        String contentSha256 = calculateContentHash(request);
        if ("required".equals(request.getHeaders().get(SignerConstants.X_SDK_CONTENT_SHA256))) {
            request.addHeader(SignerConstants.X_SDK_CONTENT_SHA256, contentSha256);
        }
        
        final String canonicalRequest = createCanonicalRequest(request, contentSha256);
        
        final String stringToSign = createStringToSign(canonicalRequest, signerParams);
        
        final byte[] signingKey = deriveSigningKey(sanitizedCredentials, signerParams);
        
        final byte[] signature = computeSignature(stringToSign, signingKey, signerParams);
        
        request.addHeader(SignerConstants.AUTHORIZATION,
            buildAuthorizationHeader(request, signature, sanitizedCredentials, signerParams));
    }

    @Override
    public void sign(Request<?> request, Credentials credentials, Properties prop) {
        setProvider(prop.getProperty(SignerConstants.SIGN_PROVIDER));
        sign(request,credentials);
    }

    @Override
    public void presignRequest(Request< ? > request, Credentials credentials, Date userSpecifiedExpirationDate) {
        long expirationInSeconds = generateExpirationDate(userSpecifiedExpirationDate);
        addHostHeader(request);
        Credentials sanitizedCredentials = sanitizeCredentials(credentials);
        
        final SignerRequestParams signerRequestParams =
            new SignerRequestParams(request, regionName, serviceName, SignerConstants.SDK_SIGNING_ALGORITHM);
            
        // Add the important parameters for v4 signing
        final String timeStamp = SignerUtils.formatTimestamp(System.currentTimeMillis());
        
        addPreSignInformationToRequest(request, sanitizedCredentials, signerRequestParams, timeStamp,
            expirationInSeconds);
            
        final String contentSha256 = calculateContentHashPresign(request);
        
        final String canonicalRequest = createCanonicalRequest(request, contentSha256);
        
        final String stringToSign = createStringToSign(canonicalRequest, signerRequestParams);
        
        final byte[] signingKey = deriveSigningKey(sanitizedCredentials, signerRequestParams);
        
        final byte[] signature = computeSignature(stringToSign, signingKey, signerRequestParams);
        
        request.addParameter(SignerConstants.X_SDK_SIGNATURE, BinaryUtils.toHex(signature));
    }
    
    /**
     * Step 1 of the Signature calculation. Refer to
     * http://docs.aws
     * .amazon.com/general/latest/gr/sigv4-create-canonical-request.html to
     * generate the canonical request.
     */
    protected String createCanonicalRequest(Request< ? > request, String contentSha256) {
        /* This would url-encode the resource path for the first time. */
        final String path = HttpUtils.appendUri(request.getEndpoint().getPath(), request.getResourcePath());
        
        final StringBuilder canonicalRequestBuilder = new StringBuilder(request.getHttpMethod().toString());
        
        canonicalRequestBuilder.append(SignerConstants.LINE_SEPARATOR)
            // This would optionally double url-encode the resource path
            .append(getCanonicalizedResourcePath(path, doubleUrlEncode)).append(SignerConstants.LINE_SEPARATOR)
            .append(getCanonicalizedQueryString(request)).append(SignerConstants.LINE_SEPARATOR)
            .append(getCanonicalizedHeaderString(request)).append(SignerConstants.LINE_SEPARATOR)
            .append(getSignedHeadersString(request)).append(SignerConstants.LINE_SEPARATOR).append(contentSha256);
            
        final String canonicalRequest = canonicalRequestBuilder.toString();
        
        return canonicalRequest;
    }
    
    /**
     * Step 2 of the Signature calculation. Refer to
     * http://docs.aws.amazon.com/general/latest/gr/sigv4-create-string-to-sign.html.
     */
    protected String createStringToSign(String canonicalRequest, SignerRequestParams signerParams) {
        
        final StringBuilder stringToSignBuilder = new StringBuilder(signerParams.getSigningAlgorithm());
        stringToSignBuilder.append(SignerConstants.LINE_SEPARATOR).append(signerParams.getFormattedSigningDateTime())
            .append(SignerConstants.LINE_SEPARATOR).append(signerParams.getScope())
            .append(SignerConstants.LINE_SEPARATOR).append(BinaryUtils.toHex(hash(canonicalRequest)));
            
        final String stringToSign = stringToSignBuilder.toString();
        
        return stringToSign;
    }
    
    /**
     * Step 3 of the Signature calculation. It involves deriving
     * the signing key and computing the signature. Refer to
     * http://docs.aws.amazon.com/general/latest/gr/sigv4-calculate-signature.html
     */
    private final byte[] deriveSigningKey(Credentials credentials, SignerRequestParams signerRequestParams) {
        
        final String cacheKey = computeSigningCacheKeyName(credentials, signerRequestParams);
        final long daysSinceEpochSigningDate =
            DateUtils.numberOfDaysSinceEpoch(signerRequestParams.getSigningDateTimeMilli());
            
        SignerKey signerKey = signerCache.get(cacheKey);
        
        if (signerKey != null) {
            if (daysSinceEpochSigningDate == signerKey.getNumberOfDaysSinceEpoch()) {
                return signerKey.getSigningKey();
            }
        }
        
        byte[] signingKey = newSigningKey(credentials, signerRequestParams.getFormattedSigningDate(),
            signerRequestParams.getRegionName(), signerRequestParams.getServiceName());
            
        signerCache.add(cacheKey, new SignerKey(daysSinceEpochSigningDate, signingKey));
        return signingKey;
    }
    
    /**
     * Computes the name to be used to reference the signing key in the cache.
     */
    private final String computeSigningCacheKeyName(Credentials credentials, SignerRequestParams signerRequestParams) {
        final StringBuilder hashKeyBuilder = new StringBuilder(credentials.getSecretKey());
        
        return hashKeyBuilder.append("-").append(signerRequestParams.getRegionName()).append("-")
            .append(signerRequestParams.getServiceName()).toString();
    }
    
    /**
     * Step 3 of the Signature calculation. It involves deriving
     * the signing key and computing the signature. Refer to
     * http://docs.aws.amazon.com/general/latest/gr/sigv4-calculate-signature.html
     */
    protected final byte[] computeSignature(String stringToSign, byte[] signingKey,
        SignerRequestParams signerRequestParams) {
        return sign(stringToSign.getBytes(StringUtils.UTF8), signingKey, SigningAlgorithm.HmacSHA256);
    }
    
    /**
     * Creates the authorization header to be included in the request.
     */
    private String buildAuthorizationHeader(Request< ? > request, byte[] signature, Credentials credentials,
        SignerRequestParams signerParams) {
        final String signingCredentials = credentials.getAccessKeyId() + "/" + signerParams.getScope();
        
        final String credential = "Credential=" + signingCredentials;
        final String signerHeaders = "SignedHeaders=" + getSignedHeadersString(request);
        final String signatureHeader = "Signature=" + BinaryUtils.toHex(signature);
        
        final StringBuilder authHeaderBuilder = new StringBuilder();
        
        authHeaderBuilder.append(SignerConstants.SDK_SIGNING_ALGORITHM).append(" ").append(credential).append(", ")
            .append(signerHeaders).append(", ").append(signatureHeader);
            
        return authHeaderBuilder.toString();
    }
    
    /**
     * Includes all the signing headers as request parameters for pre-signing.
     */
    private void addPreSignInformationToRequest(Request< ? > request, Credentials credentials,
        SignerRequestParams signerParams, String timeStamp, long expirationInSeconds) {
        
        String signingCredentials = credentials.getAccessKeyId() + "/" + signerParams.getScope();
        
        request.addParameter(SignerConstants.X_SDK_ALGORITHM, SignerConstants.SDK_SIGNING_ALGORITHM);
        request.addParameter(SignerConstants.X_SDK_DATE, timeStamp);
        request.addParameter(SignerConstants.X_SDK_SIGNED_HEADER, getSignedHeadersString(request));
        request.addParameter(SignerConstants.X_SDK_EXPIRES, Long.toString(expirationInSeconds));
        request.addParameter(SignerConstants.X_SDK_CREDENTIAL, signingCredentials);
    }
    
    protected String getCanonicalizedHeaderString(Request< ? > request) {
        final List<String> sortedHeaders = new ArrayList<String>(request.getHeaders().keySet());
        Collections.sort(sortedHeaders, String.CASE_INSENSITIVE_ORDER);
        
        final Map<String, String> requestHeaders = request.getHeaders();
        StringBuilder buffer = new StringBuilder();
        for (String header : sortedHeaders) {
            String key = header.toLowerCase().replaceAll("\\s+", " ");
            String value = requestHeaders.get(header);
            buffer.append(key).append(":");
            if (value != null) {
                buffer.append(value.replaceAll("\\s+", " "));
            }
            
            buffer.append(LINUX_NEW_LINE);
        }
        
        return buffer.toString();
    }
    
    protected String getSignedHeadersString(Request< ? > request) {
        final List<String> sortedHeaders = new ArrayList<String>(request.getHeaders().keySet());
        Collections.sort(sortedHeaders, String.CASE_INSENSITIVE_ORDER);
        
        StringBuilder buffer = new StringBuilder();
        for (String header : sortedHeaders) {
            if (buffer.length() > 0) {
                buffer.append(";");
            }
            buffer.append(header.toLowerCase());
        }
        
        return buffer.toString();
    }
    
    protected void addHostHeader(Request< ? > request) {
        // requires that we sign the Host header so we
        // have to have it in the request by the time we sign.
        final URI endpoint = request.getEndpoint();
        final StringBuilder hostHeaderBuilder = new StringBuilder(endpoint.getHost());
        if (HttpUtils.isUsingNonDefaultPort(endpoint)) {
            hostHeaderBuilder.append(":").append(endpoint.getPort());
        }
        
        request.addHeader(SignerConstants.HOST, hostHeaderBuilder.toString());
    }
    
    /**
     * Calculate the hash of the request's payload. Subclass could override this
     * method to provide different values for "x-SDK-content-sha256" header or
     * do any other necessary set-ups on the request headers. (e.g. SDK-chunked
     * uses a pre-defined header value, and needs to change some headers
     * relating to content-encoding and content-length.)
     */
    protected String calculateContentHash(Request< ? > request) {
        InputStream payloadStream = getBinaryRequestPayloadStream(request);
        WebServiceRequest req = request.getOriginalRequest();
        payloadStream.mark(req == null ? -1 : req.getReadLimit());
        String contentSha256 = BinaryUtils.toHex(hash(payloadStream));
        try {
            payloadStream.reset();
        } catch (IOException e) {
            throw new ClientException("Unable to reset stream after calculating signature", null);
        }
        return contentSha256;
    }
    
    /**
     * Calculate the hash of the request's payload. In case of pre-sign, the
     * existing code would generate the hash of an empty byte array and returns
     * it. This method can be overridden by sub classes to provide different
     * values (e.g) For S3 pre-signing, the content hash calculation is
     * different from the general implementation.
     */
    protected String calculateContentHashPresign(Request< ? > request) {
        return calculateContentHash(request);
    }
    
    /**
     * Generates an expiration date for the presigned url. If user has specified
     * an expiration date, check if it is in the given limit.
     */
    private long generateExpirationDate(Date expirationDate) {
        long expirationInSeconds =
            expirationDate != null ? ((expirationDate.getTime() - System.currentTimeMillis()) / 1000L)
                : SignerConstants.PRESIGN_URL_MAX_EXPIRATION_SECONDS;
                
        if (expirationInSeconds > SignerConstants.PRESIGN_URL_MAX_EXPIRATION_SECONDS) {
            throw new ClientException("Requests that are pre-signed by SigV4 algorithm are valid for at most 7 days. "
                + "The expiration date set on the current request ["
                + SignerUtils.formatTimestamp(expirationDate.getTime()) + "] has exceeded this limit.");
        }
        return expirationInSeconds;
    }
    
    /**
     * Generates a new signing key from the given parameters and returns it.
     */
    private byte[] newSigningKey(Credentials credentials, String dateStamp, String regionName, String serviceName) {
        byte[] kSecret = (SignerConstants.SDK_NAME + credentials.getSecretKey()).getBytes(StringUtils.UTF8);
        byte[] kDate = sign(dateStamp, kSecret, SigningAlgorithm.HmacSHA256);
        byte[] kRegion = sign(regionName, kDate, SigningAlgorithm.HmacSHA256);
        byte[] kService = sign(serviceName, kRegion, SigningAlgorithm.HmacSHA256);
        return sign(SignerConstants.SDK_TERMINATOR, kService, SigningAlgorithm.HmacSHA256);
    }

    @Override
    public boolean verify(Request<?> request, Credentials credentials, Properties prop) {
        setProvider(prop.getProperty(SignerConstants.SIGN_PROVIDER));
        return verify(request,credentials);
    }

    @Override
    public boolean verify(Request< ? > request, Credentials credentials) {
        //AK、SK
        Credentials sanitizedCredentials = sanitizeCredentials(credentials);
        //获取日期(HTTP规范头部为小写)
        String singerDate = request.getHeaders().get(SignerConstants.X_SDK_DATE.toLowerCase());
        //获取签名信息(HTTP规范头部为小写)
        String authorization = request.getHeaders().remove(SignerConstants.AUTHORIZATION.toLowerCase());
        //计算签名对象
        final SignerRequestParams signerParams = new SignerRequestParams(request, regionName, serviceName,
            SignerConstants.SDK_SIGNING_ALGORITHM, singerDate);
            
        //计算内容256
        String contentSha256 = calculateContentHash(request);
        if ("required".equals(request.getHeaders().get(SignerConstants.X_SDK_CONTENT_SHA256))) {
            request.addHeader(SignerConstants.X_SDK_CONTENT_SHA256, contentSha256);
        }
        
        final String canonicalRequest = createCanonicalRequest(request, contentSha256);
        
        final String stringToSign = createStringToSign(canonicalRequest, signerParams);
        
        final byte[] signingKey = deriveSigningKey(sanitizedCredentials, signerParams);
        
        final byte[] signature = computeSignature(stringToSign, signingKey, signerParams);
        //添加变量，方便调试和查看
        String signatureResult = buildAuthorizationHeader(request, signature, sanitizedCredentials, signerParams);
        return signatureResult.equals(authorization);
    }
}