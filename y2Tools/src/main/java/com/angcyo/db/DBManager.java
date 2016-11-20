package com.angcyo.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class DBManager {

	public DBManager(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	private final int BUFFER_SIZE = 1024;
	public static final String DB_NAME = "y2cityinfo.sqlite";
	public static final String PACKAGE_NAME = "com.angcyo.y2tools";
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME;
	private SQLiteDatabase database;
	private Context context;
	private File file = null;

	public void openDatabase() {
		Log.e("cc", DB_PATH);
		this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
	}

	public SQLiteDatabase getDatabase() {
		return this.database;
	}

	private SQLiteDatabase openDatabase(String dbfile) {
		try {
			file = new File(dbfile);
			if (!file.exists()) {
				InputStream is = context.getResources().openRawResource(
						com.angcyo.y2tools.R.raw.y2cityinfo);
				if (is != null) {
					Log.e("cc", "is null");
				} else {
				}
				FileOutputStream fos = new FileOutputStream(dbfile);
				if (fos != null) {
				}
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
					fos.flush();
				}
				fos.close();
				is.close();
			}
			database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
			return database;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}
		return null;
	}

	public void closeDatabase() {
		if (this.database != null)
			this.database.close();
	}
}
