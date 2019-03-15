package com.flyz.zwp.spacetime.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.flyz.zwp.spacetime.App;
import com.flyz.zwp.spacetime.R;
import com.flyz.zwp.spacetime.componet.FragmentTools;
import com.flyz.zwp.spacetime.componet.IDCreater;
import com.flyz.zwp.spacetime.componet.MemChainTools;
import com.flyz.zwp.spacetime.componet.MyTime;
import com.flyz.zwp.spacetime.componet.ShareBlockTools;
import com.flyz.zwp.spacetime.model.MemChain;
import com.flyz.zwp.spacetime.model.MemFragmentBody;
import com.flyz.zwp.spacetime.model.MemFragmentHead;
import com.flyz.zwp.spacetime.model.MyCellLocation;
import com.flyz.zwp.spacetime.model.MySystemCode;
import com.flyz.zwp.spacetime.model.ShareBlock;
import com.google.gson.Gson;
import com.google.gson.internal.ObjectConstructor;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity
public class MemChainActivity extends AppCompatActivity {


    private String cId;
    private String uId;
    private String cName;
    private String[] cl =  new String[]{"分享","更改状态","定位验证"};
    private boolean [] tmpBools = new boolean[]{false,false,false,false};
    private int chooseIndex = -1;

    private Boolean isOp = true;

