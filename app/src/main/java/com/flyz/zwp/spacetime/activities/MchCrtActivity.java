package com.flyz.zwp.spacetime.activities;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import com.flyz.zwp.spacetime.App;
import com.flyz.zwp.spacetime.R;
import com.flyz.zwp.spacetime.componet.MemChainTools;
import com.flyz.zwp.spacetime.model.MemChain;
import com.flyz.zwp.spacetime.model.MySystemCode;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


@EActivity
public class MchCrtActivity extends AppCompatActivity {


    String uId;//用户id


    @org.androidannotations.annotations.App
    App app;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.et_mch_crt_mchname)
    EditText etMchName;
    @ViewById(R.id.et_mch_crt_mchdetail)
    EditText etMchDetail;

    @ViewById(R.id.cb_mch_crt_syn)
    CheckBox cbSyn;
    @ViewById(R.id.cb_mch_crt_show)
    CheckBox cbShow;


    @Subscribe(threadMode = ThreadMode.MAIN)
    void updateStatusMessage(MySystemCode code){
        if(code.errorCode==MySystemCode.UPLOAD_MEM_CHAIN_OK)
           finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mch_crt);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        uId = i.getStringExtra("uId");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_fgt, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_memfgt) {
            String name = etMchName.getText().toString();
            String detail = etMchDetail.getText().toString();

            MemChain c = MemChainTools.createMemChain(uId,name,detail,
                        MemChainTools.createMemChainFlag(cbSyn.isChecked(),cbShow.isChecked()));
            Logger.json(new Gson().toJson(c));
            app.getLocMemory().insertMemChainUnRep(c);
            Bundle b = new Bundle();
            b.putString("chId",c.getChId());
            setResult(1111,new Intent().putExtras(b));

            if(MemChainTools.isSyn(c))
                app.getMyHttpClient().uploadMemChain(c);
            else
                finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
