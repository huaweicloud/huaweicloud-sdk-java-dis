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
package com.cloud.dis.iface.transfertask.response.destination;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.cloud.dis.iface.stream.request.ForceStringDeserializer;
import com.cloud.dis.iface.stream.request.dws.Options;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DwsDestinationDescription extends OBSDestinationDescription
{
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
     *  <p>
     *  Specific columns of the schema  for table in DWS to store in the DIS stream.
     *  </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("dws_table_columns")
    private String dwsTableColumns;

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
    
    @JsonProperty("options")
    private Options options;
    
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

    public String getDwsTableColumns()
    {
        return dwsTableColumns;
    }

    public void setDwsTableColumns(String dwsTableColumns)
    {
        this.dwsTableColumns = dwsTableColumns;
    }

    public String getDwsDelimiter()
    {
        return dwsDelimiter;
    }
    
    public void setDwsDelimiter(String dwsDelimiter)
    {
        this.dwsDelimiter = dwsDelimiter;
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
    
    public Options getOptions()
    {
        return options;
    }
    
    public void setOptions(Options options)
    {
        this.options = options;
    }
}
