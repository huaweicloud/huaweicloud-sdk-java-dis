package com.cloud.dis.util.compress;

import com.github.luben.zstd.Zstd;

public class ZstdUtil
{
    /**
     * @param srcByte 原始数据
     * @return 压缩后的数据
     */
    public static byte[] compressByte(byte[] srcByte)
    {
        return Zstd.compress(srcByte);
    }
}
