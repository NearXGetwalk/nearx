package in.walkin.nearxsdk.location;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;


public class LocationUpdates {
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private PendingIntent mLocationPendingIntent;
    private Context mContext;
    private onLastLocationListener mListener;
    private Map<String,Double> lastLocation= new HashMap<>();

    private static final String TAG = "LocationUpdates";


    public LocationUpdates(Context context, onLastLocationListener listener) {
        mContext = context;
        mListener=listener;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
//        createLocationRequest();
    }

//    private void createLocationRequest() {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        /* 1 minute */
//        long LOCATION_FROM_SDK = 60 * 1000;
//        mLocationRequest.setInterval(LOCATION_FROM_SDK);
//        /*  10 sec */
//        long INTERVAL_LOCATION_FROM_OTHER_APPS = 10 * 1000;
//        mLocationRequest.setFastestInterval(INTERVAL_LOCATION_FROM_OTHER_APPS);
//    }
//
//    public void startLocationUpdates() {
//        mLocationPendingIntent = getLocationPendingIntent();
//        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Task<Void> fusedLocationStatus = mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationPendingIntent);
//        Log.d(TAG, "Requesting update status: " + fusedLocationStatus.isSuccessful());
//    }
//
//    private PendingIntent getLocationPendingIntent() {
//        Log.d(TAG, "getLocationPendingIntent");
//        // Reuse the PendingIntent if we already have it.
//        if (mLocationPendingIntent != null) {
//            return mLocationPendingIntent;
//        }
//        Intent intent = new Intent(mContext, LocationBroadcastReceiver.class);
//        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
//        // calling addGeofences() and removeGeofences().
//        mLocationPendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        return mLocationPendingIntent;
//    }

    private boolean checkLocationPermission(){
        return ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void getLastKnownLocation(){
       boolean permission=checkLocationPermission();
       if(permission){
           Task<Location> lastLocationTask=mFusedLocationClient.getLastLocation();
           lastLocationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
               @Override
               public void onSuccess(Location location) {
                   if(location != null){
                       lastLocation.put("latitude",location.getLatitude());
                       lastLocation.put("longitude",location.getLongitude());
                       Log.i(TAG, "last known location successfully!");
                       mListener.onGetLastKnownLocationSuccess(lastLocation);
                   }else{
                       mListener.onGetLastKnownLocationFailure("Could not retrieve last known location");
                   }
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   String message="Error while registering geofences :(";
                   String errMessage = e.getMessage();
                   Log.e(TAG, "could not get last known location");
                  mListener.onGetLastKnownLocationFailure("Could not retrieve last known location");
               }
           });
       }

    }

    public interface onLastLocationListener{
        void onGetLastKnownLocationSuccess(Map<String, Double> lastLocation);
        void onGetLastKnownLocationFailure(String errMsg);
    }


}
