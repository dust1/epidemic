package com.dust.epidemic.net.common;

import com.dust.epidemic.net.rpc.GlobalTypes.StripingPolicy;
import com.dust.epidemic.net.rpc.GlobalTypes.Replica;

public abstract class StripingPolicyImpl {

    protected final StripingPolicy policy;

    protected final int relOsdPosition;

    StripingPolicyImpl(Replica replica, int relOsdPosition) {
        this.policy = replica.getStripingPolicy();
        this.relOsdPosition = relOsdPosition;

        policy.getStripeSize();
    }

    /**
     * 根据objectNo获取这个对象编号的大小
     * @param objectNo 对象编号
     * @return 这个对象的大小
     */
    public abstract int getStripeSizeForObject(long objectNo);

    /**
     * 根据一个文件中对象的下标数获取这个对象的编号
     * @param num 这个对象在文件中的下标数
     * @return 返回这个对象在这个文件中的编号
     */
    public abstract long getGloablObjectNumber(long num);

}
