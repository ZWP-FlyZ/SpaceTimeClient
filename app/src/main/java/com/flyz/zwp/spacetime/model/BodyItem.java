package com.flyz.zwp.spacetime.model;

/**
 * Created by zwp12 on 2017/2/15.
 */

public class BodyItem {
    Integer bFlag;
    String body;

    public BodyItem(Integer bFlag, String body) {
        this.bFlag = bFlag;
        this.body = body;
    }

    public Integer getbFlag() {
        return bFlag;
    }

    public String getBody() {
        return body;
    }
}
