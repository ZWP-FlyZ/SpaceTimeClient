package com.flyz.zwp.spacetime.model;

/**
 * Created by zwp12 on 2017/3/12.
 */

public class MySystemCode {
    public final static int UPDATE_USER_INFO_OK = 10;
    public final static int UPDATE_USER_INFO_ERR = 11;
    public final static int UPDATE_SHARE_BLOCKS_OK  =  12;
    public final static int UPDATE_SHARE_BLOCKS_ERR  =  13;

    public final static int UPLOAD_MEM_CHAIN_OK = 14;
    public final static int UPLOAD_MEM_CHAIN_ERR = 15;
    public final static int CANCEL_UPLOAD_MEM_CHAIN_OK = 16;
    public final static int CANCEL_UPLOAD_MEM_CHAIN_ERR = 17;

    public final static int UPLOAD_MEM_BODY_OK = 18;
    public final static int UPLOAD_MEM_BODY_ERR = 19;
    public final static int CANCEL_UPLOAD_MEM_BODY_OK = 20;
    public final static int CANCEL_UPLOAD_MEM_BODY_ERR = 21;

    public final static int UPLOAD_SHARE_BLOCK_OK = 22;
    public final static int UPLOAD_SHARE_BLOCK_ERR = 23;
    public final static int CANCEL_UPLOAD_SHARE_BLOCK_OK = 24;
    public final static int CANCEL_UPLOAD_SHARE_BLOCK_ERR = 25;

    public final static int CRT_SPACE_CELL_OK = 26;
    public final static int CRT_SPACE_CELL_ERR = 27;

    public int errorCode;
    public MySystemCode(){}
    public MySystemCode(int error){
        this.errorCode = error;
    }
}
