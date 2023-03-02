package com.cloud.dis.iface.coordinator.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JoinGroupRequest {

    private String projectId;

    private String groupId;

    private String clientId;

    private List<String> interestedStream;

    private String streamPattern;

    /**
     * 是否加速分配，建议只有单一Consumer时开启，加速服务端分区分配
     */
    private Boolean accelerateAssignEnabled;

    /**
     * 加入消费组的超时时间
     */
    private Long rebalanceTimeoutMs;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<String> getInterestedStream() {
        return interestedStream;
    }

    public void setInterestedStream(List<String> interestedStream) {
        this.interestedStream = interestedStream;
    }

    public String getStreamPattern() {
        return streamPattern;
    }

    public void setStreamPattern(String streamPattern) {
        this.streamPattern = streamPattern;
    }

    public Boolean isAccelerateAssignEnabled() {
        return accelerateAssignEnabled;
    }

    public void setAccelerateAssignEnabled(Boolean accelerateAssignEnabled) {
        this.accelerateAssignEnabled = accelerateAssignEnabled;
    }

    public Long getRebalanceTimeoutMs() {
        return rebalanceTimeoutMs;
    }

    public void setRebalanceTimeoutMs(Long rebalanceTimeoutMs) {
        this.rebalanceTimeoutMs = rebalanceTimeoutMs;
    }
}
