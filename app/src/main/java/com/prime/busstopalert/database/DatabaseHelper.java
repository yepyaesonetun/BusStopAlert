package com.prime.busstopalert.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SantaClaus on 29/01/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BusStops";
    private static final String TABLE_NAME = "busstop";
    public static final String COL_1 = "_id";
    public static final String COL_2 = "BUS_STOP_NAME";
    public static final String COL_3 = "LAT";
    public static final String COL_4 = "LNG";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"("+COL_1+" TEXT PRIMARY KEY AUTOINCREMENT, "+COL_2+" TEXT, "+COL_3+" DOUBLE, "+COL_4+" DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getName(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select "+COL_2+" from " + TABLE_NAME, null);
    }
}
