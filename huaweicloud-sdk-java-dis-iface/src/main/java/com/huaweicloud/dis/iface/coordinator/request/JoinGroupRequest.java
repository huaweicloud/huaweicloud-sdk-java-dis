package com.huaweicloud.dis.iface.coordinator.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by z00382129 on 2017/10/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JoinGroupRequest {

    private String projectId;

    private String groupId;

    private String clientId;

    private List<String> interestedStream;

    private String streamPattern;

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
}
