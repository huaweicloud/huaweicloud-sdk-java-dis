package com.otccloud.dis.iface.stream.request.dws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Options
{
    // 指定本次数据导入允许出现的数据格式错误个数，取值范围：整型值、 unlimited（无限制）
    @JsonProperty("reject_limit")
    private String rejectLimit;
    
    // 数据入库时，数据源文件中某行的最后一个字段缺失时，请选择是直接将字段设为Null，还是在错误表中报错提示。取值范围：true/on，false/off（缺省值）
    @JsonProperty("fill_missing_fields")
    private String fillMissingFields;
    
    // 数据源文件中的字段比外表定义列数多时，是否忽略多出的列。该参数只在数据导入过程中使用。取值范围：true/on，false/off（缺省值）
    @JsonProperty("ignore_extra_data")
    private String ignoreExtraData;
    
    // 导入非法字符容错参数。此语法仅对READ ONLY的外表有效。取值范围：true/on，false/off（缺省值）
    @JsonProperty("compatible_illegal_chars")
    private String compatibleIllegalChars;
    
    // 用于记录数据格式错误信息的错误表表名。并行导入结束后查询此错误信息表，能够获取详细的错误信息。
    @JsonProperty("error_table_name")
    private String errorTableName;
    
    public String getRejectLimit()
    {
        return rejectLimit;
    }
    
    public void setRejectLimit(String rejectLimit)
    {
        this.rejectLimit = rejectLimit;
    }
    
    public String getFillMissingFields()
    {
        return fillMissingFields;
    }
    
    public void setFillMissingFields(String fillMissingFields)
    {
        this.fillMissingFields = fillMissingFields;
    }
    
    public String getIgnoreExtraData()
    {
        return ignoreExtraData;
    }
    
    public void setIgnoreExtraData(String ignoreExtraData)
    {
        this.ignoreExtraData = ignoreExtraData;
    }
    
    public String getCompatibleIllegalChars()
    {
        return compatibleIllegalChars;
    }
    
    public void setCompatibleIllegalChars(String compatibleIllegalChars)
    {
        this.compatibleIllegalChars = compatibleIllegalChars;
    }
    
    public String getErrorTableName()
    {
        return errorTableName;
    }
    
    public void setErrorTableName(String errorTableName)
    {
        this.errorTableName = errorTableName;
    }
}
