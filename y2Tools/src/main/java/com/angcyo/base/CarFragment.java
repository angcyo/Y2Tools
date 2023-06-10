package com.angcyo.base;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.angcyo.db.CarGridAdapter;
import com.angcyo.db.CarGridListItem;
import com.angcyo.db.DBManager;
import com.angcyo.ui.SwitchButton;
import com.angcyo.util.ColorTool;
import com.angcyo.util.ToastTool;
import com.angcyo.util.UnitTool;
import com.angcyo.y2tools.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class CarFragment extends Fragment {

    public final String ZG = "zg", DG = "dg", FG = "fg", HG = "hg", MG = "mg",
            QT = "qt", RB = "rb", RD = "rd", YDL = "ydl", YG = "yg";
    private final String TAG = "CarFragment";
    public DBManager dbManager;
    public SQLiteDatabase db;
    public List<CarGridListItem> zgList = new ArrayList<CarGridListItem>();
    public List<CarGridListItem> mgList = new ArrayList<CarGridListItem>();
    public List<CarGridListItem> hgList = new ArrayList<CarGridListItem>();
    public List<CarGridListItem> ygList = new ArrayList<CarGridListItem>();
    public List<CarGridListItem> fgList = new ArrayList<CarGridListItem>();
    public List<CarGridListItem> ydlList = new ArrayList<CarGridListItem>();
    public List<CarGridListItem> rbList = new ArrayList<CarGridListItem>();
    public List<CarGridListItem> rdList = new ArrayList<CarGridListItem>();
    //	public Context context;
    public List<CarGridListItem> qtList = new ArrayList<CarGridListItem>();
    public List<CarGridListItem> dgList = new ArrayList<CarGridListItem>();
    public boolean isLoad = false;// 是否已经装载了数据
    public boolean isShowAll = false;// 是否加载所有车标
    public int LOAD_DATA_DELAY = 300;// 数据加载延迟时间
    public int LOAD_DATA_DELAY_LONG = 600;// 数据加载延迟时间
    public LoadViewHolder loadHolder = null;
    public SwitchButton switchButton;// 开关按钮
    Handler handler = new Handler();
    Runnable runLoadData = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            initGridData();
            isLoad = true;

        }
    };
    private GridView gvZG, gvDG, gvFG, gvHG, gvMG, gvQT, gvRB, gvRD, gvYDL,
            gvYG;// 中国,德国,法国,韩国,美国,其他,日本,瑞典,意大利,英国
    Runnable getDbDataRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Log.i(TAG, "getDbDataRunnable 开始读取数据库");
            getDataForDb(zgList, ZG, isShowAll);// 加载数据
            getDataForDb(dgList, DG, isShowAll);
            getDataForDb(fgList, FG, isShowAll);
            getDataForDb(mgList, MG, isShowAll);
            getDataForDb(ygList, YG, isShowAll);
            getDataForDb(hgList, HG, isShowAll);
            getDataForDb(rbList, RB, isShowAll);
            getDataForDb(ydlList, YDL, isShowAll);
            getDataForDb(rdList, RD, isShowAll);
            getDataForDb(qtList, QT, isShowAll);
            Log.i(TAG, "getDbDataRunnable 结束读取数据库");

            getActivity().runOnUiThread(new loadFinish());
        }
    };
    private TextView txZG, txDG, txFG, txHG, txMG, txQT, txRB, txRD, txYDL,
            txYG;
    private GridView[] grids = new GridView[10];
    private View adBannerView;

    public CarFragment() {
        // TODO Auto-generated constructor stub
        //this.context = this.getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCraete");

    }

    void showBannerAd(ViewGroup containerView) {
        if (adBannerView != null
                && containerView.indexOfChild(adBannerView) >= 0) {
            containerView.removeView(adBannerView);
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        Log.i(TAG, "onResume");
        if (getUserVisibleHint()) {
            showSwitchButton();
        } else {
            hideSwitchButton();
        }

        if (getUserVisibleHint() && !isLoad) {
            handler.postDelayed(new startLoad(), LOAD_DATA_DELAY);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            showSwitchButton();
        } else {
            hideSwitchButton();
        }

        if (isVisibleToUser && !isLoad) {
            // 装载数据
            handler.postDelayed(new startLoad(), LOAD_DATA_DELAY);
            // new loadDataAsync().execute();// 异步执行
        }
    }

    private void hideSwitchButton() {
        // TODO Auto-generated method stub
        if (switchButton != null) {
            switchButton.setVisibility(View.GONE);
        }
    }

    private void showSwitchButton() {
        // TODO Auto-generated method stub
        if (switchButton != null) {
            switchButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.fragment_car, container,
                false);

        gvZG = (GridView) rootView.findViewById(R.id.grid_car_zg);
        grids[0] = gvZG;
        gvDG = (GridView) rootView.findViewById(R.id.grid_car_dg);
        grids[1] = gvDG;
        gvFG = (GridView) rootView.findViewById(R.id.grid_car_fg);
        grids[2] = gvFG;
        gvHG = (GridView) rootView.findViewById(R.id.grid_car_hg);
        grids[3] = gvHG;
        gvMG = (GridView) rootView.findViewById(R.id.grid_car_mg);
        grids[4] = gvMG;
        gvQT = (GridView) rootView.findViewById(R.id.grid_car_qt);
        grids[5] = gvQT;
        gvRB = (GridView) rootView.findViewById(R.id.grid_car_rb);
        grids[6] = gvRB;
        gvRD = (GridView) rootView.findViewById(R.id.grid_car_rd);
        grids[7] = gvRD;
        gvYDL = (GridView) rootView.findViewById(R.id.grid_car_ydl);
        grids[8] = gvYDL;
        gvYG = (GridView) rootView.findViewById(R.id.grid_car_yg);
        grids[9] = gvYG;

        txZG = (TextView) rootView.findViewById(R.id.text_car_zg);
        txDG = (TextView) rootView.findViewById(R.id.text_car_dg);
        txFG = (TextView) rootView.findViewById(R.id.text_car_fg);
        txHG = (TextView) rootView.findViewById(R.id.text_car_hg);
        txMG = (TextView) rootView.findViewById(R.id.text_car_mg);
        txQT = (TextView) rootView.findViewById(R.id.text_car_qt);
        txRB = (TextView) rootView.findViewById(R.id.text_car_rb);
        txRD = (TextView) rootView.findViewById(R.id.text_car_rd);
        txYDL = (TextView) rootView.findViewById(R.id.text_car_ydl);
        txYG = (TextView) rootView.findViewById(R.id.text_car_yg);

        loadHolder = new LoadViewHolder();
        loadHolder.layout = (RelativeLayout) rootView
                .findViewById(R.id.layout_load_data);
        loadHolder.img = (ImageView) rootView.findViewById(R.id.img_load_anim);
        loadHolder.text = (TextView) rootView.findViewById(R.id.text_load_tip);
        // Log.i(TAG, "onCraeteView");

        if (switchButton != null) {
            switchButton
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            // TODO Auto-generated method stub
                            isShowAll = isChecked;
                            handler.postDelayed(new startLoad(),
                                    LOAD_DATA_DELAY);
                            if (isChecked) {
                                switchButton.setText(UnitTool.getStringForRes(
                                        getActivity(),
                                        R.string.str_switch_show_all));
                            } else {
                                switchButton.setText(UnitTool.getStringForRes(
                                        getActivity(),
                                        R.string.str_switch_show_common));
                            }

                        }
                    });
        }

