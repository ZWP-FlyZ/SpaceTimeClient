package com.flyz.zwp.spacetime.componet;


import android.util.Log;

import org.androidannotations.annotations.EBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import com.flyz.zwp.spacetime.messages.ComMessage;
import com.flyz.zwp.spacetime.messages.LogMessage;
import com.flyz.zwp.spacetime.messages.MemBodyMessage;
import com.flyz.zwp.spacetime.messages.UserMessage;
import com.flyz.zwp.spacetime.model.MemChain;
import com.flyz.zwp.spacetime.model.MemFragmentBody;
import com.flyz.zwp.spacetime.model.MemFragmentHead;
import com.flyz.zwp.spacetime.model.MyCellLocation;
import com.flyz.zwp.spacetime.model.MySystemCode;
import com.flyz.zwp.spacetime.model.ShareBlock;
import com.flyz.zwp.spacetime.model.SpaceCell;
import com.flyz.zwp.spacetime.model.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.*;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zwp12 on 2017/2/22.
 */
@EBean(scope = EBean.Scope.Singleton)
public class MyHttpClient {

    static final String BASE_URL = "http://10.65.1.86:8080/spacetime/";
    static final String CHECK_NAME_URL = "check.do";
    static final String LOGON_URL = "logon.do";
    static final String LOGIN_URL = "login.do";
    static final String MA_CELL_INFO_URL = "get_cell_info.do";
    static final String MA_CRT_CELL_URL = "crt_space_cell.do";
    static final String MA_SHARE_BLOCKS_URL = "get_share_blocks.do";
    static final String USER_INFO_URL = "get_user_info.do";
    static final String USER_INFO_MEM_CHAIN_URL = "user_mem_chains.do";
    static final String USER_INFO_MEM_BODY_URL = "user_mem_bodies.do";
    static final String USER_INFO_UPDATE_URL = "set_user_info.do";
    static final String MEM_BODY_URL = "mem_fgt_body.do";
    static final String DL_MEM_HEAD_LIST_URL = "dl_mem_heads.do";
    static final String DL_MEM_CHAIN_LIST_URL = "dl_mem_chains.do";

    /**
     *
     */
    static final String SYN_MEM_CHAIN_URL = "upload_mem_chain.do";
    static final String SYN_MEM_CHAIN_LIST_URL = "upload_mem_chains.do";
    static final String CANCEL_SYN_MEM_CHAIN_URL = "cancel_mem_chain.do";
    static final String CANCEL_SYN_MEM_CHAIN_LIST_URL = "cancel_mem_chains.do";
    static final String SYN_MEM_BODY_URL = "upload_mem_body.do";
    static final String SYN_MEM_BODY_LIST_URL = "upload_mem_bodies.do";
    static final String CANCEL_SYN_MEM_BODY_URL = "cancel_upload_mem_body.do";
    static final String CANCEL_SYN_MEM_BODY_LIST_URL = "cancel_upload_mem_bodies.do";

    static final String UPLOAD_SHARE_BLOCK_URL = "upload_share_block.do";
    static final String CANCEL_UPLOAD_SHARE_BLOCK_URL = "cancel_upload_share_block.do";


    static AsyncHttpClient client = new AsyncHttpClient();
    static Gson gson = new Gson();

