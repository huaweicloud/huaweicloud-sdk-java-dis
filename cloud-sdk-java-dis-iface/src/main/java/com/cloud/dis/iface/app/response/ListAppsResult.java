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

package com.cloud.dis.iface.app.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListAppsResult {

    @JsonProperty("has_more_app")
    private Boolean hasMoreApp;

    @JsonProperty("apps")
    private List<DescribeAppResult> apps;

    public Boolean getHasMoreApp() {
        return hasMoreApp;
    }

    public void setHasMoreApp(Boolean hasMoreApp) {
        this.hasMoreApp = hasMoreApp;
    }

    public List<DescribeAppResult> getApps() {
        return apps;
    }

    public void setApps(List<DescribeAppResult> apps) {
        this.apps = apps;
    }
}
