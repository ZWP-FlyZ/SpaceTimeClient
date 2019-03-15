package com.flyz.zwp.spacetime.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.flyz.zwp.spacetime.App;
import com.flyz.zwp.spacetime.R;
import com.flyz.zwp.spacetime.componet.Encrypt;
import com.flyz.zwp.spacetime.messages.LogMessage;
import com.flyz.zwp.spacetime.messages.UserMessage;
import com.flyz.zwp.spacetime.model.MemChain;
import com.flyz.zwp.spacetime.model.MemFragmentBody;
import com.flyz.zwp.spacetime.model.MySystemCode;
import com.flyz.zwp.spacetime.model.UserInfo;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


@EActivity
public class LogInActivity extends AppCompatActivity {

    public final static int REQUEST_CODE = 1024;
    public final static int RESULT_CODE  = 4201;

    private ProgressDialog progressDialog =null;

    private UserInfo userInfo =null;

    String uNickName;
    String uPass;
    @org.androidannotations.annotations.App
    App app;

    @ViewById(R.id.edit_login_unickename)
    EditText editLogInName;
    @ViewById(R.id.edit_login_upassword)
    EditText editLogInPassword;

    @ViewById(R.id.btn_login_login)
    Button btnLogInLogIn;
    @ViewById(R.id.btn_login_logon)
    Button btnLogInLogOn;


    @Click(R.id.btn_login_login)
    void logInButtonAction() {

        uNickName =  editLogInName.getText().toString();
        uPass = editLogInPassword.getText().toString();
        if(uNickName.equals("")||uPass.equals("")) {
            return;
        }
        LogMessage msg = new LogMessage();
        msg.uNickName =uNickName;
        msg.uPassword = uPass;
        msg.uPasswordE = Encrypt.EPassword(msg.uPassword);
        app.getMyHttpClient().login(msg);

        showLogInDialog();
        Logger.d("Login - login "+  msg.uNickName);
    }

    @Click(R.id.btn_login_logon)
    void logOnButtonAction() {
        startActivityForResult(new Intent().setClass(LogInActivity.this,LogOnActivity_.class),
                REQUEST_CODE);
        Logger.d("Login - logon ");
    }

    @OnActivityResult(REQUEST_CODE)
    void onResult(@OnActivityResult.Extra(value = "uNickName") String uNickName ,
                  @OnActivityResult.Extra(value = "uPassword") String uPassword)
    {
        this.editLogInName.setText(uNickName);
        this.editLogInPassword.setText(uPassword);
        //logInButtonAction();
        Logger.d("onActivityResult\n"+uNickName+"\n"+uPassword);
    }

    void showLogInDialog(){
        cancelProgressDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("登录中。。。");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    void showFirstlyLogInDiaLog(){
        cancelProgressDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在处理登录后的一些工作。。");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    void cancelProgressDialog(){
        if(progressDialog==null) return;
        progressDialog.dismiss();
        progressDialog =null;
    }


    void afterLogIn(){
        SharedPreferences sp = getPreferences(LogInActivity.MODE_PRIVATE);
        int cot = sp.getInt(userInfo.getUId()+"_loginC",0);

       if(cot==0){
           showFirstlyLogInDiaLog();
           app.getLocMemory().insertUserInfo(userInfo);
           app.getMyHttpClient().memChainDldByUid(userInfo.getUId());

        //syn some data
       }
       else {
           app.getLocMemory().
                   spaceTimePrefs.edit()
                   .uNickName().put(userInfo.getUNickName())
                   .uId().put(userInfo.getUId())
                   .uPasswordTemp().put(uPass)
                   .apply();
           app.setLogInC(cot);
           app.setNowUId(userInfo.getUId());
           app.setNowUNickName(userInfo.getUNickName());
           sp.edit().putInt(userInfo.getUId()+"_loginC",cot+1).commit();
           Intent i = new Intent();
           Bundle bundle = new Bundle();
           bundle.putString("log","OK");
           setResult(102, i.putExtras(bundle));

           finish();
       }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    void updateStatusMessage(MySystemCode code){
        if(code.errorCode==MySystemCode.UPLOAD_MEM_CHAIN_OK){
            cancelProgressDialog();
            otherOp();
            Intent i = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("log","OK");
            setResult(102, i.putExtras(bundle));
            finish();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    void logInSubscribe(LogMessage msg) {
        cancelProgressDialog();
        switch(msg.errorCode)
        {
            case LogMessage.LOGIN_PASS_ERR:
                Toast.makeText(LogInActivity.this,"密码或用户名错误！",Toast.LENGTH_SHORT).show();
                break ;
            case LogMessage.LOGIN_UN_ERR:
                Toast.makeText(LogInActivity.this,"未知错误！",Toast.LENGTH_SHORT).show();
                break ;
            case LogMessage.LOGIN_OK:
               // Logger.d(msg.uId);
                userInfo = msg.userInfo;
                afterLogIn();
                break;
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void logInUserChains(UserMessage msg){
        //cancelProgressDialog();

        if(msg.type==UserMessage.MEM_CHAIN_LIST_FROM_NETMEM){
            List<MemChain> chains =( List<MemChain>) msg.para;
            if(chains==null||chains.size()==0){
                MemChain c = app.getLocMemory().checkDefaultChainOrAdd(userInfo.getUId());
                app.getMyHttpClient().uploadMemChain(c);
                //cancelProgressDialog();
            }
            else {
                app.getLocMemory().insertMemChainsIntoDB(chains);
                app.getMyHttpClient().memBodyDldByUid(userInfo.getUId());
            }
        }else if(msg.type==UserMessage.MEM_BODY_LIST_FROM_NETMEM){
            List<MemFragmentBody> bodies = (List<MemFragmentBody>)msg.para;
            if(bodies!=null&&bodies.size()!=0)
                app.getLocMemory().insertMemBodiesIntoDB(bodies);
            cancelProgressDialog();
            otherOp();
            Intent i = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("log","OK");
            setResult(102, i.putExtras(bundle));
            finish();
        }
    }

    void otherOp(){
        SharedPreferences sp = getPreferences(LogInActivity.MODE_PRIVATE);
        int cot = sp.getInt(userInfo.getUId()+"_loginC",0);
        app.setLogInC(cot);
        app.setNowUId(userInfo.getUId());
        app.setNowUNickName(userInfo.getUNickName());
        sp.edit().putInt(userInfo.getUId()+"_loginC",cot+1).commit();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        uNickName = app.getLocMemory().spaceTimePrefs.uNickName().get();
        uPass = app.getLocMemory().spaceTimePrefs.uPasswordTemp().get();

        editLogInName.setText(uNickName);
        editLogInPassword.setText(uPass);

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

    @Override
    protected void onDestroy() {
        Logger.d("destroy");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("log","NO");
        setResult(102, i.putExtras(bundle));
        super.onBackPressed();
    }
}
