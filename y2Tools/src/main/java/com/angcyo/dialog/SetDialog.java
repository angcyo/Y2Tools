package com.angcyo.dialog;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.angcyo.y2tools.MainActivity;

public class SetDialog extends AlertDialog {

	private Context context;
	private TextView tx1, tx2, tx3;
	private int code = 0;

	private Timer timer;
	private TimerTask task;

	public SetDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public SetDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public SetDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = LayoutInflater.from(context);

		View rootView = inflater.inflate(
				com.angcyo.y2tools.R.layout.set_main_layout, null);
		// setContentView(com.angcyo.y2tools.R.layout.dlg_exit);
		setContentView(rootView);

		// init();
		// initTimer();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		// java.lang.System.exit(1);
	}
}
