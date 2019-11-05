package in.walkin.nearxsdk.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.bugfender.sdk.Bugfender;

import java.util.concurrent.TimeUnit;

import in.walkin.nearxsdk.app.Constants;
import in.walkin.nearxsdk.app.Notify;
import in.walkin.nearxsdk.geofence.Geofence;


public class ReRegisterWorkManager extends Worker {
    private static final String TAG = "WorkManager";

    public ReRegisterWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public static Data createWorkRequestData(String mobileNumber, String authKey) {
        return new Data.Builder()
                .putString(Constants.AUTH_KEY, authKey)
                .putString(Constants.MOBILE_NUMBER, mobileNumber)
                .build();
    }

    public static void createWorkRequest( String mobileNumber, String authKey) {
        WorkManager.getInstance().cancelAllWorkByTag("registerJob");
        Log.e(TAG," >>> createWorkRequest <<<");
        Constraints constraints = new Constraints.Builder().build();
        Data workReqData = createWorkRequestData( mobileNumber, authKey);

        OneTimeWorkRequest regGeofencesWork =
                new OneTimeWorkRequest.Builder(ReRegisterWorkManager.class)
                        .setInitialDelay(Constants.REPEAT_WORK_IN_MINUTES,TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .setInputData(workReqData)
                        .addTag("registerJob")
                        .setBackoffCriteria(
                                BackoffPolicy.LINEAR,
                                Constants.BACK_OFF_IN_MINUTES,
                                TimeUnit.MINUTES)
                        .build();
        Log.d(TAG,"createWorkRequest enqueing work req");
        Log.d(TAG,"regGeofencesWork >  "+regGeofencesWork.getId());

//        WorkManager.getInstance().enqueue(regGeofencesWork);
        WorkManager.getInstance().enqueueUniqueWork("registerJob", ExistingWorkPolicy.REPLACE, regGeofencesWork);


//        PeriodicWorkRequest pRegGeofencesWork =
//                new PeriodicWorkRequest.Builder(ReRegisterWorkManager.class,Constants.REPEAT_WORK_IN_MINUTES,TimeUnit.MINUTES)
//                        .setConstraints(constraints)
//                        .setInputData(workReqData)
//                        .addTag("registerJob")
//                        .setBackoffCriteria(
//                                BackoffPolicy.LINEAR,
//                                Constants.BACK_OFF_IN_MINUTES,
//                                TimeUnit.MINUTES)
//                        .build();
//        Log.d(TAG,"createWorkRequest enqueing work req");
//        Log.d(TAG,"regGeofencesWork >  "+pRegGeofencesWork.getId());
//
////        WorkManager.getInstance().enqueue(regGeofencesWork);
//        WorkManager.getInstance().enqueueUniquePeriodicWork("registerJobManager", ExistingPeriodicWorkPolicy.REPLACE, pRegGeofencesWork);


    }


    private void registerGeofences(String authToken, String mobileNum, Context applicationContext) {

        try {
            Geofence geofence = new Geofence(mobileNum,authToken,applicationContext,true);
            geofence.getGeofencesAndRegister();
//            Notify.sendNotification("Calling GeoFenceInit","Init Called"+mobileNum+" "+authToken,getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
            Notify.sendNotification("Init Error",e.toString(),getApplicationContext());
        }
    }

    @NonNull
    @Override
    public Result doWork() {

        try {
            Log.d(TAG, "doWork reached");

            Bugfender.w("DO WORK", "doWork reached");


            Data inputData = this.getInputData();
            String authToken=inputData.getString(Constants.AUTH_KEY);
            String mobileNum=inputData.getString(Constants.MOBILE_NUMBER);


            Log.d(TAG, "auth "+ authToken);
            Log.d(TAG, "mob "+ mobileNum);


//            Notify.sendNotification("Do Work",mobileNum,getApplicationContext());

            registerGeofences(authToken, mobileNum, getApplicationContext());
            return Result.success();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "doWork reached Failed"+e.toString());
            Notify.sendNotification("Do Work Error",e.toString(),getApplicationContext());
            Log.e(TAG, "Err  "+ Result.failure().toString());
            return Result.failure();
        }


    }

}
