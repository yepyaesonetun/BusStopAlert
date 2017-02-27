package com.prime.busstopalert.location;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.prime.busstopalert.activity.MainActivity;


public class LocService extends Service {
	
    private static final String TAG = "LocService";
    GPSTracker mGpsTracker;
   // private LocationManager mLocationManager;
   // LocationListener mLocationListener;
    public void toast(String text){
    	Log.d(TAG, text);
    	final String t =text;
        //Toast.makeText(MainActivity.appContext,t, Toast.LENGTH_SHORT).show();
    	MainActivity.runOnUI(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LocService.this,t, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private boolean isRunning  = false;

    @Override
    public void onCreate() {
    	toast( "Service onCreate");
        mGpsTracker =new GPSTracker(LocService.this);
    }
  
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

    	toast( "Service onStartCommand");
    	isRunning=true;
        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR
        new Thread(new Runnable() {
            public void run() {
            	 Looper.prepare();
                //Your logic that service will perform will be placed here
                //In this example we are just looping and waits for 1000 milliseconds in each loop.
               while(isRunning){
                  
                    try {
                        loadGPS();
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    	    
                }

                //Stop service once it finishes its task
             //   stopSelf();
            }
        }).start();

        return Service.START_STICKY;
    }

    public void loadGPS(){

        mGpsTracker=new GPSTracker(LocService.this);
        // check if GPS enabled
        if(mGpsTracker.canGetLocation()){

            double latitude = mGpsTracker.getLatitude();
            double longitude = mGpsTracker.getLongitude();

            // \n is for new line
            toast(latitude+" and " +longitude);
        }else{
            mGpsTracker.showSettingsAlert();
        }

    }
    @Override
    public IBinder onBind(Intent arg0) {
    	toast("Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {

        isRunning = false;
        toast("Service onDestroy");
    }
}