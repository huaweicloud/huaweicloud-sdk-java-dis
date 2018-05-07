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
import com.huaweicloud.dis.iface.stream.request.OBSDestinationDescriptorRequest;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DwsDestinationDescriptorRequest extends OBSDestinationDescriptorRequest
{
    /** Destination type */
    private String type;
    
    @JsonProperty("dws_cluster_name")
    private String dwsClusterName;
    
    /** DWS clusterId */
    @JsonProperty("dws_cluster_id")
    private String dwsClusterId;
    
    @JsonProperty("dws_schema")
    private String dwsSchema;
    
    @JsonProperty("dws_database_name")
    private String dwsDatabaseName;
    
    @JsonProperty("user_name")
    private String userName;
    
    @JsonProperty("user_password")
    private String userPassword;
    
    @JsonProperty("dws_table_name")
    private String dwsTableName;
    
    @JsonProperty("dws_delimiter")
    private String dwsDelimiter;
    
    @JsonProperty("kms_user_key_name")
    private String kmsUserKeyName;
    
    @JsonProperty("kms_user_key_id")
    private String kmsUserKeyId;
    
    @JsonProperty("kms_data_cipher_text")
    private String kmsDataCipherText;
    
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
    
    public String getKmsDataCipherText()
    {
        return kmsDataCipherText;
    }
    
    public void setKmsDataCipherText(String kmsDataCipherText)
    {
        this.kmsDataCipherText = kmsDataCipherText;
    }
    
    public String getDwsDelimiter()
    {
        return dwsDelimiter;
    }
    
    public void setDwsDelimiter(String dwsDelimiter)
    {
        this.dwsDelimiter = dwsDelimiter;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
}
