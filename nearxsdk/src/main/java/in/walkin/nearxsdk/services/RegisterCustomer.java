package in.walkin.nearxsdk.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import in.walkin.nearxsdk.NearX;
import in.walkin.nearxsdk.app.Constants;
import in.walkin.nearxsdk.app.NearXPref;
import in.walkin.nearxsdk.geofence.Geofence;
import in.walkin.nearxsdk.utils.DeviceDetails;

import static android.content.ContentValues.TAG;

class PostHttpConnection extends AsyncTask<String,String,String> {

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
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
            return response.toString();


        }catch(Exception e){
            e.printStackTrace();
        }



        return null;
    }

    protected void onPostExecute(String response){

        System.out.println("response <<<<<" + response);
        super.onPostExecute(response);
            try {
                 onVerifyCustomerSuccess(true);

            } catch (Exception e) {
                e.printStackTrace();
                onVerifyCustomerSuccess(true);
            }
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

public class RegisterCustomer {

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
        System.out.println("server url before req in registerCustomer>>>>>" + serviceUrl);

        try {
            new PostHttpConnection().execute(serviceUrl,"POST",jsonObject.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }

    }



}
