package com.flyz.zwp.spacetime.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.flyz.zwp.spacetime.App;
import com.flyz.zwp.spacetime.R;
import com.flyz.zwp.spacetime.componet.FragmentTools;
import com.flyz.zwp.spacetime.componet.MyConversion;
import com.flyz.zwp.spacetime.messages.MemBodyMessage;
import com.flyz.zwp.spacetime.model.MemFragmentBody;
import com.flyz.zwp.spacetime.model.MyCellLocation;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@EActivity
public class MemFragmentActivity extends AppCompatActivity {


    String fId;
    String fTitle;
    String uId;
    ProgressDialog progressDialog;

    @org.androidannotations.annotations.App
    App app;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.tv_mem_fgt_title)
    TextView tvfTitle;
    @ViewById(R.id.tv_mem_fgt_ftdetail)
    TextView tvftDetail;
    @ViewById(R.id.tv_mem_fgt_fpdetail)
    TextView tvfpDetail;
    @ViewById(R.id.tv_mem_fgt_ctime)
    TextView tvcTime;
    @ViewById(R.id.tv_mem_fgt_content)
    TextView tvContent;

    @ViewById(R.id.lv_mem_fgt_files)
    ListView lvFiles;

    @ViewById(R.id.bmapView)
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_fragment);
        fId =  getIntent().getStringExtra("FId");
        fTitle = getIntent().getStringExtra("FTitle");
        uId  = getIntent().getStringExtra("uId");
        toolbar.setTitle(fTitle);
        mapView.getMap().getUiSettings().setAllGesturesEnabled(false);
        mapView.getMap().setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
               // EventBus.getDefault().post(MyConversion.latlng2Mct(latLng.latitude,latLng.longitude));
                MapStatusUpdate update
                        =  MapStatusUpdateFactory.newLatLng(latLng);
                mapView.getMap().animateMapStatus(update);
            }
            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        MemFragmentBody tmp;
        MemBodyMessage msg = new MemBodyMessage();
        tmp = app.getLocMemory().getMemFgtBodyDirc(fId);
        if(uId.equals(app.getNowUId())){
            msg.para = tmp;
            showMsg(msg);
        }
        else{
            if(tmp.getFId()==null) {
                showLoadDiaLog();
                app.getMyHttpClient().memFgtBody(fId);
            }
            else{
                msg.para = tmp;
                showMsg(msg);
            }
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void showMsg(MemBodyMessage msg) {
        cancelProgressDialog();
        MemFragmentBody body = (MemFragmentBody)msg.para;
        if(body==null||body.getFId()==null){
            showTextDialog("数据不存在或被删除了！");
            return ;
        }

        if(msg.type==MemBodyMessage.MEM_BODY_FROM_NETMEM)
            app.getLocMemory().insertMemBodyIntoDB(body);
        FragmentTools.synJson2List(body);
        tvfTitle.setText("标题："+body.getFTitle());
        tvcTime.setText("创建时间："+body.getCTime());
        tvftDetail.setText("碎片时间："+body.getFtDetail());
        tvfpDetail.setText("碎片发生位置："+body.getFpDetail());
        tvContent.setText(body.getBodyItemList().get(0).getBody());
        drewInMap(MyConversion.latlng2Mct(body.getFLatitude(),body.getFLongitude()));
    }
    void drewInMap(MyCellLocation loc) {
        mapView.getMap().clear();
        //button.setText(m);
        LatLng pt1 = new LatLng(loc.getmSWLat(), loc.getmSWLng());
        LatLng pt2 = new LatLng(loc.getmSWLat(), loc.getmNELng());
        LatLng pt3 = new LatLng(loc.getmNELat(), loc.getmNELng());
        LatLng pt4 = new LatLng(loc.getmNELat(), loc.getmSWLng());

        List<LatLng> pts = new ArrayList<LatLng>();
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);
        pts.add(pt4);
//构建用户绘制多边形的Option对象
        OverlayOptions polygonOption = new PolygonOptions()
                .points(pts)
                .stroke(new Stroke(5, 0xAA00FF00))
                .fillColor(0x55FFFF00);
        mapView.getMap().addOverlay(polygonOption);

        LatLng point  = new LatLng(loc.getmOriginLat(),loc.getmOriginLng());
        pts.clear();
        pts.add(point);
        OverlayOptions pp = new CircleOptions().fillColor(0xffff00ff).center(point).radius(10);
        mapView.getMap().addOverlay(pp);

        MapStatusUpdate update
                =  MapStatusUpdateFactory.newLatLngZoom(new LatLng((pt1.latitude+pt3.latitude)/2.0,
                (pt1.longitude+pt3.longitude)/2.0),15.5f);
        mapView.getMap().animateMapStatus(update);
    }

    void showLoadDiaLog(){
        cancelProgressDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载数据。。。");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    void cancelProgressDialog(){
        if(progressDialog==null) return;
        progressDialog.dismiss();
        progressDialog =null;
    }
    void showTextDialog(String msg){
        new AlertDialog.Builder(this).
                setMessage(msg).setNegativeButton("确定",null).show();
    }

}
