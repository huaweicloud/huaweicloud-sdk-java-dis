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

package com.cloud.dis.core.auth.signer;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.cloud.dis.core.ClientException;
import com.cloud.dis.core.Request;
import com.cloud.dis.core.WebServiceRequest;
import com.cloud.dis.core.auth.credentials.BasicCredentials;
import com.cloud.dis.core.auth.credentials.Credentials;
import com.cloud.dis.core.internal.SdkDigestInputStream;
import com.cloud.dis.core.util.Base64;
import com.cloud.dis.core.util.HttpUtils;
import com.cloud.dis.core.util.StringUtils;

import static com.cloud.dis.core.util.StringUtils.UTF8;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.*;

/**
 * Abstract base class for signing protocol implementations. Provides
 * utilities commonly needed by signing protocols such as computing
 * canonicalized host names, query string parameters, etc.
 */
public abstract class AbstractSigner implements Signer {

    private String provider;

    protected void setProvider(String provider)
    {
        this.provider = provider;
    }

    /**
     * Computes an RFC 2104-compliant HMAC signature and returns the result as a
     * Base64 encoded string.
     */
    protected String signAndBase64Encode(String data, String key, SigningAlgorithm algorithm) throws ClientException {
        return signAndBase64Encode(data.getBytes(UTF8), key, algorithm);
    }

    /**
     * Computes an RFC 2104-compliant HMAC signature for an array of bytes and
     * returns the result as a Base64 encoded string.
     */
    protected String signAndBase64Encode(byte[] data, String key, SigningAlgorithm algorithm) throws ClientException {
        try {
            byte[] signature = sign(data, key.getBytes(UTF8), algorithm);
            return Base64.encodeAsString(signature);
        } catch (Exception e) {
            throw new ClientException("Unable to calculate a request signature: " + e.getMessage(), e);
        }
    }

    public byte[] sign(String stringData, byte[] key, SigningAlgorithm algorithm) throws ClientException {
        try {
            byte[] data = stringData.getBytes(UTF8);
            return sign(data, key, algorithm);
        } catch (Exception e) {
            throw new ClientException("Unable to calculate a request signature: " + e.getMessage(), e);
        }
    }

    protected byte[] sign(byte[] data, byte[] key, SigningAlgorithm algorithm) throws ClientException {
        try {
            Mac mac;
            if(provider == null || provider.isEmpty())
            {
                mac = Mac.getInstance(algorithm.toString());
            }
            else
            {
                mac = Mac.getInstance(algorithm.toString(), provider);
            }
            mac.init(new SecretKeySpec(key, algorithm.toString()));
            return mac.doFinal(data);
        } catch (Exception e) {
            throw new ClientException("Unable to calculate a request signature: " + e.getMessage(), e);
        }
    }

    /**
     * Hashes the string contents (assumed to be UTF-8) using the SHA-256
     * algorithm.
     *
     * @param text The string to hash.
     * @return The hashed bytes from the specified string.
     * @throws ClientException If the hash cannot be computed.
     */
    public byte[] hash(String text) throws ClientException {
        try {
            MessageDigest md;
            if(!StringUtils.isNullOrEmpty(provider)) {
                md = MessageDigest.getInstance("SHA-256",provider);
            }
            else {
                md = MessageDigest.getInstance("SHA-256");
            }
            md.update(text.getBytes(UTF8));
            return md.digest();
        } catch (Exception e) {
            throw new ClientException("Unable to compute hash while signing request: " + e.getMessage(), e);
        }
    }

    protected byte[] hash(InputStream input) throws ClientException {
        try {
            MessageDigest md;
            if(!StringUtils.isNullOrEmpty(provider)) {
                md = MessageDigest.getInstance("SHA-256",provider);
            }
            else {
                md = MessageDigest.getInstance("SHA-256");
            }
            @SuppressWarnings("resource")
            DigestInputStream digestInputStream = new SdkDigestInputStream(input, md);
            byte[] buffer = new byte[1024];
            while (digestInputStream.read(buffer) > -1) {
            }
            return digestInputStream.getMessageDigest().digest();
        } catch (Exception e) {
            throw new ClientException("Unable to compute hash while signing request: " + e.getMessage(), e);
        }
    }

