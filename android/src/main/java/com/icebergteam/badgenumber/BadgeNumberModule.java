package com.icebergteam.badgenumber;

import android.util.Log;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.util.Map;
import com.github.amarcruz.rnshortcutbadge.ShortcutBadge;
import com.github.amarcruz.rnshortcutbadge.ShortcutBadgeException;


public class BadgeNumberModule extends ReactContextBaseJavaModule {
    private static final String TAG = "BadgeNumber";
    private ShortcutBadge shortcutbadge;
    private final ReactApplicationContext reactContext;

    public BadgeNumberModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.shortcutbadge = new ShortcutBadge(this.reactContext);
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
    public void setBadgeNumber(final int count, final boolean ignoreSupportError, final Promise promise) {
        try {
          shortcutbadge.setCount(count);
          promise.resolve(true);
        } catch (ShortcutBadgeException ex) {
          Log.e(TAG, "Error setting the badge", ex);
          if(ignoreSupportError){
              promise.resolve(true);
          }else {
              promise.reject(ex);
          }
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