    @org.androidannotations.annotations.App
    App app;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.tv_chain_chainname)
    TextView tvChainName;
    @ViewById(R.id.tv_chain_chaindetail)
    TextView tvChainDetail;
    @ViewById(R.id.tv_chain_chainstate)
    TextView tvChainState;
    @ViewById(R.id.tv_chain_chainsTime)
    TextView tvChainsTime;
    @ViewById(R.id.tv_chain_chaineTime)
    TextView tvChaineTime;
    @ViewById(R.id.tv_chain_chaincTime)
    TextView tvChaincTime;

    @ViewById(R.id.lv_chain_fgts)
    ListView lvChainFgts;
    @ViewById(R.id.btn_chain_edit)
    Button btnEditChain;

    private List<MemFragmentHead> mMemFregentHeads= new ArrayList<>();
    private SimpleAdapter simpleAdapter = null;
    private List<Map<String,Object>> mHeads = new ArrayList<Map<String,Object>>();

    private MemChain memChain = null;

    private ProgressDialog progressDialog = null;
    private int tmpPos = -1;

    @ItemClick(R.id.lv_chain_fgts)
    void showFgt(int pos) {
        Intent i = new Intent();
        i.putExtra("uId",memChain.getUId());
        i.putExtra("FId",mMemFregentHeads.get(pos).getFId());
        i.putExtra("FTitle",mMemFregentHeads.get(pos).getFTitle());
        i.setClass(MemChainActivity.this,MemFragmentActivity_.class);
        startActivity(i);
    }

    @ItemLongClick(R.id.lv_chain_fgts)
    void editFgtStatus(final int pos){

        if(uId==null||!uId.equals(app.getNowUId())||!isOp)
            return;

        chooseIndex = pos;
        AlertDialog.Builder ab = new AlertDialog.Builder(MemChainActivity.this);
        ab.setTitle("选择");
        ab.setItems(cl, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    if(checkIsShareOk(memChain.getChContext().get(pos)))
                        showShareDialog(!FragmentTools.isShare(memChain.getChContext().get(pos)));
                }else if(which==1){
                    if(MemChainTools.isDefaultChainNotLoc(memChain)
                            &&FragmentTools.isShare(memChain.getChContext().get(pos))){
                        showTextDialog("碎片分享中不可更改");
                        return;
                    }
                    showChangeStatusDialog(pos);
                }else if(which==2){
                    if(FragmentTools.isVerif(memChain.getChContext().get(pos))){
                        showUnDoneVerifDialog(true);
                    }else{
                        app.getLoctionClient().getLocation();
                        showProgressDialog();
                        tmpPos= pos;
                    }

                }

            }
        });
        ab.show();
    }

    void showChangeStatusDialog(final int pos){
        final MemFragmentHead head = memChain.getChContext().get(pos);
        final View switchView = LayoutInflater.from(MemChainActivity.this)
                    .inflate(R.layout.dialog_fgt_status_switch,null);
        final Switch switchSyn =(Switch) switchView.findViewById(R.id.switch_fgt_status_syn);
        final Switch switchShow =(Switch) switchView.findViewById(R.id.switch_fgt_status_show);
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("选择需要改变的状态");
        ab.setView(switchView);
        ab.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tmpBools[2] = switchSyn.isChecked();
                tmpBools[3] = switchShow.isChecked();
                showOpStatusDialog(head,mHeads.get(pos));
            }
        });
        tmpBools[0] = FragmentTools.isSyn(head);
        tmpBools[1] = FragmentTools.isShow(head);
        if(!MemChainTools.isDefaultChainNotLoc(memChain)) {
            switchSyn.setChecked(false);
            switchSyn.setEnabled(false);
        }
        else
            switchSyn.setChecked( tmpBools[0]);
        switchShow.setChecked(tmpBools[1]);
        ab.show();
    }


    void showOpStatusDialog(final MemFragmentHead head,final Map<String,Object> damp){

        FragmentTools.setSyn(head, tmpBools[2]);
        FragmentTools.setShow(head, tmpBools[3]);
        MemChainTools.synList2Json(memChain);
        if(tmpBools[0]!=tmpBools[2]||tmpBools[1]!=tmpBools[3] ){//网络请求
            MemChainTools.synList2Json(memChain);
            if(tmpBools[0]==false&&tmpBools[2]==false) {
                app.getLocMemory().updateMemChain(memChain);
                String tmp = "isSyn:"+ FragmentTools.isSyn(head)+
                        " isShow:"+ FragmentTools.isShow(head)+
                        " isShare:"+ FragmentTools.isShare(head)+
                        " isVerif:"+ FragmentTools.isVerif(head);
                damp.put("fState",tmp);
                simpleAdapter.notifyDataSetChanged();
                if(MemChainTools.isSyn(memChain))
                    app.getMyHttpClient().uploadMemChain(memChain);
                //排除不用同步情况
            }else{
                showUpLoadingDialog();
                if(tmpBools[0]==true&&tmpBools[2]==false)
                    app.getMyHttpClient().cancelUploadMemBody(head.getFId());//取消同步
                else
                    app.getMyHttpClient().uploadMemBody(
                            app.getLocMemory().getMemFgtBodyDirc(head.getFId()));
            }

        }

    }


    void showUnDoneVerifDialog(boolean isOk){
        if(isOk)
            new AlertDialog.Builder(this)
                .setMessage("碎片已通过验证！")
                .setNegativeButton("确定",null)
                .show();
        else
            new AlertDialog.Builder(this)
                    .setMessage("碎片未通过验证！")
                    .setNegativeButton("确定",null)
                    .show();
    }
    void showProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("等待定位信息！");
        progressDialog.setMessage("等待中...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    void cancelProgressDialog(){
        if(progressDialog==null) return;
        progressDialog.dismiss();
        progressDialog =null;
    }
    void showUpLoadingDialog(){
        cancelProgressDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("修改数据中。。。");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    void showTextDialog(String msg){
        new AlertDialog.Builder(this).
                setMessage(msg).setNegativeButton("确定",null).show();
    }
    void showShareDialog(boolean needShare){
        AlertDialog.Builder ab =new AlertDialog.Builder(this);

        if(needShare){
            ab.setMessage("是否将碎片<"+memChain.getChContext().get(chooseIndex).getFTitle()+
                                ">分享到它所在的区块？");
            ab.setNegativeButton("取消",null);
            ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MemFragmentBody body =
                            app.getLocMemory().getMemFgtBodyDirc(
                                    memChain.getChContext().get(chooseIndex).getFId());
                    String ceId = IDCreater.getCEID(body.getCellX(),body.getCellY());
                    app.getMyHttpClient().uploadShareBlock(
                            ShareBlockTools.getShareBlock(memChain.getChContext().get(chooseIndex),
                                    app.getNowUNickName(),ceId));
                    showUpLoadingDialog();
                }
            });
        }else{
            ab.setMessage("是否将碎片<"+memChain.getChContext().get(chooseIndex).getFTitle()+
                    ">取消分享？");
            ab.setNegativeButton("取消",null);
            ab.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ShareBlock shareBlock = new ShareBlock();
                    shareBlock.setSId(memChain.getChContext().get(chooseIndex).getFId());
                    app.getMyHttpClient().cancelUploadShareBlock(shareBlock);
                    showUpLoadingDialog();
                }
            });
        }
        ab.show();
    }

    boolean checkIsShareOk(MemFragmentHead head){
        if(head ==null) return false;
        if(!app.isOnLine()){
            showTextDialog("离线用户不可分享！");
            return false;
        }
        if(!MemChainTools.isDefaultChainNotLoc(memChain)){
            showTextDialog("请在默认列表下分享碎片！");
            return false;
        }
        if(!FragmentTools.isSyn(head)||!FragmentTools.isShow(head)||!FragmentTools.isVerif(head)){
            showTextDialog("碎片不可被分享！请确保碎片已同步，可被其他人查看和已通过验证！");
            return false;
        }
        return true;
    }


    @Click(R.id.btn_chain_edit)
    void editChain() {
        Intent i = new Intent();
        i.putExtra("chId",cId);
        i.setClass(MemChainActivity.this,ManageFgtActivity_.class);
        startActivity(i);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    void updateStatusMessage(MySystemCode code){
        cancelProgressDialog();
        if(code.errorCode==MySystemCode.UPLOAD_MEM_CHAIN_OK)
            return ;

        if(code.errorCode==MySystemCode.UPLOAD_MEM_BODY_OK||
                code.errorCode==MySystemCode.CANCEL_UPLOAD_MEM_BODY_OK) {
           MemFragmentHead head = memChain.getChContext().get(chooseIndex);
            app.getLocMemory().updateMemChain(memChain);
            String tmp = "isSyn:"+ FragmentTools.isSyn(head)+
                    " isShow:"+ FragmentTools.isShow(head)+
                    " isShare:"+ FragmentTools.isShare(head)+
                    " isVerif:"+ FragmentTools.isVerif(head);
            mHeads.get(chooseIndex).put("fState",tmp);
            simpleAdapter.notifyDataSetChanged();
            if(MemChainTools.isSyn(memChain))
                app.getMyHttpClient().uploadMemChain(memChain);
        }else if(code.errorCode==MySystemCode.UPLOAD_MEM_BODY_ERR||
                code.errorCode==MySystemCode.CANCEL_UPLOAD_MEM_BODY_ERR){
            showTextDialog("更新错误！");
            FragmentTools.setSyn(memChain.getChContext().get(chooseIndex), tmpBools[0]);
            FragmentTools.setShow(memChain.getChContext().get(chooseIndex), tmpBools[1]);
            MemChainTools.synList2Json(memChain);
            // Logger.d(code.errorCode);
        }

        if(code.errorCode==MySystemCode.UPLOAD_SHARE_BLOCK_OK||
                code.errorCode==MySystemCode.CANCEL_UPLOAD_SHARE_BLOCK_OK){
            MemFragmentHead head = memChain.getChContext().get(chooseIndex);
            FragmentTools.setShare(head, !FragmentTools.isShare(head));
            MemChainTools.synList2Json(memChain);
            app.getLocMemory().updateMemChain(memChain);
            String tmp = "isSyn:"+ FragmentTools.isSyn(head)+
                    " isShow:"+ FragmentTools.isShow(head)+
                    " isShare:"+ FragmentTools.isShare(head)+
                    " isVerif:"+ FragmentTools.isVerif(head);
            mHeads.get(chooseIndex).put("fState",tmp);
            simpleAdapter.notifyDataSetChanged();
            if(MemChainTools.isSyn(memChain))
                app.getMyHttpClient().uploadMemChain(memChain);
        }else if(code.errorCode==MySystemCode.UPLOAD_SHARE_BLOCK_ERR||
                code.errorCode==MySystemCode.CANCEL_UPLOAD_SHARE_BLOCK_ERR){
            showTextDialog("分享更新错误！");
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    void showChainInfo(MemChain chain){

        if(chain.getChId()==null) return ;
        this.memChain = chain;
        String s = new Gson().toJson(chain);
        tvChainName.setText("记忆链名："+chain.getChName());
        tvChainDetail.setText("描述"+chain.getChDetail());
        String  tmp = "碎片数:"+ MemChainTools.getFragmentCount(chain)+
                "  isSyn:"+MemChainTools.isSyn(chain)+
                "  isShow:"+MemChainTools.isShow(chain)+
                "  isShare:"+MemChainTools.isShare(chain);
        tvChainState.setText(tmp);
        tvChainsTime.setText(chain.getScTime());
        tvChaineTime.setText(chain.getSeTime());
        tvChaincTime.setText("创建时间："+chain.getChTime());

        MemChainTools.synJson2List(memChain);
        setListViewData(memChain.getChContext());
        //simpleAdapter.notifyDataSetChanged();
        Logger.json(s);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void getLocMessage(MyCellLocation loc) {
        cancelProgressDialog();
        MemFragmentBody body = app.getLocMemory().
                getMemFgtBodyDirc(memChain.getChContext().get(tmpPos).getFId());
        if(body.getFId()==null)
            showUnDoneVerifDialog(false);
        else if(loc.getmCellX()!=body.getCellX()
                ||loc.getmCellY()!=body.getCellY())
            showUnDoneVerifDialog(false);
        else{
            FragmentTools.setVerif(memChain.getChContext().get(tmpPos),true);
            MemChainTools.synList2Json(memChain);
            app.getLocMemory().updateMemChain(memChain);
            String tmp = "isSyn:"+ FragmentTools.isSyn(memChain.getChContext().get(tmpPos))+
                    " isShow:"+ FragmentTools.isShow(memChain.getChContext().get(tmpPos))+
                    " isShare:"+ FragmentTools.isShare(memChain.getChContext().get(tmpPos))+
                    " isVerif:"+ FragmentTools.isVerif(memChain.getChContext().get(tmpPos));
            mHeads.get(tmpPos).put("fState",tmp);
            simpleAdapter.notifyDataSetChanged();
            showUnDoneVerifDialog(true);
        }
    }


    void setListViewData(List<MemFragmentHead> oriHeads){
        mMemFregentHeads.clear();
        mHeads.clear();
        if(oriHeads==null||oriHeads.size()==0) return;

        if(!uId.equals(app.getNowUId())||!isOp){
            for(MemFragmentHead h :oriHeads)
                if(FragmentTools.isShow(h))
                    mMemFregentHeads.add(h);
        }else
          mMemFregentHeads.addAll(oriHeads);

        List<MemFragmentHead> heads = mMemFregentHeads;
        String tmp ;
        Map<String,Object> map = null;
        for(MemFragmentHead head : heads){
            map = new HashMap<String,Object>();
            map.put("fTile","标题："+head.getFTitle());
            tmp = "isSyn:"+ FragmentTools.isSyn(head)+
                    " isShow:"+ FragmentTools.isShow(head)+
                    " isShare:"+ FragmentTools.isShare(head)+
                    " isVerif:"+ FragmentTools.isVerif(head);
            map.put("fState",tmp);
            map.put("ftDetail","碎片发生时间："+head.getFtDetail());
            map.put("fpDetail","碎片发生位置："+head.getFpDetail());
            map.put("cTime","创建时间："+head.getCTime());
            mHeads.add(map);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_chain);
        cId = getIntent().getStringExtra("ChId");
        uId = getIntent().getStringExtra("uId");
        cName = getIntent().getStringExtra("ChName");
        isOp = getIntent().getBooleanExtra("isOp",true);
        toolbar.setTitle(cName);
        setSupportActionBar(toolbar);
      simpleAdapter = new SimpleAdapter(this,mHeads,R.layout.item_memchain_fgts,
                new String[]{"fTile","fState","ftDetail","fpDetail","cTime"},
                new int[]{R.id.tv_memchain_fgt_ftitle,R.id.tv_memchain_fgt_state,
                        R.id.tv_memchain_fgt_ftdetail,R.id.tv_memchain_fgt_fpdetail,
                        R.id.tv_memchain_fgt_ctime});
        lvChainFgts.setAdapter(simpleAdapter);
        if(uId==null||!uId.equals(app.getNowUId())||!isOp)
            btnEditChain.setVisibility(View.GONE);

    }


    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);
            app.getLocMemory().getMemChain(cId);
        super.onResume();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }



}
