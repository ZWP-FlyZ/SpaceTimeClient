package com.flyz.zwp.spacetime.messages;

/**
 * Created by zwp12 on 2017/3/17.
 */

public class UserMessage {
    public final static int MEM_CHAIN_LIST_FROM_LOCMEM = 1;
    public final static int MEM_CHAIN_LIST_FROM_NETMEM = 2;
    public final static int MEM_BODY_LIST_FROM_LOCMEM = 3;
    public final static int MEM_BODY_LIST_FROM_NETMEM = 4;

    public int type;
    public Object para;

}
