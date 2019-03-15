package com.flyz.zwp.spacetime.componet;

import org.androidannotations.annotations.sharedpreferences.DefaultLong;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by zwp12 on 2017/2/22.
 */
@SharedPref
public interface SpaceTimePrefs {

    @DefaultString("")
    String uId();

    @DefaultString("")
    String uNickName();

    @DefaultString("")
    String uPasswordTemp();

    @DefaultLong(0)
    long startTimes();

    @DefaultLong(0)
    long updateTime();

}
