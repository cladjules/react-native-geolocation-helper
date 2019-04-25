package com.cladjules.RNGeolocationHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class RNGeolocationHelperModule extends ReactContextBaseJavaModule {
    public static final String TAG = "RNGeolocationHelper";
    public static enum LocationMode { HIGH_ACCURACY, SENSORS_ONLY, BATTERY_SAVING, OFF };
    private static final int REQUEST_SETTINGS_LOCATION = 1;

    public RNGeolocationHelperModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    public void getLocationMode(Promise promise) {
      LocationMode locationMode = LocationMode.OFF;
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
          int locationModeLevel = Settings.Secure.LOCATION_MODE_OFF;
        try {
            locationModeLevel = Settings.Secure.getInt(getReactApplicationContext().getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
          e.printStackTrace();
        }

          switch (locationModeLevel) {
              case Settings.Secure.LOCATION_MODE_HIGH_ACCURACY : locationMode = LocationMode.HIGH_ACCURACY; break;
              case Settings.Secure.LOCATION_MODE_SENSORS_ONLY : locationMode = LocationMode.SENSORS_ONLY; break;
              case Settings.Secure.LOCATION_MODE_BATTERY_SAVING : locationMode = LocationMode.BATTERY_SAVING; break;
              default : locationMode = LocationMode.OFF; break;
          }
      } else {
        String locationProviders = Settings.Secure.getString(getReactApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
          if (locationProviders.contains(LocationManager.GPS_PROVIDER) && locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
              locationMode = LocationMode.HIGH_ACCURACY;
          } else if (locationProviders.contains(LocationManager.GPS_PROVIDER)) {
              locationMode = LocationMode.SENSORS_ONLY;
          } else if (locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
              locationMode = LocationMode.BATTERY_SAVING;
          } else {
              locationMode = LocationMode.OFF;
          }
      }

      promise.resolve(locationMode.toString());
    }

    @ReactMethod
    public void requestLocationSettings(String title, String message, final Promise promise) {
        if (title == null) {
            title = "Enable High Accuracy";
        }
        if (message == null) {
            message = "Choose High Accuracy as the Location method";
        }
        final Activity activity = getCurrentActivity();
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            activity.startActivity(intent);
                            promise.resolve(null);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            promise.reject(new Error("User cancelled"));
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            promise.resolve(null);
        }
    }

    @Override
    public String getName() {
        return "RNGeolocationHelper";
    }
}
