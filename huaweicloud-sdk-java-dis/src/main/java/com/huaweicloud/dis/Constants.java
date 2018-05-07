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

package com.huaweicloud.dis;


/**
 * 
 */
public interface Constants
{
    
    /**Http URI*/
    final String PATH = "/v1/data";
    
    final String PUT_RECORDS_PATH = "/v2/records";
    
    final String GET_RECORDS_PATH = "/v2/records";
    
    final String GET_CURSORS_PATH = "/v2/cursors";
    
    final String VERSION = "v2";
    
    final String RECORDS_RESOURCES = "records";
    
    final String CURSORS_RESOURCES = "cursors";

    final String STREAMS_RESOURCES = "streams";
    
    final String CHECKPOINT_RESOURCES = "checkpoints";

    final String DATA_RESOURCES = "data";

    final String SPLITSHARD_RESOURCES = "split";

    final String MERGESHARDS_RESOURCES = "merge";

    /********************************Data Signature********************************/
    
    static final String SERVICENAME = "dis";
    
}
