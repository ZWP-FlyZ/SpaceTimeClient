package com.flyz.zwp.spacetime.messages;

import com.flyz.zwp.spacetime.model.UserInfo;

/**
 * Created by zwp12 on 2017/3/10.
 */

public class LogMessage {
    public final static int LOGON_USER_NAME_OK =  100;
    public final static int LOGON_USER_NAME_ERR =  101;
    public final static int LOGON_REG_OK = 102 ;
    public final static int LOGON_REG_ERR = 103 ;

    public final static int LOGIN_OK = 104 ;
    public final static int LOGIN_PASS_ERR = 105 ;
    public final static int LOGIN_UN_ERR = 106 ;

    public  int errorCode;
    public String uId ;
    public  String uNickName;
    public String uPassword;
    public String uPasswordE;
    public String uEmail;
    public Integer cellX;
    public Integer cellY;
    public String crDesc;
    public Integer campFlag;
    public String crTime;

    public UserInfo userInfo;
}
