package com.flyz.zwp.spacetime.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.flyz.zwp.spacetime.App;
import com.flyz.zwp.spacetime.R;
import com.flyz.zwp.spacetime.componet.ChainMangeAdapter;
import com.flyz.zwp.spacetime.componet.MemChainTools;
import com.flyz.zwp.spacetime.messages.UserMessage;
import com.flyz.zwp.spacetime.model.ItemChain;
import com.flyz.zwp.spacetime.model.MemChain;
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
public class ManageChainActivity extends AppCompatActivity {

    private List<ItemChain> mChains = new ArrayList<ItemChain>();
    private List<MemChain> mMemChains = new ArrayList<MemChain>();
    private HashMap<Integer,Boolean> isSelected = new HashMap<Integer, Boolean>();
    private ChainMangeAdapter adapter ;
    private String uId;
    private int checkedCount =0;
    @org.androidannotations.annotations.App
    App app;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.btn_mag_chain_delete)
    Button btnDelete;

    @ViewById(R.id.lv_mag_chain)
    ListView lvChains;

    @ItemClick(R.id.lv_mag_chain)
    void itemClick(int pos){
        if(mMemChains.get(pos).getChId().equals(app.getDefaultChainId())){
            Toast.makeText(ManageChainActivity.this,"默认列表不可被选中！",Toast.LENGTH_SHORT).
                    show();
            return;
        }
        if(isSelected.get(pos)) {
            isSelected.put(pos,false);
            checkedCount--;
        }
        else {
            isSelected.put(pos,true);
            checkedCount++;
        }
        if(checkedCount==0)
            btnDelete.setEnabled(false);
        else
            btnDelete.setEnabled(true);
        mChains.get(pos).setSelected(isSelected.get(pos));
        Logger.d(mChains.get(pos).isSelected());
        adapter.notifyDataSetChanged();
    }

    @Click(R.id.btn_mag_chain_delete)
    void deleteChains(){
        List<MemChain> chains = new ArrayList<MemChain>();
            for(int i =0;i<isSelected.size();i++){
                if(isSelected.get(i)){
                    if(MemChainTools.isShare(mMemChains.get(i))) {
                        new AlertDialog.Builder(this).
                                setMessage("存在分享中的记忆链").setPositiveButton("确定",null).
                                show();
                        return;
                    }
                    chains.add(mMemChains.get(i));
                }

            }
        app.getLocMemory().deleteMemChainsByChId(chains);
        finish();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    void updateUserMemChain(UserMessage msg) {
        if(msg.para==null) {
            Logger.d("get memchain error");
            return ;
        }
        mMemChains.clear();
        mChains.clear();
        isSelected.clear();
        mMemChains.addAll((List<MemChain>)msg.para);
       ItemChain cc = null;
        String tmp ;
        int i=0;
        for(MemChain c :mMemChains){
            cc = new ItemChain();
            cc.setName("记忆链名："+c.getChName());
            cc.setDetail("描述："+c.getChDetail());
            tmp = "碎片数:"+ MemChainTools.getFragmentCount(c)+
                    "  isSyn:"+MemChainTools.isSyn(c)+
                    "  isShow:"+MemChainTools.isShow(c)+
                    "  isShare:"+MemChainTools.isShare(c);
            cc.setState(tmp);
            cc.setsTime(c.getScTime());
            cc.seteTime(c.getSeTime());
            cc.setcTime("创建时间："+c.getChTime());
            cc.setSelected(false);
            isSelected.put(i++,false);
            mChains.add(cc);
        }
        adapter.notifyDataSetChanged();
        Logger.json(new Gson().toJson(mMemChains));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_chain);
        setSupportActionBar(toolbar);
        adapter =new ChainMangeAdapter(this,mChains);
        lvChains.setAdapter(adapter);

        uId = getIntent().getStringExtra("uId");
        btnDelete.setEnabled(false);
    }


    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);
        app.getLocMemory().getMemChainByUID(uId);
        super.onResume();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }
}
