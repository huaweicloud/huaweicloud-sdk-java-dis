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

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpenTSDBSchema
{
    /**
     * <p>
     * Schema configuration of the OpenTSDB data metric in the CloudTable cluster. After this parameter is set, the JSON
     * data in the stream can be converted to the metric of the OpenTSDB data.
     * </p>
     */
    @JsonProperty("metric")
    private List<SchemaField> metricSchema;
    
    /**
     * <p>
     * Schema configuration of the OpenTSDB data timestamp in the CloudTable cluster. After this parameter is set, the
     * JSON data in the stream can be converted to the timestamp of the OpenTSDB data.
     * </p>
     */
    @JsonProperty("timestamp")
    private SchemaField timestampSchema;
    
    /**
     * <p>
     * Schema configuration of the OpenTSDB data value in the CloudTable cluster. After this parameter is set, the JSON
     * data in the stream can be converted to the value of the OpenTSDB data.
     * </p>
     */
    @JsonProperty("value")
    private SchemaField valueSchema;
    
    /**
     * <p>
     * Schema configuration of the OpenTSDB data tags in the CloudTable cluster. After this parameter is set, the JSON
     * data in the stream can be converted to the tags of the OpenTSDB data.
     * </p>
     */
    @JsonProperty("tags")
    private List<SchemaField> tagsSchema;
    
    public List<SchemaField> getMetricSchema()
    {
        return metricSchema;
    }
    
    public void setMetricsSchema(List<SchemaField> metricsSchema)
    {
        this.metricSchema = metricSchema;
    }
    
    public void setMetricSchema(List<SchemaField> metricSchema)
    {
        this.metricSchema = metricSchema;
    }
    
    public SchemaField getTimestampSchema()
    {
        return timestampSchema;
    }
    
    public void setTimestampSchema(SchemaField timestampSchema)
    {
        this.timestampSchema = timestampSchema;
    }
    
    public SchemaField getValueSchema()
    {
        return valueSchema;
    }
    
    public void setValueSchema(SchemaField valueSchema)
    {
        this.valueSchema = valueSchema;
    }
    
    public List<SchemaField> getTagsSchema()
    {
        return tagsSchema;
    }
    
    public void setTagsSchema(List<SchemaField> tagsSchema)
    {
        this.tagsSchema = tagsSchema;
    }
}
