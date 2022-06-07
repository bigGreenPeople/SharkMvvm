package com.shark.mvvm.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

public class SystemInfoUtils {
    private final String TAG = "SharkChilli";
    private final Context mContext;

    public SystemInfoUtils(Context context) {
        mContext = context;
    }

    public String getDeviceName() {
        String deviceName = Build.BRAND + "-" + Build.MODEL;
        Log.i(TAG, "getDeviceName: " + deviceName);
        return deviceName;
    }

    public String getVersionName() {
        try {
            String versionName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
            Log.i(TAG, "getVersionName: " + versionName);
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getVersionName: ", e);
        }
        return "UNKNOW";
    }

    public int getVersionCode() {
        try {
            int versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_CONFIGURATIONS).versionCode;
            Log.i(TAG, "getVersionCode: " + versionCode);
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getVersionCode: ", e);
        }
        return 0;
    }

    public int geSDKVersion() {
        int versionCode = Build.VERSION.SDK_INT;
        Log.i(TAG, "geSDKVersion: " + versionCode);
        String RELEASE = Build.VERSION.RELEASE;
        Log.i(TAG, "RELEASE: " + RELEASE);
        return versionCode;
    }

    public String getTopActivity() {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo runningTaskInfo = manager.getRunningTasks(1).get(0);
        ComponentName topActivity = runningTaskInfo.topActivity;
        String className = topActivity.getClassName();
        return className;
    }

    protected void getActivityTaskInfo() {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager.RunningTaskInfo runningTaskInfo = manager.getRunningTasks(1).get(0);
        int numActivities = runningTaskInfo.numActivities;
        int taskId = runningTaskInfo.id;
        ComponentName topActivity = runningTaskInfo.topActivity;
        String className = topActivity.getClassName();

        Log.e(TAG, "getActivityTaskInfo -> #runningTaskInfo# ,"
                + " taskId:" + taskId + ", numActivities:"
                + numActivities + ", className:" + className);
    }

}
