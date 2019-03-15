package com.flyz.zwp.spacetime.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.flyz.zwp.spacetime.App;
import com.flyz.zwp.spacetime.R;
import com.flyz.zwp.spacetime.componet.FragmentTools;
import com.flyz.zwp.spacetime.componet.MemChainTools;
import com.flyz.zwp.spacetime.componet.MyConversion;
import com.flyz.zwp.spacetime.componet.MyTime;
import com.flyz.zwp.spacetime.model.BodyItem;
import com.flyz.zwp.spacetime.model.MemChain;
import com.flyz.zwp.spacetime.model.MemFragmentBody;
import com.flyz.zwp.spacetime.model.MemFragmentHead;
import com.flyz.zwp.spacetime.model.MyCellLocation;
import com.flyz.zwp.spacetime.model.MySystemCode;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;


import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@EActivity
public class FgtCrtActivity extends AppCompatActivity {




    final static int REQUEST_CODE = 1110;
    final static int REQUEST_CODE_2 = 10101;
    private MyCellLocation nowLoc = null;
    private MyCellLocation choseLoc = null;
    String title;
    boolean isVerif = false;

    private List<String> datas = new ArrayList<String>();
    private ArrayAdapter arrayAdapter ;
    private MyTime nowTime;

    private ProgressDialog progressDialog = null;

