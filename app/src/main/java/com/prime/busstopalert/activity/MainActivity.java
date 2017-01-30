package com.prime.busstopalert.activity;

import android.content.ContextWrapper;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.prime.busstopalert.R;
import com.prime.busstopalert.database.DatabaseHelper;
import com.prime.busstopalert.mflib.PrimeMMTextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {


    static String[] name;
    static int doing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PrimeMMTextView textView;
        textView = (PrimeMMTextView) findViewById(R.id.test);
        textView.setMMText(getResources().getString(R.string.test_text));
        Log.i("DATABASE EXIST : ", "" + checkDataBase());
        if (!checkDataBase())
            copyDataBase();

        DatabaseHelper dbHandaler = new DatabaseHelper(getApplicationContext());
        Cursor cursor = dbHandaler.getName();

//        if (cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            name = cursor.getString(cursor.getColumnIndex("BUS_STOP_NAME"));
//            textView.setMMText(name);
//            Log.i("name",name);
//        }

        cursor = dbHandaler.getName();
        Log.i("Length...", cursor.getCount() + "");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int i = 0;
            name = new String[cursor.getCount()];
            // fetch all data one by one
            do {
                name[i] = cursor.getString(cursor.getColumnIndex("BUS_STOP_NAME"));
                i++;
            } while (cursor.moveToNext());
            Log.i("length", name[0] + "");
        }
    }

    private void copyDataBase() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        String DB_PATH = "/data/data/com.prime.busstopalert/databases/";
        String DB_NAME = "BusStop";

        Log.i("Database", "New database is being copied to device!");
        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;
        // Open your local db as the input stream
        InputStream myInput = null;
        try {
            File filepath = new File(DB_PATH);
            if (!filepath.exists()) {
                filepath.mkdir();
            }
            myInput = MainActivity.this.getAssets().open(DB_NAME);
            // transfer bytes from the inputfile to the
            // outputfile
            myOutput = new FileOutputStream(DB_PATH + DB_NAME);
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            Log.i("Database", "New database has been copied to device!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean checkDataBase() {
        String DB_PATH = "/data/data/com.immortal.ayaypaw/databases/";
        String DB_NAME = "AyayPawData";
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }//
}
