package com.github.amarcruz.rnshortcutbadge;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;


public class ShortcutBadge {

  private static final String TAG = "RNShortcutBadge";
  private static final String BADGE_FILE = "BadgeCountFile";
  private static final String BADGE_KEY = "BadgeCount";

  private NotificationManager mNotificationManager;
  private static int mNotificationId = 0;

  private Context mContext;
  private SharedPreferences mPrefs;
  private boolean mSupported = false;
  private boolean mIsXiaomi = false;

  ShortcutBadge(Context reactContext) {
    mContext = reactContext;
    mPrefs = reactContext.getSharedPreferences(BADGE_FILE, Context.MODE_PRIVATE);

    this.getConstants();
  }

  public Map<String, Object> getConstants() {
    final HashMap<String, Object> constants = new HashMap<>();
    boolean supported = false;

    try {

      int counter = mPrefs.getInt(BADGE_KEY, 0);
      supported = ShortcutBadger.applyCount(mContext, counter);

      if (!supported && Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
        supported = true;
        mIsXiaomi = true;
        mNotificationManager = (NotificationManager)
          mContext.getSystemService(Context.NOTIFICATION_SERVICE);
      }
    } catch (Exception e) {
      Log.e(TAG, "Cannot initialize ShortcutBadger", e);
    }

    mSupported = supported;

    constants.put("launcher", getLauncherName());
    constants.put("supported", supported);

    return constants;
  }

  public void setCount(final int count) throws Exception {
    // Save the counter unconditionally
    mPrefs.edit().putInt(BADGE_KEY, count).apply();

    if (!mSupported) {
      throw new Exception("Cannot set badge.");
    }

    boolean ok;

    if (mIsXiaomi) {
      ok = setXiaomiBadge(mContext, count);
    } else {
      ok = ShortcutBadger.applyCount(mContext, count);
    }

    if (!ok) {
      throw new Exception("Cannot set badge.");
    }

  }

  public int getCount() {
    return mPrefs.getInt(BADGE_KEY, 0);
  }

  /**
   * Support Xiaomi devices.
   */
  private boolean setXiaomiBadge(final Context context, final int count) {

    mNotificationManager.cancel(mNotificationId);
    mNotificationId++;

    Notification.Builder builder = new Notification.Builder(context)
      .setContentTitle("")
      .setContentText("");
    //.setSmallIcon(R.drawable.ic_launcher);
    Notification notification = builder.build();
    ShortcutBadger.applyNotification(context, notification, count);
    mNotificationManager.notify(mNotificationId, notification);

    return true;
  }

  /**
   * Find the package name of the current launcher
   */
  private String getLauncherName() {
    String name = null;

    try {
      final Intent intent = new Intent(Intent.ACTION_MAIN);
      intent.addCategory(Intent.CATEGORY_HOME);
      final ResolveInfo resolveInfo = mContext.getPackageManager()
        .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
      name = resolveInfo.activityInfo.packageName;
    } catch (Exception ignore) {
    }

    return name;
  }
}
