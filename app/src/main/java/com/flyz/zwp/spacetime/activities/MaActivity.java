package com.flyz.zwp.spacetime.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
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
import com.baidu.platform.comapi.map.B;
import com.flyz.zwp.spacetime.App;
import com.flyz.zwp.spacetime.MainActivity;
import com.flyz.zwp.spacetime.R;
import com.flyz.zwp.spacetime.componet.MyConversion;
import com.flyz.zwp.spacetime.componet.MyScrollView;
import com.flyz.zwp.spacetime.componet.ShareBlockAdapter;
import com.flyz.zwp.spacetime.model.MyCellLocation;
import com.flyz.zwp.spacetime.model.MySystemCode;
import com.flyz.zwp.spacetime.model.ShareBlock;
import com.flyz.zwp.spacetime.model.SpaceCell;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EActivity
public class MaActivity extends AppCompatActivity {


    List<ShareBlock> allShareBlocks = new ArrayList<ShareBlock>();

    protected  MyCellLocation nowLoc=null;
    protected  MyCellLocation chooseLoc = null;
    protected  MyCellLocation stoploc = null;
    protected  boolean isNeedLoction = true;
    protected boolean isNeedResetShareBlockList = false;

    private ShareBlockAdapter adapter = null;

    private ProgressDialog progressDialog;

