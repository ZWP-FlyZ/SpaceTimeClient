package com.flyz.zwp.spacetime.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.flyz.zwp.spacetime.App;
import com.flyz.zwp.spacetime.R;
import com.flyz.zwp.spacetime.model.MySystemCode;
import com.flyz.zwp.spacetime.model.UserInfo;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EActivity
public class EditUserActivity extends AppCompatActivity {


    String uId;
    String uNickName;

    int tmpSxe=-1;

    @org.androidannotations.annotations.App
    App app;

    @ViewById(R.id.edit_user_edit_name)
    EditText editName;
    @ViewById(R.id.edit_user_edit_bri)
    EditText editBri;
    @ViewById(R.id.edit_user_edit_sign)
    EditText editSign;

    @ViewById(R.id.rg_edit_user_sex)
    RadioGroup radioGroupSex;



    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    UserInfo userInfo = null;

    @Subscribe(threadMode = ThreadMode.MAIN)
    void upUserInfo(UserInfo info) {
        userInfo = info;
        editBri.setText(info.getUBri());
        editName.setText(info.getUNickName());
        if(info.getUSex()==-1)
            radioGroupSex.check(R.id.rb_edit_user_sex_hind);
        else if(info.getUSex()==0)
            radioGroupSex.check(R.id.rb_edit_user_sex_woman);
        else if(info.getUSex()==1)
            radioGroupSex.check(R.id.rb_edit_user_sex_man);
        editSign.setText(info.getUSign());
        Logger.d(new Gson().toJson(info));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void getSystemCode(MySystemCode code) {
        if(code.errorCode==MySystemCode.UPDATE_USER_INFO_OK){
            Toast.makeText(EditUserActivity.this,"完成",Toast.LENGTH_SHORT).show();
            finish();
        }else if(code.errorCode==MySystemCode.UPDATE_USER_INFO_ERR)
        {

        }else {

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        uId = getIntent().getStringExtra("uId");
        uNickName = getIntent().getStringExtra("uNickName");
        toolbar.setTitle("编辑个人信息");
        setSupportActionBar(toolbar);

        editName.setEnabled(false);
        //toolbar.setNavigationIcon(R.mipmap.ic_launcher);
    }


    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        app.getLocMemory().getUserInfoById(uId);
        //app.getLocMemory().getUserInfoById(app.getNowUId());
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_fgt, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String tmp;
        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_memfgt) {
            tmp = editBri.getText().toString();
            if(tmp.equals(""))
                userInfo.setUBri(null);
            else
                userInfo.setUBri(tmp);

            tmp = editSign.getText().toString();
            if(tmp.equals(""))
                userInfo.setUSign(null);
            else
                userInfo.setUSign(tmp);

             if(radioGroupSex.getCheckedRadioButtonId()==R.id.rb_edit_user_sex_man)
                userInfo.setUSex(1);
            else if(radioGroupSex.getCheckedRadioButtonId()==R.id.rb_edit_user_sex_woman)
                userInfo.setUSex(0);
            else
                userInfo.setUSex(-1);
            //userInfo.setUNickName(editName.getText().toString());
            app.getLocMemory().updateUserInfo(userInfo);
            app.getMyHttpClient().updateUserInfo(userInfo);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        setResult(101001,null);
        super.onBackPressed();
    }
}
