package com.otccloud.dis.iface.coordinator;

import com.otccloud.dis.iface.coordinator.request.HeartbeatRequest;
import com.otccloud.dis.iface.coordinator.request.JoinGroupRequest;
import com.otccloud.dis.iface.coordinator.request.LeaveGroupRequest;
import com.otccloud.dis.iface.coordinator.request.SyncGroupRequest;
import com.otccloud.dis.iface.coordinator.response.HeartbeatResponse;
import com.otccloud.dis.iface.coordinator.response.JoinGroupResponse;
import com.otccloud.dis.iface.coordinator.response.SyncGroupResponse;

public interface ICoordinatorService
{
    HeartbeatResponse handleHeartbeatRequest(HeartbeatRequest heartbeatRequest);

    JoinGroupResponse handleJoinGroupRequest(JoinGroupRequest joinGroupRequest);

    SyncGroupResponse handleSyncGroupRequest(SyncGroupRequest syncGroupRequest);

    void handleLeaveGroupRequest(LeaveGroupRequest leaveGroupRequest);

}