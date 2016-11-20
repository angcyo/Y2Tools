package com.angcyo.base;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;

import com.angcyo.base.BaseApplication.LocationInfo;
import com.angcyo.base.BaseApplication.RefreshLocationInfo;
import com.angcyo.db.CityAdapter;
import com.angcyo.db.CityListItem;
import com.angcyo.db.DBManager;
import com.angcyo.db.DistrictAdapter;
import com.angcyo.db.DistrictListItem;
import com.angcyo.db.MarkAdapter;
import com.angcyo.db.MarkListItem;
import com.angcyo.db.ProvinceAdapter;
import com.angcyo.db.ProvinceListItem;
import com.angcyo.util.UnitTool;
import com.angcyo.y2tools.R;

public class CityFragment extends Fragment implements RefreshLocationInfo {

	private DBManager dbManager;
	private SQLiteDatabase db;

	public List<CityListItem> cList = new ArrayList<CityListItem>();
	public List<DistrictListItem> dList = new ArrayList<DistrictListItem>();
	public List<ProvinceListItem> pList = new ArrayList<ProvinceListItem>();
	public List<MarkListItem> mList = new ArrayList<MarkListItem>();
	private Spinner cSpinner, dSpinner, pSpinner;
	private ListView mListView;

	private boolean isLoad = false;// 是否已经装载了数据
	private int LOAD_DATA_DELAY = 300;// 数据加载延迟时间
	private int currentPselect = 0;// pList的选择
	private int currentCselect = 0;// cList的选择
	private int currentDselect = 0;// dList的选择

