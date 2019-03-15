package com.flyz.zwp.spacetime.componet;

import com.flyz.zwp.spacetime.App;
import com.flyz.zwp.spacetime.model.MemChain;
import com.flyz.zwp.spacetime.model.MemFragmentHead;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zwp12 on 2017/4/13.
 */

public class MemChainTools {

    public  final static String LOC_CHAIN_ID = "LOC_CHAIN_CHID";
    public final static String OFFLINE_USER_ID = "OFFLINE_UID";

    public final static String DEFAULT_CHAIN_ID_HEAD = "DEFAULT_CHID_";
    private final static String  LOC_CHAIN_NAME = "离线记忆链";
    private final static String  LOC_CHAIN_DETAIL = "离线用户使用的本地记忆链。";

    private final static String  DEFAULT_CHAIN_NAME   = "默认记忆链";
    private final static String   DEFAULT_CHAIN_DETAIL= "默认记忆链";
    private final static Gson  gson = new Gson();


    public static MemChain createLocMemChain(){
        MemChain memChain = new MemChain();
        memChain.setChId(LOC_CHAIN_ID);
        memChain.setUId(OFFLINE_USER_ID);
        memChain.setChName(LOC_CHAIN_NAME);
        memChain.setChDetail(LOC_CHAIN_DETAIL);
        memChain.setChFlag(0);
        memChain.setChTime(TimeTools.getNow());
        return memChain;
    }
    public static MemChain
            createMemChain(String uId,String chName, String chDetail,int chFlag){

        if(uId==null||uId.equals("")) return null;
            MemChain memChain = new MemChain();
            memChain.setChId(IDCreater.getCHID());
            memChain.setUId(uId);
            memChain.setChName(chName);
            memChain.setChDetail(chDetail);
            memChain.setChFlag(chFlag);
            memChain.setChTime(TimeTools.getNow());
        return memChain;
    }

    public static MemChain
    createDefaultMemChain(String uId,int chFlag){

        if(uId==null||uId.equals("")) return null;
        MemChain memChain = new MemChain();
        memChain.setChId(DEFAULT_CHAIN_ID_HEAD+uId);
        memChain.setUId(uId);
        memChain.setChName(DEFAULT_CHAIN_NAME);
        memChain.setChDetail(DEFAULT_CHAIN_DETAIL);
        memChain.setChFlag(chFlag);
        memChain.setChTime(TimeTools.getNow());
        return memChain;
    }


    public static int createMemChainFlag(boolean isSyn,boolean isShow){
        int tmp = 0x0;
        if(isSyn) tmp = tmp  | 1<<0;
        if(isShow) tmp = tmp | 1<<1;
        return tmp;
    }

    public static int getFragmentCount(MemChain chain){
        if(chain==null)return -1;
        return chain.getChFlag()>>3;
    }
    public static int addFrgmentCount(MemChain chain){
        if(chain==null) return -1;
        int tmp = chain.getChFlag()>>3;
        int tmp2 = chain.getChFlag()&0x7;
        tmp++;
        tmp = (tmp<<3) + tmp2;
        chain.setChFlag(tmp);
        return tmp>>3;
    }

    public static int cutFrgmentCount(MemChain chain){
        if(chain==null) return -1;
        int tmp = chain.getChFlag()>>3;
        int tmp2 = chain.getChFlag()&0x7;
        tmp--;
        if(tmp<0)tmp =0;
        tmp = (tmp<<3) + tmp2;
        chain.setChFlag(tmp);
        return tmp>>3;
    }
    public static boolean isSyn(MemChain chain){
        if(chain==null)return false;
        return (chain.getChFlag()&0x1)==1;
    }
    public static boolean isShow(MemChain chain){
        if(chain==null)return false;
        return ( (chain.getChFlag()>>1) & 0x1)==1;
    }

    public static boolean isShare(MemChain chain){
        if(chain==null)return false;
        return ( (chain.getChFlag()>>2) & 0x1)==1;
    }

