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
package io.github.dis.iface.transfertask.response.destination;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.dis.iface.stream.request.ForceStringDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MRSDestinationDescription extends OBSDestinationDescription
{
    /**
     * <p>
     * String Name of the MRS cluster to which data in the DIS stream will be dumped. The chosen MRS cluster must be
     * non-Kerberos authenticated.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("mrs_cluster_name")
    private String mrsClusterName;
    
    /**
     * <p>
     * ID of the MRS cluster to which data in the DIS stream will be dumped.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("mrs_cluster_id")
    private String mrsClusterId;
    
    /**
     * <p>
     * Hadoop Distributed File System (HDFS) path of the MRS cluster to which data in the DIS stream will be dumped.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("mrs_hdfs_path")
    private String mrsHdfsPath;
    
    /**
     * <p>
     * Directory to hold files that will be dumped to MRS. Different directory levels are separated by a forward slash
     * (/) and cannot start with a forward slash (/).
     * </p>
     */
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
