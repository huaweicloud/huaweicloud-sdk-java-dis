package com.huaweicloud.dis.iface.coordinator.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

/**
 * Created by z00382129 on 2017/10/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SyncGroupResponse {

    private SyncGroupResponseState state;

    private Long generation;

    private Map<String , List<Integer>> assignment;

    public Map<String, List<Integer>> getAssignment() {
        return assignment;
    }

    public void setAssignment(Map<String, List<Integer>> assignment) {
        this.assignment = assignment;
    }

    public SyncGroupResponseState getState() {
        return state;
    }

    public void setState(SyncGroupResponseState state) {
        this.state = state;
    }

    public Long getGeneration() {
        return generation;
    }

    public void setGeneration(Long generation) {
        this.generation = generation;
    }

    public enum SyncGroupResponseState
    {
        OK,ERROR,WAITING,REJOIN,GROUP_NOT_EXIST;
    }

}