    private static boolean isContainHead(List<MemFragmentHead> heads, MemFragmentHead head){
        for(MemFragmentHead h:heads){
                if(h.getFId().equals(head.getFId()))
                    return true;
        }
        return false;

    }
    public static int addFrgmentToChain(MemChain chain, MemFragmentHead head){
        List<MemFragmentHead> heads ;
        if(chain==null||head==null) return -1;

        String tmp = chain.getChContextJSON();
        if(tmp==null||tmp.equals("")||tmp.equals("[]")){
            heads = new ArrayList<MemFragmentHead>();
        }
        else if(chain.getChContext()==null)
                heads =  gson.fromJson(tmp, new TypeToken<  List<MemFragmentHead>>(){}.getType());
            else
                heads =  chain.getChContext();


        if(isContainHead(heads,head)) return getFragmentCount(chain);//排除重复


        heads.add(head);
        chain.setChContext(heads);
        addFrgmentCount(chain);//更改计数
        chain.setChContextJSON(gson.toJson(heads));
        if(chain.getScTime()==null||chain.getScTime().equals("")
                        ||chain.getCslTime()>=head.getSlTime()){
            chain.setCslTime(head.getSlTime());
            chain.setScTime(head.getFtDetail());
        }

        if(chain.getSeTime()==null||
                chain.getScTime().equals("")||chain.getCelTime()<=head.getSlTime()){
            chain.setCelTime(head.getSlTime());
            chain.setSeTime(head.getFtDetail());
        }


        return getFragmentCount(chain);
    }

    private static void updatesTiemAndeTime(MemChain chain){
        if(chain==null) return ;
        String tmp = chain.getChContextJSON();
        List<MemFragmentHead> heads;
        double min=0.0,max=0.0,mm;
        if(tmp==null|| tmp.equals("")||tmp.equals("[]")){
            chain.setScTime("");
            chain.setSeTime("");
            chain.setCelTime(0.0);
            chain.setCslTime(0.0);
            return;
        }else if(chain.getChContext()==null)
            synJson2List(chain);
        heads = chain.getChContext();
        for(MemFragmentHead h:heads){
            if(min==0.0) min = h.getSlTime();
            if(max==0.0) max = h.getSlTime();
            mm = h.getSlTime();
            if(mm<=min) {
                min = mm;
                chain.setScTime(h.getFtDetail());
            }
            if(mm>=max){
                max = mm;
                chain.setSeTime(h.getFtDetail());
            }
        }
        chain.setCslTime(min);
        chain.setCelTime(max);
    }
    public static int deleteFrgmentFromChain(MemChain chain, List<MemFragmentHead> heads) {
        List<MemFragmentHead> tmps;
        if (chain == null || heads == null) return -1;
        if (chain.getChContext() == null)
            synJson2List(chain);
        tmps = chain.getChContext();

        Iterator<MemFragmentHead> it = tmps.iterator();
        for (MemFragmentHead h1 : heads)
            for(int i=0;i<tmps.size();i++)
                if(tmps.get(i).getFId().equals(h1.getFId())) {
                    tmps.remove(i);
                    cutFrgmentCount(chain);
                    break;
                }

        chain.setChContext(tmps);
        synList2Json(chain);
        updatesTiemAndeTime(chain);
        return getFragmentCount(chain);
    }


    public static void setSyn(MemChain chain,boolean isSyn){
        int tmp =0;
        if(chain==null) return ;
        tmp = chain.getChFlag();
        if(isSyn)
            tmp = tmp|0x1;
        else
            tmp = tmp & (~0x1);
        chain.setChFlag(tmp);
    }

    public static void setShow(MemChain chain,boolean isShow){
        int tmp =0;
        if(chain==null) return ;
        tmp = chain.getChFlag();
        if(isShow)
            tmp = tmp | 0x2;
        else
            tmp = tmp & ( ~0X2 );
        chain.setChFlag(tmp);
    }

    public static void setShare(MemChain chain,boolean isShare){
        int tmp =0;
        if(chain==null) return ;
        tmp = chain.getChFlag();
        if(isShare)
            tmp = tmp | 0x4;
        else
            tmp = tmp & (~0x4);
        chain.setChFlag(tmp);
    }

    public static void synList2Json(MemChain chain){
        if(chain==null||chain.getChContext()==null) return ;
        chain.setChContextJSON(gson.toJson(chain.getChContext()));
    }
    public static void synJson2List(MemChain chain){
        if(chain==null||chain.getChContextJSON()==null) return ;
        List<MemFragmentHead>  heads =
                gson.fromJson(chain.getChContextJSON(),
                        new TypeToken<  List<MemFragmentHead>>(){}.getType());
      chain.setChContext(heads);
    }

    public static boolean isDefaultChainNotLoc(MemChain chain){
        if(chain==null) return false;
        if(chain.getChId()==null) return false;
        if(chain.getUId().equals(App.offUId))return false;
        if(chain.getChId().equals(DEFAULT_CHAIN_ID_HEAD+chain.getUId()))
            return true;
        return false;
    }

}
