package com.education.voicetranslator.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "translate_db";

	// table name
	public static final String TABLE_NAME = "Translate";
	public static final String KEY_ID = "ID";
	public static final String KEY_LANGUAGE_IN = "Language_In";
	public static final String KEY_LANGUAGE_OUT = "Language_Out";
	public static final String KEY_TEXT_IN = "Text_In";
	public static final String KEY_TEXT_OUT = "Text_Out";
	public static final String KEY_DOC_IN = "Doc_In";
	public static final String KEY_DOC_OUT = "Doc_Out";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_LANGUAGE_IN + " TEXT,"
				+ KEY_LANGUAGE_OUT + " TEXT," + KEY_TEXT_IN + " TEXT,"
				+ KEY_TEXT_OUT + " TEXT," + KEY_DOC_IN + " TEXT," + KEY_DOC_OUT
				+ " TEXT" + ")";
		db.execSQL(CREATE_TABLE);

	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHandler.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		// Create tables again
		onCreate(db);
	}

	public boolean isTableExists(SQLiteDatabase db, String tableName) {
		Cursor cursor = db.rawQuery(
				"select DISTINCT tbl_name from sqlite_master where tbl_name = '"
						+ tableName + "'", null);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		}
		return false;
	}

	public int getVersionDB() {
		return DATABASE_VERSION;
	}

}
