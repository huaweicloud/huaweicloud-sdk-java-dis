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

package com.huaweicloud.dis.iface.stream.request.dws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.huaweicloud.dis.iface.stream.request.ForceStringDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DwsDestinationDescriptorRequest
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
     * Name of the DWS cluster used to store data in the DIS stream.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("dws_cluster_name")
    private String dwsClusterName;
    
    /**
     * <p>
     * ID of the DWS cluster used to store data in the DIS stream.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("dws_cluster_id")
    private String dwsClusterId;
    
    /**
     * <p>
     * Schema of the DWS database used to store data in the DIS stream.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("dws_schema")
    private String dwsSchema;
    
    /**
     * <p>
     * Name of the DWS database used to store data in the DIS stream.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("dws_database_name")
    private String dwsDatabaseName;
    
    /**
     * <p>
     * Username of the DWS database used to store data in the DIS stream.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("user_name")
    private String userName;
    
    /**
     * <p>
     * Password of the DWS database used to store data in the DIS stream.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("user_password")
    private String userPassword;
    
    /**
     * <p>
     * Name of the table in the DWS database used to store data in the DIS stream.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("dws_table_name")
    private String dwsTableName;
    
    /**
     * <p>
     * An indication that implies data is in different rows.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("dws_delimiter")
    private String dwsDelimiter;
    
    /**
     * <p>
     * Key created in Key Management Service (KMS) and used to encrypt the password of the DWS database.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("kms_user_key_name")
    private String kmsUserKeyName;
    
    /**
     * <p>
     * ID of the key created in Key Management Service (KMS) and used to encrypt the password of the DWS database.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("kms_user_key_id")
    private String kmsUserKeyId;
    
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
    
    public String getDwsClusterName()
    {
        return dwsClusterName;
    }
    
    public void setDwsClusterName(String dwsClusterName)
    {
        this.dwsClusterName = dwsClusterName;
    }
    
    public String getDwsClusterId()
    {
        return dwsClusterId;
    }
    
    public void setDwsClusterId(String dwsClusterId)
    {
        this.dwsClusterId = dwsClusterId;
    }
    
    public String getDwsSchema()
    {
        return dwsSchema;
    }
    
    public void setDwsSchema(String dwsSchema)
    {
        this.dwsSchema = dwsSchema;
    }
    
    public String getDwsDatabaseName()
    {
        return dwsDatabaseName;
    }
    
    public void setDwsDatabaseName(String dwsDatabaseName)
    {
        this.dwsDatabaseName = dwsDatabaseName;
    }
    
    public String getUserName()
    {
        return userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public String getUserPassword()
    {
        return userPassword;
    }
    
    public void setUserPassword(String userPassword)
    {
        this.userPassword = userPassword;
    }
    
    public String getDwsTableName()
    {
        return dwsTableName;
    }
    
    public void setDwsTableName(String dwsTableName)
    {
        this.dwsTableName = dwsTableName;
    }
    
    public String getKmsUserKeyName()
    {
        return kmsUserKeyName;
    }
    
    public void setKmsUserKeyName(String kmsUserKeyName)
    {
        this.kmsUserKeyName = kmsUserKeyName;
    }
    
    public String getKmsUserKeyId()
    {
        return kmsUserKeyId;
    }
    
    public void setKmsUserKeyId(String kmsUserKeyId)
    {
        this.kmsUserKeyId = kmsUserKeyId;
    }
    
    public String getDwsDelimiter()
    {
        return dwsDelimiter;
    }
    
    public void setDwsDelimiter(String dwsDelimiter)
    {
        this.dwsDelimiter = dwsDelimiter;
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
}
