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

package com.huaweicloud.dis.iface.stream.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListStreamsResult {

    @JsonProperty("total_number")
    private int streamNumber;
    
    @JsonProperty("stream_names")
    private List<String> streamNames;
    
    @JsonProperty("has_more_streams")
    private Boolean hasMoreStreams;

	public int getStreamNumber() {
		return streamNumber;
	}

	public void setStreamNumber(int streamNumber) {
		this.streamNumber = streamNumber;
	}

	public List<String> getStreamNames() {
		return streamNames;
	}

	public void setStreamNames(List<String> streamNames) {
		this.streamNames = streamNames;
	}

	public Boolean getHasMoreStreams()
    {
        return hasMoreStreams;
    }

    public void setHasMoreStreams(Boolean hasMoreStreams)
    {
        this.hasMoreStreams = hasMoreStreams;
    }

    @Override
	public String toString() {
		return "ListStreamResult [streamNumber=" + streamNumber
				+ ", streamsDesc=" + streamNames + "]";
	}

}
