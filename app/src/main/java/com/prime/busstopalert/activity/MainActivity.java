package com.prime.busstopalert.activity;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.prime.busstopalert.R;
import com.prime.busstopalert.adapter.MyAdapter;
import com.prime.busstopalert.database.DatabaseHelper;
import com.prime.busstopalert.location.LocService;
import com.prime.busstopalert.mflib.PrimeMMTextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

//    DatabaseHelper helpher;
//    List<BusStop> dbList;
//    RecyclerView mRecyclerView;
    protected CheckBox mCheckUsedGps;
    protected TextView mTextView;
    private LocationManager mLocationManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    static String[] vName;
    static double lat = 0.0;
    static double lng = 0.0;
    static double currentLat = 0.0;
    static double currentLng = 0.0;
    static int doing;
    public static Context appContext;
    ArrayAdapter<String> dataAdapter = null;
    android.widget.SearchView search;
    MyAdapter myAdapter;

    private LocationListener mLocationListener = new LocationListener() {


        @Override
        public void onLocationChanged(Location location) {

            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocationManager.removeUpdates(this);

            if (location != null) {
                if (mTextView != null) {
                    mTextView.setText("Lat : " + location.getLatitude() + " - Lng : " + location.getLongitude());
                    currentLat = location.getLatitude();
                    currentLng = location.getLongitude();
                }
                findNearest(currentLat, currentLng);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            if (mTextView != null) {
                mTextView.setText("GPS is enabled!");
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            if (mTextView != null) {
                mTextView.setText("GPS is not enabled!");
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_name_list_activity);
        PrimeMMTextView textView;
//        textView = (PrimeMMTextView) findViewById(R.id.test);
//        textView.setMMText(getResources().getString(R.string.test_text));
        Log.i("DATABASE EXIST : ", "" + checkDataBase());
        if (!checkDataBase())
            copyDataBase();

        DatabaseHelper dbHandaler = new DatabaseHelper(getApplicationContext());
        Typeface font = Typeface.createFromAsset(getAssets(), "zawgyi.ttf");

//        helpher = new DatabaseHelper(this);
//        dbList = new ArrayList<BusStop>();
//        dbList = helpher.getDatafromDB();
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        mRecyclerView.setHasFixedSize(true);
//
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        mAdapter = new RecyclerAdapter(this, dbList);
//        mAdapter.notifyDataSetChanged();
//
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int i) {
//                switch (i) {
//                    case 0:
//                        Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
//                        break;
//                }
//            }
//        }));

//        if (cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            name = cursor.getString(cursor.getColumnIndex("BUS_STOP_NAME"));
//            textView.setMMText(name);
//            Log.i("name",name);
//        }
        mTextView = (TextView) findViewById(R.id.textLocation);
        mCheckUsedGps = (CheckBox) findViewById(R.id.checkGpsOnly);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Button btnCheck = (Button) findViewById(R.id.buttonCheck);
        btnCheck.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //checkLocation();
                //findNearest(currentLat,currentLng);
                //loadService();
            }
        });

        final ListView listView = (ListView) findViewById(R.id.list_view);


        Cursor cursor = dbHandaler.getAllData();
        Log.i("Length...", cursor.getCount() + "");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int i = 0;
            vName = new String[cursor.getCount()];
            // fetch all data one by one
            do {
                vName[i] = cursor.getString(cursor.getColumnIndex("BUS_STOP_NAME"));
                i++;
            } while (cursor.moveToNext());
            Log.i("length", vName[0] + "");
        }

        dataAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item, R.id.lst_name, vName);

        myAdapter = new MyAdapter(MainActivity.this, vName);

        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                String itemValue = (String) listView.getItemAtPosition(position);
                itemValue.trim();
                //dbHandler.itemvalue =itemValue;

                // Toast.makeText(SearchActivity.this, "Position "+position+" ListItem "+itemValue, Toast.LENGTH_SHORT).show();

                //getMemberDetail(itemValue);

                DatabaseHelper dbHandaler = new DatabaseHelper(getApplicationContext());
                Cursor cursor = dbHandaler.getDetail(itemValue);
                Log.i("name", itemValue);

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    doing = 1;

                    lat = cursor.getDouble(cursor.getColumnIndex("LAT"));
                    lng = cursor.getDouble(cursor.getColumnIndex("LNG"));

                    Toast.makeText(MainActivity.this, "Detail " + itemValue + "\n" + lat + "\n" + lng + "\n", Toast.LENGTH_SHORT).show();

