package in.walkin.nearxsdk.services;

import android.content.Context;
import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.walkin.nearxsdk.NearX;
import in.walkin.nearxsdk.app.Constants;
import in.walkin.nearxsdk.geofence.Geofence;
import in.walkin.nearxsdk.network.VolleySingleton;

/**
 * Created by Anvesh on 06/08/19.
 */

public class EventRegistration {

    private onGetBeaconListener mListener;

    private static final String TAG = "GetBeaconActionService";

    public EventRegistration(onGetBeaconListener listener) {
        mListener = listener;
    }


    public void postEvent(final JSONObject jsonObject, final Intent intent, Context applicationContext) {

        String serviceUrl = Constants.EVENT_REGISTRATION;

        RequestQueue queue = VolleySingleton.getInstance(applicationContext).getQueue(applicationContext);

        System.out.println("server url before req>>>>>" + serviceUrl);

        JsonObjectRequest jsonRequest = new JsonObjectRequest( Request.Method.POST, serviceUrl, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (mListener != null)  mListener.onGetBeaconSuccess(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("response in error<<<<<" + error);

                if (mListener != null)  mListener.onGetBeaconFailure(error);

            }

        })
        {  @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("token", intent.getStringExtra(Constants.AUTH_KEY));
            return headers;
        }
        };

        queue.add(jsonRequest);
    }


    public interface onGetBeaconListener {
        void onGetBeaconSuccess(JSONObject response);
        void onGetBeaconFailure(VolleyError err);
    }
}
