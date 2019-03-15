package com.flyz.zwp.spacetime.componet;

import com.flyz.zwp.spacetime.model.BodyItem;
import com.flyz.zwp.spacetime.model.MemFragmentBody;
import com.flyz.zwp.spacetime.model.MemFragmentHead;
import com.flyz.zwp.spacetime.model.MyCellLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zwp12 on 2017/4/14.
 */

public class FragmentTools {

    public class BodyType {
        public  final static int TEXT = 0;
        public final static int PICTURE = 1;
        public final static int AUDIO  = 2;
        public final static int VIDEO  = 3;
    }

    private final static Gson gson = new Gson();
    public static MemFragmentBody getMemFgtBody(String uId, MyTime time, MyCellLocation loc,
                                                String fTitle, List<BodyItem> items){

        if(uId==null||time==null||loc==null||fTitle==null||items==null)return null;

        MemFragmentBody fragmentBody = new MemFragmentBody();
        fragmentBody.setFId(IDCreater.getFID());
        fragmentBody.setUId(uId);
        fragmentBody.setCTime(TimeTools.getNow());
        fragmentBody.setTFLag(time.getTimeFlag());
        fragmentBody.setSTime(time.getTimeDetail());
        fragmentBody.setSlTime(time.getLogcTime());
        fragmentBody.setFtDetail(time.toDescribe());

        fragmentBody.setFLatitude(loc.getmOriginLat());
        fragmentBody.setFLongitude(loc.getmOriginLng());
        fragmentBody.setFpDetail(loc.getmLocDescribe());
        fragmentBody.setCoFlag(loc.getCoFLag());

        fragmentBody.setCellX(loc.getmCellX());
        fragmentBody.setCellY(loc.getmCellY());

        fragmentBody.setFTitle(fTitle);
        fragmentBody.setBodyItemList(items);

        fragmentBody.setBodyItemsJSON(gson.toJson(items));

            return fragmentBody;
    }

    public static MemFragmentHead getMemFgtHead(MemFragmentBody body,int fFlag){
        if(body==null) return null;
        MemFragmentHead head = new MemFragmentHead();

        head.setFId(body.getFId());
        head.setUId(body.getUId());
        head.setFTitle(body.getFTitle());
        head.setCTime(body.getCTime());
        head.setFtDetail(body.getFtDetail());
        head.setSlTime(body.getSlTime());
        head.setFpDetail(body.getFpDetail());
        head.setFFlag(fFlag);
        return head;
    }

    public static int getfFlag(boolean isVerify,boolean isSyn,boolean isShow){
        int tmp =0;
        if(isSyn) tmp = tmp| 1<<0;
        if(isShow) tmp = tmp| 1<<1;
        if(isVerify) tmp = tmp| 1<<3;
        return tmp;
    }

    public static boolean isSyn(MemFragmentHead head){
        if(head==null) return false;
        return ((head.getFFlag()>>0)&0x1)==1;
    }
    public static boolean isShow(MemFragmentHead head){
        if(head==null) return false;
        return ((head.getFFlag()>>1)&0x1)==1;
    }
    public static boolean isShare(MemFragmentHead head){
        if(head==null) return false;
        return ((head.getFFlag()>>2)&0x1)==1;
    }
    public static boolean isVerif(MemFragmentHead head){
        if(head==null) return false;
        return ((head.getFFlag()>>3)&0x1)==1;
    }

    public static void setSyn(MemFragmentHead head,boolean isSyn){
        if(head==null) return ;
        int tmp = 0;
        tmp = head.getFFlag();
        if(isSyn)
            tmp = tmp | 0x1;
        else
            tmp = tmp & (~0x1);
        head.setFFlag(tmp);
    }

    public static void setShow(MemFragmentHead head,boolean isShow){
        if(head==null) return ;
        int tmp = 0;
        tmp = head.getFFlag();
        if(isShow)
            tmp = tmp | 0x2;
        else
            tmp = tmp & (~0x2);
        head.setFFlag(tmp);
    }


    public static void setShare(MemFragmentHead head,boolean isShare){
        if(head==null) return ;
        int tmp = 0;
        tmp = head.getFFlag();
        if(isShare)
            tmp = tmp | 0x4;
        else
            tmp = tmp & (~0x4);
        head.setFFlag(tmp);
    }

    public static void setVerif(MemFragmentHead head,boolean isVerif){
        if(head==null) return ;
        int tmp = 0;
        tmp = head.getFFlag();
        if(isVerif)
            tmp = tmp | 0x8;
        else
            tmp = tmp & (~0x8);
        head.setFFlag(tmp);
    }





    public static void synList2Json(MemFragmentBody body){
        if(body==null||body.getBodyItemList()==null) return ;
        body.setBodyItemsJSON(gson.toJson(body.getBodyItemList()));
    }
    public static void synJson2List(MemFragmentBody body){
        if(body==null||body.getBodyItemsJSON()==null) return ;
        List<BodyItem>  items =
                gson.fromJson(body.getBodyItemsJSON(),
                        new TypeToken<List<BodyItem>>(){}.getType());
        body.setBodyItemList(items);
    }
}
