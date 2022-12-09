# react-native-badge-number

Add badge number to application icon

## Getting started

`$ yarn add react-native-badge-number`

OR

`$ yarn add 'https://github.com/daimonkor/react-native-badge-number'`


### Mostly automatic installation

`$ react-native link react-native-badge-number`


If you are using [Proguard](https://stuff.mit.edu/afs/sipb/project/android/sdk/android-sdk-linux/tools/proguard/docs/), add this to your android/app/proguard-rules.pro

See https://github.com/leolin310148/ShortcutBadger/issues/46

```
-keep class me.leolin.shortcutbadger.impl.** { <init>(...); }
```

## Usage

```js
import BadgeNumber from "react-native-badge-number";

BadgeNumber.setBadge(7);
const result = await BadgeNumber.getBadge();
```

## API

### `async setBadge(number: number)`
Set badge counter.
### `getBadge: Promise<number>`
Return badge counter.
### `requestPermissions(permissions?: {alert?: boolean; badge?: boolean; sound?: boolean;}): Promise<{alert: boolean; badge: boolean; sound: boolean;}`
Request notification permissions (iOS).
### `abandonPermissions()`
Clear notification permissions (iOS).
### `checkPermissions(callback)`
Check notification permissions (iOS).
### `decrement(i: number = 1): Promise<number>`
Decrement badge counter.
### `increment(i: number = 1): Promise<number>`
Increment badge counter.
### `supported: boolean`
Return support Androind launcher to change badge counter


## Native code (kill, background application state)

<details><summary>Android OneSignal sample <b>NotificationExtenderServiceImplement</b></summary>
<p>

```java
package com.one;

import com.onesignal.OSNotificationPayload;
import com.onesignal.OSNotificationReceivedResult;
import com.onesignal.NotificationExtenderService;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import java.util.List;
import com.github.amarcruz.rnshortcutbadge.ShortcutBadge;


public class NotificationExtenderServiceImplement extends NotificationExtenderService {
private static final String BADGE_KEY = "BadgeCount";
    private static final String BADGE_FILE = "BadgeCountFile";

    public boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
        for (RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }
        return false;
    }

   @Override
   protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
      // Return true to stop the notification from displaying.
      Log.e("BADGE_KEY", String.format("%s, %s", receivedResult.restoring, receivedResult.isAppInFocus));
      Context context = getApplicationContext();
      if(!this.isRunning(context)){
          try{
              ShortcutBadge badge = new ShortcutBadge(context);
              badge.setCount(badge.getCount() + 1);
          }catch(Exception e){
              Log.e("BADGE_KEY", String.format("%s, %s", "can not change icon badge", e));
          }
      }
      return false;
   }
}
```
</p>
</details>

<details><summary>iOS OneSignal sample <b>NotificationService</b></summary>
<p>

```objective-c
#import <OneSignal/OneSignal.h>

#import "NotificationService.h"
#import <UIKit/UIKit.h>

@interface NotificationService ()

@property (nonatomic, strong) void (^contentHandler)(UNNotificationContent *contentToDeliver);
@property (nonatomic, strong) UNNotificationRequest *receivedRequest;
@property (nonatomic, strong) UNMutableNotificationContent *bestAttemptContent;

@end

@implementation NotificationService

- (void)didReceiveNotificationRequest:(UNNotificationRequest *)request withContentHandler:(void (^)(UNNotificationContent * _Nonnull))contentHandler {
    self.receivedRequest = request;
    self.contentHandler = contentHandler;
    self.bestAttemptContent = [request.content mutableCopy];
    [OneSignal didReceiveNotificationExtensionRequest:self.receivedRequest withMutableNotificationContent:self.bestAttemptContent];

    NSLog(@"Running NotificationServiceExtension");

    NSUserDefaults *userDefault = [[NSUserDefaults alloc] initWithSuiteName:@"group.tech.magnesium.ecology"];
    NSLog(@"One Signal, badge count: %@", [userDefault  integerForKey:@"BADGE_COUNT"]);
    self.bestAttemptContent.badge = [NSNumber numberWithInt: [userDefault  integerForKey:@"BADGE_COUNT"] + 1];
    [userDefault setValue:self.bestAttemptContent.badge forKey:@"BADGE_COUNT"];
    self.contentHandler(self.bestAttemptContent);
}

- (void)serviceExtensionTimeWillExpire {
    // Called just before the extension will be terminated by the system.
    // Use this as an opportunity to deliver your "best attempt" at modified content, otherwise the original push payload will be used.

    [OneSignal serviceExtensionTimeWillExpireRequest:self.receivedRequest withMutableNotificationContent:self.bestAttemptContent];

    self.contentHandler(self.bestAttemptContent);
}

@end

```
</p>
</details>

## License

MIT
