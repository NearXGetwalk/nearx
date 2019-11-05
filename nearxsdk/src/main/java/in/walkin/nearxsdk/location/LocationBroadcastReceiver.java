package in.walkin.nearxsdk.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

import static com.google.android.gms.location.LocationResult.extractResult;
import static com.google.android.gms.location.LocationResult.hasResult;

public class LocationBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "LocationBroadcastReceiv";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

        if (hasResult(intent)) {
            LocationResult locationResult = extractResult(intent);
            Log.d(TAG, locationResult.toString());
        }
    }
}