	public CityFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);

		if (isVisibleToUser) {
			Log.i("CityFragment", "Show");
			handler.postDelayed(new loadRunnable(), LOAD_DATA_DELAY);
		} else {
			Log.i("CityFragment", "Hide");
		}
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		Log.i("CityFragment", "onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("CityFragment", "onCreate");
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.i("CityFragment", "onDestroyView");

		// pSpinner.setSelection(position);
		currentPselect = pSpinner.getSelectedItemPosition();// 视图销毁的时候,保存下列列表的选择位置
		currentCselect = cSpinner.getSelectedItemPosition();
		currentDselect = dSpinner.getSelectedItemPosition();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.i("CityFragment", "onDetach");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("CityFragment", "onPause");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("CityFragment", "onStrart");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("CityFragment", "onStop");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Log.i("CityFragment", "onResume");
		if (getUserVisibleHint()) {
			// handler.postDelayed(new loadRunnable(), LOAD_DATA_DELAY);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		Log.i("CityFragment", "onDestroy");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		Log.i("CityFragment", "onCreateView");
		isLoad = false;

		View rootView = inflater.inflate(R.layout.fragment_city, container,
				false);

		cSpinner = (Spinner) rootView.findViewById(R.id.spinner_city);
		dSpinner = (Spinner) rootView.findViewById(R.id.spinner_district);
		pSpinner = (Spinner) rootView.findViewById(R.id.spinner_province);

		mListView = (ListView) rootView.findViewById(R.id.list_car_mark);

		((BaseApplication) getActivity().getApplication())
				.setOnRefreshLoaction(this);

		handler.postDelayed(new loadRunnable(), LOAD_DATA_DELAY);
		return rootView;
	}

	/**
	 * 初始化省份
	 */
	public void initPList(boolean isReSet) {
		// TODO Auto-generated method stub
		dbManager = new DBManager(this.getActivity());
		dbManager.openDatabase();
		db = dbManager.getDatabase();

		pList.clear();

		String sql = "select * from province";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();

		do {
			ProvinceListItem listItem = new ProvinceListItem();
			listItem.setCapital(cursor.getString(cursor
					.getColumnIndex("capital")));
			listItem.setCode(cursor.getString(cursor.getColumnIndex("code")));
			listItem.setName(cursor.getString(cursor.getColumnIndex("name")));
			listItem.setStrShort(cursor.getString(cursor
					.getColumnIndex("short")));

			pList.add(listItem);

		} while (cursor.moveToNext());

		cursor.close();
		dbManager.closeDatabase();
		db.close();

		ProvinceAdapter pAdapter = new ProvinceAdapter(
				CityFragment.this.getActivity(), pList);
		pSpinner.setAdapter(pAdapter);
		pSpinner.setOnItemSelectedListener(new ProvinceItemListener());

		if (isReSet)
			pSpinner.setSelection(currentPselect);
	}

	/**
	 * 根据指定的城区,初始化区域
	 * 
	 * @param pcode
	 *            指定的城市代码
	 */
	public void initDList(String pcode, boolean isReSet) {
		// TODO Auto-generated method stub
		dbManager = new DBManager(this.getActivity());
		dbManager.openDatabase();
		db = dbManager.getDatabase();

		dList.clear();

		String sql = "select * from district where pcode='" + pcode + "'";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		do {
			DistrictListItem listItem = new DistrictListItem();
			listItem.setCode(cursor.getString(cursor.getColumnIndex("code")));
			listItem.setName(cursor.getString(cursor.getColumnIndex("name")));
			listItem.setPcode(cursor.getString(cursor.getColumnIndex("pcode")));
			listItem.setZip(cursor.getString(cursor.getColumnIndex("zip")));

			dList.add(listItem);

		} while (cursor.moveToNext());

		cursor.close();
		dbManager.closeDatabase();
		db.close();

		DistrictAdapter dAdapter = new DistrictAdapter(this.getActivity(),
				dList);
		dSpinner.setAdapter(dAdapter);
		dSpinner.setOnItemSelectedListener(new DistrictItemListener());

		if (isReSet)
			dSpinner.setSelection(currentDselect);
	}

	/**
	 * 初始化指定省份的城市
	 * 
	 * @param pcode
	 *            指定的省份代码
	 * @param isReSet
	 *            是否重置为上次视图销毁的状态
	 */
	public void initCList(String pcode, boolean isReSet) {
		// TODO Auto-generated method stub
		dbManager = new DBManager(this.getActivity());
		dbManager.openDatabase();
		db = dbManager.getDatabase();

		cList.clear();

		String sql = "select * from city where pcode='" + pcode + "'";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		do {
			CityListItem listItem = new CityListItem();

			listItem.setZone(cursor.getString(cursor.getColumnIndex("zone")));
			listItem.setCode(cursor.getString(cursor.getColumnIndex("code")));
			listItem.setName(cursor.getString(cursor.getColumnIndex("name")));
			listItem.setPcode(cursor.getString(cursor.getColumnIndex("pcode")));

			cList.add(listItem);

		} while (cursor.moveToNext());

		cursor.close();
		dbManager.closeDatabase();
		db.close();

		CityAdapter cAdapter = new CityAdapter(this.getActivity(), cList);
		cSpinner.setAdapter(cAdapter);
		cSpinner.setOnItemSelectedListener(new CityItemListener());

		if (isReSet) {
			cSpinner.setSelection(currentCselect);
		}
	}

	public void initMarkList(String pcode) {
		// TODO Auto-generated method stub
		dbManager = new DBManager(this.getActivity());
		dbManager.openDatabase();
		db = dbManager.getDatabase();

		mList.clear();

		String sql = "select * from mark where pcode='" + pcode + "'";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		do {
			MarkListItem listItem = new MarkListItem();

			listItem.setMark(cursor.getString(cursor.getColumnIndex("name")));
			listItem.setMarkdistrict(cursor.getString(cursor
					.getColumnIndex("markdistrict")));

			mList.add(listItem);

		} while (cursor.moveToNext());
		cursor.close();

		dbManager.closeDatabase();
		db.close();

		MarkAdapter mAdapter = new MarkAdapter(getActivity(), mList);

		mListView.setAdapter(mAdapter);
		// CityAdapter cAdapter = new CityAdapter(this.getActivity(), cList);
		// cSpinner.setAdapter(cAdapter);
		// cSpinner.setOnItemSelectedListener(new CityItemListener());
	}

	public class CityItemListener implements OnItemSelectedListener {

		public CityItemListener() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			CityListItem cItem = (CityListItem) parent
					.getItemAtPosition(position);
			initDList(cItem.getCode(), false);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}

	}

	public class DistrictItemListener implements OnItemSelectedListener {

		public DistrictItemListener() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}

	}

	public class ProvinceItemListener implements OnItemSelectedListener {

		public ProvinceItemListener() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			ProvinceListItem pItem = (ProvinceListItem) parent
					.getItemAtPosition(position);
			initCList(pItem.getCode(), false);
			initMarkList(pItem.getCode());
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}

	}

	Handler handler = new Handler();

	class pRunnable implements Runnable {

		String name;

		public pRunnable(String name) {
			super();
			this.name = name;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			pSpinner.setSelection(getProvinceIndexForList(name), true);

		}
	};

	class cRunnable implements Runnable {

		String name;

		public cRunnable(String name) {
			super();
			this.name = name;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			cSpinner.setSelection(getCityIndexForList(name), true);

		}
	};

	class dRunnable implements Runnable {
		String name;

		public dRunnable(String name) {
			super();
			this.name = name;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			dSpinner.setSelection(getDistrictIndexForList(name), true);
		}
	};

	@Override
	public void onRefreshLocation(LocationInfo info) {
		// TODO Auto-generated method stub
		Log.i("City_Fragment", info.strProvince + info.strCity
				+ info.strDistrict);
		handler.postDelayed(new pRunnable(info.strProvince), 1000);
		handler.postDelayed(new cRunnable(info.strCity), 2000);
		handler.postDelayed(new dRunnable(info.strDistrict), 3000);

		insertDatabase(info.strAddr, UnitTool.getDateAndTime());
	}

	public void insertDatabase(String location, String time) {
		dbManager = new DBManager(getActivity());
		dbManager.openDatabase();
		db = dbManager.getDatabase();

		String sql = "SELECT * from location";// 首先查询有多少行
		// String sqlInsert = "INSERT INTO location VALUES (?, ?, ?)";

		Cursor cursor = db.rawQuery(sql, null);
		// cursor.moveToFirst();
		int count = cursor.getCount();// 得到行数
		cursor.close();
		// cursor = db.rawQuery(sqlInsert, new String[] {
		// String.valueOf(++count),
		// location, time });
		ContentValues values = new ContentValues();
		values.put("id", ++count);
		values.put("location", location);
		values.put("time", time);
		values.put("devname", UnitTool.getDeviceName());
		values.put("telnum", UnitTool.getTelNumber(getActivity()));
		values.put("osver", UnitTool.getOsVersion());
		db.insert("location", null, values);

		dbManager.closeDatabase();
		// db.close();

		handler.postDelayed(new guangRunnable(), 2000);
	}

	class guangRunnable implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			toggleGuangAnim();
		}
	}

	class loadRunnable implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (!isLoad) {
				initPList(true);
				isLoad = true;
			}
			// initPList();

		}

	}

	public void toggleGuangAnim() {
		BaseApplication application = (BaseApplication) getActivity()
				.getApplication();

		if (application.img_guang == null) {
			return;
		}

		if (application.img_guang.getVisibility() == View.GONE) {
			application.img_guang.setVisibility(View.VISIBLE);
			AnimationDrawable drawable = (AnimationDrawable) application.img_guang
					.getDrawable();
			drawable.start();
		} else {
			AnimationDrawable drawable = (AnimationDrawable) application.img_guang
					.getDrawable();
			drawable.stop();
			application.img_guang.setVisibility(View.GONE);
		}

	}

	private int getProvinceIndexForList(String name) {
		for (int i = 0; i < pList.size(); i++) {
			if (name.equals(pList.get(i).getName())) {
				return i;
			}
		}
		return 0;

	}

	private int getCityIndexForList(String name) {
		for (int i = 0; i < cList.size(); i++) {
			if (name.equals(cList.get(i).getName())) {
				return i;
			}
		}
		return 0;

	}

	private int getDistrictIndexForList(String name) {
		for (int i = 0; i < dList.size(); i++) {
			if (name.equals(dList.get(i).getName())) {
				return i;
			}
		}
		return 0;

	}

}
