package com.flyz.zwp.spacetime.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.flyz.zwp.spacetime.App;
import com.flyz.zwp.spacetime.R;
import com.flyz.zwp.spacetime.model.MemChain;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EActivity
public class EditChainActivity extends AppCompatActivity {


    @org.androidannotations.annotations.App
    App app;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.tv_edit_chain_msg)
    TextView tvMsg;

    String chId;

    MemChain mMemChain = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_chain);
         chId = getIntent().getStringExtra("CId");
        toolbar.setTitle(chId);
        setSupportActionBar(toolbar);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void showMsg(MemChain chain){
        String s = new Gson().toJson(chain);
        tvMsg.setText(s);
        mMemChain = chain;
        Logger.json(s);
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }


    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);
        if(mMemChain==null)
          app.getLocMemory().getMemChain(chId);
        super.onResume();
    }
}
