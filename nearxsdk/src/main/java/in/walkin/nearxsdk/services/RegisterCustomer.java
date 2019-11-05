package in.walkin.nearxsdk.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.walkin.nearxsdk.NearX;
import in.walkin.nearxsdk.app.Constants;
import in.walkin.nearxsdk.app.NearXPref;
import in.walkin.nearxsdk.geofence.Geofence;
import in.walkin.nearxsdk.network.VolleySingleton;
import in.walkin.nearxsdk.utils.DeviceDetails;

public class RegisterCustomer {

//    private static CustomerSingleton mCustomer = CustomerSingleton.getInstance();

    private static final String TAG = "RegisterCustomerAndGe";
    private Context mContext;

    public RegisterCustomer(Context mContext) {
        this.mContext = mContext;
    }





    public void verifyValidCustomerAndStartScanning() {
        DeviceDetails device = new DeviceDetails(mContext);

        JSONObject jsonObject = null;
        String serviceUrl = Constants.VERIFY_CUSTOMER_URL;
        jsonObject = new JSONObject();
        try {
            jsonObject.put("osVersion",device.getOsVersion());
            jsonObject.put("deviceId",device.getDeviceId() );
            jsonObject.put("modelNumber",  device.getDeviceName());
            jsonObject.put("mobileNumber", NearXPref.getMobileNum());
            jsonObject.put("userName", NearXPref.getUserName());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("VERIFY CUSTOMER", jsonObject + "");

//        VolleySingleton.getInstance(NearX.getContect()).addToQueueWithJsonRequest(jsonObject, serviceUrl, mContext, null);



        RequestQueue queue = VolleySingleton.getInstance(NearX.getContect()).getQueue(NearX.getContect());

        System.out.println("server url before req>>>>>" + serviceUrl);

        JsonObjectRequest jsonRequest = new JsonObjectRequest( Request.Method.POST, serviceUrl, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        onVerifyCustomerSuccess(true);

                }
            }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("response in error<<<<<" + error);

                        onVerifyCustomerSuccess(true);

                    }

            })
            {  @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("token", NearX.getAuthToken());
                    return headers;
                }
            };

        queue.add(jsonRequest);

    }


        public void onVerifyCustomerSuccess(boolean nearXUser) {
            if (nearXUser) {
                Log.d(TAG,"Successful onVerifyCustomer true");
                Geofence geofence = new Geofence(NearXPref.getMobileNum(),NearX.getAuthToken(),NearX.getContect(), false);
                geofence.getGeofencesAndRegister();
            } else {
                Log.d(TAG, "Successful onVerifyCustomer false");
            }
        }


        public void onVerifyCustomerFailure() {
            Log.d(TAG, "Failure onVerifyCustomer");
        }


}
