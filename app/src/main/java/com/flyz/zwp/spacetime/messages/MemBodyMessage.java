package com.flyz.zwp.spacetime.messages;

/**
 * Created by zwp12 on 2017/5/8.
 */

public class MemBodyMessage {
    public final static int MEM_BODY_FROM_LOCMEM = 1;
    public final static int MEM_BODY_FROM_NETMEM = 2;
    public int type;
    public Object para;

}
