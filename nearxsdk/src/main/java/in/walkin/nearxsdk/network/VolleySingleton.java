package in.walkin.nearxsdk.network;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.util.Log;

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

import in.walkin.nearxsdk.NearX;

/**
 * Created by Anvesh on 04/08/2019.
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
//
//                            ((UpdateVolleyData) context).updateFromVolley(response);
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                System.out.println("response in error<<<<<" + error);
//
//                    ((UpdateVolleyData) context).updateFromVolley(error);
//            }
//
//        })
//        {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("token", NearX.getAuthToken());
////                headers.put("ANOTHER_CUSTOM_HEADER", "Google");
//                return headers;
//            }
//        };
//
//
//
//        queue.add(jsonRequest);
//    }
//
//    public void addToQueueWithJsonRequestAndResultCode(JSONObject reqObject, String webService, final Context context, final Fragment fragment, final int resultCode) {
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
//                            ((UpdateVolleyData) fragment).updateFromVolley(response,resultCode);
//                        } else {
//                            ((UpdateVolleyData) context).updateFromVolley(response,resultCode);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//                System.out.println("response in error<<<<<" + error);
//                if (fragment != null) {
//                    ((UpdateVolleyData) fragment).updateFromVolley(error,resultCode);
//                } else {
//                    ((UpdateVolleyData) context).updateFromVolley(error,resultCode);
//                }
//            }
//        });
//
//        queue.add(jsonRequest);
//    }



//
//    StringRequest stringRequest = new StringRequest(
//            Request.Method.GET,
//            mUrlString,
//            new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    // Do something with response string
//                    mTextView.setText(response);
//                }
//            },
//            new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    // Do something when get error
//                    Snackbar.make(mCLayout,"Error...",Snackbar.LENGTH_LONG).show();
//                }
//            }
//    );
//
//    // Add StringRequest to the RequestQueue
//                MySingleton.getInstance(mContext).addToRequestQueue(stringRequest);


}
