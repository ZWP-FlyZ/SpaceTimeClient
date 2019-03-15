package com.flyz.zwp.spacetime.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.flyz.zwp.spacetime.componet.ShareBlockTools;
import com.flyz.zwp.spacetime.messages.UserMessage;
import com.flyz.zwp.spacetime.model.MemChain;
import com.flyz.zwp.spacetime.model.MemFragmentBody;
import com.flyz.zwp.spacetime.model.MemFragmentHead;
import com.flyz.zwp.spacetime.model.MySystemCode;
import com.flyz.zwp.spacetime.model.ShareBlock;
import com.flyz.zwp.spacetime.model.UserInfo;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity
public class UserInfoActivity extends AppCompatActivity {

    private String uId = null;
    private String uNickName = null;
    private UserInfo info=null;
    private String[] cl = new String[]{"分享","更改状态"};
    private List<String> longClickMem = new ArrayList<String>();

    private List<Map<String,Object>> mChains = new ArrayList<Map<String,Object>>();
    private List<MemChain> mMemChains = new ArrayList<MemChain>();

    private SimpleAdapter simpleAdapter = null;
    private ArrayAdapter<String> arrayAdapter = null;

    private ProgressDialog progressDialog = null;
    private boolean [] showDialog = new boolean[]{false,false};
    private boolean [] tmpBools = new boolean[]{false,false,false,false};
    private int chooseIndex = -1;

    private Boolean isOp = true;

