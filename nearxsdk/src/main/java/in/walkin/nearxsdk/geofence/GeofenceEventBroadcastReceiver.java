package in.walkin.nearxsdk.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Anvesh on 05/08/19.
 */
public class GeofenceEventBroadcastReceiver extends BroadcastReceiver{

    private static final String TAG = "GeofenceEventBroadcastR";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive");
        GeofenceTransitionsJobIntentService.enqueueWork(context, intent);
    }
}
