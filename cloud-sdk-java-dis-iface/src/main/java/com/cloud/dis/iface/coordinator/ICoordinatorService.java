package com.cloud.dis.iface.coordinator;

import com.cloud.dis.iface.coordinator.request.HeartbeatRequest;
import com.cloud.dis.iface.coordinator.request.JoinGroupRequest;
import com.cloud.dis.iface.coordinator.request.LeaveGroupRequest;
import com.cloud.dis.iface.coordinator.request.SyncGroupRequest;
import com.cloud.dis.iface.coordinator.response.HeartbeatResponse;
import com.cloud.dis.iface.coordinator.response.JoinGroupResponse;
import com.cloud.dis.iface.coordinator.response.SyncGroupResponse;

public interface ICoordinatorService
{
    HeartbeatResponse handleHeartbeatRequest(HeartbeatRequest heartbeatRequest);

    JoinGroupResponse handleJoinGroupRequest(JoinGroupRequest joinGroupRequest);

    SyncGroupResponse handleSyncGroupRequest(SyncGroupRequest syncGroupRequest);

    void handleLeaveGroupRequest(LeaveGroupRequest leaveGroupRequest);

}