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

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CloudtableSchema
{
    /**
     * <p>
     * HBase table Schema used by the CloudTable cluster to put JSON data into different HBase table.
     Value range: 1 to 64
     * </p>
     */
    @JsonProperty("table_name")
    private String tableSchema;

    /**
     * <p>
     * HBase rowkey Schema used by the CloudTable cluster to convert JSON data into HBase rowkeys.
     Value range: 1 to 64
     * </p>
     */
    @JsonProperty("row_key")
    private List<SchemaField> rowKeySchema;

    /**
     * <p>
     * HBase column Schema used by the CloudTable cluster to convert JSON data into HBase columns.
     * </p>
     */
    @JsonProperty("columns")
    private List<SchemaField> columnsSchema;

    public List<SchemaField> getRowKeySchema() {
        return rowKeySchema;
    }

    public void setRowKeySchema(List<SchemaField> rowKeySchema) {
        this.rowKeySchema = rowKeySchema;
    }

    public List<SchemaField> getColumnsSchema() {
        return columnsSchema;
    }

    public void setColumnsSchema(List<SchemaField> columnsSchema) {
        this.columnsSchema = columnsSchema;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }
}
