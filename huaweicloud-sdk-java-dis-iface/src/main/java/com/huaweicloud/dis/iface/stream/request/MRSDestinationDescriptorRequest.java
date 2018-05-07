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

package com.huaweicloud.dis.iface.stream.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by s00348548 on 2017/10/25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MRSDestinationDescriptorRequest extends OBSDestinationDescriptorRequest
{
    /** MRS clusterName */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("mrs_cluster_name")
    private String mrsClusterName;
    
    /** MRS clusterId */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("mrs_cluster_id")
    private String mrsClusterId;
    
    /** MRS hdfsPath */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("mrs_hdfs_path")
    private String mrsHdfsPath;
    
    /** appends the hdfs folder prefix to delivered files */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("hdfs_prefix_folder")
    private String hdfsPrefixFolder;
    
    public String getMrsClusterName()
    {
        return mrsClusterName;
    }
    
    public void setMrsClusterName(String mrsClusterName)
    {
        this.mrsClusterName = mrsClusterName;
    }
    
    public String getMrsClusterId()
    {
        return mrsClusterId;
    }
    
    public void setMrsClusterId(String mrsClusterId)
    {
        this.mrsClusterId = mrsClusterId;
    }
    
    public String getMrsHdfsPath()
    {
        return mrsHdfsPath;
    }
    
    public void setMrsHdfsPath(String mrsHdfsPath)
    {
        this.mrsHdfsPath = mrsHdfsPath;
    }
    
    public String getHdfsPrefixFolder()
    {
        return hdfsPrefixFolder;
    }
    
    public void setHdfsPrefixFolder(String hdfsPrefixFolder)
    {
        this.hdfsPrefixFolder = hdfsPrefixFolder;
    }
    
}
