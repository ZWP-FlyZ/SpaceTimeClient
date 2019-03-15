package com.flyz.zwp.spacetime.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.flyz.zwp.spacetime.App;
import com.flyz.zwp.spacetime.R;
import com.flyz.zwp.spacetime.componet.FgtManageAdapter;
import com.flyz.zwp.spacetime.componet.FragmentTools;
import com.flyz.zwp.spacetime.componet.MemChainTools;
import com.flyz.zwp.spacetime.messages.UserMessage;
import com.flyz.zwp.spacetime.model.MemChain;
import com.flyz.zwp.spacetime.model.MemFragmentBody;
import com.flyz.zwp.spacetime.model.MemFragmentHead;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity
public class ManageFgtActivity extends AppCompatActivity {

    private List<MemFragmentHead> mMemFregentHeads;
    private FgtManageAdapter adapter = null;
    private ArrayList<HashMap<String,Object>> mHeads = new ArrayList<HashMap<String,Object>>();
    private Map<Integer,Boolean> isSelected = new HashMap<Integer,Boolean>();
    private MemChain memChain = null;
    private int checkedCount= 0;
    private String chId;
    private boolean needUpdateView = true;

    // add to chain?
    private ArrayAdapter<String> arrayAdapter;
    private List<String> mChainNames= new ArrayList<>();
    private List<MemChain> mChains;

    @org.androidannotations.annotations.App
    App app;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.lv_mag_fgt)
    ListView lvFgts;

    @ViewById(R.id.btn_mag_fgt_delete)
    Button btnDelete;

    @ViewById(R.id.btn_mag_fgt_addto)
    Button btnAddto;

    @ItemClick(R.id.lv_mag_fgt)
    void itemClick(int pos){
        if(isSelected.get(pos)){
            checkedCount--;
            isSelected.put(pos,false);
        }else {
            checkedCount++;
            isSelected.put(pos,true);
        }
        if(checkedCount==0){
            btnAddto.setEnabled(false);
            btnDelete.setEnabled(false);
        }else{
            btnAddto.setEnabled(true);
            btnDelete.setEnabled(true);
        }
        mHeads.get(pos).put("isSelected",isSelected.get(pos));
        adapter.notifyDataSetChanged();
    }

    @Click(R.id.btn_mag_fgt_delete)
    void fgtsDelete(){
        List<MemFragmentHead> heads  = new ArrayList<MemFragmentHead>();
       //
        for(int i=0;i<isSelected.size();i++){
            if(isSelected.get(i)){
                if(FragmentTools.isShare(mMemFregentHeads.get(i))) {
                    new AlertDialog.Builder(this).
                            setMessage("存在分享中的记忆碎片！删除操作取消。")
                            .setPositiveButton("确定",null).
                            show();
                    return;
                }
                heads.add(mMemFregentHeads.get(i));
            }

        }

        if(chId.equals(app.getDefaultChainId())){
            //delete memhead , body  fgts in chain
            //delete other
            //app.getLocMemory().deleteMemFgtHeadsByFId(heads);//db heads
            app.getLocMemory().deleteMemFgtBodysByHeads(heads);
        }


        MemChainTools.deleteFrgmentFromChain(memChain,heads);// reset chain
        app.getLocMemory().updateMemChain(memChain);// update chain
        app.getLocMemory().getMemChain(chId);//update view
    }

    @Click(R.id.btn_mag_fgt_addto)
    void fgtsAddto(){
        app.getLocMemory().getMemChainByUID(app.getNowUId());
    }





    @Subscribe(threadMode = ThreadMode.MAIN)
    void showChainInfo(MemChain chain){
        checkedCount = 0;
        memChain = chain;
        btnDelete.setEnabled(false);
        btnAddto.setEnabled(false);

        MemChainTools.synJson2List(memChain);
        setListViewData(memChain.getChContext());
        adapter.notifyDataSetChanged();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void updateUserMemChain(UserMessage msg) {
        if(msg.para==null) {
            Logger.d("get memchain error");
            return ;
        }
        mChainNames.clear();
        mChains = (List<MemChain>)msg.para;
        for(MemChain c:mChains){
            mChainNames.add(c.getChName());
        }
        arrayAdapter.notifyDataSetChanged();

        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("选中要添加的记忆链！");
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Logger.d(mChainNames.get(which));
                String tmpChId = mChains.get(which).getChId();
                if(tmpChId.equals(app.getDefaultChainId()))
                    return ;
                List<MemFragmentHead> heads  = new ArrayList<MemFragmentHead>();
                for(int i=0;i<isSelected.size();i++)
                    if(isSelected.get(i))
                        heads.add(mMemFregentHeads.get(i));
                for(MemFragmentHead h:heads){
                    MemFragmentHead tmpHead =  h.clone();//?
                    MemChainTools.addFrgmentToChain(mChains.get(which),tmpHead);

                }
                app.getLocMemory().updateMemChain(mChains.get(which));
                if(MemChainTools.isSyn(mChains.get(which)))
                    app.getMyHttpClient().uploadMemChain(mChains.get(which));


            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }



    void setListViewData(List<MemFragmentHead> heads){
        mHeads.clear();
        isSelected.clear();
        mMemFregentHeads = heads;
        if(heads==null||heads.size()==0) return;
        String tmp ;
        int i = 0;
        HashMap<String,Object> map = null;
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
            map.put("isSelected",false);
            isSelected.put(i++,false);
            mHeads.add(map);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_fgt);
        setSupportActionBar(toolbar);
        adapter = new FgtManageAdapter(this,mHeads);
        lvFgts.setAdapter(adapter);
        chId  = getIntent().getStringExtra("chId");

        arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mChainNames);
        btnDelete.setEnabled(false);
        btnAddto.setEnabled(false);
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {
         app.getLocMemory().getMemChain(chId);
        super.onResume();
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
