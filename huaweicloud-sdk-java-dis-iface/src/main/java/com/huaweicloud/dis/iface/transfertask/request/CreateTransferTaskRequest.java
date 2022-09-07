/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huaweicloud.dis.iface.transfertask.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.huaweicloud.dis.iface.stream.request.OBSDestinationDescriptorRequest;
import com.huaweicloud.dis.iface.stream.request.StreamDestinationType;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateTransferTaskRequest {
    /**
     * <p>
     * The name of the delivery stream.
     * </p>
     */
    @JsonProperty("stream_name")
    private String streamName;

    /**
     * <p>
     * The destination type of the delivery task. For Example, OBS.
     * </p>
     */
    @JsonProperty("destination_type")
    private StreamDestinationType destinationType;

    /**
     * <p>
     * Parameter list of the OBS to which data in the DIS stream will be dumped.
     * </p>
     */
    @JsonProperty("obs_destination_descriptor")
    private OBSDestinationDescriptorRequest obsDestinationDescriptor;

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public StreamDestinationType getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(StreamDestinationType destinationType) {
        this.destinationType = destinationType;
    }

    public OBSDestinationDescriptorRequest getObsDestinationDescriptor() {
        return obsDestinationDescriptor;
    }

    public void setObsDestinationDescriptor(OBSDestinationDescriptorRequest obsDestinationDescriptor) {
        this.obsDestinationDescriptor = obsDestinationDescriptor;
    }

    @Override
    public String toString() {
        return "CreateTransferTaskRequest{" + "streamName='" + streamName + '\'' + '}';
    }
}
