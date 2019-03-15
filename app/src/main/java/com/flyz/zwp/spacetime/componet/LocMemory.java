package com.flyz.zwp.spacetime.componet;

import android.content.Context;

import com.flyz.zwp.spacetime.UserDao;
import com.flyz.zwp.spacetime.messages.ComMessage;
import com.flyz.zwp.spacetime.messages.UserMessage;
import com.flyz.zwp.spacetime.model.DaoMaster;
import com.flyz.zwp.spacetime.model.DaoSession;
import com.flyz.zwp.spacetime.model.MemChain;
import com.flyz.zwp.spacetime.model.MemChainDao;
import com.flyz.zwp.spacetime.model.MemFragmentBody;
import com.flyz.zwp.spacetime.model.MemFragmentBodyDao;
import com.flyz.zwp.spacetime.model.MemFragmentHead;
import com.flyz.zwp.spacetime.model.MemFragmentHeadDao;
import com.flyz.zwp.spacetime.model.MyCellLocation;
import com.flyz.zwp.spacetime.model.MySystemCode;
import com.flyz.zwp.spacetime.model.ShareBlock;
import com.flyz.zwp.spacetime.model.UserInfo;
import com.flyz.zwp.spacetime.model.UserInfoDao;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.DimensionPixelOffsetRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.QueryBuilder;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by zwp12 on 2017/2/22.
 */
@EBean(scope = EBean.Scope.Singleton)
public class LocMemory {

    public final static String DATABASE_NAME ="db_spacetime";
    public final static String testUID = "UID_TEST_ONLY";
    public final static String offUId = "OFFLINE_UID";
    @RootContext
    Context context;

    @Pref
    public SpaceTimePrefs_ spaceTimePrefs;

    DaoSession daoSession;

    MyLRU<String,MemChain>        mLRUmemChain = new MyLRU<String,MemChain>(100);
    MyLRU<String,MemFragmentHead> mLRUmemFgtHead = new MyLRU<String,MemFragmentHead>(100);

    List<String> tmpHeads = new ArrayList<String>();
    List<String> tmpChains = new ArrayList<String>();


    boolean[] bs = new boolean[2];

    @AfterInject
     void aftInject()
    {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,DATABASE_NAME);
        daoSession = new DaoMaster(helper.getWritableDb()).newSession();
        bs[0] = bs[1] = false;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    void msgForHttp(ComMessage msg) {

        if(msg.askCode==ComMessage.RE_DLD_HEADS_OK||msg.askCode==ComMessage.RE_DLD_CHAINS_OK){
        synchronized (bs){
            if(msg.askCode==ComMessage.RE_DLD_HEADS_OK) {
                insertMemHeadsIntoDB((List<MemFragmentHead>)msg.pram);
                insertMemHeadsIntoLRU((List<MemFragmentHead>)msg.pram);
                mLRUmemFgtHead.printAll();
                bs[0] = true;
            }else if(msg.askCode==ComMessage.RE_DLD_CHAINS_OK){
                insertMemChainsIntoDB((List<MemChain>)msg.pram);
                insertMemChainsIntoLRU((List<MemChain>)msg.pram);
                bs[1] = true;
                mLRUmemChain.printAll();
            }
            if(bs[0]==true&&bs[1]==true)
                EventBus.getDefault().post(new MySystemCode(MySystemCode.UPDATE_SHARE_BLOCKS_OK));
        }
    }
        // thread unsafe


    }



    public void checkLocChainOrAdd(){
//        List<MemChain> chains = daoSession.getMemChainDao().queryBuilder().
//                where(MemChainDao.Properties.ChId.eq(MemChainTools.LOC_CHAIN_ID)).list();
////        Logger.d(chains.size());
////        Logger.json(new Gson().toJson(chains));
//        if(chains==null||chains.size()==0){
//            daoSession.getMemChainDao().insertOrReplace(MemChainTools.createLocMemChain());
//        }
        checkDefaultChainOrAdd(offUId);
    }


    public MemChain checkDefaultChainOrAdd(String uId){
        List<MemChain> chains = daoSession.getMemChainDao().queryBuilder().
                where(MemChainDao.Properties.ChId.eq(MemChainTools.DEFAULT_CHAIN_ID_HEAD+uId)).list();
//        Logger.d(chains.size());
//        Logger.json(new Gson().toJson(chains));
        if(chains==null||chains.size()==0){
            MemChain chain = MemChainTools.createDefaultMemChain(uId,3);
            daoSession.getMemChainDao().insertOrReplace(chain);
            return chain;
        }
        return chains.get(0);
    }

