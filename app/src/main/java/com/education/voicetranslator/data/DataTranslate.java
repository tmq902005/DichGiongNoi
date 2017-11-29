package com.education.voicetranslator.data;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.education.voicetranslator.ngonngu.item_translate;

public class DataTranslate extends DatabaseHandler {

	public DataTranslate(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	// Adding new item_translate
	public int additem_translate(item_translate item) {

		int ketqua;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_LANGUAGE_IN, item.getLagin());
		values.put(KEY_LANGUAGE_OUT, item.getLagout());
		values.put(KEY_TEXT_IN, item.getTextin());
		values.put(KEY_TEXT_OUT, item.getTextout());
		values.put(KEY_DOC_IN, item.getDocmauin());
		values.put(KEY_DOC_OUT, item.getDocmauout());
		// Inserting Row
		ketqua = (int) db.insert(TABLE_NAME, null, values);
		// db.close(); // Closing database connection
		return ketqua;

	}

	// Getting All item
	public ArrayList<item_translate> getAllItem() {
		ArrayList<item_translate> itemList = new ArrayList<item_translate>();
		// Select All Query

		// if(getItemCount()>0){

		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY "
				+ KEY_ID + " DESC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				item_translate item = new item_translate();
				item.setId(Integer.parseInt(cursor.getString(0)));
				item.setLagin(cursor.getString(1));
				item.setLagout(cursor.getString(2));
				item.setTextin(cursor.getString(3));
				item.setTextout(cursor.getString(4));
				item.setDocmauin(cursor.getString(5));
				item.setDocmauout(cursor.getString(6));

				// Adding chude to list
				itemList.add(item);
			} while (cursor.moveToNext());
		}
		// }
		// return chude list
		return itemList;
	}

	// Get item
	public item_translate getItem(String Textin) {
		// item_translate item = new item_translate();
		SQLiteDatabase db = this.getWritableDatabase();
		String[] thecolumns = new String[] { KEY_ID, KEY_LANGUAGE_IN,
				KEY_LANGUAGE_OUT, KEY_TEXT_IN, KEY_TEXT_OUT };
		Cursor cursor = db.query(TABLE_NAME, thecolumns, KEY_TEXT_IN + "='"
				+ Textin + "'", null, null, null, null);
		item_translate result;
		if (cursor != null) {
			cursor.moveToFirst();
			result = new item_translate(Integer.parseInt(cursor.getString(0)),
					cursor.getString(1), cursor.getString(2),
					cursor.getString(3), cursor.getString(4));
			return result;
		}

		return null;
	}

	// Getting item Count
	public int getItemCount() {
		String countQuery = "SELECT  * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		// return count
		return cursor.getCount();
	}

	// Delete item
	public int delItem(item_translate item) {
		SQLiteDatabase db = this.getWritableDatabase();
		int idxoa;
		idxoa = db.delete(TABLE_NAME, KEY_ID + " = ?",
				new String[] { String.valueOf(item.getId()) });
		return idxoa;
	}

	// Delete item
	public int delAllItem() {
		SQLiteDatabase db = this.getWritableDatabase();
		int idxoa;
		idxoa = db.delete(TABLE_NAME, null, null);
		return idxoa;
	}
}
