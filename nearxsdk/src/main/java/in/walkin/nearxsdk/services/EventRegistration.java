package in.walkin.nearxsdk.services;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import in.walkin.nearxsdk.app.Constants;
import in.walkin.nearxsdk.geofence.Geofence;

/**
 * Created by Anvesh on 06/08/19.
 * Modified by Haripriya on 24/02/19.
 **/

class PostHttpEvents extends AsyncTask<String,String,String> {

    @Override
    protected String doInBackground(String... strings) {
        try{
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();


            connection.setRequestMethod(strings[1]);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("token", Geofence.getAuthToken());
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(strings[2]);

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
            wr.flush();
            wr.close();
            return response.toString();


        }catch(Exception e){
            e.printStackTrace();
        }



        return null;
    }

    protected void onPostExecute(String response){
        super.onPostExecute(response);
    }
}

public class EventRegistration {

    private onGetBeaconListener mListener;

    private static final String TAG = "GetBeaconActionService";

    public EventRegistration(onGetBeaconListener listener) {
        mListener = listener;
    }


    public void postEvent(final JSONObject jsonObject, final Intent intent, Context applicationContext) {

        String serviceUrl = Constants.EVENT_REGISTRATION;
        System.out.println("server url before req>>>>>" + serviceUrl);

        try{
            String response = new PostHttpEvents().execute(serviceUrl,"POST",jsonObject.toString()).get();

            JSONObject responseJSON = new JSONObject(response);
            if (mListener != null)  mListener.onGetBeaconSuccess(responseJSON);

        }catch(Exception e){
            e.printStackTrace();
            if (mListener != null)  mListener.onGetBeaconFailure(e);

        }
    }


    public interface onGetBeaconListener {
        void onGetBeaconSuccess(JSONObject response);
        void onGetBeaconFailure(Exception e);
    }
}
