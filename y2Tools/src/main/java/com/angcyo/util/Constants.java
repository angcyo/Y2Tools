package com.angcyo.util;

public class Constants {
	public static final String SAVE_SETTING = "saveSetting";// 用于保存一些设置
	public static final long ANIM_DURATION = 200;// 动画默认持续时间

	public static final String DB_NAME = "birthday.db";
	public static final int DB_VERSION = 1;
	public static final String TABLE = "birthday";

	// 数据库的列名
	public static final String TABLE_COLUMN_MSG = "msg";
	public static final String TABLE_COLUMN_TITLE = "title";
	public static final String TABLE_COLUMN_DATTIME = "date_time";
	public static final String TABLE_COLUMN_ID = "_id";
	public static final String TABLE_COLUMN_TAG = "tag";

	// 数据库的语句
	public static final String CREATE_TABLE = "create table IF NOT EXISTS birthday "
			+ "(_id integer primary key autoincrement, title text, date_time varchar(20), msg text)";
	public static final String DRPO_TABLE = "drop table if exists birthday";
	public static final String INSERT_INFO = "INSERT INTO birthday VALUES(null, ?, ?, ?)";
	public static final String QUERY_ALL = "SELECT * FROM birthday";

	// 定位服务
	public static final String LOCATION = "location";
	public static final String LOCATION_ACTION = "locationAction";

	// 偏好设置
	public static final String SETTING_FILENAME = "Y2INI";

	public static final int TRUE = 1;
	public static final int FALSE = 0;

	// 豌豆荚广告
	public static final String ADS_APP_ID = "100015739";
	public static final String ADS_SECRET_KEY = "dbe6e03853022877a5c60cb828de1db7";

	public static final String TAG_LIST = "fb02bbcd15fcd27315c7306be800956a";
	public static final String TAG_INTERSTITIAL_FULLSCREEN = "f3285435c00a3234e4365da20888ff21";
	public static final String TAG_INTERSTITIAL_WIDGET = "f3285435c00a3234e4365da20888ff21";
	public static final String TAG_BANNER = "928157cda6c13bb9ef061cc7986de34d";

}
