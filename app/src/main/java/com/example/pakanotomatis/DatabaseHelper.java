package com.example.pakanotomatis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "jadwal_db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_JADWAL = "jadwal";
    public static final String COL_ID = "id";
    public static final String COL_JUDUL = "judul";
    public static final String COL_JAM = "jam";
    public static final String COL_AKTIF = "aktif";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_JADWAL + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_JUDUL + " TEXT, " +
                COL_JAM + " TEXT, " +
                COL_AKTIF + " INTEGER)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JADWAL);
        onCreate(db);
    }

    public void insertJadwal(String judul, String jam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_JUDUL, judul);
        values.put(COL_JAM, jam);
        values.put(COL_AKTIF, 1);
        db.insert(TABLE_JADWAL, null, values);
        db.close();
    }

    public ArrayList<Jadwal> getAllJadwal() {
        ArrayList<Jadwal> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_JADWAL, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Jadwal(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3) == 1
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void updateAktif(int id, boolean aktif) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_AKTIF, aktif ? 1 : 0);
        db.update(TABLE_JADWAL, values, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteJadwal(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_JADWAL, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}


