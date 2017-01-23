package com.jedga95.reddit.reddits.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jedga95.reddit.reddits.json.model.RedditItem;

import java.util.ArrayList;

/**
 * Created by jesdga95 on 21/01/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reddit_database";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE reddits (_id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", url TEXT"
                + ", title TEXT"
                + ", thumb TEXT"
                + ", description TEXT"
                + ", banner TEXT"
                + ", content TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS reddits");
        onCreate(db);
    }

    public void addRedditEntry(RedditItem item) {
        ContentValues values = new ContentValues(6);
        values.put("url", item.getUrl());
        values.put("title", item.getDisplayName());
        values.put("thumb", item.getIconImgUrl());
        values.put("description", item.getDescription());
        values.put("banner", item.getBannerImageUrl());
        values.put("content", item.getContent());

        getWritableDatabase().insertOrThrow("reddits", null, values);

    }

    public ArrayList<RedditItem> getAllEntries() {
        ArrayList<RedditItem> items = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query("reddits",
                new String[] { "_id", "url", "title", "thumb", "description", "banner", "content"},
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                final RedditItem item = new RedditItem();
                item.setUrl(cursor.getString(1));
                item.setDisplayName(cursor.getString(2));
                item.setIconImgUrl(cursor.getString(3));
                item.setDescription(cursor.getString(4));
                item.setBannerImageUrl(cursor.getString(5));
                item.setContent(cursor.getString(6));

                items.add(item);

                cursor.moveToNext();
            }
        }
        return items;
    }

    public void clearAll() {
        getWritableDatabase().delete("reddits", null, null);
    }
}