package com.huaweicloud.dis.core.util;

import com.huaweicloud.dis.core.builder.AkSkHolder;

public class AkSkUtils {
    private static volatile AkSkUtils instance;

    private AkSkHolder akSkHolder;


    public AkSkHolder getAkSkHolder() {
        return akSkHolder;
    }

    public void setAkSkHolder(AkSkHolder akSkHolder) {
        this.akSkHolder = akSkHolder;
    }

    public static AkSkUtils getInstance(){
        if(instance==null){
            synchronized (AkSkUtils.class){
                if(instance==null){
                    instance=new AkSkUtils();
                }
            }
        }
        return instance;
    }
}
