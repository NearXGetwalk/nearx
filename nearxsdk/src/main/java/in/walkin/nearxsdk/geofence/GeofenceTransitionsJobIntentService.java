package in.walkin.nearxsdk.geofence;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.walkin.nearxsdk.R;
import in.walkin.nearxsdk.app.Constants;
import in.walkin.nearxsdk.app.Notify;
import in.walkin.nearxsdk.services.EventRegistration;


/**
 * Created by Anvesh on 06/08/19.
 */

public class GeofenceTransitionsJobIntentService extends JobIntentService
        implements EventRegistration.onGetBeaconListener
{
    private static final int JOB_ID = 573;

    private static final String TAG = "GeofenceTransitionsIS";

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, GeofenceTransitionsJobIntentService.class, JOB_ID, intent);
    }

    /**
     * Handles incoming intents.
     *
     * @param intent sent by Location Services. This Intent is provided to Location
     *               Services (inside a PendingIntent) when addGeofences() is
     *               called.
     */
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeoFenceErrorMessages.getErrorString(this, geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER
                || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT
                || geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {

            Log.w(TAG, "FENCE TRIGGRED");

            // Get the geofences that were triggered. A single event can trigger multiple
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            JSONObject geofenceTransitionDetails = getGeofenceTransitionDetails(geofenceTransition, triggeringGeofences, intent);



                EventRegistration getBeaconActionService = new EventRegistration(this);
                getBeaconActionService.postEvent(geofenceTransitionDetails, intent, getApplicationContext());

        } else {
            // Log the error.
            Log.e(TAG, getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }
    }


    /**
     * Gets transition details and returns them as a formatted string.
     *
     * @param geofenceTransition  The ID of the geofence transition.
     * @param triggeringGeofences The geofence(s) triggered.
     * @param intent
     * @return The transition details formatted as String.
     */
    private JSONObject getGeofenceTransitionDetails(int geofenceTransition, List<Geofence> triggeringGeofences, Intent intent) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        JSONArray names = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject eventData = new JSONObject();
        StringBuilder namesStr = new StringBuilder(" ");

        try {

        for (Geofence geofence : triggeringGeofences)
            {
                names.put(geofence.getRequestId());
                namesStr.insert(0, geofence.getRequestId()+", " );
            }

            eventData.put("locationNames",names );
            eventData.put("mobileNumber", intent.getStringExtra(Constants.MOBILE_NUMBER));
            eventData.put("eventType",geofenceTransitionString);

            jsonObject.put("authKey",intent.getStringExtra(Constants.AUTH_KEY));
            jsonObject.put("eventData",eventData);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Notify.sendNotification(namesStr+" :   "+geofenceTransitionString,
                geofenceTransitionString+"   "+intent.getStringExtra(Constants.MOBILE_NUMBER),getApplicationContext());

        Log.w("EVENT>>>", jsonObject + "");
        return jsonObject;
    }


    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return Constants.EVENT_TYPE_ENTRY;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return Constants.EVENT_TYPE_EXIT;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                return Constants.EVENT_TYPE_DWELL;
            default:
                return Constants.EVENT_TYPE_ENTRY;
        }
    }

    @Override
    public void onGetBeaconSuccess(JSONObject response) {
        Log.d(TAG, "Event Registration Success >>> "+ response );
    }

    @Override
    public void onGetBeaconFailure(Exception e) {
        Log.e(TAG, "Event Registration Failure >>> "+e);
    }
}
