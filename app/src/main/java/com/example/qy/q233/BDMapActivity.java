package com.example.qy.q233;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.location.BDNotifyListener;//假如用到位置提醒功能，需要import该类
//import com.baidu.location.Poi;

/**
 * Created by Qi Yao on 17-3-19.
 */

public class BDMapActivity extends AppCompatActivity {
    boolean moving;
    private static final int UPDATE_RESULT = 1;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();
    private MapStatusUpdate mStatusUpdate;
    private double lastLatitude, currentLatitude, currentLongitude, lastLongitude;
    private double centerLatitude, centerLongitude;
    private boolean start = false;
    private String s = "";
    int screenWidth;
    int screenHeight;
//    Handler mHandler = new MyHandler();
//    Timer mTimer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.baidu_map);

        screenWidth  = getWindowManager().getDefaultDisplay().getWidth();       // 屏幕宽（像素，如：480px）
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();      // 屏幕高（像素，如：800p）

        moving = false;

        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfigeration(config);
        mStatusUpdate = MapStatusUpdateFactory.zoomTo(19);
        mBaiduMap.setMapStatus(mStatusUpdate);
        mBaiduMap.getUiSettings().setScrollGesturesEnabled(false);

        mLocationClient = new LocationClient(getApplicationContext());
        initLocation();

        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);

        //注册监听函数
        mLocationClient.start();

//        MyTimerTask mTimerTask = new MyTimerTask(mHandler);
//        mTimerTask.setMsg(UPDATE_RESULT);
//        mTimer.schedule(mTimerTask, 1, 500);
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span=1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case Permission.CODE_ACCESS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getApplicationContext(), "GET LOCATION PERMISSION DENIED!", Toast.LENGTH_LONG).show();
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED);
            case Permission.CODE_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getApplicationContext(), "WRITE/READ STORAGE PERMISSION DENIED!", Toast.LENGTH_LONG).show();
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED);
            default:
                break;
        }
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location.getLocType() == 167)
            {
                if (((MyApp) getApplication()).getApiVersion() >= 23) {
                    requestPermissions(new String[]{Permission.allPermissions[0]}, Permission.Codes[0]);
                }
                return;
            }
            if (!start) {
                lastLatitude = location.getLatitude();
                lastLongitude = location.getLongitude();
                mBaiduMap.setMapStatus(MapStatusUpdateFactory
                        .newLatLng(new LatLng(lastLatitude, lastLongitude)));
                start = true;
            } else
            {
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();

                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(currentLatitude)
                        .longitude(currentLongitude).build();
                // 设置定位数据
                mBaiduMap.setMyLocationData(locData);
                // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）

               // LatLng a =  mBaiduMap.getProjection().fromScreenLocation(new Point(screenWidth/2, screenHeight/2));
//                mBaiduMap.setMapStatus(MapStatusUpdateFactory
//                        .newLatLng(new LatLng(currentLatitude, currentLongitude)));
//                if(!moving){
//                  new MoveMap().start();
//                }



//                latitude[n%2] = location.getLatitude();
//                longitude[n%2] = location.getLongitude();
//
//
                LatLng pt1 = new LatLng(lastLatitude, lastLongitude);
                LatLng pt2 = new LatLng(currentLatitude, currentLongitude);
//
                List<LatLng> points = new ArrayList<>();
//                List<Integer> index = new ArrayList<Integer>();
                points.add(pt1);//点元素
//                //index.add(0);//设置该点的纹理索引
                points.add(pt2);//点元素
//                //index.add(0);//设置该点的纹理索引
//
                PolylineOptions ooPolyline = new PolylineOptions().width(15).color(0xAAFF0000).points(points);//.customTextureList(customList).textureIndex(index);
//
                mMapView.getMap().addOverlay(ooPolyline);




                lastLatitude = currentLatitude;
                lastLongitude = currentLongitude;

            }