//		ViewGroup viewGroup = (ViewGroup) rootView
//				.findViewById(R.id.banner_ad_container1);

        if (UnitTool.isNetworkOK(getActivity())) {
            //showBannerAd(viewGroup);
        }

        return rootView;
    }

    private void initGridData() {
        // for (int i = 1; i < grids.length; i++) {
        // grids[i].setAdapter(new CarGridAdapter(getActivity(), getData()));
        // // grids[i].no
        // }
        // gvZG.setAdapter(new CarGridAdapter(getActivity(), getData()));

        // Thread thread = new
        // Thread(getDbDataRunnable);//无法在UI之外的线程更新UI,呵呵要用异步任务类了
        // thread.start();
        new Thread(getDbDataRunnable).start();
//        handler.post(getDbDataRunnable);
//        handler.postDelayed(new loadFinish(), LOAD_DATA_DELAY_LONG);
    }

    /**
     * @param strCountry 要查询的国家
     * @param isShowAll  是否显示全部
     */
    public String getDataForDb(List<CarGridListItem> list, final String strCountry,
                               boolean isShowAll) {
        Log.i(TAG, "getDataForDb 准备读取数据库");
        dbManager = new DBManager(this.getActivity());
        dbManager.openDatabase();
        db = dbManager.getDatabase();

        list.clear();

        Log.i(TAG, "test1" + strCountry);

        String sql = "SELECT * from car WHERE country='" + strCountry + "'";
        if (!isShowAll) {
            sql = "SELECT * from car WHERE common=1 and country='" + strCountry
                    + "'";
        }
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        do {
            CarGridListItem listItem = new CarGridListItem();
            String strFilePath;
            listItem.count = cursor.getCount();
            listItem.setCommon(cursor.getInt(cursor.getColumnIndex("common")));
            strFilePath = cursor.getString(cursor.getColumnIndex("logo"));
            Bitmap bm = null;
            try {
                bm = CarGridListItem.getAssetBitmap(this.getActivity(), strFilePath);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            if (bm == null) {
                listItem.setLogo(CarGridListItem.getBmpForRes(this.getActivity(),
                        R.drawable.y2logo));
            } else {
                listItem.setLogo(bm);
            }
            listItem.setName(cursor.getString(cursor.getColumnIndex("name")));
            listItem.setUrl(cursor.getString(cursor.getColumnIndex("url")));

            list.add(listItem);

        } while (cursor.moveToNext());

        cursor.close();
        dbManager.closeDatabase();
        db.close();

        Log.i(TAG, "test2" + strCountry);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setGridAdapter(strCountry);
            }
        });
        return strCountry;
    }

    public void setGridAdapter(String strCountry) {
        CarGridAdapter zgAdapter = new CarGridAdapter(this.getActivity(), zgList);
        CarGridAdapter dgAdapter = new CarGridAdapter(this.getActivity(), dgList);
        CarGridAdapter hgAdapter = new CarGridAdapter(this.getActivity(), hgList);
        CarGridAdapter ygAdapter = new CarGridAdapter(this.getActivity(), ygList);
        CarGridAdapter mgAdapter = new CarGridAdapter(this.getActivity(), mgList);
        CarGridAdapter fgAdapter = new CarGridAdapter(this.getActivity(), fgList);
        CarGridAdapter rdAdapter = new CarGridAdapter(this.getActivity(), rdList);
        CarGridAdapter rbAdapter = new CarGridAdapter(this.getActivity(), rbList);
        CarGridAdapter qtAdapter = new CarGridAdapter(this.getActivity(), qtList);
        CarGridAdapter ydlAdapter = new CarGridAdapter(this.getActivity(), ydlList);

        if (strCountry == ZG)
            gvZG.setAdapter(zgAdapter);
        if (strCountry == DG)
            gvDG.setAdapter(dgAdapter);
        if (strCountry == HG)
            gvHG.setAdapter(hgAdapter);
        if (strCountry == RB)
            gvRB.setAdapter(rbAdapter);
        if (strCountry == YDL)
            gvYDL.setAdapter(ydlAdapter);
        if (strCountry == YG)
            gvYG.setAdapter(ygAdapter);
        if (strCountry == RD)
            gvRD.setAdapter(rdAdapter);
        if (strCountry == QT)
            gvQT.setAdapter(qtAdapter);
        if (strCountry == MG)
            gvMG.setAdapter(mgAdapter);
        if (strCountry == FG)
            gvFG.setAdapter(fgAdapter);

        gvZG.setOnItemClickListener(new itemClick());
        gvHG.setOnItemClickListener(new itemClick());
        gvFG.setOnItemClickListener(new itemClick());
        gvYG.setOnItemClickListener(new itemClick());
        gvMG.setOnItemClickListener(new itemClick());
        gvDG.setOnItemClickListener(new itemClick());
        gvRB.setOnItemClickListener(new itemClick());
        gvRD.setOnItemClickListener(new itemClick());
        gvYDL.setOnItemClickListener(new itemClick());
        gvQT.setOnItemClickListener(new itemClick());
    }

    private List<CarGridListItem> getData() throws IOException {
        List<CarGridListItem> list = new ArrayList<CarGridListItem>();
        // for (int i = 0; i < grids.length; i++) {
        // CarGridListItem item = new CarGridListItem();
        // item.name = "阿斯顿马丁";
        // item.logo = CarGridListItem.getAssetBitmap(getActivity(),
        // "car/deguo/dazhong.png");
        //
        // list.add(item);
        // }

        return list;
    }

    // 设置标题,用于显示输出的数据数量
    private void setCountryTitle() {
        // TODO Auto-generated method stub
        // 这代码,我自己看着都疼,但是没有找到其他方法.太笨了! come on
        int zgC = zgList.get(0).count;
        int hgC = hgList.get(0).count;
        int mgC = mgList.get(0).count;
        int ygC = ygList.get(0).count;
        int fgC = fgList.get(0).count;
        int dgC = dgList.get(0).count;
        int rbC = rbList.get(0).count;
        int rdC = rdList.get(0).count;
        int ydlC = ydlList.get(0).count;
        int qtC = qtList.get(0).count;

        String zgS = txZG.getText().toString().substring(0, 2);
        String dgS = txDG.getText().toString().substring(0, 2);
        String fgS = txFG.getText().toString().substring(0, 2);
        String ygS = txYG.getText().toString().substring(0, 2);
        String hgS = txHG.getText().toString().substring(0, 2);
        String mgS = txMG.getText().toString().substring(0, 2);
        String rdS = txRD.getText().toString().substring(0, 2);
        String rbS = txRB.getText().toString().substring(0, 2);
        String ydlS = txYDL.getText().toString().substring(0, 3);
        String qtS = txQT.getText().toString().substring(0, 2);

        String zgSe = "  "
                + String.valueOf(zgC)
                + "/75&nbsp;&nbsp;&nbsp;&nbsp;比亚迪 &nbsp;&nbsp;奇瑞 &nbsp;&nbsp;帝豪&nbsp;&nbsp;瑞麒&nbsp;&nbsp;北京汽车&nbsp;&nbsp;吉利&nbsp;&nbsp;东风";
        String hgSe = "  "
                + String.valueOf(hgC)
                + "/4&nbsp;&nbsp;&nbsp;&nbsp;现代&nbsp;&nbsp;起亚&nbsp;&nbsp;双龙&nbsp;&nbsp;大宇";
        String dgSe = "  "
                + String.valueOf(dgC)
                + "/46&nbsp;&nbsp;&nbsp;&nbsp;大众&nbsp;&nbsp;宝马&nbsp;&nbsp;奔驰&nbsp;&nbsp;奥迪&nbsp;&nbsp;保时捷&nbsp;&nbsp;迈巴赫&nbsp;&nbsp;Mini";
        String mgSe = "  "
                + String.valueOf(mgC)
                + "/35&nbsp;&nbsp;&nbsp;&nbsp;福特&nbsp;&nbsp;卡迪拉克&nbsp;&nbsp;别克&nbsp;&nbsp;悍马&nbsp;&nbsp;特斯拉&nbsp;&nbsp;雪佛兰&nbsp;&nbsp;克莱斯勒";
        String ygSe = "  "
                + String.valueOf(ygC)
                + "/44&nbsp;&nbsp;&nbsp;&nbsp;路虎&nbsp;&nbsp;宾利&nbsp;&nbsp;阿斯顿马丁&nbsp;&nbsp;捷豹&nbsp;&nbsp;劳斯莱斯&nbsp;&nbsp;名爵";
        String fgSe = "  "
                + String.valueOf(fgC)
                + "/45&nbsp;&nbsp;&nbsp;&nbsp;布加迪&nbsp;&nbsp;标致&nbsp;&nbsp;雷诺&nbsp;&nbsp;DS";
        String rbSe = "  "
                + String.valueOf(rbC)
                + "/30&nbsp;&nbsp;&nbsp;&nbsp;本田&nbsp;&nbsp;丰田&nbsp;&nbsp;英菲尼迪&nbsp;&nbsp;讴歌&nbsp;&nbsp;雷克萨斯&nbsp;&nbsp;马自达&nbsp;&nbsp;NISSAN";
        String rdSe = "  " + String.valueOf(rdC)
                + "/4&nbsp;&nbsp;&nbsp;&nbsp;沃尔沃&nbsp;&nbsp;科尼塞克";
        String ydlSe = "  "
                + String.valueOf(ydlC)
                + "/41&nbsp;&nbsp;&nbsp;&nbsp;兰博基尼&nbsp;&nbsp;法拉利&nbsp;&nbsp;玛莎拉蒂&nbsp;&nbsp;菲亚特&nbsp;&nbsp;阿尔法罗密欧";
        String qtSe = "  " + String.valueOf(qtC)
                + "/40&nbsp;&nbsp;&nbsp;&nbsp;找不出认识的品牌,sorry...";

        CharSequence zgT = ToastTool.getHtml2(zgS, zgSe,
                ColorTool.getRandomColorString2());
        CharSequence hgT = ToastTool.getHtml2(hgS, hgSe,
                ColorTool.getRandomColorString2());
        CharSequence mgT = ToastTool.getHtml2(mgS, mgSe,
                ColorTool.getRandomColorString2());
        CharSequence ygT = ToastTool.getHtml2(ygS, ygSe,
                ColorTool.getRandomColorString2());
        CharSequence fgT = ToastTool.getHtml2(fgS, fgSe,
                ColorTool.getRandomColorString2());
        CharSequence dgT = ToastTool.getHtml2(dgS, dgSe,
                ColorTool.getRandomColorString2());
        CharSequence rbT = ToastTool.getHtml2(rbS, rbSe,
                ColorTool.getRandomColorString2());
        CharSequence rdT = ToastTool.getHtml2(rdS, rdSe,
                ColorTool.getRandomColorString2());
        CharSequence ydlT = ToastTool.getHtml2(ydlS, ydlSe,
                ColorTool.getRandomColorString2());
        CharSequence qtT = ToastTool.getHtml2(qtS, qtSe,
                ColorTool.getRandomColorString2());

        txZG.setText(zgT);
        txHG.setText(hgT);
        txMG.setText(mgT);
        txYG.setText(ygT);
        txDG.setText(dgT);
        txFG.setText(fgT);
        txRB.setText(rbT);
        txRD.setText(rdT);
        txYDL.setText(ydlT);
        txQT.setText(qtT);
    }

    private class itemClick implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub
            CarGridListItem item = (CarGridListItem) parent
                    .getItemAtPosition(position);

            String url = item.getUrl();
            if (url == null || url == "" || url.isEmpty()) {
                ToastTool.getCentertToast(getActivity(), ToastTool.getHtml(
                        UnitTool.getRandomStringForUrl(getActivity()),
                        ColorTool.getRandomColorString2()), Toast.LENGTH_SHORT);
            } else {
                CarGridListItem.openUrl(getActivity(), url);
            }
        }
    }

    class loadDataAsync extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            // String strCountry;
            // strCountry = getDataForDb(zgList, ZG, isShowAll);// 加载数据
            // publishProgress(strCountry);
            // strCountry = getDataForDb(dgList, DG, isShowAll);
            // publishProgress(strCountry);
            // strCountry = getDataForDb(fgList, FG, isShowAll);
            // publishProgress(strCountry);
            // strCountry = getDataForDb(mgList, MG, isShowAll);
            // publishProgress(strCountry);
            // strCountry = getDataForDb(ygList, YG, isShowAll);
            // publishProgress(strCountry);
            // strCountry = getDataForDb(hgList, HG, isShowAll);
            // publishProgress(strCountry);
            // strCountry = getDataForDb(rbList, RB, isShowAll);
            // publishProgress(strCountry);
            // strCountry = getDataForDb(ydlList, YDL, isShowAll);
            // publishProgress(strCountry);
            // strCountry = getDataForDb(rdList, RD, isShowAll);
            // publishProgress(strCountry);
            // strCountry = getDataForDb(qtList, QT, isShowAll);
            // publishProgress(strCountry);
            return null;
        }

        @Override
        protected void onProgressUpdate(String... strs) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(strs);

            // setGridAdapter(strs[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            // 执行完成后,在ui线程
            handler.postDelayed(new loadFinish(), LOAD_DATA_DELAY_LONG);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            // 执行之前,在ui线程
            loadHolder.layout.setVisibility(View.VISIBLE);// 显示数据加载提示
            AnimationDrawable animDrawble = (AnimationDrawable) loadHolder.img
                    .getDrawable();
            animDrawble.start();
            loadHolder.text.setText(UnitTool.getRandomString(getActivity()));
        }

    }

    class startLoad implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (loadHolder != null) {
                Log.i(TAG, "runLoadData3");
                loadHolder.layout.setVisibility(View.VISIBLE);// 显示数据加载提示
                AnimationDrawable animDrawble = (AnimationDrawable) loadHolder.img
                        .getDrawable();
                animDrawble.start();
                loadHolder.text
                        .setText(UnitTool.getRandomString(getActivity()));

                handler.postDelayed(runLoadData, LOAD_DATA_DELAY);
            }
        }

    }

    class loadFinish implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (loadHolder == null) {
                return;
            }
            loadHolder.layout.setVisibility(View.GONE);//
            AnimationDrawable animDrawble = (AnimationDrawable) loadHolder.img
                    .getDrawable();
            animDrawble.stop();
            // loadHolder.text.setText(UnitTool.getRandomString(getActivity()));

            setCountryTitle();
        }

    }

    class tipRunnable implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub

        }

    }

    private class LoadViewHolder {
        RelativeLayout layout;// 根布局
        ImageView img;// 图片
        TextView text;// 文字提示
    }
}
