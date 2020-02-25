package in.walkin.nearx_example;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bugfender.sdk.Bugfender;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.walkin.nearx_example.Interface.UpdateNetworkData;
import in.walkin.nearx_example.adapters.GeofenceAdapter;
import in.walkin.nearx_example.model.GeofencePojo;
import in.walkin.nearxsdk.NearX;
import in.walkin.nearxsdk.app.Constants;
import in.walkin.nearxsdk.location.LocationUpdates;

import static androidx.constraintlayout.widget.Constraints.TAG;

class GetHttpConnection extends AsyncTask<String,String, JSONObject> {

    NearX nearx;

    @Override
    protected JSONObject doInBackground(String... strings) {
        try{
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("token", strings[1]);
            Log.i(TAG,"strings[1] is.."+strings[1]);
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
        try{
             super.onPostExecute(response);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

public class MainActivity extends AppCompatActivity implements View.OnClickListener, UpdateNetworkData, LocationUpdates.onLastLocationListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity" ;
    ImageView btn_set;
    NearX nearx;
    TextView gtxt,war;
    RecyclerView R1;
    public static String Auth_key;
    public static String mobile;
    GeofenceAdapter geofenceAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    Button reg;

    ArrayList<GeofencePojo> geofencePojoArray;

    @Override
    public void onRefresh() {
        LocationUpdates locationUpdates = new LocationUpdates(this, this);
        locationUpdates.getLastKnownLocation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout=findViewById(R.id.swipe_refresh_layout);
        R1=findViewById(R.id.geofences);
        reg = findViewById(R.id.reg);
        btn_set = findViewById(R.id.btn_settings);
        gtxt = findViewById(R.id.geo_title);
        war = findViewById(R.id.wr_txt);

        btn_set.setOnClickListener(this);
        reg.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        R1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        nearx = new NearX(this);
        nearx.testGpsStatus();

        if(nearx.getAuthKey().equals("")){
            Intent i = new Intent(MainActivity.this,SettingsScreen.class );
            startActivity(i);
        }else{
            Auth_key = nearx.getAuthKey();
            mobile = nearx.getPhoneNumber();
            gtxt.setVisibility(View.VISIBLE);
            reg.setVisibility(View.VISIBLE);
            R1.setVisibility(View.VISIBLE);
            war.setVisibility(View.GONE);
        }


        LocationUpdates locationUpdates = new LocationUpdates(this, this);
        locationUpdates.getLastKnownLocation();


        Bugfender.init(this, "UHoF32v19S1a2eKYW6qUhV4PqqjG1nZA", BuildConfig.DEBUG);
        Bugfender.enableCrashReporting();
        Bugfender.enableUIEventLogging(getApplication());
        Bugfender.enableLogcatLogging(); // optional, if you want logs automatically collected from logcat
        Bugfender.d("onCreate", "Hello world!"); // you can also use Bugfender to log messages

    }

    private void callGeofences(Double latitude, Double longitude) {
        try{

            String serviceUrl = Constants.GET_GEOFENCES_BASE_URL+"latitude="+latitude+"&longitude="+longitude+"&limit="+Constants.NUMBER_OF_GEOFENCES+"&within="+Constants.GEOFENCES_WITHIN_SPECIFIC_DISTANCE;
            JSONObject result;
            swipeRefreshLayout.setRefreshing(true);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
                result = new GetHttpConnection().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serviceUrl, Auth_key, null).get();
            }
            else {
                result = new GetHttpConnection().execute(serviceUrl, Auth_key, null).get();
            }
            ((UpdateNetworkData) this).updateFromNetwork(result,200);

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    void notifyAdapter(int resultCode) {
        if (geofenceAdapter == null) {
            geofenceAdapter = new GeofenceAdapter(this,this);
            geofenceAdapter.addAll(geofencePojoArray);
            R1.setAdapter(geofenceAdapter);
        } else {
            geofenceAdapter.clear();
            geofenceAdapter.addAll(geofencePojoArray);
            geofenceAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_settings:
                Intent i = new Intent(MainActivity.this,SettingsScreen.class );
                startActivity(i);
                break;
            case R.id.reg:
                if (util.isNetworkAvailable(getApplicationContext())) {
                    onRefresh();
                    nearx.reRegister(Auth_key, this);
                }else   Toast.makeText(MainActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void updateFromNetwork(Object result) { }

    @Override
    public void updateFromNetwork(Object result, int resultCode) {
        if (result instanceof JSONObject) {
            swipeRefreshLayout.setRefreshing(true);
            Log.d(TAG, "updateFromVolley: "+result.toString());
            if (((JSONObject) result).has("locations")) {
                try {
                    Gson gson = new Gson();
                    JSONArray data = ((JSONObject) result).getJSONArray("locations");
                    Type listType = new TypeToken<List<GeofencePojo>>() {}.getType();
                    geofencePojoArray = gson.fromJson(data.toString(), listType);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "GET GROFENCE EXCEPTIONr" + e);
                }
            }else System.out.println("NO DATA FOUND");
            notifyAdapter(resultCode);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onGetLastKnownLocationSuccess(Map<String, Double> lastLocation) {
        Double lastLat = lastLocation.get("latitude");
        Double lastLong = lastLocation.get("longitude");
        Log.d(TAG,"last known loc "+ lastLat +" "+ lastLong);
        callGeofences(lastLat, lastLong);
    }

    @Override
    public void onGetLastKnownLocationFailure(String errMsg) {
        Log.d(TAG, errMsg);
    }


}