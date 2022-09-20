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

package com.huaweicloud.dis.util.compress;

/**
 * 上传数据到 DIS 时，选择的压缩算法。
 */
public enum CompressionType {
    /**Ωƒ
     * SNAPPY 算法 - 其目标不是最大限度压缩或者兼容其他压缩格式，而是旨在提供高速压缩速度和合理的压缩率。
     */
    SNAPPY("snappy"),

    /**
     * ZSTD 算法 - 一种新的无损压缩算法，旨在提供快速压缩，并实现高压缩比。
     */
    ZSTD("zstd");

    private final String compressionType;

    private CompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return compressionType;
    }

}
