package com.angcyo.db;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class CarGridListItem {

	private String name;// 汽车的中文名称
	private Bitmap logo;// 汽车的图标
	private String url;// 汽车的介绍链接
	private int common;// 是否为常见汽车
	public int count;// 返回数据的数量

	public CarGridListItem() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Bitmap getLogo() {
		return logo;
	}

	public void setLogo(Bitmap logo) {
		this.logo = logo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getCommon() {
		return common;
	}

	public void setCommon(int common) {
		this.common = common;
	}

	public static void openUrl(Context context, String url) {
		Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		// it.setClassName("com.android.browser",
		// "com.android.browser.BrowserActivity");// 暂且不知道作用
		context.startActivity(it);
	}

	public static Bitmap getBmpForInputStream(InputStream is) {
		return BitmapFactory.decodeStream(is);
	}

	/**
	 * 打开assets 文件夹中的png文件
	 * 
	 * @param context
	 *            上下文
	 * @param filePath
	 *            文件路径,全路径 car/deguo/adele.png
	 * @return 返回文件流
	 * @throws IOException
	 */
	public static InputStream openAssetsPng(Context context, String filePath)
			throws IOException {
		AssetManager am = context.getAssets();

		return am.open(filePath);

	}

	/**
	 * @param context
	 *            上下文
	 * @param filePath
	 *            assets文件夹中的文件路径
	 * @return 返回bitmap对象
	 * @throws IOException
	 */
	public static Bitmap getAssetBitmap(Context context, String filePath)
			throws IOException {
		return getBmpForInputStream(openAssetsPng(context, filePath));
	}

	public static Bitmap getBmpForRes(Context context, int id) {
		return BitmapFactory.decodeResource(context.getResources(), id);
	}
}
