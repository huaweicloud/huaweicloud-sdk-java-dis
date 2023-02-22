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

import io.github.dis.DISConfig;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;

public class ApacheUtils
{
    
    /**
     * Returns a new Credentials Provider for use with proxy authentication.
     */
    public static CredentialsProvider newProxyCredentialsProvider
    (DISConfig disConfig) {
        final CredentialsProvider provider = new BasicCredentialsProvider();
//        provider.setCredentials(newAuthScope(disConfig), newNTCredentials(disConfig));
        provider.setCredentials(AuthScope.ANY, newNTCredentials(disConfig));
        return provider;
    }
    
    /**
     * Returns a new instance of NTCredentials used for proxy authentication.
     */
    private static Credentials newNTCredentials(DISConfig disConfig) {
        return new NTCredentials(disConfig.getProxyUsername(),
            disConfig.getProxyPassword(),
            disConfig.getProxyWorkstation(),
            disConfig.getProxyDomain());
    }
    
    /**
     * Returns a new instance of AuthScope used for proxy authentication.
     */
    private static AuthScope newAuthScope(DISConfig disConfig) {
        return new AuthScope(disConfig.getProxyHost(), disConfig.getProxyPort());
    }
    
}
