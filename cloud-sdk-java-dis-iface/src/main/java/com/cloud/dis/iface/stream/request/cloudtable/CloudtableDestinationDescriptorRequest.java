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

package com.cloud.dis.iface.stream.request.cloudtable;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.cloud.dis.iface.stream.request.ForceStringDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CloudtableDestinationDescriptorRequest
{
    /**
     * <p>
     * The name of the transfer task.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("task_name")
    private String transferTaskName;
    
    /**
     * <p>
     * Name of the agency created in IAM. DIS uses an agency to access your specified resources.For Example, DIS will
     * use this agency to dump data from a DIS stream to an OBS bucket.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("agency_name")
    private String agencyName;
    
    /**
     * <p>
     * Name of the CloudTable cluster to which data will be dumped. If data will be dumped to OpenTSDB, the CloudTable
     * cluster must enable OpenTSDB.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("cloudtable_cluster_name")
    private String cloudtableClusterName;
    
    /**
     * <p>
     * ID of the CloudTable cluster to which data will be dumped. If data will be dumped to OpenTSDB, the CloudTable
     * cluster must enable OpenTSDB.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("cloudtable_cluster_id")
    private String cloudtableClusterId;
    
    /**
     * <p>
     * HBase table name of the CloudTable cluster to which data will be dumped. The parameter is mandatory when data is
     * dumped to CloudTable HBase.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("cloudtable_table_name")
    private String cloudtableTableName;
    
    /**
     * <p>
     * Delimiter of the CloudTable HBase data' rowkey. Default value: Spot "."
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("cloudtable_row_key_delimiter")
    private String cloudtableRowkeyDelimiter;
    
    /**
     * <p>
     * Schema configuration of the CloudTable HBase data. After this parameter is set, the JSON data in the stream can
     * be converted to another format and then be imported to CloudTable HBase. You can set either this parameter or
     * opentsdb_schema, but this parameter is mandatory when data will be dumped to HBase.
     * </p>
     */
    @JsonProperty("cloudtable_schema")
    private CloudtableSchema cloudtableSchema;
    
    /**
     * <p>
     * Schema configuration of the CloudTable OpenTSDB data. After this parameter is set, the JSON data in the stream
     * can be converted to another format and then be imported to the CloudTable OpenTSDB. You can set either this
     * parameter or opentsdb_schema, but this parameter is mandatory when data will be dumped to OpenTSDB.
     * </p>
     */
    @JsonProperty("opentsdb_schema")
    private List<OpenTSDBSchema> opentsdbSchema;
    
    /**
     * <p>
     * Name of the OBS bucket used to back up data that failed to be dumped to CloudTable.
     * </p>
     */
    @JsonProperty("obs_backup_bucket_path")
    private String obsBackupBucketPath;
    
    /**
     * <p>
     * Name of the OBS bucket used to back up data that failed to be dumped to CloudTable. Self-definable directory
     * created in the OBS bucket and used to back up data that failed to be dumped to CloudTable. Directory levels are
     * separated by slash (/) and cannot start with a slash. The entered directory name cannot exceed 50 characters. By
     * default, this parameter is left unspecified.
     * </p>
     */
    @JsonProperty("backup_file_prefix")
    private String backupfilePrefix;
    
    /**
     * <p>
     * 设置从DIS拉取数据时的初始偏移量: 默认LATEST - 从最新的记录开始读取; TRIM_HORIZON - 从最早的记录开始读取
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("consumer_strategy")
    private String consumerStrategy;
    
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
    
    public String getTransferTaskName()
    {
        return transferTaskName;
    }
    
    public void setTransferTaskName(String transferTaskName)
    {
        this.transferTaskName = transferTaskName;
    }
    
    public String getConsumerStrategy()
    {
        return consumerStrategy;
    }
    
    public void setConsumerStrategy(String consumerStrategy)
    {
        this.consumerStrategy = consumerStrategy;
    }
    
    public String getAgencyName()
    {
        return agencyName;
    }
    
    public void setAgencyName(String agencyName)
    {
        this.agencyName = agencyName;
    }
}
