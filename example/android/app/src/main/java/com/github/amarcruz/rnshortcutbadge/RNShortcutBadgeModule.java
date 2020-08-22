package com.github.amarcruz.rnshortcutbadge;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.content.SharedPreferences;
import android.util.Log;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.util.HashMap;
import java.util.Map;


public class RNShortcutBadgeModule extends ReactContextBaseJavaModule {
    private static final String TAG = "BadgeNumber";
    private ReactApplicationContext mReactContext;
    private ShortcutBadge shortcutbadge;

    RNShortcutBadgeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
        shortcutbadge = new ShortcutBadge(mReactContext);
    }

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public Map<String, Object> getConstants() {
      return shortcutbadge.getConstants();
    }

    /**
     * Get the current position. This can return almost immediately if the location is cached or
     * request an update, which might take a while.
     */
    @ReactMethod
    public void setBadgeNumber(final int count, final Promise promise) {
        try {
          shortcutbadge.setCount(count);
          promise.resolve(true);
        } catch (Exception ex) {
            Log.e(TAG, "Error setting the badge", ex);
            promise.reject(ex);
        }
    }

    /**
     * Get the badge from the storage.
     */
    @ReactMethod
    public void getBadgeNumber(final Promise promise) {
        promise.resolve(shortcutbadge.getCount());
    }

    /**
     * Dummy method to request permissions in Android.
     */
    @ReactMethod
    public void checkPermissions(final Promise promise) {
        promise.resolve(true);
    }

    @ReactMethod
    public void abandonPermissions(final Promise promise) {
        promise.resolve(true);
    }
}
