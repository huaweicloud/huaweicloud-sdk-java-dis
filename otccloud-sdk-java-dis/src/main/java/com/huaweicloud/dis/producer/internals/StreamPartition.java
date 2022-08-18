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

package com.otccloud.dis.producer.internals;

import java.io.Serializable;

/**
 * A topic name and partition number
 */
public final class StreamPartition implements Serializable {

    private int hash = 0;
    private final String partitionId;
    private final String streamName;
    private final String streamId;

    public StreamPartition(String topic, String streamId, String partitionId) {
        this.partitionId = partitionId;
        this.streamName = topic;
        this.streamId = streamId;
    }

    public StreamPartition(String topic, String partitionId) {
        this(topic, null, partitionId);
    }

    public String partition() {
        return partitionId;
    }

    public String topic() {
        return streamName;
    }

    public String streamId() {
        return streamId;
    }

    @Override
    public int hashCode() {
        if (hash != 0)
            return hash;
        final int prime = 31;
        int result = 1;
        result = prime * result + ((partitionId == null) ? 0 : partitionId.hashCode());
        result = prime * result + ((streamName == null) ? 0 : streamName.hashCode());
        this.hash = result;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StreamPartition other = (StreamPartition) obj;
        
        if (partitionId == null) {
            if (other.partitionId != null)
                return false;
        } else if (!partitionId.equals(other.partitionId))
            return false;
        
        if (streamName == null) {
            if (other.streamName != null)
                return false;
        } else if (!streamName.equals(other.streamName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return streamName + "-" + partitionId;
    }
}
