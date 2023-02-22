package io.github.dis.iface.coordinator;

import io.github.dis.iface.coordinator.request.HeartbeatRequest;
import io.github.dis.iface.coordinator.request.JoinGroupRequest;
import io.github.dis.iface.coordinator.request.LeaveGroupRequest;
import io.github.dis.iface.coordinator.request.SyncGroupRequest;
import io.github.dis.iface.coordinator.response.HeartbeatResponse;
import io.github.dis.iface.coordinator.response.JoinGroupResponse;
import io.github.dis.iface.coordinator.response.SyncGroupResponse;

public interface ICoordinatorService
{
    HeartbeatResponse handleHeartbeatRequest(HeartbeatRequest heartbeatRequest);

    JoinGroupResponse handleJoinGroupRequest(JoinGroupRequest joinGroupRequest);

    SyncGroupResponse handleSyncGroupRequest(SyncGroupRequest syncGroupRequest);

    void handleLeaveGroupRequest(LeaveGroupRequest leaveGroupRequest);

}