//
//            //获取定位结果
//            StringBuffer sb = new StringBuffer(256);
//
//            sb.append("time : ");
//            sb.append(location.getTime());    //获取定位时间
//
//            sb.append("\nerror code : ");
//            sb.append(location.getLocType());    //获取类型类型
//
//            sb.append("\nlatitude : ");
//            sb.append(location.getLatitude());    //获取纬度信息
//
//            sb.append("\nlontitude : ");
//            sb.append(location.getLongitude());    //获取经度信息
//
//            sb.append("\nradius : ");
//            sb.append(location.getRadius());    //获取定位精准度
//
//            if (location.getLocType() == BDLocation.TypeGpsLocation){
//
//                // GPS定位结果
//                sb.append("\nspeed : ");
//                sb.append(location.getSpeed());    // 单位：公里每小时
//
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());    //获取卫星数
//
//                sb.append("\nheight : ");
//                sb.append(location.getAltitude());    //获取海拔高度信息，单位米
//
//                sb.append("\ndirection : ");
//                sb.append(location.getDirection());    //获取方向信息，单位度
//
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());    //获取地址信息
//
//                sb.append("\ndescribe : ");
//                sb.append("gps定位成功");
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//
//                // 网络定位结果
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());    //获取地址信息
//
//                sb.append("\noperationers : ");
//                sb.append(location.getOperators());    //获取运营商信息
//
//                sb.append("\ndescribe : ");
//                sb.append("网络定位成功");
//
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
//
//                // 离线定位结果
//                sb.append("\ndescribe : ");
//                sb.append("离线定位成功，离线定位结果也是有效的");
//
//            } else if (location.getLocType() == BDLocation.TypeServerError) {
//
//                sb.append("\ndescribe : ");
//                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//
//                sb.append("\ndescribe : ");
//                sb.append("网络不同导致定位失败，请检查网络是否通畅");
//
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//
//                sb.append("\ndescribe : ");
//                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//
//            }
//
//            sb.append("\nlocationdescribe : ");
//            sb.append(location.getLocationDescribe());    //位置语义化信息
//
//            List<Poi> list = location.getPoiList();    // POI数据
//            if (list != null) {
//                sb.append("\npoilist size = : ");
//                sb.append(list.size());
//                for (Poi p : list) {
//                    sb.append("\npoi= : ");
//                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//                }
//            }
//            //((TextView) findViewById(R.id.lat)).setText(sb.toString());
//            s = sb.toString();
//            Log.i("BaiduLocationApiDem", sb.toString());
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

//    class MoveMap extends Thread{
//        LatLng center;
//        double latitude, longitude;
//        MoveMap(){
//           center = mBaiduMap.getProjection().fromScreenLocation(new Point(screenWidth/2, screenHeight/2));
//        }
//
//        @Override
//        public void run(){
//            if(!moving) {
//                moving = true;
//
//                while (center.latitude != currentLatitude || center.longitude != currentLongitude) {
//
////                    if(currentLatitude > center.latitude) {
////                        latitude = currentLatitude - center.latitude > 0.000000001 ? center.latitude + 0.000000001 : currentLatitude;
////                    }else {
////                        latitude = currentLatitude - center.latitude < -0.000000001 ? center.latitude - 0.000000001 : currentLatitude;
////                    }
//                    if(currentLongitude > center.longitude) {
//                        longitude = currentLongitude - center.longitude > 0.000000001 ? center.longitude + 0.000000001 : currentLongitude;
//                    }else {
//                        longitude = currentLongitude - center.longitude < -0.000000001 ? center.longitude - 0.000000001 : currentLongitude;
//                    }
//                    //mBaiduMap.setMapStatus(MapStatusUpdateFactory
//                      //      .newLatLng(new LatLng(currentLatitude, longitude)));
//                    center = mBaiduMap.getProjection().fromScreenLocation(new Point(screenWidth / 2, screenHeight / 2));
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            moving = false;
//        }
//    }
//    class MyHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case UPDATE_RESULT:
//
//                    //((TextView) findViewById(R.id.lat)).setText(s);
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
}
