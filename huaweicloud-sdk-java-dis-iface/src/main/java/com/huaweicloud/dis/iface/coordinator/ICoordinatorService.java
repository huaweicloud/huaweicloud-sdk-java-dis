package com.huaweicloud.dis.iface.coordinator;

import com.huaweicloud.dis.iface.coordinator.request.HeartbeatRequest;
import com.huaweicloud.dis.iface.coordinator.request.JoinGroupRequest;
import com.huaweicloud.dis.iface.coordinator.request.LeaveGroupRequest;
import com.huaweicloud.dis.iface.coordinator.request.SyncGroupRequest;
import com.huaweicloud.dis.iface.coordinator.response.HeartbeatResponse;
import com.huaweicloud.dis.iface.coordinator.response.JoinGroupResponse;
import com.huaweicloud.dis.iface.coordinator.response.SyncGroupResponse;

public interface ICoordinatorService
{
    HeartbeatResponse handleHeartbeatRequest(HeartbeatRequest heartbeatRequest);

    JoinGroupResponse handleJoinGroupRequest(JoinGroupRequest joinGroupRequest);

    SyncGroupResponse handleSyncGroupRequest(SyncGroupRequest syncGroupRequest);

    void handleLeaveGroupRequest(LeaveGroupRequest leaveGroupRequest);

}