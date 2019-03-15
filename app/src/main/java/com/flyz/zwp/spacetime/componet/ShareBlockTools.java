package com.flyz.zwp.spacetime.componet;

import com.flyz.zwp.spacetime.model.MemChain;
import com.flyz.zwp.spacetime.model.MemFragmentHead;
import com.flyz.zwp.spacetime.model.ShareBlock;

/**
 * Created by zwp12 on 2017/5/10.
 */

public class ShareBlockTools {

    public static ShareBlock getShareBlock(MemFragmentHead head,String uNickName,String ceId){
        ShareBlock shareBlock = new ShareBlock();
        shareBlock.setSId(head.getFId());
        shareBlock.setCeId(ceId);
        shareBlock.setSFlag(0);
        shareBlock.setSTime(TimeTools.getNow());
        shareBlock.setPokerCount(0);
        shareBlock.setUId(head.getUId());
        shareBlock.setUNickName(uNickName);

        shareBlock.setSData1(head.getFTitle());
        shareBlock.setSData2(head.getFtDetail());
        shareBlock.setSData3(head.getFpDetail());
        return shareBlock;
    }

    public static ShareBlock getShareBlock(MemChain chain,String uNickName,String ceId){
        ShareBlock shareBlock = new ShareBlock();
        shareBlock.setSId(chain.getChId());
        shareBlock.setCeId(ceId);
        shareBlock.setSFlag(1);
        shareBlock.setSTime(TimeTools.getNow());
        shareBlock.setPokerCount(0);
        shareBlock.setUId(chain.getUId());
        shareBlock.setUNickName(uNickName);

        shareBlock.setSData1(chain.getChName());
        shareBlock.setSData2(chain.getChDetail());
        shareBlock.setSData3("碎片数："+MemChainTools.getFragmentCount(chain));
        return shareBlock;
    }


}
