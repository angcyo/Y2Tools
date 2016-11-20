package com.angcyo.util;

import java.util.Random;

import android.R.integer;
import android.graphics.Color;

public class ColorTool {

	private static int preColInt = -1;

	private static String[] col = { "#56663E", "#A55928", "#7E3A36", "#481E1E",
			"#952633", "#0F403F", "#0C559E", "#EC8E2A", "#E8485A", "#5A9FB1",
			"#4C214A", "#73355C", "#2FB0C7", "#551B44", "#A41B53", "#134566",
			"#712423", "#9A3131", "#407039", "#C23F57", "#036160", "#982023",
			"#E84961", "#F1AC29", "#024C63" };// 深色集合

	private static String[] col2 = { "#F6EB77", "#60A7E7", "#05F1D6",
			"#3DB399", "#F7A6B5", "#7CC0D9", "#BEEEA9", "#991A1A", "#F5C904",
			"#E84418", "#D9D6CF", "#FE0000", "#FC991E", "#FAFF00", "#A4E501",
			"#00E0FC", "#1C85FD", "#AE20FF", "#FD22D8", "#7A1DDD", "#CB9CF4",
			"#FB6865", "#6CEEFC", "#D58AFD", "#FD8DEB" };// 亮色集合

	public ColorTool() {
		// TODO Auto-generated constructor stub
	}

	//获得深色随机
	public static int getRandomColorInt() {
		Random random = new Random();
		int col = random.nextInt(ColorTool.col.length);
		while (preColInt == col) {
			col = random.nextInt(ColorTool.col.length);
		}
		preColInt = col;
		return Color.parseColor(ColorTool.col[col]);
	}
	
	//获得亮色随机
	public static int getRandomColorInt2() {
		Random random = new Random();
		int col = random.nextInt(ColorTool.col2.length);
		while (preColInt == col) {
			col = random.nextInt(ColorTool.col2.length);
		}
		preColInt = col;
		return Color.parseColor(ColorTool.col2[col]);
	}

	//深色随机
	public static String getRandomColorString() {
		Random random = new Random();
		int col = random.nextInt(ColorTool.col.length);
		while (preColInt == col) {
			col = random.nextInt(ColorTool.col.length);
		}
		preColInt = col;
		return ColorTool.col[col];
	}
	
	//亮色随机
	public static String getRandomColorString2() {
		Random random = new Random();
		int col = random.nextInt(ColorTool.col2.length);
		while (preColInt == col) {
			col = random.nextInt(ColorTool.col2.length);
		}
		preColInt = col;
		return ColorTool.col2[col];
	}

}
