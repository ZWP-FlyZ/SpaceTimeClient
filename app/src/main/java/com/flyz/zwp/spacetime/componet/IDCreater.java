package com.flyz.zwp.spacetime.componet;

import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;

import java.util.Random;
import java.util.UUID;

/**
 * Created by zwp12 on 2017/4/6.
 */

public class IDCreater {
    private static int random(int min, int max){
        Random r = new Random();
        int s = r.nextInt(max)%(max-min+1) + min;
        return s;
    }
    public static String getUUID(){
        return UUID.randomUUID().toString();
    }
    public static String getUUID_RD(){
        String s = getUUID();
        StringBuffer sb = new StringBuffer();
        int i = random(0,6);
        sb.append(s.substring(i,i+2));
        i = random(9,12);
        sb.append(s.substring(i,i+1));
        i = random(14,17);
        sb.append(s.substring(i,i+1));
        i = random(19,22);
        sb.append(s.substring(i,i+1));
        i = random(24,32);
        sb.append(s.substring(i,i+3));
        return sb.toString();
    }
    public static String getUNIXTime(){
        return System.currentTimeMillis()/100L+"";
    }

    @NonNull
    public static String getUNIXTime(int be){
        return getUNIXTime().substring(be);
    }


    /**
     * 用户ID
     * @return
     */
    public static String getUID(){
        String tmp = "UID_";
        tmp+=getUNIXTime(6);
        tmp+=getUUID_RD();

        return tmp;
    }

    /**
     * 碎片ID
     * @return
     */
    public static String getFID(){
        String tmp = "FID_";
        tmp+=getUNIXTime(6);
        tmp+=getUUID_RD();
        return tmp;
    }

    /**
     * 记忆链ID
     * @return
     */
    public static String getCHID(){
        String tmp = "CHID_";
        tmp+=getUNIXTime(6);
        tmp+=getUUID_RD();
        return tmp;
    }

    /**
     * 区块ID
     * @return
     */
    public static String getCEID(int cellX,int cellY){
        String s = Integer.toHexString(cellX);
        String tmp = "CEID_"+s;
        s = Integer.toHexString(cellY);
        tmp+="_"+s;
        return tmp;
    }

    public static boolean isFID(String Id){
        if(Id==null||Id.equals("")) return false;
        if(Id.substring(0,3).equals("FID")) return true;
        else return false;
    }


}
