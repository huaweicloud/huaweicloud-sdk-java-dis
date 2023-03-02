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

package com.cloud.dis.util.cache;

/**
 * 缓存管理接口
 *
 */
public interface ICacheManager<T>
{
    
    /**
     * 加入缓存
     * 
     * @param t 缓存内容
     */
    public void putToCache(T t);
    
    /**
     * 是否还有足够的缓存空间
     * 
     * @param data 待缓存的数据
     * @return <code>true</code> 空间足够
     *          <code>false</code> 空间不足
     */
    public boolean hasEnoughSpace(String data);
    
    /**
     * 归档缓存临时文件
     */
    public void archive();
}
