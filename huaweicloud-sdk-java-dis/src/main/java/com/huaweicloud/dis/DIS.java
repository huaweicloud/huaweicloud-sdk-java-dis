package com.huaweicloud.dis;

import com.huaweicloud.dis.iface.data.IDataService;
import com.huaweicloud.dis.iface.data.request.PutRecordRequest;
import com.huaweicloud.dis.iface.data.response.PutRecordResult;
import com.huaweicloud.dis.iface.stream.IStreamService;

/**
 * SDK定义的API，可以理解为对RESTAPI的高层次封装
 * 
 * 1、注册用户ak和sk
 * 2、上传数据和下载数据功能
 * 
 * 
 */
public interface DIS extends IDataService,IStreamService
{
    
    /**
     * 上传单条记录到通道
     * */
    PutRecordResult putRecord(PutRecordRequest putRecordParam);
    
    
}
