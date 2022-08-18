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

package com.otccloud.dis.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.otccloud.dis.core.Request;
import com.otccloud.dis.core.http.HttpMethodName;

public class HttpUtils {

    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Regex which matches any of the sequences that we need to fix up after
     * URLEncoder.encode().
     */
    private static final Pattern ENCODED_CHARACTERS_PATTERN;

    static {
        StringBuilder pattern = new StringBuilder();

        pattern.append(Pattern.quote("+"))
                .append("|")
                .append(Pattern.quote("*"))
                .append("|")
                .append(Pattern.quote("%7E"))
                .append("|")
                .append(Pattern.quote("%2F"));

        ENCODED_CHARACTERS_PATTERN = Pattern.compile(pattern.toString());
    }

    /**
     * Encode a string for use in the path of a URL; uses URLEncoder.encode,
     * (which encodes a string for use in the query portion of a URL), then
     * applies some postfilters to fix things up per the RFC. Can optionally
     * handle strings which are meant to encode a path (ie include '/'es
     * which should NOT be escaped).
     *
     * @param value the value to encode
     * @param path  true if the value is intended to represent a path
     * @return the encoded value
     */
    public static String urlEncode(final String value, final boolean path) {
        if (value == null) {
            return "";
        }

        try {
            String encoded = URLEncoder.encode(value, DEFAULT_ENCODING);

            Matcher matcher = ENCODED_CHARACTERS_PATTERN.matcher(encoded);
            StringBuffer buffer = new StringBuffer(encoded.length());

            while (matcher.find()) {
                String replacement = matcher.group(0);

                if ("+".equals(replacement)) {
                    replacement = "%20";
                } else if ("*".equals(replacement)) {
                    replacement = "%2A";
                } else if ("%7E".equals(replacement)) {
                    replacement = "~";
                } else if (path && "%2F".equals(replacement)) {
                    replacement = "/";
                }

                matcher.appendReplacement(buffer, replacement);
            }

            matcher.appendTail(buffer);
            return buffer.toString();

        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

	/**
	 * Returns true if the specified URI is using a non-standard port (i.e. any port
	 * other than 80 for HTTP URIs or any port other than 443 for HTTPS URIs).
	 *
	 * @param uri
	 *            The specified URI.
	 * @return True if the specified URI is using a non-standard port, otherwise
	 *         false.
	 */
    public static boolean isUsingNonDefaultPort(URI uri) {
        String scheme = uri.getScheme().toLowerCase();
        int port = uri.getPort();
        if (port <= 0) {
            return false;
        }
        if (scheme.equals("http") && port == 80) {
            return false;
        }
        return !(scheme.equals("https") && port == 443);
    }

    public static boolean usePayloadForQueryParameters(Request<?> request) {
        boolean requestIsPOST = HttpMethodName.POST.equals(request.getHttpMethod());
        boolean requestHasNoPayload = (request.getContent() == null);

        return requestIsPOST && requestHasNoPayload;
    }

    /**
     * Creates an encoded query string from all the parameters in the specified
     * request.
     *
     * @param request The request containing the parameters to encode.
     * @return Null if no parameters were present, otherwise the encoded query
     * string for the parameters present in the specified request.
     */
    public static String encodeParameters(Request<?> request) {
        List<NameValuePair> nameValuePairs = null;
        int size = request.getParameters().size();
        if (size > 0) {
            nameValuePairs = new ArrayList<NameValuePair>(size);
            List parameters = new ArrayList(request.getParameters().entrySet());
            Collections.sort(parameters, new Comparator() {
                public int compare(Object arg1, Object arg2) {
                    Map.Entry<String, String> obj1 = (Map.Entry) arg1;
                    Map.Entry<String, String> obj2 = (Map.Entry) arg2;
                    return (obj1.getKey()).toString().compareTo(obj2.getKey());
                }
            });
            Iterator iterator = parameters.iterator();
            Map.Entry<String, String> parameter;
            while (iterator.hasNext()) {
                parameter = (Map.Entry<String, String>) iterator.next();
                nameValuePairs.add(new BasicNameValuePair(parameter.getKey(), parameter.getValue()));
            }
        }

        String encodedParams = null;
        if (nameValuePairs != null) {
            encodedParams = URLEncodedUtils.format(nameValuePairs, DEFAULT_ENCODING);
        }

        return encodedParams;
    }

	/**
	 * Append the given path to the given baseUri. By default, all slash characters
	 * in path will not be url-encoded.
	 * 
	 * @param baseUri
	 *            Base uri to be appended.
	 * @param path
	 *            Path string to append.
	 * @return Uri string after appending the given path.
	 */
    public static String appendUri(String baseUri, String path) {
        return appendUri(baseUri, path, false);
    }

    /**
     * Append the given path to the given baseUri.
     * <p>This method will encode the given path but not the given
     * baseUri.</p>
     *
     * @param baseUri           The URI to append to (required, may be relative)
     * @param path              The path to append (may be null or empty)
     * @param escapeDoubleSlash Whether double-slash in the path should be escaped to "/%2F"
     * @return The baseUri with the (encoded) path appended
     */
    public static String appendUri(final String baseUri, String path, final boolean escapeDoubleSlash) {
        String resultUri = baseUri;
        if (path != null && path.length() > 0) {
            if (path.startsWith("/")) {
                // trim the trailing slash in baseUri, since the path already starts with a slash
                if (resultUri.endsWith("/")) {
                    resultUri = resultUri.substring(0, resultUri.length() - 1);
                }
            } else if (!resultUri.endsWith("/")) {
                resultUri += "/";
            }
            String encodedPath = HttpUtils.urlEncode(path, true);
            if (escapeDoubleSlash) {
                encodedPath = encodedPath.replace("//", "/%2F");
            }
            resultUri += encodedPath;
        } else if (!resultUri.endsWith("/")) {
            resultUri += "/";
        }

        return resultUri;
    }
}