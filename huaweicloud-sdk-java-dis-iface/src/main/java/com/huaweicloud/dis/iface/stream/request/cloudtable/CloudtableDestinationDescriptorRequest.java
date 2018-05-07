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

package com.huaweicloud.dis.iface.stream.request.cloudtable;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.huaweicloud.dis.iface.stream.request.OBSDestinationDescriptorRequest;

/**
 * Created by s00348548 on 2018/1/10.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CloudtableDestinationDescriptorRequest extends OBSDestinationDescriptorRequest
{
    /** Cloudtable cluster name */
    @JsonProperty("cloudtable_cluster_name")
    private String cloudtableClusterName;
    
    @JsonProperty("cloudtable_cluster_id")
    private String cloudtableClusterId;
    
    /** Cloudtable table name */
    @JsonProperty("cloudtable_table_name")
    private String cloudtableTableName;
    
    /** rowKey delimiter */
    @JsonProperty("cloudtable_row_key_delimiter")
    private String cloudtableRowkeyDelimiter;
    
    /** Cloudtable schema */
    @JsonProperty("cloudtable_schema")
    private CloudtableSchema cloudtableSchema;
    
    /** openTSDB schema */
    @JsonProperty("opentsdb_schema")
    private List<OpenTSDBSchema> opentsdbSchema;
    
    /** OBS error backup file name prefix */
    @JsonProperty("backup_file_prefix")
    private String backupfilePrefix;
    
    /** obs error backup Bucket path */
    @JsonProperty("obs_backup_bucket_path")
    private String obsBackupBucketPath;
    
    public String getCloudtableClusterName()
    {
        return cloudtableClusterName;
    }
    
    public void setCloudtableClusterName(String cloudtableClusterName)
    {
        this.cloudtableClusterName = cloudtableClusterName;
    }
    
    public String getCloudtableClusterId()
    {
        return cloudtableClusterId;
    }
    
    public void setCloudtableClusterId(String cloudtableClusterId)
    {
        this.cloudtableClusterId = cloudtableClusterId;
    }
    
    public String getCloudtableTableName()
    {
        return cloudtableTableName;
    }
    
    public void setCloudtableTableName(String cloudtableTableName)
    {
        this.cloudtableTableName = cloudtableTableName;
    }
    
    public CloudtableSchema getCloudtableSchema()
    {
        return cloudtableSchema;
    }
    
    public void setCloudtableSchema(CloudtableSchema cloudtableSchema)
    {
        this.cloudtableSchema = cloudtableSchema;
    }
    
    public List<OpenTSDBSchema> getOpentsdbSchema()
    {
        return opentsdbSchema;
    }
    
    public void setOpentsdbSchema(List<OpenTSDBSchema> opentsdbSchema)
    {
        this.opentsdbSchema = opentsdbSchema;
    }
    
    public String getCloudtableRowkeyDelimiter()
    {
        return cloudtableRowkeyDelimiter;
    }
    
    public void setCloudtableRowkeyDelimiter(String cloudtableRowkeyDelimiter)
    {
        this.cloudtableRowkeyDelimiter = cloudtableRowkeyDelimiter;
    }
    
    public String getBackupfilePrefix()
    {
        return backupfilePrefix;
    }
    
    public void setBackupfilePrefix(String backupfilePrefix)
    {
        this.backupfilePrefix = backupfilePrefix;
    }
    
    public String getObsBackupBucketPath()
    {
        return obsBackupBucketPath;
    }
    
    public void setObsBackupBucketPath(String obsBackupBucketPath)
    {
        this.obsBackupBucketPath = obsBackupBucketPath;
    }
}
