/*
 * Copyright 2002-2016 the original author or authors.
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
package io.github.dis.iface.coordinator.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JoinGroupResponse
{
    private JoinGroupResponseState state;

    private List<String > subscription;

    private Long syncDelayedTimeMs;

    public JoinGroupResponseState getState() {
        return state;
    }

    public void setState(JoinGroupResponseState state) {
        this.state = state;
    }

    public Long getSyncDelayedTimeMs() {
        return syncDelayedTimeMs;
    }

    public void setSyncDelayedTimeMs(Long syncDelayedTimeMs) {
        this.syncDelayedTimeMs = syncDelayedTimeMs;
    }

    public List<String> getSubscription() {
        return subscription;
    }

    public void setSubscription(List<String> subscription) {
        this.subscription = subscription;
    }

    public enum JoinGroupResponseState
    {
        OK,ERROR,REJOIN,ERR_SUBSCRIPTION,GROUP_NOT_EXIST;
    }
}
