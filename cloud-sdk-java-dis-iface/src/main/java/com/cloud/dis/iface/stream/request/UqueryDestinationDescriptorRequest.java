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

package com.cloud.dis.iface.stream.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UqueryDestinationDescriptorRequest
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
     * Name of the DLI database to which data in the DIS stream will be dumped.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("dli_database_name")
    private String dliDatabaseName;
    
    /**
     * <p>
     * Name of the DLI table to which data in the DIS stream will be dumped.Currently, only tables in DLI are supported.
     * Before selecting a DLI table, ensure that you have the permission to insert data into the table.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("dli_table_name")
    private String dliTableName;
    
    /**
     * <p>
     * Name of the OBS bucket used to store data from the DIS stream.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("obs_bucket_path")
    private String obsBucketPath;
    
    /**
     * <p>
     * Directory to hold files that will be dumped to OBS. Different directory levels are separated by a forward slash
     * (/) and cannot start with a forward slash (/). A directory name is 1 to 50 characters long. Only letters, digits,
     * and underscores (_) are allowed.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("file_prefix")
    private String filePrefix;
    
    /**
     * <p>
     * User-defined interval at which data from the DIS stream is imported into OBS. If no data was pushed to the DIS
     * stream during the current interval, no dump file will be generated for this cycle. Value range: 60s to 900s
     * Default value: 300s
     * </p>
     */
    @JsonProperty("deliver_time_interval")
    private int deliverTimeInterval;
    
    /**
     * <p>
     * 设置从DIS拉取数据时的初始偏移量: 默认LATEST - 从最新的记录开始读取; TRIM_HORIZON - 从最早的记录开始读取
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("consumer_strategy")
    private String consumerStrategy;
    
    /**
     * <p>
     * for old use. Name of the DLI database to which data in the DIS stream will be dumped.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("uquery_database_name")
    private String uqueryDatabaseName;
    
    /**
     * <p>
     * for old use. Name of the DLI table to which data in the DIS stream will be dumped.Currently, only tables in DLI are supported.
     * Before selecting a DLI table, ensure that you have the permission to insert data into the table.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("uquery_table_name")
    private String uqueryTableName;
    
    public String getDliDatabaseName()
    {
        return dliDatabaseName;
    }
    
    public void setDliDatabaseName(String dliDatabaseName)
    {
        this.dliDatabaseName = dliDatabaseName;
    }
    
    public String getDliTableName()
    {
        return dliTableName;
    }
    
    public void setDliTableName(String dliTableName)
    {
        this.dliTableName = dliTableName;
    }
    
    public String getTransferTaskName()
    {
        return transferTaskName;
    }
    
    public void setTransferTaskName(String transferTaskName)
    {
        this.transferTaskName = transferTaskName;
    }
    
    public String getAgencyName()
    {
        return agencyName;
    }
    
    public void setAgencyName(String agencyName)
    {
        this.agencyName = agencyName;
    }
    
    public String getObsBucketPath()
    {
        return obsBucketPath;
    }
    
    public void setObsBucketPath(String obsBucketPath)
    {
        this.obsBucketPath = obsBucketPath;
    }
    
    public String getFilePrefix()
    {
        return filePrefix;
    }
    
    public void setFilePrefix(String filePrefix)
    {
        this.filePrefix = filePrefix;
    }
    
    public int getDeliverTimeInterval()
    {
        return deliverTimeInterval;
    }
    
    public void setDeliverTimeInterval(int deliverTimeInterval)
    {
        this.deliverTimeInterval = deliverTimeInterval;
    }
    
    public String getConsumerStrategy()
    {
        return consumerStrategy;
    }
    
    public void setConsumerStrategy(String consumerStrategy)
    {
        this.consumerStrategy = consumerStrategy;
    }
    
    public String getUqueryDatabaseName()
    {
        return uqueryDatabaseName;
    }
    
    public void setUqueryDatabaseName(String uqueryDatabaseName)
    {
        this.uqueryDatabaseName = uqueryDatabaseName;
    }
    
    public String getUqueryTableName()
    {
        return uqueryTableName;
    }
    
    public void setUqueryTableName(String uqueryTableName)
    {
        this.uqueryTableName = uqueryTableName;
    }
}
