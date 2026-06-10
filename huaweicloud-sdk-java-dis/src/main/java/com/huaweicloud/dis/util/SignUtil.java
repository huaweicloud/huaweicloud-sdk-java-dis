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

import com.huaweicloud.dis.core.auth.signer.internal.SignerConstants;
import com.huaweicloud.dis.core.auth.signer.internal.SignerUtils;
import com.huaweicloud.dis.core.util.StringUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.http.HttpRequest;

import com.huaweicloud.dis.core.Request;
import com.huaweicloud.dis.core.auth.credentials.BasicCredentials;
import com.huaweicloud.dis.core.auth.signer.Signer;
import com.huaweicloud.dis.core.auth.signer.SignerFactory;
import com.huaweicloud.dis.Constants;

import java.util.Properties;
import static com.huaweicloud.dis.core.auth.signer.internal.SignerConstants.DERIVATION_KEY_SIGNING_ALGORITHM;

public class SignUtil
{
    public static Request<HttpRequest> sign(Request<HttpRequest> request, String ak, String sk, String region)
    {
        // sign request
        Signer signer = SignerFactory.getSigner(Constants.SERVICENAME, region);
        signer.sign(request, new BasicCredentials(ak, sk));
        return request;
    }

    public static Request<HttpRequest> sign(Request<HttpRequest> request, String ak, String sk, String region, Properties prop)
    {
        // sign request
        Signer signer = SignerFactory.getSigner(Constants.SERVICENAME, region);
        signer.sign(request, new BasicCredentials(ak, sk),prop);
        return request;
    }

    public static Request<HttpRequest> signWithDerivationKey(Request<HttpRequest> request, String ak, String sk, String region, Properties prop) {
        String singerDate = request.getHeaders().get(SignerConstants.X_SDK_DATE);
        if (StringUtils.isNullOrEmpty(singerDate) || singerDate.length() < 8) {
            singerDate = SignerUtils.formatTimestamp(SignUtil.getSigningDate(request));
        }

        String info = singerDate.substring(0,8) + "/" + "region" + "/" + Constants.SERVICENAME;
        String derivationKey = HKDF.getDerKey(ak, sk, info, DERIVATION_KEY_SIGNING_ALGORITHM);

        return sign(request, ak, derivationKey, region, prop);
    }

    public static long getSigningDate(Request<?> request) {
        return System.currentTimeMillis() - request.getTimeOffset() * 1000;
    }

    public static boolean getDerivationKeySwitch(Properties prop) {
        try {
            return prop.getProperty(SignerConstants.DERIVATION_KEY_SWITCH).equals("true");
        }catch (Exception e){
            return true;
        }
    }
}
