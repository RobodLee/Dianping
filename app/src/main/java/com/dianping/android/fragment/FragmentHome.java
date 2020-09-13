package com.dianping.android.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dianping.android.CityListActivity;
import com.dianping.android.MyApplication;
import com.dianping.android.R;
import com.dianping.android.adapter.HomeNavAdapter;
import com.dianping.android.enity.Navigation;
import com.dianping.android.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class FragmentHome extends Fragment{

    private TextView topCity;       //用于显示位置信息的TextView
    private String cityName;        //城市名称

    private LocationClient mLocationClient;

    private RecyclerView homeNavRecyclerView;
    private List<Navigation> navigations = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_index,container,false);

        topCity = (TextView) view.findViewById(R.id.index_top_city);
        topCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CityListActivity.class);
                startActivityForResult(intent,1);
            }
        });

        mLocationClient = new LocationClient(MyApplication.getContext());
        mLocationClient.registerLocationListener(new MyLocationListener());

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
        //设置RecyclerView的相关属性
        initNavgations();
        homeNavRecyclerView = (RecyclerView) view.findViewById(R.id.home_nav);
        GridLayoutManager manager = new GridLayoutManager(getContext(),3);
        homeNavRecyclerView.setLayoutManager(manager);
        HomeNavAdapter homeNavAdapter = new HomeNavAdapter(navigations);
        homeNavRecyclerView.setAdapter(homeNavAdapter);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result:grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MyApplication.getContext(),"必须同意所有权限",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(MyApplication.getContext(),"发生未知错误",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cityName = data.getStringExtra("cityName");
                    topCity.setText(cityName);
                }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLocationClient.stop();
    }

    private void initNavgations() {
        for (int i = 0; i<MyUtils.navsSort.length; i++) {
            Navigation navigation = new Navigation(MyUtils.navsSort[i],R.drawable.nav_image);
            navigations.add(navigation);
        }
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
//            double lat = 0.0, lng = 0.0;
//            Address address = null;
//            if (bdLocation != null) {
//                lat = bdLocation.getLatitude();
//                lng = bdLocation.getLongitude();
//            }
//            List<Address> addressList = null;
//            Geocoder ge = new Geocoder(getActivity());
//            try {
//                addressList = ge.getFromLocation(lat,lng,2);
//                for (int i = 0; i < addressList.size(); i++) {
//                    address = addressList.get(i);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            cityName = address.getLocality();
            cityName = bdLocation.getDistrict();
            if (cityName != null) {
                topCity.setText(cityName);
            } else {
                topCity.setText("定位失败");
            }
        }
    }
}