    public void getBaiduTest() {

        client.get("http://www.baidu.com", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Logger.d(new String(responseBody));
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Logger.d(new String(responseBody));
            }
        });
    }

    public void checkNickName(String name) {
        RequestParams params = new RequestParams();
        params.put("uNickName",name);
        client.get(BASE_URL+CHECK_NAME_URL , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                LogMessage msg = new LogMessage();
                msg.errorCode = Integer.parseInt(new String(responseBody));
                EventBus.getDefault().post(msg);
                Logger.d(new String(responseBody));
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                LogMessage msg = new LogMessage();
                msg.errorCode = LogMessage.LOGON_USER_NAME_ERR;
                EventBus.getDefault().post(msg);
                Logger.d(error.toString());
            }
        });
    }

    public void logonUser(final LogMessage msg) {
        final UserInfo userInfo = new UserInfo();
        userInfo.setUNickName(msg.uNickName);
        userInfo.setUPassword(msg.uPasswordE);
        userInfo.setCellX(msg.cellX);
        userInfo.setCellY(msg.cellY);
        userInfo.setCrDesc(msg.crDesc);
        RequestParams params = new RequestParams();
        params.put("userInfoJson",gson.toJson(userInfo));

        client.post(BASE_URL + LOGON_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                UserInfo info = gson.fromJson(new String(responseBody)
                        ,new TypeToken<UserInfo>(){}.getType());
                Logger.json(gson.toJson(info));
                if(info.getId()== LogMessage.LOGON_REG_OK) {
                    msg.errorCode = LogMessage.LOGON_REG_OK;
                    msg.uId = info.getUId();
                    msg.campFlag = info.getCampFlag();
                    msg.crTime = info.getCrTime();
                }else {
                    msg.errorCode = LogMessage.LOGON_REG_ERR;
                }
                EventBus.getDefault().post(msg);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                msg.errorCode = LogMessage.LOGON_REG_ERR;
                EventBus.getDefault().post(msg);
            }
        });
    }

    public void login(final LogMessage msg) {
        RequestParams params = new RequestParams();
        params.put("uNickName",msg.uNickName);
        params.put("uPasswordE",msg.uPasswordE);

        client.post(BASE_URL + LOGIN_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                UserInfo info = gson.fromJson(new String(responseBody)
                        ,new TypeToken<UserInfo>(){}.getType());
                if(info.getId()==LogMessage.LOGIN_PASS_ERR)
                    msg.errorCode = LogMessage.LOGIN_PASS_ERR;
                else if(info.getId()==LogMessage.LOGIN_OK){
                    msg.errorCode = LogMessage.LOGIN_OK;
                    info.setId(null);
                    msg.userInfo = info;
                }
                else
                    msg.errorCode = LogMessage.LOGIN_UN_ERR;
                
                EventBus.getDefault().post(msg);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                msg.errorCode = LogMessage.LOGIN_UN_ERR;
                EventBus.getDefault().post(msg);
            }
        });
    }

    public void cellInfo(final MyCellLocation loc){
        RequestParams params = new RequestParams();
        params.put("cellX",loc.getmCellX());
        params.put("cellY",loc.getmCellY());

        client.get(BASE_URL + MA_CELL_INFO_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                SpaceCell cellInfo = gson.fromJson(new String(responseBody)
                        ,new TypeToken<SpaceCell>(){}.getType());
                EventBus.getDefault().post(cellInfo);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Logger.d(error.toString());
                EventBus.getDefault().post(new SpaceCell());
            }
        });
    }

    public void createSpaceCell(MyCellLocation loc,String uId,String uNickName){
        RequestParams params = new RequestParams();
        params.put("cellX",loc.getmCellX());
        params.put("cellY",loc.getmCellY());
        params.put("uId",uId);
        params.put("uNickName",uNickName);
        client.get(BASE_URL + MA_CRT_CELL_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                MySystemCode code = new MySystemCode();
                code.errorCode = Integer.parseInt(new String(responseBody));
                EventBus.getDefault().post(code);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                MySystemCode code = new MySystemCode();
                code.errorCode = MySystemCode.CRT_SPACE_CELL_ERR;
                EventBus.getDefault().post(code);
            }
        });

    }

    public void shareBlock(MyCellLocation loc , int index){
        RequestParams params = new RequestParams();
        params.put("cellX",loc.getmCellX());
        params.put("cellY",loc.getmCellY());
        params.put("index",index);

        client.get(BASE_URL + MA_SHARE_BLOCKS_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                List<ShareBlock> blocks = gson.fromJson(new String(responseBody)
                        ,new TypeToken<List<ShareBlock>>(){}.getType());
                EventBus.getDefault().post(blocks);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                EventBus.getDefault().post(new ArrayList<ShareBlock>());
            }
        });
    }

    public void memFgtBody(String fId){
        RequestParams params = new RequestParams();
        params.put("fId",fId);
        client.get(BASE_URL + MEM_BODY_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                MemFragmentBody block = gson.fromJson(new String(responseBody)
                        ,new TypeToken<MemFragmentBody>(){}.getType());
                MemBodyMessage msg = new MemBodyMessage();
                msg.type = MemBodyMessage.MEM_BODY_FROM_NETMEM;
                msg.para = block;
                EventBus.getDefault().post(msg);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                EventBus.getDefault().post(new MemFragmentBody());
            }
        });

    }

    protected void memFgtHeadDLd(List<String> fIds){
        RequestParams params = new RequestParams();
        params.put("jsonFids",gson.toJson(fIds));

        client.get(BASE_URL + DL_MEM_HEAD_LIST_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                List<MemFragmentHead> heads = gson.fromJson(new String(responseBody)
                        ,new TypeToken<List<MemFragmentHead>>(){}.getType());
                EventBus.getDefault().post(new ComMessage(ComMessage.RE_DLD_HEADS_OK,heads));
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                EventBus.getDefault().post(new ComMessage(ComMessage.RE_DLD_HEADS_ERR,null));
            }
        });
    }

    protected void memChainDLd(List<String> chIds){
        RequestParams params = new RequestParams();
        params.put("jsonChids",gson.toJson(chIds));
        client.get(BASE_URL + DL_MEM_CHAIN_LIST_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                List<MemChain> chains = gson.fromJson(new String(responseBody)
                        ,new TypeToken<List<MemChain>>(){}.getType());
                EventBus.getDefault().post(new ComMessage(ComMessage.RE_DLD_CHAINS_OK,chains));
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                EventBus.getDefault().post(new ComMessage(ComMessage.RE_DLD_CHAINS_ERR,null));
            }
        });


    }

    public void memChainDldByUid(String uId){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        client.get(BASE_URL + USER_INFO_MEM_CHAIN_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                List<MemChain> chains = gson.fromJson(new String(responseBody)
                        ,new TypeToken<List<MemChain>>(){}.getType());
                UserMessage msg = new UserMessage();
                msg.type = UserMessage.MEM_CHAIN_LIST_FROM_NETMEM;
                msg.para = chains;
                EventBus.getDefault().post(msg);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                EventBus.getDefault().post(new UserMessage());
            }
        });
    }

    public void memBodyDldByUid(String uId){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        client.get(BASE_URL + USER_INFO_MEM_BODY_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                List<MemFragmentBody> chains = gson.fromJson(new String(responseBody)
                        ,new TypeToken<List<MemFragmentBody>>(){}.getType());
                UserMessage msg = new UserMessage();
                msg.type = UserMessage.MEM_BODY_LIST_FROM_NETMEM;
                msg.para = chains;
                EventBus.getDefault().post(msg);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                EventBus.getDefault().post(new UserMessage());
            }
        });
    }

    /**
     * if (uId == offline_UID) post new UserInfo(uId == offline_UID)
     * if net error   post   new UserInfo(uId == null)
     * @param uId
     */
    public void getUserInfo(String uId) {

        RequestParams params = new RequestParams();
        params.put("uId",uId);
        client.get(BASE_URL + USER_INFO_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
               UserInfo info = gson.fromJson(new String(responseBody)
                        ,new TypeToken<UserInfo>(){}.getType());
                if(info==null) info = new UserInfo();
                EventBus.getDefault().post(info);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                EventBus.getDefault().post(new UserInfo());
            }
        });

