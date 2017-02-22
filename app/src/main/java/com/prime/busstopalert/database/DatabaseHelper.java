package com.prime.busstopalert.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.prime.busstopalert.model.BusStop;

import java.util.ArrayList;
import java.util.List;

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
        DB.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"("+COL_1+" TEXT PRIMARY KEY, "+COL_2+" TEXT, "+COL_3+" DOUBLE, "+COL_4+" DOUBLE)");
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

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME, null);
    }

    public Cursor getDetail(String name){
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_2 + "='" + name + "'";
        Log.e("Select All from Member", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public List<BusStop> getDatafromDB(){
        List<BusStop> modelList = new ArrayList<>();
        String query  = "select * from "+TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do {
                BusStop model = new BusStop();
                model.setName(cursor.getString(1));
                model.setLat(cursor.getDouble(2));
                model.setLog(cursor.getDouble(3));

                modelList.add(model);
            }while (cursor.moveToNext());
        }
        Log.d("BusStop data: ",modelList.toString());

        return modelList;
    }
}
