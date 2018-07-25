package com.huaweicloud.dis.iface.stream;

/**
 * Created by s00348548 on 2018/4/23.
 */
public enum DestinationFileTypeEnum
{
    TEXT("text", 1), PARQUET("parquet", 2), CARBON("carbon", 3);
    
    private String type; // 创建转储任务的转储文件格式：text parquet carbon等
    
    private int value; // 对应的转储文件格式的值
    
    // 构造方法
    private DestinationFileTypeEnum(String type, int value)
    {
        this.type = type;
        this.value = value;
    }
    
    public String getType()
    {
        return type;
    }
    
    public int getValue()
    {
        return value;
    }
    
    public static DestinationFileTypeEnum getEnum(String type)
    {
        for (DestinationFileTypeEnum de : DestinationFileTypeEnum.values())
        {
            if (de.getType().equals(type))
            {
                return de;
            }
        }
        return null;
    }
    
    public static DestinationFileTypeEnum getEnumByValue(int value)
    {
        for (DestinationFileTypeEnum de : DestinationFileTypeEnum.values())
        {
            if (de.getValue() == value)
            {
                return de;
            }
        }
        return null;
    }
}
