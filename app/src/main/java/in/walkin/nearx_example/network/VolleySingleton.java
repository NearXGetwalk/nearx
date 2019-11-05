package in.walkin.nearx_example.network;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.walkin.nearx_example.Interface.UpdateVolleyData;
import in.walkin.nearx_example.MainActivity;

/**
 * Created by Anvesh on 3/3/2018.
 */

public class VolleySingleton {

    private static VolleySingleton instance;
    private static RequestQueue requestQueue;
    public static int MY_SOCKET_TIMEOUT_MS = 30000;




    private VolleySingleton(Context context) {
        requestQueue = getQueue(context);
    }

    public RequestQueue getQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public static synchronized VolleySingleton getInstance(Context context) {

        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }


    public void addToQueueWithJsonRequestAndResultCode(JSONObject reqObject, String webService, final Context context, final Fragment fragment, final int resultCode) {
        int req;

        if (reqObject != null) {
            System.out.println("request before req>>>>>" + reqObject);
            req = Request.Method.POST;
        } else {
            req = Request.Method.GET;
        }
        RequestQueue queue = requestQueue;

        System.out.println("server url before req>>>>>" + webService);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                req, webService, reqObject,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("response<<<<<" + response);
                        if (fragment != null) {
                            ((UpdateVolleyData) fragment).updateFromVolley(response,resultCode);
                        } else {
                            ((UpdateVolleyData) context).updateFromVolley(response,resultCode);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("response in error<<<<<" + error);
                if (fragment != null) {
                    ((UpdateVolleyData) fragment).updateFromVolley(error,resultCode);
                } else {
                    ((UpdateVolleyData) context).updateFromVolley(error,resultCode);
                }
            }
        })
        {  @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("token", MainActivity.Auth_key);
            System.out.println("Headder<<<<<" + MainActivity.Auth_key);
            return headers;
        }};

        queue.add(jsonRequest);
    }

//
//    public void addToQueueWithJsonRequest(JSONObject reqObject, String webService, final Context context, final Fragment fragment) {
//        int req;
//
//        if (reqObject != null) {
//            System.out.println("request before req>>>>>" + reqObject);
//            req = Request.Method.POST;
//        } else {
//            req = Request.Method.GET;
//        }
//        RequestQueue queue = requestQueue;
//
//        System.out.println("server url before req>>>>>" + webService);
//
//        JsonObjectRequest jsonRequest = new JsonObjectRequest(
//                req, webService, reqObject,
//
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        System.out.println("response<<<<<" + response);
//                        if (fragment != null) {
//                            ((UpdateVolleyData) fragment).updateFromVolley(response);
//                        } else {
//                            ((UpdateVolleyData) context).updateFromVolley(response);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                System.out.println("response in error<<<<<" + error);
//                if (fragment != null) {
//                    ((UpdateVolleyData) fragment).updateFromVolley(error);
//                } else {
//                    ((UpdateVolleyData) context).updateFromVolley(error);
//                }
//            }
//
//        })
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("token", SettingsScreen.Auth_Key);
//                Log.d(TAG,SettingsScreen.Auth_Key);
//                return headers;
//            }
//        };
//        queue.add(jsonRequest);
//    }


}
