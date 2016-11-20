package com.angcyo.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.angcyo.ui.SecretTextView;
import com.angcyo.util.ColorTool;
import com.angcyo.util.UnitTool;
import com.angcyo.y2tools.R;

public class AboutFragment extends Fragment implements OnClickListener {

	private SecretTextView[] tx = new SecretTextView[7];
	private Animation[] anim = new Animation[7];
	private final String TAG = "AboutFragment";

	// tx1, tx2, tx3, tx4, tx5, tx6, txVer;

	public AboutFragment() {
		// TODO Auto-generated constructor stub

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onCreateView");
		View rootView = inflater.inflate(R.layout.fragment_about, container,
				false);

		tx[0] = (SecretTextView) rootView.findViewById(R.id.text_about1);
		tx[1] = (SecretTextView) rootView.findViewById(R.id.text_about2);
		tx[2] = (SecretTextView) rootView.findViewById(R.id.text_about3);
		tx[3] = (SecretTextView) rootView.findViewById(R.id.text_about4);
		tx[4] = (SecretTextView) rootView.findViewById(R.id.text_about5);
		tx[5] = (SecretTextView) rootView.findViewById(R.id.text_about6);
		tx[6] = (SecretTextView) rootView.findViewById(R.id.text_about_ver);

		for (SecretTextView view : tx) {
			view.setOnClickListener(this);
		}

		String str1 = getString(R.string.str_about_text1);
		String str2 = getString(R.string.str_about_text2);
		String str3 = getString(R.string.str_about_text3);
		String str4 = getString(R.string.str_about_text4);
		String str5 = getString(R.string.str_about_text5);
		String str6 = getString(R.string.str_about_text6);
		String strVer = "当前版本: " + UnitTool.getAppVersionName(getActivity());

		tx[0].setText(str1);
		tx[0].setTextColor(ColorTool.getRandomColorInt());
		tx[1].setText(str2);
		tx[1].setTextColor(ColorTool.getRandomColorInt2());
		tx[2].setText(str3);
		tx[2].setTextColor(ColorTool.getRandomColorInt());
		tx[3].setText(str4);
		tx[3].setTextColor(ColorTool.getRandomColorInt2());
		tx[4].setText(str5);
		tx[4].setTextColor(ColorTool.getRandomColorInt());
		tx[5].setText(str6);
		tx[5].setTextColor(ColorTool.getRandomColorInt2());
		tx[6].setText(strVer);
		tx[6].setTextColor(ColorTool.getRandomColorInt());

		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume");
		if (getUserVisibleHint()) {
			showAnim();
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			Log.i(TAG, "showAnim__setUser");
			showAnim();
		} else {
			Log.i(TAG, "hideAnim__setUser");
			hideAnim();
		}
	}

	private void hideAnim() {
		// TODO Auto-generated method stub

	}

	private void showAnim() {
		// TODO Auto-generated method stub
		// tx[0].setAnimation(animation);
		if (tx[0] != null) {
			for (int i = 0; i < tx.length; i++) {
				tx[i].setmDuration(4000);
				tx[i].setIsVisible(false);
				tx[i].toggle();
			}

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (UnitTool.isNetworkOK(getActivity())) {
			// interstitialAd.load();// 需要调用装载,才能显示
		}

	}
}
