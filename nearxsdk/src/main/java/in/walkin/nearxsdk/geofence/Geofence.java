package in.walkin.nearxsdk.geofence;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;


import in.walkin.nearxsdk.app.Notify;
import in.walkin.nearxsdk.model.GeofencePojo;
import in.walkin.nearxsdk.services.GetGeofences;
import in.walkin.nearxsdk.location.LocationUpdates;
import in.walkin.nearxsdk.workmanager.ReRegisterWorkManager;

/**
 * Created by Anvesh on 05/08/19.
 */

public class Geofence implements
        GetGeofences.onGetGeofencesListener,
        LocationUpdates.onLastLocationListener,
        RegisterGeofencesService.onRegisterGeofencesListener
{
      public static Context mContext;
      public static String mAuthkey;
      public static String mMobileNumber;
      boolean workerStatus;

    private static final String TAG = "Geofence";


    public Geofence(String mobile, String mAuthkey, Context context, boolean worker){
        this.mMobileNumber = mobile;
        this.mContext = context;
        this.mAuthkey = mAuthkey;
        this.workerStatus = worker;
       //Notify.sendNotification("Geofence Init",mobile+" "+ mAuthkey, context);
    }


    public static Context getContect(){
        return mContext;
    }
    public static String getAuthToken() { return mAuthkey; }
    public static String getmMobileNumber() { return mMobileNumber; }





    public void getGeofencesAndRegister() {
        Log.d(TAG,"getCurrentLocation");
//        Notify.sendNotification("GRT LOC","getCurrentLocation", mContext);

        LocationUpdates locationUpdates = new LocationUpdates(mContext, this);
        locationUpdates.getLastKnownLocation();

    }

    @Override
    public void onGetLastKnownLocationSuccess(Map<String, Double> lastLocation) {

        Double lastLat = lastLocation.get("latitude");
        Double lastLong = lastLocation.get("longitude");
        Log.d(TAG,"last known loc "+ lastLat +" "+ lastLong);
        callAPI(lastLat, lastLong);
    }

    @Override
    public void onGetLastKnownLocationFailure(String errMsg) {
        Log.d(TAG, errMsg);
    }


    /**
     * Calls the API and gets a list of geofences
     * Registers the geofences once the API gives back results
     *
     * @param latitude
     * @param longitude
     */
    public void callAPI(Double latitude, Double longitude) {
        Log.d(TAG,"callAPI");
        GetGeofences getGeo = new GetGeofences(mContext,this);
        getGeo.GetNearbyGeofences(latitude,longitude);

    }

    @Override
    public void onGetGeofencesSuccess(ArrayList<GeofencePojo> geofencePojoArray) {
        Log.d(TAG,"Fence Array Created");

        RegisterGeofencesService geofence = new RegisterGeofencesService(mContext,this,geofencePojoArray);
        geofence.removeAndRegisterGeofences();

//        geofence.addGeoFences();
    }

    @Override
    public void onGetGeofencesFailure(String err) {
        Log.d(TAG,"GET GEOFENCE ERROR");

    }

    @Override
    public void onRegisterGeofencesSuccess(String message) {
        Log.d(TAG,"GEOFENCE REGESTER SUCCESS >>"+message);

//        if(!workerStatus)
            ReRegisterWorkManager.createWorkRequest(mMobileNumber,mAuthkey);

    }

    @Override
    public void onRegisterGeofencesFailure(String message, String errMessage) {
        Log.d(TAG,"GEOFENCE REGESTER c >>"+message);
        Log.e(TAG,"Err >>"+errMessage);
    }

    
}
