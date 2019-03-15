package com.flyz.zwp.spacetime.messages;

import java.util.Objects;

/**
 * Created by zwp12 on 2017/3/15.
 */

public class ComMessage {
    public final static int ASK_DLD_HEADS = 1;
    public final static int ASK_DLD_CHAINS = 2;
    public final static int RE_DLD_HEADS_OK = 3;
    public final static int RE_DLD_HEADS_ERR = 4;
    public final static int RE_DLD_CHAINS_OK = 5;
    public final static int RE_DLD_CHAINS_ERR = 6;
    public int askCode ;
    public Object pram;

    public ComMessage(){}

    public ComMessage(int code,Object pram){
        this.askCode = code;
        this.pram = pram;
    }

}
