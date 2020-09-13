package com.dianping.android.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.dianping.android.MyApplication;
import com.dianping.android.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentSearch extends Fragment {

    private LocationClient mLocationClient;

    private BaiduMap baiduMap;
    private MapView bdMapView;

    private boolean isFirstLocate = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(MyApplication.getContext());
        View view = inflater.inflate(R.layout.secarch_index,container,false);

        mLocationClient = new LocationClient(MyApplication.getContext());
        mLocationClient.registerLocationListener(new MyLocationListener());

        bdMapView = (MapView) view.findViewById(R.id.bdMapView);

        baiduMap = bdMapView.getMap();
        baiduMap.setMyLocationEnabled(true);

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MyApplication.getContext(),Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MyApplication.getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(),permissions,1);
        } else {
            requestLocation();
        }
        return view;
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setCoorType("bd09ll");
        mLocationClient.setLocOption(option);

    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation.getLocType()== BDLocation.TypeGpsLocation || bdLocation.getLocType()==BDLocation.TypeNetWorkLocation) {
                if (isFirstLocate) {
                    LatLng latLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                    MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
                    baiduMap.animateMapStatus(update);
                    update = MapStatusUpdateFactory.zoomTo(17f);
                    baiduMap.animateMapStatus(update);
                    isFirstLocate = false;
                }
                MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
                locationBuilder.latitude(bdLocation.getLatitude());
                locationBuilder.longitude(bdLocation.getLongitude());
                MyLocationData locationData = locationBuilder.build();
                baiduMap.setMyLocationData(locationData);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        bdMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        bdMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bdMapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }
}