    @org.androidannotations.annotations.App
    App app;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.fab)
    FloatingActionButton fab;
    @Click(R.id.fab)
    void clickFab(View view) {
        startActivity(new Intent().setClass(UserInfoActivity.this,FgtCrtActivity_.class));
    }

    @ViewById(R.id.btn_user_edit)
    Button btnEditUserInfo;
    @Click(R.id.btn_user_edit)
    void editUserInfo() {
        Intent i = new Intent();
        i.setClass(UserInfoActivity.this,EditUserActivity_.class);
        i.putExtra("uId",uId);
        i.putExtra("uNickName",uNickName);
        startActivityForResult(i,1111);
    }

    @OnActivityResult(1111)
    void onResultForUserInfo(){
        app.getMyHttpClient().getUserInfo(uId);
    }


    @ViewById(R.id.btn_uinfo_chain_set)
    Button btnAddChain;
    @Click(R.id.btn_uinfo_chain_set)
    void addChain() {
        Intent i = new Intent();
        i.setClass(UserInfoActivity.this,MchCrtActivity_.class);
        i.putExtra("uId",uId);
        startActivityForResult(i,1101);
    }

    @ViewById(R.id.btn_uinfo_chain_manage)
    Button btnManageChain;
    @Click(R.id.btn_uinfo_chain_manage)
    void manageChain() {
        Intent i = new Intent();
        i.setClass(UserInfoActivity.this,ManageChainActivity_.class);
        i.putExtra("uId",uId);
        startActivityForResult(i,1101);
    }

    @OnActivityResult(1101)
    void onResult(@OnActivityResult.Extra(value = "chId") String chId) {
        if(chId==null||chId.equals(""))return ;
//        MemChain c = app.getLocMemory().getMemChainDirc(chId);
//        mMemChains.add(c);
//
//        Map<String,Object> map =   map = new HashMap<String,Object>();
//        map.put("name","记忆链名："+c.getChName());
//        map.put("detail","描述："+c.getChDetail());
//        String tmp = "碎片数:"+ MemChainTools.getFragmentCount(c)+
//                "  isSyn:"+MemChainTools.isSyn(c)+
//                "  isShow:"+MemChainTools.isShow(c)+
//                "  isShare:"+MemChainTools.isShare(c);
//        map.put("state",tmp);
//        map.put("sTime",""+c.getScTime());
//        map.put("eTime",""+c.getSeTime());
//        map.put("cTime","创建时间："+c.getChTime());
//        mChains.add(map);
//
//        Logger.json(new Gson().toJson(mMemChains));
//        simpleAdapter.notifyDataSetChanged();
    }

    @ViewById(R.id.lv_user_chains)
    ListView lvMemChains;

    @ViewById(R.id.tv_user_info)
    TextView tvUserInfo;

    @ItemClick(R.id.lv_user_chains)
    void showMemChain(int pos){
        Intent i = new Intent();
        i.putExtra("ChId",mMemChains.get(pos).getChId());
        i.putExtra("ChName",mMemChains.get(pos).getChName());
        i.putExtra("uId",uId);
        i.setClass(UserInfoActivity.this,MemChainActivity_.class);
        startActivity(i);
    }

    @ItemLongClick(R.id.lv_user_chains)
    void showChainLongClick(final int pos){

        chooseIndex = pos;
        if(!app.isOnLine()||!uId.equals(app.getNowUId())||!isOp)
            return ;
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
                ab.setTitle("选择操作");
                ab.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0)//share select
                        {
                            if(!checkIsShareOk(mMemChains.get(pos)))
                                return ;

                            if(MemChainTools.isShare(mMemChains.get(pos)))
                                cancelShareDialog();
                            else
                                showChooseShareDialog();
                        }else if(which==1){
                            showStatusChangeDialog(mMemChains.get(pos), mChains.get(pos));
                        }
                app.getLocMemory().updateMemChain(mMemChains.get(pos));
                    String tmp = "碎片数:"+ MemChainTools.getFragmentCount(mMemChains.get(pos))+
                            "  isSyn:"+MemChainTools.isSyn(mMemChains.get(pos))+
                            "  isShow:"+MemChainTools.isShow(mMemChains.get(pos))+
                            "  isShare:"+MemChainTools.isShare(mMemChains.get(pos));

                    mChains.get(pos).put("state",tmp);
                    simpleAdapter.notifyDataSetChanged();
                //UserInfoActivity.this.askForDataForUpate();
            }
        });
        ab.show();

    }

    void showStatusChangeDialog(final MemChain chain,final Map<String,Object> damp){

        final View switchView = LayoutInflater.from(UserInfoActivity.this).
                inflate(R.layout.dialog_chain_status_switch_,null);
        final Switch switchSyn =(Switch) switchView.findViewById(R.id.switch_chain_status_syn);
        final Switch switchShow =(Switch) switchView.findViewById(R.id.switch_chain_status_show);

        Arrays.fill(tmpBools,false);

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("选择需要改变的状态");
        ab.setView(switchView);
        ab.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                MemChainTools.setSyn(chain, switchSyn.isChecked());
//                MemChainTools.setShow(chain, switchShow.isChecked());
//                String tmp = "碎片数:"+ MemChainTools.getFragmentCount(chain)+
//                        "  isSyn:"+MemChainTools.isSyn(chain)+
//                        "  isShow:"+MemChainTools.isShow(chain)+
//                        "  isShare:"+MemChainTools.isShare(chain);
//                damp.put("state",tmp);
//                simpleAdapter.notifyDataSetChanged();
                tmpBools[2] = switchSyn.isChecked();
                tmpBools[3] = switchShow.isChecked();
                showOpStatusDialog(chain,damp);

            }
        });

        tmpBools[0] = MemChainTools.isSyn(chain);
        tmpBools[1] = MemChainTools.isShow(chain);
        switchSyn.setChecked(tmpBools[0]);
        switchShow.setChecked(tmpBools[1]);
       ab.show();
    }

    void showOpStatusDialog(final MemChain chain,final Map<String,Object> damp){
        MemChainTools.setSyn(chain, tmpBools[2]);
        MemChainTools.setShow(chain, tmpBools[3]);

       if(tmpBools[0]!=tmpBools[2]||tmpBools[1]!=tmpBools[3] ){//网络请求
           if(tmpBools[0]==false&&tmpBools[2]==false) {
               app.getLocMemory().updateMemChain(chain);
               String tmp = "碎片数:"+ MemChainTools.getFragmentCount(chain)+
                       "  isSyn:"+MemChainTools.isSyn(chain)+
                       "  isShow:"+MemChainTools.isShow(chain)+
                       "  isShare:"+MemChainTools.isShare(chain);
               damp.put("state",tmp);
               simpleAdapter.notifyDataSetChanged();
               return; //排除不用同步情况
           }
           showUpLoadingDialog();
           if(tmpBools[0]==true&&tmpBools[2]==false)
               app.getMyHttpClient().cancelUploadMemChain(chain.getChId());//取消同步
           else
               app.getMyHttpClient().uploadMemChain(chain);
       }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void updateUserInfo(UserInfo info) {
        this.info = null;
        this.info = info;
        StringBuffer sb = new StringBuffer();
        if(!app.isOnLine()){
            sb.append("离线用户");
        }else{
            sb.append("昵称："+info.getUNickName()+"\n");
            sb.append("创生区块坐标：("+info.getCellX()+","+info.getCellY()+") \n");
            sb.append("创生位置描述："+info.getCrDesc()+" \n");
            sb.append("创生时间："+info.getCrTime()+" \n");
            sb.append("所属阵营：");
            if(info.getCampFlag()==0)
                sb.append("R阵营 \n");
            else if(info.getCampFlag()==1)
                sb.append("G阵营 \n");
            else if(info.getCampFlag()==1)
                sb.append("B阵营 \n");
            sb.append("性别：");
            if(info.getUSex()==-1)
                sb.append(" 隐藏 \n");
            else if(info.getUSex()==0)
                sb.append(" ♀ \n");
            else if(info.getUSex()==1)
                sb.append(" ♂ \n");
            if(info.getUBri()!=null&&!info.getUBri().equals(""))
                sb.append("生日："+info.getUBri()+"\n");
            if(info.getUSign()!=null&&!info.getUSign().equals(""))
                sb.append("签名："+info.getUSign()+"\n");
        }


        tvUserInfo.setText(sb.toString());
        Logger.d(sb.toString());

        showDialog[0]= true;
        if(showDialog[0]&&showDialog[1])
            cancelProgressDialog();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void updateUserMemChain(UserMessage msg) {

        if(msg.para==null) {
             Logger.d("get memchain error");
             return ;
        }

        if(msg.type==UserMessage.MEM_CHAIN_LIST_FROM_NETMEM)
            app.getLocMemory().insertMemChainsIntoDB((List<MemChain>)msg.para);

        showDialog[1]= true;
        if(showDialog[0]&&showDialog[1])
            cancelProgressDialog();

        mMemChains.clear();
        mChains.clear();
        if(!uId.equals(app.getNowUId())){
            for(MemChain c:(List<MemChain>)msg.para)
                if(MemChainTools.isShow(c))
                   mMemChains.add(c);
        }
        else
            mMemChains.addAll((List<MemChain>)msg.para);

        Map<String,Object> map = null;
        String tmp ;
        for(MemChain c :mMemChains){
            map = new HashMap<String,Object>();
            map.put("name","记忆链名："+c.getChName());
            map.put("detail","描述："+c.getChDetail());
            tmp = "碎片数:"+ MemChainTools.getFragmentCount(c)+
                    "  isSyn:"+MemChainTools.isSyn(c)+
                    "  isShow:"+MemChainTools.isShow(c)+
                    "  isShare:"+MemChainTools.isShare(c);
            map.put("state",tmp);
            map.put("sTime",c.getScTime());
            map.put("eTime",c.getSeTime());
            map.put("cTime","创建时间："+c.getChTime());
            mChains.add(map);
        }
        simpleAdapter.notifyDataSetChanged();
        Logger.json(new Gson().toJson(mMemChains));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void updateStatusMessage(MySystemCode code){
        cancelProgressDialog();
        if(code.errorCode==MySystemCode.UPLOAD_MEM_CHAIN_OK||
                code.errorCode==MySystemCode.CANCEL_UPLOAD_MEM_CHAIN_OK ) {
            MemChain chain = mMemChains.get(chooseIndex);
            app.getLocMemory().updateMemChain(chain);
            String tmp = "碎片数:"+ MemChainTools.getFragmentCount(chain)+
                    "  isSyn:"+MemChainTools.isSyn(chain)+
                    "  isShow:"+MemChainTools.isShow(chain)+
                    "  isShare:"+MemChainTools.isShare(chain);
            mChains.get(chooseIndex).put("state",tmp);
            simpleAdapter.notifyDataSetChanged();
        }else if(code.errorCode==MySystemCode.UPLOAD_MEM_CHAIN_ERR||
                code.errorCode==MySystemCode.CANCEL_UPLOAD_MEM_CHAIN_ERR ){
            showTextDialog("更新错误！");
            MemChainTools.setSyn(mMemChains.get(chooseIndex), tmpBools[0]);
            MemChainTools.setShow(mMemChains.get(chooseIndex), tmpBools[1]);
           // Logger.d(code.errorCode);
        }else if(code.errorCode==MySystemCode.UPLOAD_SHARE_BLOCK_OK||
                code.errorCode==MySystemCode.CANCEL_UPLOAD_SHARE_BLOCK_OK){
            MemChain chain = mMemChains.get(chooseIndex);
            MemChainTools.setShare(chain,!MemChainTools.isShare(chain));
            if(MemChainTools.isSyn(chain)){
                app.getMyHttpClient().uploadMemChain(chain);
            }else {
                app.getLocMemory().updateMemChain(chain);
                String tmp = "碎片数:"+ MemChainTools.getFragmentCount(chain)+
                        "  isSyn:"+MemChainTools.isSyn(chain)+
                        "  isShow:"+MemChainTools.isShow(chain)+
                        "  isShare:"+MemChainTools.isShare(chain);
                mChains.get(chooseIndex).put("state",tmp);
                simpleAdapter.notifyDataSetChanged();
            }


        }else if(code.errorCode==MySystemCode.UPLOAD_SHARE_BLOCK_ERR||
                code.errorCode==MySystemCode.CANCEL_UPLOAD_SHARE_BLOCK_ERR) {
            showTextDialog("分享错误！");
        }


    }

    protected  void askForDataForUpate(){
        showDialog[0]=showDialog[1]= false;
        if(info==null&&!uId.equals(app.getNowUId()))
        {
            showLoadDiaLog();
            app.getMyHttpClient().getUserInfo(uId);// 通过网络获得用户信息
        }
        else if(info==null&&uId.equals(app.getNowUId()))
            app.getLocMemory().getUserInfoById(uId);

        if(mMemChains.size()==0)
        {
            //处理流程见文档
            if(uId.equals(App.offUId)||uId.equals(app.getNowUId())){
                app.getLocMemory().getMemChainByUID(uId);//获得用户的记忆链，本地数据
            }
            else {
                app.getMyHttpClient().memChainDldByUid(uId);//获得网络同步数据
            }
        }else
            app.getLocMemory().getMemChainByUID(uId);
    }


    void showChooseShareDialog(){
        List<String> fTitles = new ArrayList<>();
        final List<String> fIds = new ArrayList<>();
        MemChainTools.synJson2List(mMemChains.get(chooseIndex));
        for(MemFragmentHead head:mMemChains.get(chooseIndex).getChContext()){
            if(FragmentTools.isVerif(head))
            {
                fTitles.add("记忆碎片："+head.getFTitle());
                fIds.add(head.getFId());
            }
        }
        String[] ss = new String[fTitles.size()];
        ss = fTitles.toArray(ss);
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("选择要分享哪一个碎片所在的区块？");
        ab.setItems(ss, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MemFragmentBody body =
                        app.getLocMemory().getMemFgtBodyDirc(fIds.get(which));
                String ceId = IDCreater.getCEID(body.getCellX(),body.getCellY());
                app.getMyHttpClient().uploadShareBlock(
                        ShareBlockTools.getShareBlock(mMemChains.get(chooseIndex),
                                app.getNowUNickName(),ceId)
                );
            }
        });
        ab.show();

    }

    void cancelShareDialog(){
        new AlertDialog.Builder(this).
                setMessage("是否取消记忆链<"+
                        mMemChains.get(chooseIndex).getChName()+">的分享？").
                setNegativeButton("取消",null).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ShareBlock shareBlock = new ShareBlock();
                shareBlock.setSId(mMemChains.get(chooseIndex).getChId());
                app.getMyHttpClient().cancelUploadShareBlock(shareBlock);
            }
        }).show();
    }

    void showTextDialog(String msg){
        new AlertDialog.Builder(this).
                setMessage(msg).setNegativeButton("确定",null).show();
    }

    void showLoadDiaLog(){
        cancelProgressDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载数据。。。");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    void showUpLoadingDialog(){
        cancelProgressDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("修改数据中。。。");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    void cancelProgressDialog(){
        if(progressDialog==null) return;
        progressDialog.dismiss();
        progressDialog =null;
    }

    boolean checkIsShareOk(MemChain chain){
        if(chain==null) return false;

        if(MemChainTools.getFragmentCount(chain)==0){
            showTextDialog("记忆链为空！");
            return false;
        }
        if(!MemChainTools.isSyn(chain)||!MemChainTools.isShow(chain)){
            showTextDialog("记忆链不可被分享！请确记忆链已同步和可被其他人查看！");
            return false;
        }
        if(MemChainTools.isDefaultChainNotLoc(chain)){
            //showTextDialog("默认列表不能被分享");
            return true;
        }
    return true;
    }



    @Override
    public void onBackPressed() {
        cancelProgressDialog();
        //finish();
        super.onBackPressed();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        uId = getIntent().getStringExtra("uId");
        uNickName = getIntent().getStringExtra("uNickName");
        isOp =getIntent().getBooleanExtra("isOp",true);

        if(!isOp){
            btnEditUserInfo.setVisibility(View.GONE);
            btnManageChain.setVisibility(View.GONE);
            btnAddChain.setVisibility(View.GONE);

        }
        toolbar.setTitle(uNickName);

        setSupportActionBar(toolbar);

        //toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        simpleAdapter = new SimpleAdapter(this,mChains,R.layout.item_userinfo_chains,
                new String[]{"name","detail","state","sTime","eTime","cTime"},
                new int[]{R.id.item_uinfo_chain_name,R.id.item_uinfo_chain_detail,
                        R.id.item_uinfo_chain_state,R.id.item_uinfo_chain_sTime,
                        R.id.item_uinfo_chain_eTime,R.id.item_uinfo_chain_cTime});

        lvMemChains.setAdapter(simpleAdapter);


        if(!app.isOnLine())//离线用户界面变化
        {
            btnEditUserInfo.setVisibility(View.GONE);
            btnAddChain.setVisibility(View.GONE);
            btnManageChain.setVisibility(View.GONE);
        }

        if(!uId.equals(app.getNowUId())) {//其他用户界面变化
            btnAddChain.setVisibility(View.GONE);
            btnEditUserInfo.setVisibility(View.GONE);
            btnManageChain.setVisibility(View.GONE);
        }

        longClickMem.add(cl[0]);
        longClickMem.add(cl[1]);
        arrayAdapter= new ArrayAdapter<String>(UserInfoActivity.this,
                android.R.layout.simple_list_item_1,longClickMem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        if(uId.equals(app.getNowUId()))
//            getMenuInflater().inflate(R.menu.menu_user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_user_setting) {
            Toast.makeText(UserInfoActivity.this,"设置",Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);
        askForDataForUpate();
        super.onResume();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        //EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
