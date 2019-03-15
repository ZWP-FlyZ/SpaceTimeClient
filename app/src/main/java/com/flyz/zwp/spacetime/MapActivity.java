package com.flyz.zwp.spacetime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.flyz.zwp.spacetime.componet.MyConversion;
import com.flyz.zwp.spacetime.model.MyCellLocation;

import org.androidannotations.annotations.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@EActivity
public class MapActivity extends AppCompatActivity {

    @ViewById(R.id.bmapView)
    MapView mapView;

    @org.androidannotations.annotations.App
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        EventBus.getDefault().register(this);
        app.myLocationClient.getLocation();


        mapView.getMap().setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                EventBus.getDefault().post(MyConversion.latlng2Mct(latLng.latitude,latLng.longitude));
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    void updateInMap(MyCellLocation loc)
    {
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
                .fillColor(0xAAFFFF00);
        mapView.getMap().addOverlay(polygonOption);

        LatLng point  = new LatLng(loc.getmOriginLat(),loc.getmOriginLng());
        pts.clear();
        pts.add(point);
        OverlayOptions pp = new CircleOptions().fillColor(0xffff00ff).center(point).radius(10);
        mapView.getMap().addOverlay(pp);

  MapStatusUpdate update
          =  MapStatusUpdateFactory.newLatLngZoom(new LatLng((pt1.latitude+pt3.latitude)/2.0,
                                                            (pt1.longitude+pt3.longitude)/2.0),16);
        mapView.getMap().animateMapStatus(update,1400);


    }

}