    @Background
    public void getUserInfoById(String uId) {
        UserInfo info;
        List<UserInfo> infoes = daoSession.getUserInfoDao().queryBuilder().
                where(UserInfoDao.Properties.UId.eq(uId)).list();
        if(infoes.size()>0)
            info = infoes.get(0);
        else
            info = new UserInfo();
        EventBus.getDefault().post(info);
    }

    public void getMemFgtBody(String fId){
       if(fId==null) return ;
        MemFragmentBody body;
        List<MemFragmentBody> bodies =
                daoSession.getMemFragmentBodyDao().queryBuilder().
                        where(MemFragmentBodyDao.Properties.FId.eq(fId)).list();
        if(bodies.size()>0)
            body = bodies.get(0);
        else
            body = new MemFragmentBody();
        EventBus.getDefault().post(body);
    }

    public void getMemChain(String chId) {
        EventBus.getDefault().post(getMemChainDirc(chId));
    }

    public MemChain getMemChainDirc(String chId) {
        MemChain chain ;
        List<MemChain> chains = daoSession.getMemChainDao().queryBuilder().
                where(MemChainDao.Properties.ChId.eq(chId)).list();
        if(chains.size()>0)
            chain = chains.get(0);
        else
            chain = new MemChain();
        return chain;
    }

    public MemFragmentBody getMemFgtBodyDirc(String fId){
        MemFragmentBody body ;
        List<MemFragmentBody> bodies = daoSession.getMemFragmentBodyDao().queryBuilder().
                where(MemFragmentBodyDao.Properties.FId.eq(fId)).list();
        if(bodies.size()>0)
            body = bodies.get(0);
        else
            body = new MemFragmentBody();
        return body;
    }

    public MemChain getDefaultChain(String uId){
        String chId = MemChainTools.DEFAULT_CHAIN_ID_HEAD+uId;
        return getMemChainDirc(chId);
    }


    public void getMemChainByUID(String uId){

        if(uId!=null&&!uId.equals("")) {
                List<MemChain> chains = daoSession.getMemChainDao().queryBuilder().
                    where(MemChainDao.Properties.UId.eq(uId)).
                        orderAsc(MemChainDao.Properties.ChTime).
                        list();
            UserMessage msg = new UserMessage();
            msg.type = UserMessage.MEM_CHAIN_LIST_FROM_LOCMEM;
            msg.para = chains;
            EventBus.getDefault().post(msg);
        }else{
            Logger.e("uId = null");
        }

    }

    public void insertMemChainUnRep(MemChain chain){
        daoSession.getMemChainDao().insert(chain);
    }

    public void updateMemChain(MemChain chain){
        daoSession.getMemChainDao().update(chain);
    }


    protected MemFragmentHead getMemHeadFromDB(String fId){
        List<MemFragmentHead> heads = daoSession.getMemFragmentHeadDao().queryBuilder().
                where(MemFragmentHeadDao.Properties.FId.eq(fId)).
                limit(1).list();
        if(heads==null|| heads.size()==0)
            return null;
        else
            return heads.get(0);
    }

    public void insertMemHeadIntoDB(MemFragmentHead head){
        daoSession.getMemFragmentHeadDao().insert(head);
    }
    public void insertMemHeadsIntoDB(List<MemFragmentHead> heads){
        if(heads==null) return;
        daoSession.getMemFragmentHeadDao().insertOrReplaceInTx(heads);
    }
    public void insertMemBodyIntoDB(MemFragmentBody body){
        daoSession.getMemFragmentBodyDao().insertOrReplace(body);
    }
    public void insertMemBodiesIntoDB(List<MemFragmentBody> bodies){
        daoSession.getMemFragmentBodyDao().insertOrReplaceInTx(bodies);
    }

    public void insertMemHeadsIntoLRU(List<MemFragmentHead> heads){
        if(heads==null) return;
        for(MemFragmentHead head : heads)
              mLRUmemFgtHead.add(head.getFId(),head);
    }


    public void insertMemChainsIntoDB(List<MemChain> chains){
        if(chains==null) return ;
        daoSession.getMemChainDao().insertOrReplaceInTx(chains);
    }

    public void insertMemChainsIntoLRU(List<MemChain> chains){
        if(chains==null) return ;
        for(MemChain chain:chains)
            mLRUmemChain.add(chain.getChId(),chain);
    }


