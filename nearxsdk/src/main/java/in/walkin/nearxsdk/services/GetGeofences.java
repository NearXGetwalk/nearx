package in.walkin.nearxsdk.services;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import in.walkin.nearxsdk.app.Constants;
import in.walkin.nearxsdk.geofence.Geofence;
import in.walkin.nearxsdk.model.GeofencePojo;

import static android.content.ContentValues.TAG;

class GetHttpConnection extends AsyncTask<String,String,JSONObject>{

    ArrayList<GeofencePojo> geofencePojoArray;



    @Override
    protected JSONObject doInBackground(String... strings) {
        try{
            URL url = new URL(strings[0]);
            Log.i(TAG,"trying to exxecuteeee");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();


            connection.setRequestMethod(strings[1]);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("token", Geofence.getAuthToken());
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONObject responseJSON = new JSONObject(response.toString());
                return responseJSON;
            } else {
                System.out.println("GET request not worked");
            }

        }catch(Exception e){
            e.printStackTrace();
        }



        return null;
    }

    protected void onPostExecute(JSONObject response){
        super.onPostExecute(response);
    }

}


public class GetGeofences {
    Context mContext;
    ArrayList<GeofencePojo> geofencePojoArray;
    private onGetGeofencesListener mListener;

    public GetGeofences(Context context,onGetGeofencesListener listener) {
        this.mListener = listener;
        mContext = context;
    }


    public void GetNearbyGeofences(Double latitude, Double longitude ) {

        try {

            String serviceUrl = Constants.GET_GEOFENCES_BASE_URL + "latitude=" + latitude + "&longitude=" + longitude + "&limit=" + Constants.NUMBER_OF_GEOFENCES + "&within=" + Constants.GEOFENCES_WITHIN_SPECIFIC_DISTANCE;


            System.out.println("server url before req in SDK>>>>>" + serviceUrl);

            JSONObject response;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                response = new GetHttpConnection().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serviceUrl, "GET", null).get();
            else {
                response = new GetHttpConnection().execute(serviceUrl, "GET", null).get();
            }
                if (((JSONObject) response).has("locations")) {
                    try {

                        Gson gson = new Gson();
                        JSONArray data = ((JSONObject) response).getJSONArray("locations");
                        Type listType = new TypeToken<List<GeofencePojo>>() {
                        }.getType();
                        geofencePojoArray = gson.fromJson(data.toString(), listType);
                        if (mListener != null && geofencePojoArray.size() > 0) {
                            mListener.onGetGeofencesSuccess(geofencePojoArray);
                        } else Log.i(TAG, "NO Fence Found or mListener Not Created: " + response);


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i(TAG, "GET GEOFENCE EXCEPTIONr" + e);
                        if (mListener != null) mListener.onGetGeofencesFailure(e.toString());
                    }
                } else {
                    System.out.println("NO DATA FOUND");
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface onGetGeofencesListener {
        void onGetGeofencesSuccess( ArrayList<GeofencePojo> geofencePojoArray);
        void onGetGeofencesFailure(String error);
    }
}
