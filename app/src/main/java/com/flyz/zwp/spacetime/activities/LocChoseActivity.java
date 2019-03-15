package com.flyz.zwp.spacetime.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.EditText;


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
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.flyz.zwp.spacetime.App;
import com.flyz.zwp.spacetime.R;

import com.flyz.zwp.spacetime.componet.MyConversion;

import com.flyz.zwp.spacetime.model.MyCellLocation;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

@EActivity
public class LocChoseActivity extends AppCompatActivity {


    MyCellLocation nowLoc;
    MyCellLocation choseLoc;

    private GeoCoder mSearch =null;


    @org.androidannotations.annotations.App
    App app;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.et_loc_chose_addr)
    EditText etChoseAddr;
    @ViewById(R.id.bmapView)
    MapView mapView;

    @Subscribe
    void getNowLocation(MyCellLocation loc){
        if(loc.getCoFLag()!=3)
            nowLoc = loc;
        else {
            choseLoc = loc;
            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(
                    new LatLng(loc.getmOriginLat(),loc.getmOriginLng())));
            Logger.json(new Gson().toJson(loc));
        }

        drewInMap(loc);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_chose);
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
        mapView.getMap().getUiSettings().setOverlookingGesturesEnabled(false);
        mapView.getMap().getUiSettings().setRotateGesturesEnabled(false);
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if(reverseGeoCodeResult!=null ||
                        reverseGeoCodeResult.error!= SearchResult.ERRORNO.NO_ERROR) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(reverseGeoCodeResult.getAddressDetail().street);
                    if(reverseGeoCodeResult.getPoiList().size()>0){
                        sb.append(reverseGeoCodeResult.getPoiList().get(0).name);
                    }
                    sb.append("附近");
                   choseLoc.setmLocDescribe(sb.toString());
                    etChoseAddr.setHint(sb.toString());
                }

            }
        });

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
            if(choseLoc==null)
                choseLoc = nowLoc;
            Intent i = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("desc",choseLoc.getmLocDescribe());
            bundle.putDouble("lat",choseLoc.getmOriginLat());
            bundle.putDouble("lng",choseLoc.getmOriginLng());
            bundle.putInt("type",choseLoc.getCoFLag());
            i.putExtras(bundle);
            setResult(101,i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                =  MapStatusUpdateFactory.newLatLngZoom(new LatLng(loc.getmOriginLat(),
                loc.getmOriginLng()), zf);
        mapView.getMap().animateMapStatus(update);
    }



    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        if(nowLoc==null){
            double lat = getIntent().getDoubleExtra("lat",0.0);
            double lng = getIntent().getDoubleExtra("lng",0.0);
            int type = getIntent().getIntExtra("type",-1);
            String desc = getIntent().getStringExtra("desc");
            MyCellLocation loc = MyConversion.latlng2Mct(lat,lng);
            loc.setCoFLag(type);
            loc.setmLocDescribe(desc);
            etChoseAddr.setHint(desc);
            EventBus.getDefault().post(loc);
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        mSearch.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("desc",nowLoc.getmLocDescribe());
        bundle.putDouble("lat",nowLoc.getmOriginLat());
        bundle.putDouble("lng",nowLoc.getmOriginLng());
        bundle.putInt("type",nowLoc.getCoFLag());
        i.putExtras(bundle);
        setResult(101,i);
        super.onBackPressed();
    }

}
