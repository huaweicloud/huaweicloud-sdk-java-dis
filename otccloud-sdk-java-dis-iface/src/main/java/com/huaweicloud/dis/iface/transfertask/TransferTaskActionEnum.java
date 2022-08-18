package com.otccloud.dis.iface.transfertask;

public enum TransferTaskActionEnum {
    /**
     * 启动转储任务
     */
    START("start"),

    /**
     * 停止转储任务
     */
    STOP("stop");

    private String value;

    // 构造方法
    private TransferTaskActionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TransferTaskActionEnum getEnum(String value) {
        for (TransferTaskActionEnum tte : TransferTaskActionEnum.values()) {
            if (tte.getValue().equals(value)) {
                return tte;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