    @org.androidannotations.annotations.App
    App app;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.edit_fgt_crt_title)
    EditText editFgtTile;
    @ViewById(R.id.edit_fgt_crt_content)
    EditText editFgtContent;
    @ViewById(R.id.tv_fgt_crt_time)
    TextView tvFgtTime;
    @ViewById(R.id.tv_fgt_crt_locdesc)
    TextView tvFgtlocdesc;

    @ViewById(R.id.tv_fgt_crt_verif)
    TextView tvFgtlocverif;

    @ViewById(R.id.btn_fgt_crt_add)
    Button btnFgtAdd;
    @ViewById(R.id.lv_fgt_crt_more_info)
    ListView lvFgtMoreInfo;

    @ViewById(R.id.cb_fgt_crt_syn)
    CheckBox cbSyn;
    @ViewById(R.id.cb_fgt_crt_show)
    CheckBox cbShow;

    @Click(R.id.tv_fgt_crt_locdesc)
    void getLoction() {
       // app.getLoctionClient().getLocation();
        if(nowLoc==null||nowTime==null){
            Toast.makeText(FgtCrtActivity.this,"等待定位数据！",Toast.LENGTH_LONG).show();
            return;
        }
        Intent i = new Intent();
        i.putExtra("lat",choseLoc.getmOriginLat());
        i.putExtra("lng",choseLoc.getmOriginLng());
        i.putExtra("desc",choseLoc.getmLocDescribe());
        i.putExtra("type",choseLoc.getCoFLag());
        i.setClass(FgtCrtActivity.this,LocChoseActivity_.class);
        startActivityForResult(i,REQUEST_CODE_2);
    }
    @Click(R.id.btn_fgt_crt_add)
    void updateFiles() {
        datas.add("file"+datas.size());
        arrayAdapter.notifyDataSetChanged();
    }

    @Click(R.id.tv_fgt_crt_time)
    void timeCrt(){
        if(nowTime==null)return ;
        Intent i = new Intent();
        i.setClass(FgtCrtActivity.this,TimeCrtActivity_.class);
        i.putExtra("tFlag",nowTime.getTimeFlag());
        i.putExtra("detail",nowTime.getTimeDetail());
        startActivityForResult(i,REQUEST_CODE);
    }

    @OnActivityResult(REQUEST_CODE)
    void onResult(@OnActivityResult.Extra(value = "tFlag") int tFlag ,
                  @OnActivityResult.Extra(value = "detail") String detail) {
        nowTime = MyTime.getMyTimeByDetail(tFlag,detail);
        tvFgtTime.setText(nowTime.toDescribe());

    }

    @OnActivityResult(REQUEST_CODE_2)
    void onResultForLocation(@OnActivityResult.Extra(value = "desc") String desc ,
                             @OnActivityResult.Extra(value = "lat") Double lat,
                             @OnActivityResult.Extra(value = "lng") Double lng,
                             @OnActivityResult.Extra(value = "type") Integer type) {
       choseLoc = MyConversion.latlng2Mct(lat,lng);
        choseLoc.setmLocDescribe(desc);
        choseLoc.setCoFLag(type);
        tvFgtlocdesc.setText(desc);
    }

    @TextChange(R.id.tv_fgt_crt_locdesc)
    void locChanged(){
        isVerif = false;
        tvFgtlocverif.setText("验证未通过!");
        if(tvFgtlocverif==null||tvFgtlocverif.equals(""))return ;

        if(nowLoc.getmCellX()==choseLoc.getmCellX()&&
                nowLoc.getmCellY()==choseLoc.getmCellY())
        {
            tvFgtlocverif.setText("可通过定位验证的碎片!");
            isVerif = true;
        }
    }


    @ItemClick(R.id.lv_fgt_crt_more_info)
    void itemClick(int pos) {
        datas.remove(pos);
        arrayAdapter.notifyDataSetChanged();
        Logger.d(datas.get(pos));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    void getLocMessage(MyCellLocation loc) {

        choseLoc = nowLoc = loc;
        if(nowTime==null) nowTime = MyTime.buildNowTime();
        tvFgtlocdesc.setText(loc.getmLocDescribe());
        tvFgtTime.setText(nowTime.toDescribe());
        Logger.json(new Gson().toJson(loc));
        //Toast.makeText(FgtCrtActivity.this,"location update",Toast.LENGTH_SHORT).show();
        cancelProgressDialog();
    }


    void showProgressDialog(){
        cancelProgressDialog();
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

    void showUploadingDialog(){
        cancelProgressDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("更新数据！");
        progressDialog.setMessage("等待中...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    void updateStatusMessage(MySystemCode code){
        if(code.errorCode==MySystemCode.UPLOAD_MEM_CHAIN_OK)
            return ;

        if(code.errorCode==MySystemCode.UPLOAD_MEM_BODY_OK){
           cancelProgressDialog();
            finish();
        }else
            cancelProgressDialog();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fgt_crt);

        toolbar.setTitle("new fragment");
        setSupportActionBar(toolbar);

        arrayAdapter = new ArrayAdapter<String>(FgtCrtActivity.this,
                android.R.layout.simple_expandable_list_item_1,datas);
        lvFgtMoreInfo.setAdapter(arrayAdapter);
    }



    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);
        if(nowLoc ==null ) {
            app.getLoctionClient().getLocation();
            showProgressDialog();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_fgt, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_memfgt) {
            if(nowLoc==null||nowTime==null)
                Toast.makeText(FgtCrtActivity.this,"等待定位数据！",Toast.LENGTH_LONG).show();
            else{
                String fTile = editFgtTile.getText().toString();
                String fContent = editFgtContent.getText().toString();
                List<BodyItem> items = new ArrayList<BodyItem>();
                items.add(new BodyItem(FragmentTools.BodyType.TEXT,fContent));
                MemFragmentBody body =
                        FragmentTools.getMemFgtBody(app.getNowUId(),nowTime,choseLoc,fTile,items);
                MemFragmentHead head =
                        FragmentTools.getMemFgtHead(body,FragmentTools.
                                 getfFlag(isVerif,cbSyn.isChecked(),cbShow.isChecked()));
                MemChain chainDefault = app.getLocMemory().getDefaultChain(app.getNowUId());
                MemChainTools.addFrgmentToChain(chainDefault,head);

                if(cbSyn.isChecked())
                    app.getMyHttpClient().uploadMemBody(body);
                app.getMyHttpClient().uploadMemChain(chainDefault);

                showUploadingDialog();
                app.getLocMemory().updateMemChain(chainDefault);
                app.getLocMemory().insertMemBodyIntoDB(body);
                //app.getLocMemory().insertMemHeadIntoDB(head);

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
