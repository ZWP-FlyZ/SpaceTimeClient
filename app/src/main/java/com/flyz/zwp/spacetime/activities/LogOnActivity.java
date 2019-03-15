package com.flyz.zwp.spacetime.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flyz.zwp.spacetime.App;
import com.flyz.zwp.spacetime.R;
import com.flyz.zwp.spacetime.componet.Encrypt;
import com.flyz.zwp.spacetime.messages.LogMessage;
import com.flyz.zwp.spacetime.model.MyCellLocation;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EActivity
public class LogOnActivity extends AppCompatActivity {


    private MyCellLocation nowLoc =null;


    @org.androidannotations.annotations.App
    App app;
    @ViewById(R.id.edit_logon_unickname)
    EditText editLogOnName;
    @ViewById(R.id.edit_logon_upassword)
    EditText editLogOnPassword;
    @ViewById(R.id.edit_logon_upassword_r)
    EditText editLogOnPasswordR;
    @ViewById(R.id.btn_logon_logon)
    Button btnLogOnLogOn;
    @ViewById(R.id.tv_logon_locinfo)
    TextView tvLocation;

    @Click(R.id.btn_logon_logon)
     void logOnBtnAction() {
        String uNickName = editLogOnName.getText().toString();
        String uPassword = editLogOnPassword.getText().toString();
        String uPasswordR = editLogOnPasswordR.getText().toString();
        if(!uPassword.equals(uPasswordR))
        {
            Toast.makeText(LogOnActivity.this,"密码不同！",Toast.LENGTH_SHORT).show();
            return;
        }
        uPasswordR = Encrypt.EPassword(uPassword);
        LogMessage msg = new LogMessage();
        msg.uNickName = uNickName;
        msg.uPassword = uPassword;
        msg.uPasswordE = uPasswordR;
        msg.cellX = nowLoc.getmCellX();
        msg.cellY = nowLoc.getmCellY();
        msg.crDesc = nowLoc.getmLocDescribe();

        app.getMyHttpClient().logonUser(msg);
        btnLogOnLogOn.setEnabled(false);
        btnLogOnLogOn.setText("注册中。。。");
    }

    @FocusChange(R.id.edit_logon_unickname)
    void foucusChangeNickName(boolean hasFouces) {
        if(!hasFouces)
          app.getMyHttpClient().checkNickName(editLogOnName.getText().toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void logOnSubscriber(LogMessage msg) {

        btnLogOnLogOn.setEnabled(true);
        btnLogOnLogOn.setText("注册");

        if(msg.errorCode== LogMessage.LOGON_USER_NAME_ERR)
            Toast.makeText(LogOnActivity.this,"用户名不可用！",Toast.LENGTH_SHORT).show();
        else  if(msg.errorCode== LogMessage.LOGON_USER_NAME_OK)
            Toast.makeText(LogOnActivity.this,"用户名可用！",Toast.LENGTH_SHORT).show();
        else  if(msg.errorCode== LogMessage.LOGON_REG_ERR)
            Toast.makeText(LogOnActivity.this,"注册出错！",Toast.LENGTH_SHORT).show();
        else  if(msg.errorCode== LogMessage.LOGON_REG_OK){
            Toast.makeText(LogOnActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
            Logger.d(msg.uId);
            Intent i = new Intent();
            Bundle b  = new Bundle();
            b.putString("uNickName",
                    msg.uNickName);
            b.putString("uPassword",
                    msg.uPassword);
            setResult(1024, i.putExtras(b));
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void getLocation(MyCellLocation loc) {
            nowLoc = loc;
        tvLocation.setText(loc.getmLocDescribe());
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_on);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if(nowLoc==null)
            app.getLoctionClient().getLocation();

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
