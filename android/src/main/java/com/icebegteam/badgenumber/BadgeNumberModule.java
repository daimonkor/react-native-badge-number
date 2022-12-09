package com.icebegteam.badgenumber;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.github.amarcruz.rnshortcutbadge.ShortcutBadge;
import com.github.amarcruz.rnshortcutbadge.ShortcutBadgeException;

;import java.util.Map;

@ReactModule(name = BadgeNumberModule.NAME)
public class BadgeNumberModule extends ReactContextBaseJavaModule {
  public static final String NAME = "BadgeNumber";
  private ShortcutBadge shortcutbadge;


  public BadgeNumberModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.shortcutbadge = new ShortcutBadge(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
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
      Log.e(NAME, "Error setting the badge", ex);
      if(ignoreSupportError){
        promise.resolve(true);
      }else {
        promise.reject(ex);
      }
    } catch (Exception ex) {
      Log.e(NAME, "Error setting the badge", ex);
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
