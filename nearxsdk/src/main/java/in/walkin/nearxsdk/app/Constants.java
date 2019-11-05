package in.walkin.nearxsdk.app;

/**
 * Created by Anvesh on 04/08/19.
 */

public class Constants {



    public static final String NEARX_PREF = "nearxpref";

    public static final String AUTH_KEY = "AUTH_KEY";
    public static final String MOBILE_NUMBER = "MOBILE_NUMBER";
    public static final String USER_NAME = "USER_NAME";


    // Geofence Constants
    public static final String EVENT_TYPE_ENTRY = "GEOFENCE_ENTRY";
    public static final String EVENT_TYPE_EXIT = "GEOFENCE_EXIT";
    public static final String EVENT_TYPE_DWELL = "GEOFENCE_DWELL";
    public static final int LOITERING_DELAY = 120000; // 1 minutes
    public static final int GEOFENCES_WITHIN_SPECIFIC_DISTANCE = 100000; // 100000 meters=100km
    public static final int NUMBER_OF_GEOFENCES = 100;




    // Work Manager Constants
    public static final int BACK_OFF_IN_MINUTES = 10;
    public static final int REPEAT_WORK_IN_MINUTES= 30;



    // Services Constants
    public static final String API_KEY = "bCGqhWU5UD6XtJEl6pyR91FvTCrh32xf7vnACpYG";

    public static final String NEARX_REST_BASE_URL = "https://dev-api.getwalkin.in/nearx/consumer/s3001";
    public static final String BASE_URL = "https://dev-api.getwalkin.in/nearx/consumer/s3010/api/nearx";



//    public static final String NEARX_REST_BASE_URL = "http://167.99.31.169:3001";
//    public static final String BASE_URL = "http://167.99.31.169:3001/api/nearx";



    public static final String VERIFY_CUSTOMER_URL = NEARX_REST_BASE_URL + "/api/verifyCustomer";
    public static final String GET_GEOFENCES_BASE_URL = NEARX_REST_BASE_URL + "/api/configure/locations/sdk?";
    public static final String EVENT_REGISTRATION = BASE_URL + "/processEvent";

}
