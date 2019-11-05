package in.walkin.nearxsdk;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import in.walkin.nearxsdk.app.NearXPref;
import in.walkin.nearxsdk.geofence.Geofence;
import in.walkin.nearxsdk.services.RegisterCustomer;
import in.walkin.nearxsdk.utils.GpsTest;
import in.walkin.nearxsdk.utils.Utils;

public class NearX {

    public static Context mContext;
    public static Activity mActivity;
    public static String AUTH_KEY;
    private static final String TAG = "NearX SDK";

    public NearX(Activity activity){
        NearX.mActivity = activity;
        NearX.mContext = activity.getApplicationContext();
    }
    public static Context getContect(){
        return mContext;
    }
    public static String getAuthToken() { return AUTH_KEY; }

    public String getPhoneNumber(){
        return NearXPref.getMobileNum();
    }

    public String getUserNmae(){
        return NearXPref.getUserName();
    }

    public String getAuthKey(){
        return NearXPref.getAuthKey();
    }



    public void saveUser(String mobile, String userName, String authKey){
         NearXPref.saveMobileNum(mobile);
         NearXPref.saveUserName(userName);
        NearXPref.saveAuthKey(authKey);
        NearX.AUTH_KEY = authKey;
        checkLocationPermission();
    }


    public static void checkLocationPermission(){
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            RegisterCustomer rcag = new RegisterCustomer(mContext);
            rcag.verifyValidCustomerAndStartScanning();
        }
    }

    public void testGpsStatus(){
        GpsTest gpsTest = new GpsTest();
        gpsTest.checkGpsTest();
    }


    public void checkHandset(){
        if(!NearXPref.getHandsetCheck().equals("CHECKED")){
            Utils check = new Utils();
            check.checkHandset(mContext,mActivity);
        }
    }

    public void reRegister(String auth_key, Activity mActivity){
        Geofence geofence = new Geofence(NearXPref.getMobileNum(),auth_key,mActivity.getApplicationContext(), false);
        geofence.getGeofencesAndRegister();
    }

}
