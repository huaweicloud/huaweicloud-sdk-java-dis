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

package com.huaweicloud.dis.iface.data.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.huaweicloud.dis.iface.stream.request.ForceStringDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileUploadResult
{
    public static String STATE_NOT_FOUND = "not found";

    public static String STATE_RECEIVING = "receiving";

    public static String STATE_UPLOADING = "uploading";

    public static String STATE_IN_OBS = "in obs";

    public static String STATE_CANCELLED = "cancelled";

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public String getDeliverDataId() {
        return deliverDataId;
    }

    public void setDeliverDataId(String deliverDataId) {
        this.deliverDataId = deliverDataId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("stream_name")
    private String streamName;

    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("file_name")
    private String fileName;

    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("deliver_data_id")
    private String deliverDataId;

    @JsonDeserialize(using = ForceStringDeserializer.class)
    private String state;

    @Override
    public String toString()
    {
        return "FileUploadResult [streamName=" + streamName + ", fileName=" + fileName + ", deliverDataId="
            + deliverDataId + ", state=" + state + "]";
    }
    
}
