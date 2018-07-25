package com.huaweicloud.dis.iface.coordinator.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by z00382129 on 2017/10/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeaveGroupResponse {
    public enum LeaveGroupResponseState
    {
        OK,ERROR;
    }
    LeaveGroupResponseState state;
    public LeaveGroupResponseState getState() {
        return state;
    }

    public void setState(LeaveGroupResponseState state) {
        this.state = state;
    }
}
