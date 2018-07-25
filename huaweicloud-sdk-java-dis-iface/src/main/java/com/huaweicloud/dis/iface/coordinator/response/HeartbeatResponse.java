package com.huaweicloud.dis.iface.coordinator.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by z00382129 on 2017/10/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HeartbeatResponse
{
     private HeartBeatResponseState state;

    public HeartBeatResponseState getState() {
        return state;
    }

    public void setState(HeartBeatResponseState state) {
        this.state = state;
    }

    public enum HeartBeatResponseState {
        JOINING,SYNCING,STABLE,GROUP_NOT_EXIST;
    }
}
