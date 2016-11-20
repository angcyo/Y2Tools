package com.angcyo.util;

import java.util.Locale;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ToastTool {

	/**
	 * 返回指定大小的Html字符
	 * 
	 * @param source
	 * @param size
	 * @return
	 */
	public static CharSequence getHtml(String source, int size) {
		String strItem = String.format(Locale.CHINA, "<font size=%d>%s</font>",
				size, // 大小
				source); // 字符串
		return Html.fromHtml(strItem);
	}

	/**
	 * 返回指定颜色的Html字符
	 * 
	 * @param source
	 * @param col
	 * @return
	 */
	public static CharSequence getHtml(String source, String col) {
		String strItem = String.format(Locale.CHINA,
				"<font color=\"%s\">%s</font>", col, // 颜色
				source); // 字符串
		return Html.fromHtml(strItem);
	}

	/**
	 * 返回指定颜色在前的Html字符
	 * 
	 * @param source
	 *            需要变色的字符
	 * @param string
	 *            不需要变色的字符
	 * @param col
	 *            颜色
	 * @return
	 */
	public static CharSequence getHtml(String source, String string, String col) {
		String strItem = String.format(Locale.CHINA,
				"<font color=\"%s\">%s</font><font>%s</font>", col, // 颜色
				source, string); // 字符串
		return Html.fromHtml(strItem);
	}

	/**
	 * 返回颜色在后的Html字符
	 * 
	 * @param string
	 * @param source
	 * @param col
	 * @return
	 */
	public static CharSequence getHtml2(String string, String source, String col) {
		String strItem = String.format(Locale.CHINA,
				"<font>%s</font><font color=\"%s\">%s</font>", string, // 颜色
				col, source); // 字符串
		return Html.fromHtml(strItem);
	}

	/**
	 * 返回指定颜色大小在前的Html字符
	 * 
	 * @param source
	 * @param string
	 * @param col
	 * @param fSize
	 * @return
	 */
	public static CharSequence getHtml(String source, String string,
			String col, int fSize) {
		String strItem = String.format(
				"<font size=%d color=\"%s\">%s</font><font>%s</font>", fSize, // 字体大小
				col, // 颜色
				source, string); // 字符串

		return Html.fromHtml(strItem);
	}

	/**
	 * 获取一个居中显示的Toast,默认是显示时间Long
	 * 
	 * @param context
	 * @param charSequence
	 * @return
	 */
	public static Toast getCentertToast(Context context,
			CharSequence charSequence) {
		Toast toast = Toast.makeText(context, charSequence, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		return toast;
	}

	/**
	 * 获取一个居中显示的Toast,可以设置显示的时间
	 * 
	 * @param context
	 * @param charSequence
	 * @param lg
	 * @return
	 */
	public static Toast getCentertToast(Context context,
			CharSequence charSequence, int lg) {
		Toast toast = Toast.makeText(context, charSequence, lg);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		return toast;
	}

	/**
	 * 获取一个带有图片的toast,时间long
	 * 
	 * @param context
	 * @param resId
	 * @param charSequence
	 * @return
	 */
	public static Toast getImageToast(Context context, int resId,
			CharSequence charSequence) {
		Toast toast = Toast.makeText(context, charSequence, Toast.LENGTH_LONG);
		// toast.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout toastView = (LinearLayout) toast.getView();
		ImageView imageCodeProject = new ImageView(context);
		imageCodeProject.setImageResource(resId);
		toastView.addView(imageCodeProject, 0);
		toast.show();
		return toast;
	}

	/**
	 * 获取一个默认时长为long的toast
	 * 
	 * @param context
	 * @param charSequence
	 * @return
	 */
	public static Toast getToast(Context context, CharSequence charSequence) {
		Toast toast = Toast.makeText(context, charSequence, Toast.LENGTH_LONG);
		toast.show();
		return toast;
	}

	/**
	 * 获取一个带有颜色的Toast,时长为long
	 * 
	 * @param context
	 * @param charSequence
	 * @param col
	 * @return
	 */
	public static Toast getColToast(Context context, CharSequence charSequence,
			String col) {
		CharSequence sequence = ToastTool.getHtml(charSequence.toString(), "",
				col);
		Toast toast = Toast.makeText(context, sequence, Toast.LENGTH_LONG);
		toast.show();
		return toast;
	}

	/**
	 * 获取一个随机颜色的Toast
	 * 
	 * @param context
	 * @param charSequence
	 * @return
	 */
	public static Toast getRandomColToast(Context context,
			CharSequence charSequence) {
		CharSequence sequence = ToastTool.getHtml(charSequence.toString(), "",
				ColorTool.getRandomColorString2());
		Toast toast = Toast.makeText(context, sequence, Toast.LENGTH_LONG);
		toast.show();
		return toast;
	}

	/**
	 * 随机颜色,并且居中的toast,默认时长long
	 * 
	 * @param context
	 * @param charSequence
	 * @return
	 */
	public static Toast getRandomCentertColToast(Context context,
			CharSequence charSequence) {
		CharSequence sequence = ToastTool.getHtml(charSequence.toString(), "",
				ColorTool.getRandomColorString2());
		Toast toast = Toast.makeText(context, sequence, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		return toast;
	}

	/**
	 * 获取一个自定义视图的toast
	 * 
	 * @param context
	 * @param view
	 * @return
	 */
	public static Toast getCustomToast(Context context, View view) {
		// LayoutInflater inflater = getLayoutInflater();
		// View layout = inflater.inflate(R.layout.custom,
		// (ViewGroup) findViewById(R.id.llToast));
		// ImageView image = (ImageView) layout.findViewById(R.id.tvImageToast);
		// image.setImageResource(R.drawable.icon);
		// TextView title = (TextView) layout.findViewById(R.id.tvTitleToast);
		// title.setText("Attention");
		// TextView text = (TextView) layout.findViewById(R.id.tvTextToast);
		// text.setText("完全自定义Toast");

		Toast toast = new Toast(context);
		// toast.setGravity(Gravity.RIGHT | Gravity.TOP, 12, 40);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(view);
		return toast;

	}

}
