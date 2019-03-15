package com.flyz.zwp.spacetime;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.platform.comapi.map.A;
import com.baidu.platform.comapi.map.E;
import com.flyz.zwp.spacetime.componet.LocMemory;
import com.flyz.zwp.spacetime.componet.MemChainTools;
import com.flyz.zwp.spacetime.componet.MyHttpClient;
import com.flyz.zwp.spacetime.componet.MyLocationClient;
import com.flyz.zwp.spacetime.model.DaoSession;
import com.flyz.zwp.spacetime.model.MyCellLocation;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by zwp12 on 2017/2/13.
 */
@EApplication
public class App extends Application {

    @Bean
    MyHttpClient myHttpClient;
    @Bean
    LocMemory locMemory;
    @Bean
    MyLocationClient myLocationClient;

    public final static String offUId = "OFFLINE_UID";
    public final static String offUNickName = "本地用户";
    public final static String testUID = "UID_TEST_ONLY";
    public final static String testUNickName = "测试用户名";

    String nowUId = offUId;
    String nowUNickName = offUNickName;
    int logInC = 0;
    MyCellLocation nowLoc;


    @Override
    public void onCreate() {
        super.onCreate();

        EventBus.getDefault().register(locMemory);
        EventBus.getDefault().register(myHttpClient);
        SDKInitializer.initialize(getApplicationContext());

        //添加离线记忆链
        locMemory.checkLocChainOrAdd();

        //test chain
        locMemory.checkDefaultChainOrAdd(nowUId);
    }


    public DaoSession getDaoSession() {
        return locMemory.getDaoSession();
    }
    public MyLocationClient getLoctionClient(){
        return this.myLocationClient;
    }
    public MyHttpClient getMyHttpClient() {
        return myHttpClient;
    }
    public LocMemory getLocMemory() {
        return locMemory;
    }
    public String getNowUId() {
        return nowUId;
    }
    public boolean isOnLine(){
        return !nowUId.equals(offUId);
    }
    public void setNowUId(String nowUId) {
        this.nowUId = nowUId;
    }

    public int getLogInC() {
        return logInC;
    }

    public void setLogInC(int logInC) {
        this.logInC = logInC;
    }

    public String getDefaultChainId(){
        return MemChainTools.DEFAULT_CHAIN_ID_HEAD+nowUId;
    }

    public MyCellLocation getNowLoc() {
        return nowLoc;
    }

    public void setNowLoc(MyCellLocation nowLoc) {
        this.nowLoc = nowLoc;
    }

    public String getNowUNickName() {
        return nowUNickName;
    }

    public void setNowUNickName(String nowUNickName) {
        this.nowUNickName = nowUNickName;
    }

    public void logOut(){
        nowUId = offUId;
        nowUNickName = offUNickName;
        logInC = 0;
        myHttpClient.logOut();
    }
}