//                    final AppCompatDialog appCompatDialog = new AppCompatDialog(getApplicationContext(), R.style.Theme_CustomDialog);
//                    appCompatDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    appCompatDialog.setContentView(R.layout.dialog_profile);
//
//                    TextView tvName = (TextView) appCompatDialog.findViewById(R.id.textViewName);
//                    TextView tvID = (TextView) appCompatDialog.findViewById(R.id.textViewID);
//                    TextView tvPosition = (TextView) appCompatDialog.findViewById(R.id.textViewTeam);
//
//
//                    tvName.setText(itemValue);
//                    tvID.setText(String.valueOf(lat));
//                    tvPosition.setText(String.valueOf(lng));
//
//
//                    appCompatDialog.setCanceledOnTouchOutside(true);
//                    appCompatDialog.show();
                } else
                    doing = 0;
            }
        });


        search = (android.widget.SearchView) findViewById(R.id.search);
        int id = search.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView tv = (TextView) search.findViewById(id);
        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv.setHintTextColor(getResources().getColor(R.color.colorAccent));
        AutoCompleteTextView searchTextView = (AutoCompleteTextView) search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
        }
        search.setQueryHint("Type Name");

        //*** setOnQueryTextFocusChangeListener ***
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

//                Toast.makeText(getBaseContext(), String.valueOf(hasFocus),
//                        Toast.LENGTH_SHORT).show();
            }
        });
        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

//                Toast.makeText(getBaseContext(), query,
//                        Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub

                if (newText == "") {
                    String[] arr = new String[vName.length];
                    String[] name;
                    int j = 0;
                    for (int i = 0; i < vName.length; i++) {
                        if (vName[i].startsWith("ေ")) {
                            arr[j++] = vName[i];
                        }

                    }
                    name = new String[j];
                    for (int a = 0; a < j; a++) {
                        name[a] = arr[a];
                    }
                    dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, vName);
                    myAdapter = new MyAdapter(MainActivity.this, vName);
                    // final EditText det = (EditText) findViewById(R.id.editText);

                    listView.setAdapter(dataAdapter);
                } else {
                    MainActivity.this.dataAdapter.getFilter().filter(newText.toString());
                }
                return false;
            }
        });

//        cursor = dbHandaler.getName();
//        Log.i("Length...", cursor.getCount() + "");
//        if (cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            int i = 0;
//            name = new String[cursor.getCount()];
//            // fetch all data one by one
//            do {
//                name[i] = cursor.getString(cursor.getColumnIndex("BUS_STOP_NAME"));
//                i++;
//            } while (cursor.moveToNext());
//            Log.i("length", name[0] + "");
//        }
    }

    private void loadService() {
        MainActivity.appContext=MainActivity.this.getApplicationContext();
        Toast.makeText(MainActivity.this,"started",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, LocService.class);
        startService(intent);
    }

    protected void checkLocation() {
        if (mCheckUsedGps != null && mCheckUsedGps.isChecked()) {
            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);

                // ..service start..
                //loadService();

            } else {
                mTextView.setText("GPS is not enabled!");
            }
        } else {
            if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);

                // ..service start..
                //loadService();
            } else {
                mTextView.setText("Can't connect to internet!");
            }
        }
    }

    private void copyDataBase() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        String DB_PATH = "/data/data/com.prime.busstopalert/databases/";
        String DB_NAME = "BusStops";

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
        String DB_NAME = "BusStops";
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }//

    protected void findNearest(double x, double y) {

    }

    public static void runOnUI(Runnable runnable) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }
}