    @org.androidannotations.annotations.App
    App app;
    @ViewById(R.id.bmapView)
    MapView mapView;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.fab)
    FloatingActionButton fab;
    @ViewById(R.id.btn_ma_user)
    Button btnUserInfo;
    @ViewById(R.id.tv_ma_cell_info)
    TextView tvCellInfo;
    @ViewById(R.id.lv_ma_share)
    ListView lvShareBlocks;
    @ViewById(R.id.content_ma)
    MyScrollView sv;
    @ViewById(R.id.tv_ma_non_list)
    TextView tvNonList;
    @ViewById(R.id.btn_ma_log)
    Button btnLog;

    @Click(R.id.btn_ma_log)
    void btnLogClick(){
        if(app.isOnLine()){
            app.logOut();
            btnLog.setText("登录");
        }else{
            Intent i = new Intent();
            i.setClass(MaActivity.this,LogInActivity_.class);
            startActivityForResult(i,2222);
        }

    }

    @Click(R.id.fab)
    void createMemFgt() {
        startActivity(new Intent().setClass(MaActivity.this,FgtCrtActivity_.class));
    }
    @Click(R.id.btn_ma_user)
    void showUserInfo() {
        Intent i = new Intent();
        i.putExtra("uId",app.getNowUId());
        i.putExtra("uNickName",app.getNowUNickName());
        i.setClass(MaActivity.this,UserInfoActivity_.class);
        startActivity(i);
    }

    void showUnDoneVerifDialog(boolean isOk){
        if(isOk)
            new AlertDialog.Builder(this)
                    .setMessage("碎片已通过验证！")
                    .setNegativeButton("确定",null)
                    .show();
        else
            new AlertDialog.Builder(this)
                    .setMessage("碎片未通过验证！")
                    .setNegativeButton("确定",null)
                    .show();
    }
    void showProgressDialog(){
        cancelProgressDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("开拓区块！");
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
    void showUpLoadingDialog(){
        cancelProgressDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("修改数据中。。。");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    void showTextDialog(String msg){
        new AlertDialog.Builder(this).
                setMessage(msg).setNegativeButton("确定",null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma);
        setSupportActionBar(toolbar);
        mapView.getMap().setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MyCellLocation loc = MyConversion.latlng2Mct(latLng.latitude,latLng.longitude);
                loc.setCoFLag(3);
                EventBus.getDefault().post(loc);
            }
            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        adapter = new ShareBlockAdapter(this,allShareBlocks);
        lvShareBlocks.setAdapter(adapter);
        lvShareBlocks.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    sv.requestDisallowInterceptTouchEvent(false);
                }else{
                    sv.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        lvShareBlocks.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
                                       int oldTop, int oldRight, int oldBottom) {
                mapView.setFocusable(true);
                mapView.setFocusableInTouchMode(true);
                mapView.requestFocus();
            }
        });
        tvCellInfo.setClickable(false);
        tvCellInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!app.isOnLine()){
                    showTextDialog("离线用户可以在此区块内注册完成开拓！");
                    return ;
                }
                if(chooseLoc==null||
                        (chooseLoc.getmCellX()==nowLoc.getmCellX()&&
                                chooseLoc.getmCellY()==nowLoc.getmCellY())){
                    showProgressDialog();
                    app.getMyHttpClient().createSpaceCell(nowLoc,
                            app.getNowUId(),app.getNowUNickName());
                }else{
                    showTextDialog("你不在所选择的区块内，不能开拓区块！");
                }
            }
        });

        tvCellInfo.setText("等待数据中。。。");

    }


    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onResume() {
        if(isNeedLoction)
            app.getLoctionClient().getLocation();
        if(stoploc!=null)
        {
            isNeedResetShareBlockList = true;
            app.getMyHttpClient().shareBlock(stoploc,0);
            app.getMyHttpClient().cellInfo(stoploc);
        }
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        sv.unRegisterOnScrollViewScrollToBottom();
        super.onDestroy();
    }


    @OnActivityResult(2222)
    void onResult( @OnActivityResult.Extra(value = "log") String log){
        if(log.equals("OK"))
            btnLog.setText("注销");
        app.getLoctionClient().getLocation();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    void updateInMa(MyCellLocation loc) {
        isNeedLoction = false;
        isNeedResetShareBlockList = true;
        if(loc.getCoFLag()!=3)
            nowLoc = loc;
        else
            chooseLoc = loc;
        stoploc = loc;
        Logger.json(new Gson().toJson(loc));
        drewInMap(loc);
        app.getMyHttpClient().cellInfo(loc);
        app.getMyHttpClient().shareBlock(loc,0);
        //app.getLocMemory().getLocShareBlocks(loc,0);
        tvNonList.setVisibility(View.VISIBLE);
        lvShareBlocks.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void getCellInfo(SpaceCell cell){
        tvCellInfo.setClickable(false);
        if(cell==null||cell.getCeId()==null){
            tvCellInfo.setClickable(true);
            tvCellInfo.setText("当前区块还没有被开拓，" +
                    "点击这里或者在这个区块内注册用户可以尝试开拓，" +
                    "你也可以成为这个区块的开拓者！");
        }else{
            //String s = new Gson().toJson(cell);
            StringBuffer sb = new StringBuffer();
            sb.append("区块坐标：("+cell.getCellX()+","+cell.getCellY()+") \n");
            sb.append("开创者： "+cell.getUNickName()+" \n");
            sb.append("开创者时间： "+cell.getCeTime()+" \n");
            if(cell.getCeDownLTime()!=null||true){
                sb.append("记忆跨度：\n从 <"+cell.getCeDownTime()+">\n");
                sb.append("到 <"+cell.getCeUpTime()+"> \n");
            }
            int all = cell.getMB()+cell.getMR()+cell.getMG();
            if(all!=0||true){
                sb.append("三阵营数据：\n");
                sb.append(" \t\t R阵营（"+cell.getMR()+" / "+all+"）\n");
                sb.append(" \t\t G阵营（"+cell.getMG()+" / "+all+"）\n");
                sb.append(" \t\t B阵营（"+cell.getMB()+" / "+all+"）\n");
            }

            tvCellInfo.setText(sb.toString());
            Logger.d(sb.toString());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void getShareBlocks(List<ShareBlock> blocks){
        Logger.json(new Gson().toJson(blocks));
        if(isNeedResetShareBlockList){
            allShareBlocks.clear();
            isNeedResetShareBlockList = false;
            allShareBlocks.addAll(blocks);
            if(blocks.size()!=0){
                tvNonList.setVisibility(View.GONE);
                lvShareBlocks.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetInvalidated();
        }else  if(blocks!=null&&blocks.size()!=0){

            for(int i=0;i<blocks.size();i++){
                if(!allShareBlocks.contains(blocks.get(i)))
                    allShareBlocks.add(blocks.get(i));
            }

            adapter.notifyDataSetChanged();
        }

        //app.getLocMemory().loadMem(blocks);

        // update view


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void systemMessage(MySystemCode code){
        if(code.errorCode == MySystemCode.UPDATE_SHARE_BLOCKS_OK){
            Logger.d("show ShareBlocks");
            return ;
        }else if(code.errorCode == MySystemCode.CRT_SPACE_CELL_OK){
            cancelProgressDialog();
            updateInMa(nowLoc);
            tvCellInfo.setClickable(false);
        }else if(code.errorCode == MySystemCode.CRT_SPACE_CELL_ERR){
            cancelProgressDialog();
            showTextDialog("开拓遇到问题，请重新尝试！");
            updateInMa(nowLoc);
        }

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
        float zf = mapView.getMap().getMapStatus().zoom;
        zf = zf<14.0f?16.0f:zf;
        MapStatusUpdate update
                =  MapStatusUpdateFactory.newLatLngZoom(new LatLng((pt1.latitude+pt3.latitude)/2.0,
                (pt1.longitude+pt3.longitude)/2.0),zf);
        mapView.getMap().animateMapStatus(update,900);
    }

}
