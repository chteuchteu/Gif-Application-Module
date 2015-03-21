package com.chteuchteu.gifapplicationlibrary.hlpr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.chteuchteu.gifapplicationlibrary.obj.Gif;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "gifsCache.db";

	// Table names
	private static final String TABLE_GIFS = "gifs";

	// Fields
	private static final String KEY_ID = "id";

	private static final String KEY_GIFS_NAME = "name";
	private static final String KEY_GIFS_ARTICLEURL = "articleUrl";
	private static final String KEY_GIFS_GIFURL = "gifUrl";
	private static final String KEY_GIFS_DATE = "date";

	private static final String CREATE_TABLE_GIFS = "CREATE TABLE " + TABLE_GIFS + " ("
			+ KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_GIFS_NAME + " TEXT,"
			+ KEY_GIFS_ARTICLEURL + " TEXT,"
			+ KEY_GIFS_GIFURL + " TEXT,"
			+ KEY_GIFS_DATE + " INTEGER)";

	private Context context;

	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_GIFS);

		// Delete "gifs" SharedPreference
		MainUtil.Prefs.removePref(context, "gifs");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

	private static void close(Cursor cursor, SQLiteDatabase db) {
		if (cursor != null)
			cursor.close();
		if (db != null)
			db.close();
	}

	/**
	 * Insert new gifs in DB
	 * @param gifs List of all gifs
	 * @param forceSave If true, don't mind if id==-1 and insert anyway
	 */
	public void saveGifs(List<Gif> gifs, boolean forceSave) {
		if (gifs.isEmpty())
			return;

		SQLiteDatabase db = this.getWritableDatabase();

		for (Gif gif : gifs) {
			if (!forceSave && gif.getId() != -1)
				continue;

			ContentValues values = new ContentValues();
			values.put(KEY_GIFS_NAME, gif.getName());
			values.put(KEY_GIFS_ARTICLEURL, gif.getArticleUrl());
			values.put(KEY_GIFS_GIFURL, gif.getGifUrl());
			values.put(KEY_GIFS_DATE, persistDate(gif.getDate()));

			gif.setId(db.insert(TABLE_GIFS, null, values));
		}

		close(null, db);
	}

	public List<Gif> getGifs() {
		List<Gif> gifs = new ArrayList<>();

		String selectQuery = "SELECT * FROM " + TABLE_GIFS + " ORDER BY " + KEY_GIFS_DATE + " DESC";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null && c.moveToFirst()) {
			do {
				gifs.add(new Gif(
						c.getInt(c.getColumnIndex(KEY_ID)),
						c.getString(c.getColumnIndex(KEY_GIFS_NAME)),
						c.getString(c.getColumnIndex(KEY_GIFS_ARTICLEURL)),
						c.getString(c.getColumnIndex(KEY_GIFS_GIFURL)),
						loadDate(c, c.getColumnIndex(KEY_GIFS_DATE))
				));
			} while(c.moveToNext());
		}

		close(c, db);

		return gifs;
	}

	public void removeGifs() {
		String query = "DELETE FROM " + TABLE_GIFS;
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_GIFS, null, null);
	}

	public static Long persistDate(Calendar calendar) {
		return calendar != null ? calendar.getTimeInMillis() : null;
	}

	public static Calendar loadDate(Cursor cursor, int index) {
		if (cursor.isNull(index))
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(cursor.getLong(index));
		return calendar;
	}
}
