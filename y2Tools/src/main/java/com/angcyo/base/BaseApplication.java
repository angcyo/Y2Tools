package com.angcyo.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.angcyo.util.Constants;
import com.angcyo.util.ToastTool;
import com.angcyo.y2tools.R;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

public class BaseApplication extends Application {

    // private DBManager dbManager;
    // private SQLiteDatabase db;

    // public List<CityListItem> cList = new ArrayList<CityListItem>();
    // public List<DistrictListItem> dList = new ArrayList<DistrictListItem>();
    // public List<ProvinceListItem> pList = new ArrayList<ProvinceListItem>();

    public final String FIRST_RUN = "FRUN";// 首次运行key
    public final String VER_NAME = "VER_NAME";// 版本名称key
    public final String RUN_COUNT = "RUN_COUNT";// 运行次数key
    private final int TIMER_DELAY_TIME = 2000;// 毫秒
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    // 定位布局
    public RelativeLayout layout_location;
    public ImageView img_location;
    public TextView text_location;
    public TextView text_location_tip;
    public LocationInfo lf;
    // 偏好设置
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    // 数据库保存成功后,使用这个view提示
    public ImageView img_guang;
    protected RefreshLocationInfo rlf;
    private Handler handler = new Handler();
    private Runnable runHideLocationTip = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (text_location_tip != null) {
                text_location_tip.setVisibility(View.GONE);
            }// 利用消息机制,将此段代码在UI线程中运行,就可以修改UI.
        }
    };

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);

        preferences = getSharedPreferences(Constants.SETTING_FILENAME,
                Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setOnRefreshLoaction(RefreshLocationInfo info) {
        this.rlf = info;
    }

    private String getVersionName() {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        Log.i("版本名称:", version);
        return version;
    }

    //
    // private Timer timer = new Timer();
    // private TimerTask taskHideLocationTip = new TimerTask() {
    //
    // @Override
    // public void run() {
    // // TODO Auto-generated method stub
    // if (text_location_tip != null) {
    // text_location_tip.setVisibility(View.GONE);
    // }// 不能在非UI线程,对视图进行操作.可以使用消息机制.handle
    // timer.cancel();
    // }
    // };

    public interface RefreshLocationInfo {
        public void onRefreshLocation(LocationInfo info);
    }

    /**
     * 实现实位回调监听
     */
    @SuppressLint("NewApi")
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // Receive Location
            // Log.i("Application", "test");
            // final StringBuffer sb = new StringBuffer(256);
            // sb.append(location.getAddrStr());
            // if (location == null) {
            // return;
            // }
            final String strLocation = location.getAddrStr();
            if (strLocation == null) {
                if (text_location != null) {
                    text_location.setText(R.string.str_location_fail);
                    if (text_location_tip != null) {
                        text_location_tip.setVisibility(View.VISIBLE);
                        text_location_tip
                                .setText(R.string.str_location_fail_tip);
                        // timer.schedule(taskHideLocationTip,
                        // TIMER_DELAY_TIME);//报错,无法修改UI
                        handler.postDelayed(runHideLocationTip,
                                TIMER_DELAY_TIME);
                    }
                }
                text_location.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        // mLocationClient.start();
                        int code = mLocationClient.requestLocation();
                        Log.i("请求位置返回代码", String.valueOf(code));
                        text_location.setText(R.string.str_location);
                    }
                });
                return;
            } else {
                lf = new LocationInfo();
                lf.strAddr = location.getAddrStr();
                lf.strProvince = location.getProvince(); // 获取省份信息
                lf.strCity = location.getCity(); // 获取城市信息
                lf.strDistrict = location.getDistrict(); // 获取区县信息
                Log.i("定位信息:", strLocation);
                if (text_location != null) {
                    text_location.setText(strLocation);
                }

                if (rlf != null) {
                    rlf.onRefreshLocation(lf);
                    mLocationClient.stop();
                    text_location
                            .setOnClickListener(new View.OnClickListener() {

                                @SuppressWarnings("deprecation")
                                @SuppressLint("NewApi")
                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    ClipboardManager cmb = (ClipboardManager) getApplicationContext()
                                            .getSystemService(
                                                    Context.CLIPBOARD_SERVICE);
                                    cmb.setText(strLocation);

                                    ToastTool
                                            .getRandomColToast(
                                                    getApplicationContext(),
                                                    "位置已复制剪切板");
                                }
                            });

                    if (text_location_tip != null
                            && !getVersionName().equals(
                            preferences.getString(VER_NAME, "0"))) {
                        text_location_tip.setVisibility(View.VISIBLE);
                        text_location_tip.setText(R.string.str_location_tip);
                        // timer.schedule(taskHideLocationTip,
                        // TIMER_DELAY_TIME);//报错,无法修改UI
                        handler.postDelayed(runHideLocationTip,
                                TIMER_DELAY_TIME);
                        Log.i("显示提示", text_location_tip.getText().toString());
                        editor.putString(VER_NAME, getVersionName()).commit();
                    }
                }

            }

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
            // if (location.getLocType() == BDLocation.TypeGpsLocation) {
            // sb.append("\nspeed : ");
            // sb.append(location.getSpeed());
            // sb.append("\nsatellite : ");
            // sb.append(location.getSatelliteNumber());
            // sb.append("\ndirection : ");
            // sb.append("\naddr : ");
            // sb.append(location.getAddrStr());
            // sb.append(location.getDirection());
            // } else if (location.getLocType() ==
            // BDLocation.TypeNetWorkLocation) {
            // sb.append("\naddr : ");
            // sb.append(location.getAddrStr());
            // // 运营商信息
            // sb.append("\noperationers : ");
            // sb.append(location.getOperators());
            // }
            // Log.i("BaiduLocationApiDem  Application", sb.toString());
        }
    }

    /**
     * 初始化省份
     */
    // public void initPList() {
    // // TODO Auto-generated method stub
    // dbManager = new DBManager(this);
    // dbManager.openDatabase();
    // db = dbManager.getDatabase();
    //
    // String sql = "select * from province";
    // Cursor cursor = db.rawQuery(sql, null);
    // cursor.moveToFirst();
    //
    // do {
    // ProvinceListItem listItem = new ProvinceListItem();
    // listItem.setCapital(cursor.getString(cursor
    // .getColumnIndex("capital")));
    // listItem.setCode(cursor.getString(cursor.getColumnIndex("code")));
    // listItem.setName(cursor.getString(cursor.getColumnIndex("name")));
    // listItem.setStrShort(cursor.getString(cursor
    // .getColumnIndex("short")));
    //
    // pList.add(listItem);
    //
    // } while (cursor.moveToNext());
    //
    // dbManager.closeDatabase();
    // db.close();
    //
    // }
    //
    // /**
    // * 根据指定的城区,初始化区域
    // *
    // * @param pcode
    // * 指定的城市代码
    // */
    // public void initDList(String pcode) {
    // // TODO Auto-generated method stub
    // dbManager = new DBManager(this);
    // dbManager.openDatabase();
    // db = dbManager.getDatabase();
    //
    // String sql = "select * from district where pcode='" + pcode + "'";
    // Cursor cursor = db.rawQuery(sql, null);
    // cursor.moveToFirst();
    // do {
    // DistrictListItem listItem = new DistrictListItem();
    // listItem.setCode(cursor.getString(cursor.getColumnIndex("code")));
    // listItem.setName(cursor.getString(cursor.getColumnIndex("name")));
    // listItem.setPcode(cursor.getString(cursor.getColumnIndex("pcode")));
    // listItem.setZip(cursor.getString(cursor.getColumnIndex("zip")));
    //
    // dList.add(listItem);
    //
    // } while (cursor.moveToNext());
    //
    // dbManager.closeDatabase();
    // db.close();
    // }
    //
    // /**
    // * 初始化指定省份的城市
    // *
    // * @param pcode
    // * 指定的省份代码
    // */
    // public void initCList(String pcode) {
    // // TODO Auto-generated method stub
    // dbManager = new DBManager(this);
    // dbManager.openDatabase();
    // db = dbManager.getDatabase();
    //
    // String sql = "select * from city where pcode='" + pcode + "'";
    // Cursor cursor = db.rawQuery(sql, null);
    // cursor.moveToFirst();
    // do {
    // CityListItem listItem = new CityListItem();
    //
    // listItem.setZone(cursor.getString(cursor.getColumnIndex("zone")));
    // listItem.setCode(cursor.getString(cursor.getColumnIndex("code")));
    // listItem.setName(cursor.getString(cursor.getColumnIndex("name")));
    // listItem.setPcode(cursor.getString(cursor.getColumnIndex("pcode")));
    //
    // cList.add(listItem);
    //
    // } while (cursor.moveToNext());
    //
    // dbManager.closeDatabase();
    // db.close();
    // }

    public class LocationInfo {
        public String strAddr;// 全名
        public String strProvince; // 获取省份信息
        public String strCity; // 获取城市信息
        public String strDistrict; // 获取区县信息
    }
}
