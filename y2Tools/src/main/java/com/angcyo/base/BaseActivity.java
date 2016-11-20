package com.angcyo.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.angcyo.util.ColorTool;
import com.angcyo.util.ToastTool;

public class BaseActivity extends Activity {

	private long exitTime = 0;

	public BaseActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		if ((System.currentTimeMillis() - exitTime) > 2000) {

			ToastTool.getCentertToast(
					BaseActivity.this,
					ToastTool.getHtml("再按一次", "退出...",
							ColorTool.getRandomColorString2()),
					Toast.LENGTH_SHORT);
			exitTime = System.currentTimeMillis();
		} else {
			// super.onBackPressed();
			finish();
			System.exit(0);
		}
	}

}
