package com.huaweicloud.dis.iface.coordinator.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by z00382129 on 2017/10/17.
 */
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
