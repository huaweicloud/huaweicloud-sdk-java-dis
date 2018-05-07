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

package com.huaweicloud.dis.iface.data.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum DataTarget {
    Dis_20170120_PutRecord,
    Dis_20170120_PutRecords,
    Dis_20170120_GetRecords,
    Dis_20170120_GetRecordsWithCheckpointing,
    Dis_20170120_GetShardIterator,
    Dis_20170120_CommitCheckpoint,
    Dis_20170120_GetCheckpoint,
    Dis_20170120_RegisterStreamSchema,
    Dis_20170120_GetStreamSchema,
    Dis_20170120_AggregateRecords,
    Dis_20170120_FilterRecords,
    Dis_20170120_ConvertRecords,
    Dis_20170120_JoinRecords,
    Dis_20170120_GetStreamAnalyticsPerformance,
    Dis_20170120_GetStreamThreadsInfo,
    Dis_20170120_CloseThreadsForOneStreamTask,
    Dis_20170120_CloseAllStreamThreads,
    Dis_20170120_RecoverAllStreamThreads,
    Dis_20170120_GetPerformanceLog,
    Dis_20170120_ConfigureRule,
    Dis_20170120_GetStatistics,
    Dis_20170120_GetRecordsFromConsumerGroup
}
