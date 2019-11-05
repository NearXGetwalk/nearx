package in.walkin.nearxsdk.geofence;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import in.walkin.nearxsdk.app.Constants;
import in.walkin.nearxsdk.model.GeofencePojo;


/**
 * Created by Anvesh on 04/08/19.
 */

public class RegisterGeofencesService implements OnCompleteListener<Void> {

    private static final String TAG = "GeoFenceService";

    private GeofencingClient mGeofencingClient;
    private PendingIntent mGeofencePendingIntent;

    private Context mContext;
    private onRegisterGeofencesListener mListener;
    ArrayList<GeofencePojo> geofencePojoArray;

    public RegisterGeofencesService(Context context, onRegisterGeofencesListener listener, ArrayList<GeofencePojo> geofencePojoArray) {
        this.mContext = context;
        mGeofencingClient = LocationServices.getGeofencingClient(context);
        mListener = listener;
        this.geofencePojoArray = geofencePojoArray;
    }

    public void addGeoFences() {
        List<Geofence> fenceArray = new ArrayList<>();
        try {
            for (GeofencePojo g : geofencePojoArray) {
                System.out.println(g.getCanonicalName()+" "+g.getLatitude()+" "+g.getLongitude()+" "+g.getRadius());
                fenceArray.add(createGeofence(g));
            }
            Log.i(TAG, "fenceArray created");
            GeofencingRequest geofencingRequest = getGeofencingRequest(fenceArray);
            PendingIntent geofencePendingIntent = getGeofencePendingIntent();
            Log.i(TAG, "PendingIntent created");

            addGeofences(geofencingRequest, geofencePendingIntent);
        }catch (Exception e){
            Log.i(TAG, "fenceArray creation Exception"+e);
        }

    }

    public void removeAndRegisterGeofences() {
        mGeofencingClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(this);
    }


    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            Log.i(TAG, "Removed Old Fences");
            addGeoFences();

        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeoFenceErrorMessages.getErrorString(mContext, task.getException());
            Log.w(TAG, errorMessage);
        }
    }

    public Geofence createGeofence(GeofencePojo geoFenceObject) {
        Geofence  build = new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this giofence
                .setRequestId(geoFenceObject.getCanonicalName())
                .setCircularRegion(geoFenceObject.getLatitude(), geoFenceObject.getLongitude(), geoFenceObject.getRadius())
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(Constants.LOITERING_DELAY)
                .build();
        Log.i(TAG,build.toString());
        return build;
    }


    public GeofencingRequest getGeofencingRequest(List<Geofence> geofenceList) {
        Log.d(TAG, "geofenceRequest");
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    public PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(mContext, GeofenceEventBroadcastReceiver.class);
        intent.putExtra(Constants.AUTH_KEY, in.walkin.nearxsdk.geofence.Geofence.getAuthToken());
        intent.putExtra(Constants.MOBILE_NUMBER, in.walkin.nearxsdk.geofence.Geofence.getmMobileNumber());
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    public void addGeofences(GeofencingRequest geofencingRequest, PendingIntent geofencePendingIntent) {
        Log.d(TAG, "addGeofences");
        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "permission not available");
            return;
        }

        final Task<Void> listen = mGeofencingClient.addGeofences(geofencingRequest, geofencePendingIntent);

        listen.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                String message = "Geofences registered successfully!";
                Log.e(TAG, message);
                mListener.onRegisterGeofencesSuccess(message);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String errMessage = e.getMessage();
                mListener.onRegisterGeofencesFailure("Error while registering geofences :(", errMessage);
            }
        });

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                Log.d(TAG, "is successful " + listen.isSuccessful());
                Log.d(TAG, "is completed " + listen.isComplete());
                Log.d(TAG, "is canceled " + listen.isCanceled());
            }
        }, 5000);
    }




    public interface onRegisterGeofencesListener {
        void onRegisterGeofencesSuccess(String message);
        void onRegisterGeofencesFailure(String message, String errMessage);
    }
}