    /**
     * Hashes the binary data using the SHA-256 algorithm.
     *
     * @param data The binary data to hash.
     * @return The hashed bytes from the specified data.
     * @throws ClientException If the hash cannot be computed.
     */
    public byte[] hash(byte[] data) throws ClientException {
        try {
            MessageDigest md;
            if(!StringUtils.isNullOrEmpty(provider)) {
                md = MessageDigest.getInstance("SHA-256",provider);
            }
            else {
                md = MessageDigest.getInstance("SHA-256");
            }
            md.update(data);
            return md.digest();
        } catch (Exception e) {
            throw new ClientException("Unable to compute hash while signing request: " + e.getMessage(), e);
        }
    }

    /**
     * Examines the specified query string parameters and returns a
     * canonicalized form.
     * <p/>
     * The canonicalized query string is formed by first sorting all the query
     * string parameters, then URI encoding both the key and value and then
     * joining them, in order, separating key value pairs with an '&'.
     *
     * @param parameters The query string parameters to be canonicalized.
     * @return A canonicalized form for the specified query string parameters.
     */
    protected String getCanonicalizedQueryString(Map<String, String> parameters) {
        SortedMap<String, String> sorted = new TreeMap<String, String>();
        Iterator<Map.Entry<String, String>> pairs = parameters.entrySet().iterator();
        while (pairs.hasNext()) {
            Map.Entry<String, String> pair = pairs.next();
            String key = pair.getKey();
            String value = pair.getValue();
            sorted.put(HttpUtils.urlEncode(key, false), HttpUtils.urlEncode(value, false));
        }

        StringBuilder builder = new StringBuilder();
        pairs = sorted.entrySet().iterator();
        while (pairs.hasNext()) {
            Map.Entry<String, String> pair = pairs.next();
            builder.append(pair.getKey());
            builder.append("=");
            builder.append(pair.getValue());
            if (pairs.hasNext()) {
                builder.append("&");
            }
        }

        return builder.toString();
    }

    protected String getCanonicalizedQueryString(Request<?> request) {
        /*
         * If we're using POST and we don't have any request payload content,
         * then any request query parameters will be sent as the payload, and
         * not in the actual query string.
         */
        if (HttpUtils.usePayloadForQueryParameters(request)) {
            return "";
        }
        return this.getCanonicalizedQueryString(request.getParameters());
    }

    /**
     * Returns the request's payload as binary data.
     *
     * @param request The request
     * @return The data from the request's payload, as binary data.
     */
    protected byte[] getBinaryRequestPayload(Request<?> request) {
        if (HttpUtils.usePayloadForQueryParameters(request)) {
            String encodedParameters = HttpUtils.encodeParameters(request);
            if (encodedParameters == null) {
                return new byte[0];
            }
            return encodedParameters.getBytes(UTF8);
        }

        return getBinaryRequestPayloadWithoutQueryParams(request);
    }

    /**
     * Returns the request's payload as a String.
     *
     * @param request The request
     * @return The data from the request's payload, as a string.
     */
    protected String getRequestPayload(Request<?> request) {
        return newString(getBinaryRequestPayload(request));
    }

    /**
     * Returns the request's payload contents as a String, without processing
     * any query string params (i.e. no form encoding for query params).
     *
     * @param request The request
     * @return the request's payload contents as a String, not including any
     * form encoding of query string params.
     */
    protected String getRequestPayloadWithoutQueryParams(Request<?> request) {
        return newString(getBinaryRequestPayloadWithoutQueryParams(request));
    }

