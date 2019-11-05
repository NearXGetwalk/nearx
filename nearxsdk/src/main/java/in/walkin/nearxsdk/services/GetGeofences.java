package in.walkin.nearxsdk.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.walkin.nearxsdk.NearX;
import in.walkin.nearxsdk.app.Constants;
import in.walkin.nearxsdk.geofence.Geofence;
import in.walkin.nearxsdk.model.GeofencePojo;
import in.walkin.nearxsdk.network.VolleySingleton;

import static android.content.ContentValues.TAG;

public class GetGeofences {
    Context mContext;
    ArrayList<GeofencePojo> geofencePojoArray;
    private onGetGeofencesListener mListener;

    public GetGeofences(Context context,onGetGeofencesListener listener) {
        this.mListener = listener;
        mContext = context;
    }





    public void GetNearbyGeofences(Double latitude, Double longitude ) {

        String serviceUrl = Constants.GET_GEOFENCES_BASE_URL+"latitude="+latitude+"&longitude="+longitude+"&limit="+Constants.NUMBER_OF_GEOFENCES+"&within="+Constants.GEOFENCES_WITHIN_SPECIFIC_DISTANCE;

        RequestQueue queue = VolleySingleton.getInstance(mContext).getQueue(mContext);

        System.out.println("server url before req>>>>>" + serviceUrl);

        JsonObjectRequest jsonRequest = new JsonObjectRequest( Request.Method.GET, serviceUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response <<<<<" + response);
                        if (((JSONObject) response).has("locations")) {
                        try {


                            Gson gson = new Gson();
                            JSONArray data = ((JSONObject) response).getJSONArray("locations");
                            Type listType = new TypeToken<List<GeofencePojo>>() {}.getType();
                            geofencePojoArray = gson.fromJson(data.toString(), listType);

                            if (mListener != null&&geofencePojoArray.size()>0) {
                                mListener.onGetGeofencesSuccess(geofencePojoArray);
                            } else Log.i(TAG, "NO Fence Found or mListener Not Created: " + response);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i(TAG, "GET GROFENCE EXCEPTIONr" + e);
                        }
                        }else{
                            System.out.println("NO DATA FOUND");
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("response in error<<<<<" + error);

                if (mListener != null)  mListener.onGetGeofencesFailure(error.toString());

            }

        })
        {  @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token",Geofence.getAuthToken());
                return headers;
        }
        }; queue.add(jsonRequest);

    }


    public interface onGetGeofencesListener {
        void onGetGeofencesSuccess( ArrayList<GeofencePojo> geofencePojoArray);
        void onGetGeofencesFailure(String error);
    }
}
