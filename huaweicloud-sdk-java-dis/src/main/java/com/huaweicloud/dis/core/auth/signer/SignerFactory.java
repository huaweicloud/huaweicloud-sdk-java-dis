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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.huaweicloud.dis.core.internal.config.InternalConfig;
import com.huaweicloud.dis.core.internal.config.SignerConfig;

/**
 * Signer factory.
 */
public final class SignerFactory {
    
    private static final String DEFAULT_SIGNER = "DefaultSignerType";
    
    private static final Map<String, Class< ? extends Signer>> SIGNERS =
        new ConcurrentHashMap<String, Class< ? extends Signer>>();
        
    static {
        // Register the standard signer types.
        SIGNERS.put(DEFAULT_SIGNER, DefaultSigner.class);
    }
    
    /**
     * Private so you're not tempted to instantiate me.
     */
    private SignerFactory() {
    }
    
    /**
     * Register an implementation class for the given signer type.
     *
     * @param signerType  The name of the signer type to register.
     * @param signerClass The class implementing the given signature protocol.
     */
    public static void registerSigner(final String signerType, final Class< ? extends Signer> signerClass) {
        
        if (signerType == null) {
            throw new IllegalArgumentException("signerType cannot be null");
        }
        if (signerClass == null) {
            throw new IllegalArgumentException("signerClass cannot be null");
        }
        
        SIGNERS.put(signerType, signerClass);
    }
    
    /**
     * Returns a non-null signer for the specified service and region according
     * to the internal configuration which provides a basic default algorithm
     * used for signer determination.
     *
     * @param serviceName The name of the service to talk to.
     * @param regionName  The name of the region to talk to; not necessarily the region
     *                    used for signing.
     */
    public static Signer getSigner(String serviceName, String regionName) {
        return lookupAndCreateSigner(serviceName, regionName);
    }
    
    /**
     * Returns an instance of the given signer type and configures it with the
     * given service name (if applicable).
     *
     * @param signerType  The type of signer to create.
     * @param serviceName The name of the service to configure on the signer.
     * @return a non-null signer.
     */
    public static Signer getSignerByTypeAndService(String signerType, final String serviceName) {
        return createSigner(signerType, serviceName, null);
    }
    
    /**
     * Internal implementation for looking up and creating a signer by service
     * name and region.
     */
    private static Signer lookupAndCreateSigner(String serviceName, String regionName) {
        InternalConfig config = InternalConfig.Factory.getInternalConfig();
        SignerConfig signerConfig = config.getSignerConfig(serviceName, regionName);
        String signerType = signerConfig.getSignerType();
        return createSigner(signerType, serviceName, regionName);
    }
    
    /**
     * Internal implementation to create a signer by type and service name,
     * and configuring it with the service name if applicable.
     */
    private static Signer createSigner(String signerType, final String serviceName, final String regionName) {
        Class< ? extends Signer> signerClass = SIGNERS.get(signerType);
        if (signerClass == null) {
            throw new IllegalArgumentException("unknown signer type: " + signerType);
        }
        Signer signer;
        try {
            signer = signerClass.newInstance();
        } catch (InstantiationException ex) {
            throw new IllegalStateException("Cannot create an instance of " + signerClass.getName(), ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("Cannot create an instance of " + signerClass.getName(), ex);
        }
        
        if (signer instanceof ServiceSigner) {
            ((ServiceSigner) signer).setServiceName(serviceName);
        }
        
        if (signer instanceof RegionSigner) {
            ((RegionSigner) signer).setRegionName(regionName);
        }
        return signer;
    }
}