    /**
     * Returns the request's payload contents as binary data, without processing
     * any query string params (i.e. no form encoding for query params).
     *
     * @param request The request
     * @return The request's payload contents as binary data, not including any
     * form encoding of query string params.
     */
    protected byte[] getBinaryRequestPayloadWithoutQueryParams(Request<?> request) {
        InputStream content = getBinaryRequestPayloadStreamWithoutQueryParams(request);
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            WebServiceRequest req = request.getOriginalRequest();
            content.mark(req == null ? -1 : req.getReadLimit());
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 5];
            while (true) {
                int bytesRead = content.read(buffer);
                if (bytesRead == -1) {
                    break;
                }
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            content.reset();

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new ClientException("Unable to read request payload to sign request: " + e.getMessage(), e);
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    throw new ClientException("Unable to close byteArrayOutputStream: " + e.getMessage(), e);
                }
            }
        }
    }

    protected InputStream getBinaryRequestPayloadStream(Request<?> request) {
        if (HttpUtils.usePayloadForQueryParameters(request)) {
            String encodedParameters = HttpUtils.encodeParameters(request);
            if (encodedParameters == null) {
                return new ByteArrayInputStream(new byte[0]);
            }
            return new ByteArrayInputStream(encodedParameters.getBytes(UTF8));
        }

        return getBinaryRequestPayloadStreamWithoutQueryParams(request);
    }

    @SuppressWarnings("resource")
    protected InputStream getBinaryRequestPayloadStreamWithoutQueryParams(Request<?> request) {
        try {
            InputStream is = request.getContent();
            if (is == null) {
                return new ByteArrayInputStream(new byte[0]);
            }
            if (!is.markSupported()) {
                throw new ClientException("Unable to read request payload to sign request.");
            }
            return is;
        } catch (Exception e) {
            throw new ClientException("Unable to read request payload to sign request: " + e.getMessage(), e);
        }
    }

    protected String getCanonicalizedResourcePath(String resourcePath) {
        return getCanonicalizedResourcePath(resourcePath, true);
    }


    protected String getCanonicalizedResourcePath(String resourcePath, boolean urlEncode) {
        if (resourcePath == null || resourcePath.isEmpty()) {
            return "/";
        }
        String value = urlEncode ? HttpUtils.urlEncode(resourcePath, true) : resourcePath;
        if (value.startsWith("/")) {
            return value;
        }
        return "/".concat(value);
    }

    protected String getCanonicalizedEndpoint(URI endpoint) {
        String endpointForStringToSign = endpoint.getHost().toLowerCase();
        /*
         * Apache InnerHttpClient will omit the port in the Host header for default
         * port values (i.e. 80 for HTTP and 443 for HTTPS) even if we
         * explicitly specify it, so we need to be careful that we use the same
         * value here when we calculate the string to sign and in the Host
         * header we send in the HTTP request.
         */
        if (HttpUtils.isUsingNonDefaultPort(endpoint)) {
            endpointForStringToSign += ":" + endpoint.getPort();
        }

        return endpointForStringToSign;
    }

    /**
     * Loads the individual access key ID and secret key from the specified
     * credentials, ensuring that access to the credentials is synchronized on
     * the credentials object itself, and trimming any extra whitespace from the
     * credentials.
     * <p/>
     * Returns a {@link BasicCredentials} object, depending on the input type.
     *
     * @param credentials
     * @return A new credentials object with the sanitized credentials.
     */
    protected Credentials sanitizeCredentials(Credentials credentials) {
        String accessKeyId = credentials.getAccessKeyId();
        String secretKey = credentials.getSecretKey();
        if (secretKey != null) {
            secretKey = secretKey.trim();
        }
        if (accessKeyId != null) {
            accessKeyId = accessKeyId.trim();
        }
        return new BasicCredentials(accessKeyId, secretKey);
    }

    /**
     * Safely converts a UTF-8 encoded byte array into a String.
     *
     * @param bytes UTF-8 encoded binary character data.
     * @return The converted String object.
     */
    protected String newString(byte[] bytes) {
        return new String(bytes, UTF8);
    }

    /**
     * Returns the current time minus the given offset in seconds.
     *
     * @param offsetInSeconds offset in seconds
     */
    protected Date getSignatureDate(int offsetInSeconds) {
        return new Date(System.currentTimeMillis() - offsetInSeconds * 1000);
    }
}