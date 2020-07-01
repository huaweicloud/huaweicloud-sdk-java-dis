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

package com.huaweicloud.dis.core.internal.config;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.huaweicloud.dis.core.util.ClassLoaderHelper;
import com.huaweicloud.dis.core.util.json.Jackson;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Internal configuration for Java SDK.
 */
@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class InternalConfig {

    private static final Log log = LogFactory.getLog(InternalConfig.class);

    static final String DEFAULT_CONFIG_RESOURCE = "sdk_config_default.json";
    static final String CONFIG_OVERRIDE_RESOURCE = "sdk_config_override.json";
    private static final String SERVICE_REGION_DELIMITER = "/";

    private final SignerConfig defaultSignerConfig;
    private final Map<String, SignerConfig> serviceRegionSigners;
    private final Map<String, SignerConfig> regionSigners;
    private final Map<String, SignerConfig> serviceSigners;

    /**
     * @param defaults default configuration
     * @param override override configuration
     */
    InternalConfig(InternalConfigJsonHelper defaults, InternalConfigJsonHelper override) {
        SignerConfigJsonHelper scb = defaults.getDefaultSigner();
        this.defaultSignerConfig = scb == null ? null : scb.build();

        regionSigners = mergeSignerMap(defaults.getRegionSigners(), override.getRegionSigners(), "region");
        serviceSigners = mergeSignerMap(defaults.getServiceSigners(), override.getServiceSigners(), "service");
        serviceRegionSigners = mergeSignerMap(defaults.getServiceRegionSigners(), override.getServiceRegionSigners(), "service" + SERVICE_REGION_DELIMITER + "region");
    }

    /**
     * Returns an immutable map by merging the override signer configuration
     * into the default signer configuration for the given theme.
     *
     * @param defaults  default signer configuration
     * @param overrides signer configurations overrides
     * @param theme     used for message logging. eg region, service, region+service
     */
    private Map<String, SignerConfig> mergeSignerMap(JsonIndex<SignerConfigJsonHelper, SignerConfig>[] defaults,
                                                     JsonIndex<SignerConfigJsonHelper, SignerConfig>[] overrides, String theme) {
        Map<String, SignerConfig> map = buildSignerMap(defaults, theme);
        Map<String, SignerConfig> mapOverride = buildSignerMap(overrides, theme);
        map.putAll(mapOverride);
        return Collections.unmodifiableMap(map);
    }

    /**
     * Builds and returns a signer configuration map.
     *
     * @param signerIndexes signer configuration entries loaded from JSON
     * @param theme         used for message logging. eg region, service, region+service
     */
    private Map<String, SignerConfig> buildSignerMap(JsonIndex<SignerConfigJsonHelper, SignerConfig>[] signerIndexes, String theme) {
        Map<String, SignerConfig> map = new HashMap<String, SignerConfig>();
        if (signerIndexes != null) {
            for (JsonIndex<SignerConfigJsonHelper, SignerConfig> index : signerIndexes) {
                String region = index.getKey();
                SignerConfig prev = map.put(region, index.newReadOnlyConfig());
                if (prev != null) {
                    log.warn("Duplicate definition of signer for " + theme + " " + index.getKey());
                }
            }
        }
        return map;
    }

    /**
     * Returns the signer configuration for the specified service, not
     * specific to any region.
     */
    public SignerConfig getSignerConfig(String serviceName) {
        return getSignerConfig(serviceName, null);
    }

    /**
     * Returns the signer configuration for the specified service name and
     * an optional region name.
     *
     * @param serviceName must not be null
     * @param regionName  region name;can be null.
     * @return the signer
     */
    public SignerConfig getSignerConfig(String serviceName, String regionName) {
        if (serviceName == null) {
            throw new IllegalArgumentException();
        }
        SignerConfig signerConfig = null;
        if (regionName != null) {
            // Service+Region signer config has the highest precedence
            String key = serviceName + SERVICE_REGION_DELIMITER + regionName;
            signerConfig = serviceRegionSigners.get(key);
            if (signerConfig != null) {
                return signerConfig;
            }
            // Region signer config has the 2nd highest precedence
            signerConfig = regionSigners.get(regionName);
            if (signerConfig != null) {
                return signerConfig;
            }
        }
        // Service signer config has the 3rd highest precedence
        signerConfig = serviceSigners.get(serviceName);
        // Fall back to the default
        return signerConfig == null ? defaultSignerConfig : signerConfig;
    }

    static InternalConfigJsonHelper loadfrom(URL url) throws IOException, JsonMappingException {
        if (url == null) {
            throw new IllegalArgumentException();
        }
        InternalConfigJsonHelper target = Jackson.getObjectMapper().readValue(url, InternalConfigJsonHelper.class);
        return target;
    }

    /**
     * Loads and returns the Java SDK internal configuration from the
     * classpath.
     */
    static InternalConfig load() throws IOException, JsonMappingException {
        URL url = ClassLoaderHelper.getResource(File.separator + DEFAULT_CONFIG_RESOURCE, InternalConfig.class);
        if (url == null) {
            url = ClassLoaderHelper.getResource(DEFAULT_CONFIG_RESOURCE, InternalConfig.class);
        }
        InternalConfigJsonHelper config = loadfrom(url);
        InternalConfigJsonHelper configOverride;
        URL overrideUrl = ClassLoaderHelper.getResource(File.separator + CONFIG_OVERRIDE_RESOURCE, InternalConfig.class);
        if (overrideUrl == null) {
            overrideUrl = ClassLoaderHelper.getResource(CONFIG_OVERRIDE_RESOURCE, InternalConfig.class);
        }
        if (overrideUrl == null) {
            log.debug("Configuration override " + CONFIG_OVERRIDE_RESOURCE + " not found.");
            configOverride = new InternalConfigJsonHelper();
        } else {
            configOverride = loadfrom(overrideUrl);
        }
        return new InternalConfig(config, configOverride);
    }

    // For debugging purposes
    void dump() {
        StringBuilder sb = new StringBuilder().append("defaultSignerConfig: ")
                .append(defaultSignerConfig).append("\n")
                .append("serviceRegionSigners: ").append(serviceRegionSigners)
                .append("\n").append("regionSigners: ").append(regionSigners)
                .append("\n").append("serviceSigners: ").append(serviceSigners);
        log.debug(sb.toString());
    }

    public static class Factory {
        private static final InternalConfig SINGLETON;

        static {
            InternalConfig config;
            try {
                config = InternalConfig.load();
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new IllegalStateException("Fatal: Failed to load the internal config for Java SDK", ex);
            }
            SINGLETON = config;
        }

        /**
         * Returns a non-null and immutable instance of the SDK internal
         * configuration.
         */
        public static InternalConfig getInternalConfig() {
            return SINGLETON;
        }
    }
}