//        UserInfo userInfo = new UserInfo();
//        userInfo.setUNickName("dadf");
//        userInfo.setUId(uId);
//        userInfo.setUSex(0);
//        EventBus.getDefault().post(userInfo);
    }
    public void updateUserInfo(UserInfo info){

        RequestParams params = new RequestParams();
        params.put("uId",info.getUId());
        params.put("uNickName",info.getUNickName());
        params.put("uSex",info.getUSex());
        params.put("uBri",info.getUBri());
        params.put("uSign",info.getUSign());

        //params.put("userInfo",info);
        client.get(BASE_URL + USER_INFO_UPDATE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                MySystemCode code = new MySystemCode();
                code.errorCode = Integer.parseInt(new String(responseBody));
                EventBus.getDefault().post(code);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                MySystemCode code = new MySystemCode();
                code.errorCode = MySystemCode.UPDATE_USER_INFO_ERR;
                EventBus.getDefault().post(code);
            }
        });

    }

    public void uploadMemChain(MemChain chain){
        RequestParams params = new RequestParams();
        params.put("memChainJson",gson.toJson(chain));
        client.get(BASE_URL + SYN_MEM_CHAIN_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                MySystemCode code = new MySystemCode();
                code.errorCode = Integer.parseInt(new String(responseBody));
                EventBus.getDefault().post(code);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                MySystemCode code = new MySystemCode();
                code.errorCode = MySystemCode.UPLOAD_MEM_CHAIN_ERR;
                EventBus.getDefault().post(code);
            }
        });

    }
    public void cancelUploadMemChain(String chId){
        RequestParams params = new RequestParams();
        params.put("chId",chId);
        params.put("uId","");
        client.get(BASE_URL + CANCEL_SYN_MEM_CHAIN_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                MySystemCode code = new MySystemCode();
                code.errorCode = Integer.parseInt(new String(responseBody));
                EventBus.getDefault().post(code);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                MySystemCode code = new MySystemCode();
                code.errorCode = MySystemCode.CANCEL_UPLOAD_MEM_CHAIN_ERR;
                EventBus.getDefault().post(code);
            }
        });

    }
    public void uploadMemBody(MemFragmentBody body){
        RequestParams params = new RequestParams();
        params.put("memBodyJson",gson.toJson(body));

        client.get(BASE_URL + SYN_MEM_BODY_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                MySystemCode code = new MySystemCode();
                code.errorCode = Integer.parseInt(new String(responseBody));
                EventBus.getDefault().post(code);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                MySystemCode code = new MySystemCode();
                code.errorCode = MySystemCode.UPLOAD_MEM_BODY_ERR;
                EventBus.getDefault().post(code);
            }
        });

    }
    public void cancelUploadMemBody(String fId){
        RequestParams params = new RequestParams();
        params.put("Fid",fId);

        client.get(BASE_URL + CANCEL_SYN_MEM_BODY_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                MySystemCode code = new MySystemCode();
                code.errorCode = Integer.parseInt(new String(responseBody));
                EventBus.getDefault().post(code);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                MySystemCode code = new MySystemCode();
                code.errorCode = MySystemCode.CANCEL_UPLOAD_MEM_BODY_ERR;
                EventBus.getDefault().post(code);
            }
        });

    }


    public void uploadShareBlock(ShareBlock block){

        RequestParams params = new RequestParams();
        params.put("shareBlockJson",gson.toJson(block));
        client.get(BASE_URL + UPLOAD_SHARE_BLOCK_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                MySystemCode code = new MySystemCode();
                code.errorCode = Integer.parseInt(new String(responseBody));
                EventBus.getDefault().post(code);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                MySystemCode code = new MySystemCode();
                code.errorCode = MySystemCode.UPLOAD_SHARE_BLOCK_ERR;
                EventBus.getDefault().post(code);
            }
        });
    }



    public void cancelUploadShareBlock(ShareBlock block){
        RequestParams params = new RequestParams();
        params.put("sId",block.getSId());
        params.put("ceId",block.getCeId());
        client.get(BASE_URL + CANCEL_UPLOAD_SHARE_BLOCK_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                MySystemCode code = new MySystemCode();
                code.errorCode = Integer.parseInt(new String(responseBody));
                EventBus.getDefault().post(code);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                MySystemCode code = new MySystemCode();
                code.errorCode = MySystemCode.CANCEL_UPLOAD_SHARE_BLOCK_ERR;
                EventBus.getDefault().post(code);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    void askFromOtherCom(ComMessage msg){
        if(msg.askCode==ComMessage.ASK_DLD_HEADS){
            memFgtHeadDLd((List<String>) msg.pram);
        }else if(msg.askCode == ComMessage.ASK_DLD_CHAINS){
            memChainDLd((List<String>) msg.pram);
        }
    }

    public void logOut(){}

}
