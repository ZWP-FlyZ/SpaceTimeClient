package com.flyz.zwp.spacetime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import org.androidannotations.annotations.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import com.baidu.platform.comapi.map.B;
import com.flyz.zwp.spacetime.model.BodyItem;
import com.flyz.zwp.spacetime.model.MemFragmentBody;
import com.flyz.zwp.spacetime.model.TestMessage;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zwp12 on 2017/2/13.
 */



@EActivity
public class MyActivity extends AppCompatActivity {


    private static int i =0;

    @ViewById(R.id.textview)
    TextView tv;

    @ViewById(R.id.button)
    Button btn ;

    @org.androidannotations.annotations.App
    App app ;

    private UserDao userDao =null;
    @AfterInject
    void aftInject()
    {
        userDao = app.getDaoSession().getUserDao();
        i =  userDao.queryBuilder().where(UserDao.Properties.Id.gt(-1)).list().size();
    }

    @Click(R.id.button)
    void myButtonClick()
    {
        MyEventMessage m = new MyEventMessage();
        m.messgage = ""+i++;
        Logger.d("hello "+i);
        User user = new User(null,"uId"+i,"uNickname"+i,"uPassword"+i,i);
        userDao.insert(user);

        //app.myHttpClient.getBaiduTest();

        EventBus.getDefault().post(m);
        EventBus.getDefault().post("hello evevnt buss");
      //  asyHttpTest();


        app.myLocationClient.getLocation();

        jsonTest();
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_layout);

    }


   public  class MyEventMessage
    {
        String messgage;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mySubscribe(TestMessage msg)
    {
       List<User> users =
               userDao.queryBuilder().where(UserDao.Properties.Id.gt(-1)).list();
        StringBuffer sb = new StringBuffer();
        for(User u :users)
        {
            sb.append("-------------------------------\n");
            sb.append(" id = "+u.getId());
            sb.append(" uId = "+u.getuId());
            sb.append(" uName = "+u.getUNickName());
            sb.append(" uPassword = "+u.getuPassword());
            sb.append(" Age ="+u.getuAge());
            sb.append("\n-------------------------------\n");
        }
        tv.setText(msg.message);
    }



    public void jsonTest()
    {
//        MemFragmentBody fragmentBody = new MemFragmentBody();
//        fragmentBody.setfId("fragment_usdwdf2132sdf");
//        ArrayList<BodyItem> arrayList = new ArrayList<BodyItem>();
//        arrayList.add(new BodyItem(0,"test1"));
//        arrayList.add(new BodyItem(1,"http://aass"));
//        arrayList.add(new BodyItem(2,"test1sff"));
//        arrayList.add(new BodyItem(3,"test1"));
//        fragmentBody.setBodyItemList(arrayList);
//        Gson gson = new Gson();
//        String s = gson.toJson(fragmentBody);
//        Logger.json(s);
//        MemFragmentBody mm = gson.fromJson(s,MemFragmentBody.class);
//        Logger.d(mm.getBodyItemList());

    }

}
