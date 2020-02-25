package in.walkin.nearxsdk.location;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
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