    public MemChain getMemChainFromDB(String chId){
        List<MemChain> heads = daoSession.getMemChainDao().queryBuilder().
                where(MemChainDao.Properties.ChId.eq(chId)).
                limit(1).list();
        if(heads==null|| heads.size()==0)
            return null;
        else
            return heads.get(0);
    }

    public void deleteMemChainsByChId(List<MemChain> chains){
        daoSession.getMemChainDao().deleteInTx(chains);
    }

    public void deleteMemFgtHeadsByFId(List<MemFragmentHead> heads){
        if(heads==null) return ;
        for(MemFragmentHead h :heads) {
            QueryBuilder<MemFragmentHead> qb =
                    daoSession.getMemFragmentHeadDao().queryBuilder();
            qb.where(MemFragmentHeadDao.Properties.FId.eq(h.getFId()));
            daoSession.getMemFragmentHeadDao().deleteInTx(qb.list());
        }

    }

    public void deleteMemFgtBodysByHeads(List<MemFragmentHead> heads){
        if(heads==null) return ;
        for(MemFragmentHead h :heads) {
            QueryBuilder<MemFragmentBody> qb =
                    daoSession.getMemFragmentBodyDao().queryBuilder();
            qb.where(MemFragmentBodyDao.Properties.FId.eq(h.getFId()));
            daoSession.getMemFragmentBodyDao().deleteInTx(qb.list());
        }

    }

    public void getLocShareBlocks(MyCellLocation loc,int index){
        ShareBlock block = null;
        List<ShareBlock> blocks = new ArrayList<>();
        int tmp =0;
        for(int i=0;i<10;i++){
            block = new ShareBlock();
            tmp = i%2;
            block.setUId(IDCreater.getUID());
            block.setUNickName("NickName"+i);
            block.setSTime(TimeTools.getNow());
            block.setPokerCount(i);
            block.setSFlag(tmp);
            if(tmp==0){
                block.setSId(IDCreater.getFID());
                block.setSData1("fTitle"+i);
                block.setSData2(MyTime.buildNowTime().toDescribe());
                block.setSData3("地点"+i);
            }else{
                block.setSId(IDCreater.getCHID());
                block.setSData1("chName"+i);
                block.setSData2("chDetail"+i);
                block.setSData3(i+"-"+tmp);
            }
            blocks.add(block);
        }
        EventBus.getDefault().post(blocks);
    }


    public void insertUserInfo(UserInfo userInfo){
        daoSession.getUserInfoDao().insertOrReplace(userInfo);
    }


    public void updateUserInfo(UserInfo userInfo){
        daoSession.getUserInfoDao().update(userInfo);
    }

    @Background
    public void loadMem(List<ShareBlock> blocks){
        Logger.d("loading heads and chains");
        boolean isMemFgts = true;
        bs[0] = bs[1] = true;
        ComMessage msg ;
        MemFragmentHead tpHead;
        MemChain tpChain;
        tmpHeads.clear();
        tmpChains.clear();
          for(ShareBlock block : blocks) {
              isMemFgts = isMemFgt(block.getSFlag());
              if(isMemFgts){
                    if(!mLRUmemFgtHead.contain(block.getSId())){
                        tpHead = getMemHeadFromDB(block.getSId());
                        if(tpHead!=null)
                            mLRUmemFgtHead.add(block.getSId(),tpHead);
                        else
                            tmpHeads.add(block.getSId());
                    }// end if
              }//is MemFgts
              else {
                  if(!mLRUmemChain.contain(block.getSId())){
                      tpChain = getMemChainFromDB(block.getSId());
                      if(tpChain!=null)
                          mLRUmemChain.add(block.getSId(),tpChain);
                      else
                          tmpChains.add(block.getSId());
                  }// end if
              }//end else is MemFgts
          }//end for

        if(tmpChains.size()!=0){
            msg = new ComMessage(ComMessage.ASK_DLD_HEADS,tmpHeads);
            EventBus.getDefault().post(msg);
            bs[0] = false;
        }
        if(tmpHeads.size()!=0){
            msg = new ComMessage(ComMessage.ASK_DLD_CHAINS,tmpChains);
            EventBus.getDefault().post(msg);
            bs[1] = false;
        }
        if(bs[0]==true&&bs[1]==true){
            EventBus.getDefault().post(new MySystemCode(MySystemCode.UPDATE_SHARE_BLOCKS_OK));
        }
    }

    boolean isMemFgt(Integer sFlag){
            return sFlag == null || sFlag == 0;
    }


}
