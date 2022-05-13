package com.angcyo.y2tools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.angcyo.base.AboutFragment;
import com.angcyo.base.BaseApplication;
import com.angcyo.base.BaseApplication.LocationInfo;
import com.angcyo.base.BaseApplication.RefreshLocationInfo;
import com.angcyo.base.BaseFragmentActivity;
import com.angcyo.base.CarFragment;
import com.angcyo.base.CityFragment;
import com.angcyo.base.MainFragmentPagerAdapter;
import com.angcyo.gps.LocationSvc;
import com.angcyo.ui.SwitchButton;
import com.angcyo.util.ColorTool;
import com.angcyo.util.Constants;
import com.angcyo.util.ToastTool;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseFragmentActivity implements
        BDLocationListener, RefreshLocationInfo {

    // public static Handler handler = new Handler();
    public static final int TEXT_RUN = 1;
    private final int INDEX_CITY = 0, INDEX_CAR = 1, INDEX_ABOUT = 2;
    private final int EXIT_DELAY_TIME = 400;
    public LocationClient mLocationClient = null;
    Handler handler = new Handler();
    private ArrayList<Fragment> fragments;
    private ViewPager mPager;
    private ImageView mImgIndicator;
    private RelativeLayout mHeadBgLayout;
    private TextView tab_tx_city, tab_tx_car, tab_tx_about, tabTitle, tx1, tx2,
            tx3;
    private RadioGroup mRadioGroup;
    private RadioButton radio_city, radio_car, radio_about;
    private TextView title1, title2;
    private Timer timer = null;
    private int code = 0;
    private RelativeLayout exit_rlayout;
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            switch (code) {
                case 0:
                    tx3.setVisibility(View.INVISIBLE);
                    ++code;
                    handler.postDelayed(runnable, EXIT_DELAY_TIME);
                    break;
                case 1:
                    tx2.setVisibility(View.INVISIBLE);
                    ++code;
                    handler.postDelayed(runnable, EXIT_DELAY_TIME);
                    break;
                case 2:
                    tx1.setVisibility(View.INVISIBLE);
                    ++code;
                    handler.postDelayed(runnable, EXIT_DELAY_TIME);
                    break;
                default:
                    code = 0;
                    exit_rlayout.setVisibility(View.GONE);
                    tx1.setVisibility(View.VISIBLE);
                    tx2.setVisibility(View.VISIBLE);
                    tx3.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    private LocationManager locationManager;
    private GpsStatus gpsstatus;
    private RelativeLayout layout_location;
    private ImageView img_location;
    private TextView text_location;
    private TextView text_location_tip;
    private TextView text_about_support;
    // carfragment的开关
    private SwitchButton switchButton;
    // 数据库保存成功后,使用这个view提示
    private ImageView img_guang;
    private PopupWindow mPopupWindow;
    // 屏幕的width
    private int mScreenWidth;
    // 屏幕的height
    private int mScreenHeight;

    // public BDLocationListener myListener = new MyLocationListener();
    // PopupWindow的width
    private int mPopupWindowWidth;
    // PopupWindow的height
    private int mPopupWindowHeight;
    // 创建位置监听器
    private LocationListener locationListener = new LocationListener() {
        // 位置发生改变时调用
        @Override
        public void onLocationChanged(Location location) {
            Log.d("Location", "onLocationChanged");
            Log.d("Location",
                    "onLocationChanged Latitude" + location.getLatitude());
            Log.d("Location",
                    "onLocationChanged location" + location.getLongitude());
        }

        // provider失效时调用
        @Override
        public void onProviderDisabled(String provider) {
            Log.d("Location", "onProviderDisabled");
        }

        // provider启用时调用
        @Override
        public void onProviderEnabled(String provider) {
            Log.d("Location", "onProviderEnabled");
        }

        // 状态改变时调用
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("Location", "onStatusChanged");
        }
    };
    private GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
        // GPS状态发生变化时触发
        @Override
        public void onGpsStatusChanged(int event) {
            // 获取当前状态
            gpsstatus = locationManager.getGpsStatus(null);
            switch (event) {
                // 第一次定位时的事件
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Log.d("Location", "准备定位");
                    break;
                // 开始定位的事件
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.d("Location", "开始定位");
                    break;
                // 发送GPS卫星状态事件
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    Toast.makeText(MainActivity.this, "GPS_EVENT_SATELLITE_STATUS",
                            Toast.LENGTH_SHORT).show();
                    Iterable<GpsSatellite> allSatellites = gpsstatus
                            .getSatellites();
                    Iterator<GpsSatellite> it = allSatellites.iterator();
                    int count = 0;
                    while (it.hasNext()) {
                        count++;
                    }
                    Toast.makeText(MainActivity.this, "Satellite Count:" + count,
                            Toast.LENGTH_SHORT).show();
                    break;
                // 停止定位事件
                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.d("Location", "GPS_EVENT_STOPPED");
                    break;
            }
        }
    };
    // 创建位置监听器
    private LocationListener locationListener2 = new LocationListener() {
        // 位置发生改变时调用
        @Override
        public void onLocationChanged(Location location) {
            Log.d("Location", "onLocationChanged");
        }

        // provider失效时调用
        @Override
        public void onProviderDisabled(String provider) {
            Log.d("Location", "onProviderDisabled");
        }

        // provider启用时调用
        @Override
        public void onProviderEnabled(String provider) {
            Log.d("Location", "onProviderEnabled");
        }

        // 状态改变时调用
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("Location", "onStatusChanged");
        }
    };

    private static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content))
                .getChildAt(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        initImgIndicator();
        initViewPager();

        initBaiduGps();
        // initGps();
        // getLocationForNet();
        // getLocationForGps();
        // new Thread(new RunnableGetLocation()).start();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        mLocationClient.stop();
        super.onStop();
    }

    private void initBaiduGps() {
        // TODO Auto-generated method stub
        mLocationClient = ((BaseApplication) getApplication()).mLocationClient; // 声明LocationClient类
        // mLocationClient.registerLocationListener(this); // 注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Battery_Saving);// 设置定位模式 节电模式
        // option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        // option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        // option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);

        mLocationClient.start();// 启动定位
        // mLocationClient.requestLocation();// 发起定位,获取位置

        layout_location.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable) img_location
                .getDrawable();

        animationDrawable.start();

        ((BaseApplication) getApplication()).setOnRefreshLoaction(this);

    }

    private void initGps() {
        // TODO Auto-generated method stub
        // 注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.LOCATION_ACTION);
        Log.i("注册广播", "test");
        this.registerReceiver(new LocationBroadcastReceiver(), filter);

        // 启动服务
        Intent intent = new Intent();
        intent.setClass(this, LocationSvc.class);
        Log.i("test", "启动服务");
        startService(intent);
    }

    private void initViewPager() {
        // TODO Auto-generated method stub
        mPager = (ViewPager) findViewById(R.id.pager);
        fragments = new ArrayList<Fragment>();

        Fragment cityFragment = new CityFragment();
        Fragment carFragment = new CarFragment();
        Fragment aboutFragment = new AboutFragment();

        ((CarFragment) carFragment).switchButton = switchButton;// 保存按钮对象,方便在CarFragmeng中监听单击事件

        fragments.add(INDEX_CITY, cityFragment);
        fragments.add(INDEX_CAR, carFragment);
        fragments.add(INDEX_ABOUT, aboutFragment);

        mPager.setAdapter(new MainFragmentPagerAdapter(
                getSupportFragmentManager(), fragments));

        mPager.setOnPageChangeListener(new MainOnPageChangeListener());

    }

    private void initImgIndicator() {
        // TODO Auto-generated method stub

        // mImgIndicator = (ImageView) findViewById(R.id.img_indicator);
        // mImgIndicator = new ImageView(MainActivity.this);
        // mImgIndicator.setImageResource(R.drawable.tab_front_bg);
        //
        // mHeadBgLayout.addView(mImgIndicator);
        // int startLeft = 0;
        // startLeft = mImgIndicator.getWidth();
        // Log.i("startLeft", String.valueOf(startLeft));
        // AnimUtil.moveView(mImgIndicator, 0, 410, 0, 0, 700, true);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // int startLeft = 0;
        // startLeft = mImgIndicator.getRight();
        // Log.i("MainActivity", String.valueOf(startLeft));
        // AnimUtil.moveView(mImgIndicator, 0, startLeft, 0, 0, 3000, true);
    }

    private void initView() {
        // TODO Auto-generated method stub

        tx1 = (TextView) findViewById(R.id.tx1);
        tx2 = (TextView) findViewById(R.id.tx2);
        tx3 = (TextView) findViewById(R.id.tx3);

        title1 = (TextView) findViewById(R.id.title);
        title2 = (TextView) findViewById(R.id.title2);

        tabTitle = (TextView) findViewById(R.id.title);
        mHeadBgLayout = (RelativeLayout) findViewById(R.id.head_bg);

        layout_location = (RelativeLayout) findViewById(R.id.layout_location);
        img_location = (ImageView) findViewById(R.id.img_location);
        text_location = (TextView) findViewById(R.id.text_location);
        text_location_tip = (TextView) findViewById(R.id.text_location_tip);
        text_about_support = (TextView) findViewById(R.id.text_about_support);
        text_about_support.setTextColor(ColorTool.getRandomColorInt2());
        text_about_support.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                text_about_support.setTextColor(ColorTool.getRandomColorInt2());
                ToastTool.getRandomCentertColToast(MainActivity.this,
                        "作者已收到你精神上的支持\nO(∩_∩)O谢谢");
            }
        });

        ((BaseApplication) getApplication()).layout_location = layout_location;
        ((BaseApplication) getApplication()).img_location = img_location;
        ((BaseApplication) getApplication()).text_location = text_location;
        ((BaseApplication) getApplication()).text_location_tip = text_location_tip;

        img_guang = (ImageView) findViewById(R.id.img_guang);
        ((BaseApplication) getApplication()).img_guang = img_guang;

        switchButton = (SwitchButton) findViewById(R.id.switch_show_all);

        // tab_tx_car.setOnClickListener(new txClickListener(1));
        // tab_tx_city.setOnClickListener(new txClickListener(0));
        // tab_tx_about.setOnClickListener(new txClickListener(2));

        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radio_city = (RadioButton) findViewById(R.id.radio_city);
        radio_car = (RadioButton) findViewById(R.id.radio_car);
        radio_about = (RadioButton) findViewById(R.id.radio_about);

        mRadioGroup
                .setOnCheckedChangeListener(new MainOnCheckedChangeListener());

        exit_rlayout = (RelativeLayout) findViewById(R.id.exit_rlayou);

        setHeadTitle(INDEX_CITY);// 设置默认显示的pager
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // ToastTool.getRandomColToast(getApplicationContext(), "hehe");
        // Dialog dialog = new SetDialog(getApplicationContext());
        // dialog.show();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            if (mPopupWindow == null || !mPopupWindow.isShowing()) {
                getPopupWindowInstance();
                Rect frame = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(frame); // 得到状态栏的高度
                mPopupWindow.showAtLocation(getRootView(this), Gravity.TOP, 0,
                        frame.top);
            } else {
                mPopupWindow.dismiss();
                mPopupWindow = null;
            }

            // mPopupWindow.showAsDropDown(getRootView(this));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * 创建PopupWindow
     */
    @SuppressWarnings("deprecation")
    private void initPopuptWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View popupWindow = layoutInflater.inflate(R.layout.set_main_layout,
                null);
        final EditText editText = (EditText) popupWindow
                .findViewById(R.id.et_wel);

        popupWindow.findViewById(R.id.bt_setting_ok).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        ((BaseApplication) getApplication()).editor.putString(
                                "TEXT_WEl", editText.getText().toString())
                                .commit();
                        mPopupWindow.dismiss();
                        ToastTool.getRandomColToast(getApplicationContext(),
                                "保存成功,重启APP可以查看效果");
                    }
                });

        // 创建一个PopupWindow
        // 参数1：contentView 指定PopupWindow的内容
        // 参数2：width 指定PopupWindow的width
        // 参数3：height 指定PopupWindow的height
        mPopupWindow = new PopupWindow(popupWindow, LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        // 获取屏幕和PopupWindow的width和height
        mScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
        mScreenWidth = getWindowManager().getDefaultDisplay().getHeight();
        mPopupWindowWidth = mPopupWindow.getWidth();
        mPopupWindowHeight = mPopupWindow.getHeight();

        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());// 设置一个空的背景
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.popup_anim);
    }

    /*
     * 获取PopupWindow实例
     */
    private void getPopupWindowInstance() {
        if (null != mPopupWindow) {
            mPopupWindow.dismiss();
            return;
        } else {
            initPopuptWindow();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();
        // AlertDialog alertDialog = new ExitDialog(MainActivity.this,
        // R.style.ExitDlg);

        // alertDialog.setContentView(R.layout.dlg_exit);

        // autoExit();
        if (exit_rlayout.getVisibility() == View.VISIBLE) {
            //System.exit(1);
            finish();
        }

        handler.postDelayed(runnable, EXIT_DELAY_TIME);

        exit_rlayout.setVisibility(View.VISIBLE);
    }

    public void setHeadTitle(int arg0) {
        // TODO Auto-generated method stub
        title2.setMovementMethod(LinkMovementMethod.getInstance());
        switch (arg0) {
            case INDEX_CITY:
                title1.setText(getResources().getString(R.string.str_city_title1));
                title2.setText(getResources().getString(R.string.str_city_title2));
                break;
            case INDEX_CAR:
                title1.setText(getResources().getString(R.string.str_car_title1));
                title2.setText(getResources().getString(R.string.str_car_title2));
                break;
            case INDEX_ABOUT:
                title1.setText(getResources().getString(R.string.str_about_title1));
                title2.setText(getResources().getString(R.string.str_about_title2));
                break;
            default:
                break;
        }
    }

    private void autoExit() {

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                switch (code) {
                    case 0:
                        tx3.setVisibility(View.INVISIBLE);
                        ++code;
                        break;
                    case 1:
                        tx2.setVisibility(View.INVISIBLE);
                        ++code;
                        break;
                    case 2:
                        tx1.setVisibility(View.INVISIBLE);
                        ++code;
                        break;

                    default:
                        code = 0;
                        timer.cancel();
                        break;
                }
            }
        }, 300, 300);
    }

    private void getLocationForJASON() {
        // ToastTool.getToast(MainActivity.this, "正在定位...");

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation gsmCell = (GsmCellLocation) tm.getCellLocation();
        int cid = gsmCell.getCid();
        int lac = gsmCell.getLac();
        String netOperator = tm.getNetworkOperator();
        int mcc = Integer.valueOf(netOperator.substring(0, 3));
        int mnc = Integer.valueOf(netOperator.substring(3, 5));
        JSONObject holder = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject data = new JSONObject();
        try {
            holder.put("version", "1.1.0");
            holder.put("host", "maps.google.com");
            holder.put("address_language", "zh_CN");
            holder.put("request_address", true);
            holder.put("radio_type", "gsm");
            holder.put("carrier", "HTC");
            data.put("cell_id", cid);
            data.put("location_area_code", lac);
            data.put("mobile_countyr_code", mcc);
            data.put("mobile_network_code", mnc);
            array.put(data);
            holder.put("cell_towers", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://www.google.com/loc/json");
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(holder.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(stringEntity);
        HttpResponse httpResponse = null;
        try {
            httpResponse = client.execute(httpPost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream is = null;
        try {
            is = httpEntity.getContent();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);
        StringBuffer stringBuffer = new StringBuffer();
        try {
            String result = "";
            while ((result = reader.readLine()) != null) {
                stringBuffer.append(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Location", stringBuffer.toString());
        // txtLocation.setText(stringBuffer.toString());
    }

    private void getLocationForNet() {
        // 获取到LocationManager对象
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 创建一个Criteria对象
        Criteria criteria = new Criteria();
        // 设置粗略精确度
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        // 设置是否需要返回海拔信息
        criteria.setAltitudeRequired(false);
        // 设置是否需要返回方位信息
        criteria.setBearingRequired(false);
        // 设置是否允许付费服务
        criteria.setCostAllowed(true);
        // 设置电量消耗等级
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        // 设置是否需要返回速度信息
        criteria.setSpeedRequired(false);

        // 根据设置的Criteria对象，获取最符合此标准的provider对象
        String currentProvider = locationManager
                .getBestProvider(criteria, true);
        Log.d("Location", "currentProvider: " + currentProvider);
        // 根据当前provider对象获取最后一次位置信息
        Location currentLocation = locationManager
                .getLastKnownLocation(currentProvider);
        // 如果位置信息为null，则请求更新位置信息
        if (currentLocation == null) {
            locationManager.requestLocationUpdates(currentProvider, 0, 0,
                    locationListener);
        }
        // 直到获得最后一次位置信息为止，如果未获得最后一次位置信息，则显示默认经纬度
        // 每隔10秒获取一次位置信息
        // while (true) {
        currentLocation = locationManager.getLastKnownLocation(currentProvider);
        if (currentLocation != null) {
            Log.d("Location", "Latitude: " + currentLocation.getLatitude());
            Log.d("Location", "location: " + currentLocation.getLongitude());
            // break;
        } else {
            Log.d("Location", "LatitudeNet: " + 0);
            Log.d("Location", "locationNet: " + 0);
        }
        // try {
        // Thread.sleep(10000);
        // } catch (InterruptedException e) {
        // Log.e("Location", e.getMessage());
        // }
        // }

        // 解析地址并显示
        // Geocoder geoCoder = new Geocoder(this);
        // try {
        // int latitude = (int) currentLocation.getLatitude();
        // int longitude = (int) currentLocation.getLongitude();
        // List<Address> list = geoCoder.getFromLocation(latitude, longitude,
        // 2);
        // for (int i = 0; i < list.size(); i++) {
        // Address address = list.get(i);
        // // Toast.makeText(
        // // MainActivity.this,
        // // address.getCountryName() + address.getAdminArea()
        // // + address.getFeatureName(), Toast.LENGTH_LONG)
        // // .show();
        // }
        // } catch (IOException e) {
        // // Toast.makeText(MainActivity.this, e.getMessage(),
        // Toast.LENGTH_LONG)
        // // .show();
        // }

    }

    private void getLocationForGps() {
        // 获取到LocationManager对象
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 根据设置的Criteria对象，获取最符合此标准的provider对象
        String currentProvider = locationManager.getProvider(
                LocationManager.GPS_PROVIDER).getName();

        // 根据当前provider对象获取最后一次位置信息
        Location currentLocation = locationManager
                .getLastKnownLocation(currentProvider);
        // 如果位置信息为null，则请求更新位置信息
        if (currentLocation == null) {
            locationManager.requestLocationUpdates(currentProvider, 0, 0,
                    locationListener2);
        }
        // 增加GPS状态监听器
        Log.d("Location", "增加GPS状态监听器:");
        locationManager.addGpsStatusListener(gpsListener);

        // 直到获得最后一次位置信息为止，如果未获得最后一次位置信息，则显示默认经纬度
        // 每隔10秒获取一次位置信息
        while (true) {
            currentLocation = locationManager
                    .getLastKnownLocation(currentProvider);
            if (currentLocation != null) {
                Log.d("Location", "Latitude: " + currentLocation.getLatitude());
                Log.d("Location", "location: " + currentLocation.getLongitude());
                break;
            } else {
                Log.d("Location", "LatitudeGPS: " + 0);
                Log.d("Location", "locationGPS: " + 0);
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Log.e("Location", e.getMessage());
            }
        }
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        // TODO Auto-generated method stub
        Log.d("Location", "得到定位信息");

        if (location == null)
            return;
        StringBuffer sb = new StringBuffer(256);
        // sb.append("time : ");
        // sb.append(location.getTime());
        // sb.append("\nerror code : ");
        // sb.append(location.getLocType());
        // sb.append("\nlatitude : ");
        // sb.append(location.getLatitude());
        // sb.append("\nlontitude : ");
        // sb.append(location.getLongitude());
        // sb.append("\nradius : ");
        // sb.append(location.getRadius());
        if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
            sb.append(location.getAddrStr());
            ToastTool.getToast(getApplicationContext(), sb);
        }
    }

    @Override
    public void onRefreshLocation(LocationInfo lf) {
        // TODO Auto-generated method stub
        Log.i("Location Ref", lf.strCity + lf.strDistrict + lf.strProvince);
    }

    private class LocationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.i("收到广播", intent.getAction());
            if (!intent.getAction().equals(Constants.LOCATION_ACTION))
                return;
            String locationInfo = intent.getStringExtra(Constants.LOCATION);
            ToastTool.getToast(MainActivity.this, locationInfo);
            // text.setText(locationInfo);
            // dialog.dismiss();
            MainActivity.this.unregisterReceiver(this);// 不需要时注销
        }
    }

    private class MainOnCheckedChangeListener implements
            OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub

            switch (checkedId) {
                case R.id.radio_city:
                    mPager.setCurrentItem(INDEX_CITY);
                    break;
                case R.id.radio_car:
                    mPager.setCurrentItem(INDEX_CAR);
                    break;
                case R.id.radio_about:
                    mPager.setCurrentItem(INDEX_ABOUT);
                    break;

                default:
                    break;
            }
        }

    }

    public class txClickListener implements View.OnClickListener {
        private int index = 0;

        public txClickListener(int index) {
            super();
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mPager.setCurrentItem(index);

            switch (index) {
                case 0:
                    tab_tx_city.setPressed(true);
                    break;
                case 1:
                    tab_tx_car.setPressed(true);
                    break;
                case 2:
                    tab_tx_about.setPressed(true);
                    break;

                default:
                    break;
            }

        }

    }

    public class MainOnPageChangeListener implements OnPageChangeListener {

        public MainOnPageChangeListener() {
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            //
            setHeadTitle(arg0);

            switch (arg0) {
                case INDEX_CITY:
                    mRadioGroup.check(R.id.radio_city);
                    layout_location.setVisibility(View.VISIBLE);
                    text_about_support.setVisibility(View.GONE);
                    break;
                case INDEX_CAR:
                    mRadioGroup.check(R.id.radio_car);
                    layout_location.setVisibility(View.GONE);
                    text_about_support.setVisibility(View.GONE);
                    break;
                case INDEX_ABOUT:
                    mRadioGroup.check(R.id.radio_about);
                    layout_location.setVisibility(View.GONE);
                    text_about_support.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        }

    }

    public class RunnableGetLocation implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            // ToastTool.getToast(MainActivity.this, "正在定位...");
            // getLocation();
        }

    }
}
