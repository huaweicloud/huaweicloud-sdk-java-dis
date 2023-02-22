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

package io.github.dis.util;

import io.github.dis.Constants;
import io.github.dis.core.Request;
import io.github.dis.core.auth.signer.Signer;
import io.github.dis.core.auth.signer.SignerFactory;
import org.apache.http.HttpRequest;

import io.github.dis.core.auth.credentials.BasicCredentials;

import java.util.Properties;

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
}
