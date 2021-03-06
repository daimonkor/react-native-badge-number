#import "BadgeNumber.h"
#import <React/RCTBridge.h>
#import <React/RCTUtils.h>
#import <React/RCTConvert.h>
#import <React/RCTEventDispatcher.h>
#import <UserNotifications/UserNotifications.h>

@implementation BadgeNumber

static NSString *const kErrorUnableToRequestPermissions = @"E_UNABLE_TO_REQUEST_PERMISSIONS";

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(requestPermissions:(NSDictionary *)permissions
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
  if (RCTRunningInAppExtension()) {
    reject(kErrorUnableToRequestPermissions, nil, RCTErrorWithMessage(@"Requesting push notifications is currently unavailable in an app extension"));
    return;
  }

  UIUserNotificationType types = UIUserNotificationTypeNone;
  if (permissions) {
    if ([RCTConvert BOOL:permissions[@"alert"]]) {
      types |= UIUserNotificationTypeAlert;
    }
    if ([RCTConvert BOOL:permissions[@"badge"]]) {
      types |= UIUserNotificationTypeBadge;
    }
    if ([RCTConvert BOOL:permissions[@"sound"]]) {
      types |= UIUserNotificationTypeSound;
    }
  } else {
    types = UIUserNotificationTypeAlert | UIUserNotificationTypeBadge | UIUserNotificationTypeSound;
  }

  [UNUserNotificationCenter.currentNotificationCenter
    requestAuthorizationWithOptions:types
    completionHandler:^(BOOL granted, NSError *_Nullable error) {

    if (error != NULL) {
      reject(@"-1", @"Error - Push authorization request failed.", error);
    } else {
      dispatch_async(dispatch_get_main_queue(), ^(void){
        [RCTSharedApplication() registerForRemoteNotifications];
      });
      [UNUserNotificationCenter.currentNotificationCenter getNotificationSettingsWithCompletionHandler:^(UNNotificationSettings * _Nonnull settings) {
        resolve(RCTPromiseResolveValueForUNNotificationSettings(settings));
      }];
    }
  }];
}

RCT_EXPORT_METHOD(abandonPermissions)
{
  [RCTSharedApplication() unregisterForRemoteNotifications];
}

RCT_EXPORT_METHOD(checkPermissions:(RCTResponseSenderBlock)callback)
{
  if (RCTRunningInAppExtension()) {
    callback(@[RCTSettingsDictForUNNotificationSettings(NO, NO, NO, NO, NO)]);
    return;
  }

  [UNUserNotificationCenter.currentNotificationCenter getNotificationSettingsWithCompletionHandler:^(UNNotificationSettings * _Nonnull settings) {
    callback(@[RCTPromiseResolveValueForUNNotificationSettings(settings)]);
    }];
  }

static inline NSDictionary *RCTPromiseResolveValueForUNNotificationSettings(UNNotificationSettings* _Nonnull settings) {
  return RCTSettingsDictForUNNotificationSettings(settings.alertSetting == UNNotificationSettingEnabled,
                                                  settings.badgeSetting == UNNotificationSettingEnabled,
                                                  settings.soundSetting == UNNotificationSettingEnabled,
                                                  settings.lockScreenSetting == UNNotificationSettingEnabled,
                                                  settings.notificationCenterSetting == UNNotificationSettingEnabled);
  }

static inline NSDictionary *RCTSettingsDictForUNNotificationSettings(BOOL alert, BOOL badge, BOOL sound, BOOL lockScreen, BOOL notificationCenter) {
  return @{@"alert": @(alert), @"badge": @(badge), @"sound": @(sound), @"lockScreen": @(lockScreen), @"notificationCenter": @(notificationCenter)};
  }

/**
 * Update the application icon badge number on the home screen
 */
RCT_EXPORT_METHOD(setBadgeNumber:(NSInteger)number
ignoreSupportError: BOOL
)
{

  RCTSharedApplication().applicationIconBadgeNumber = number;
}

/**
 * Get the current application icon badge number on the home screen
 */

RCT_REMAP_METHOD(getBadgeNumber,
                 getBadgeNumberWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
  resolve([NSNumber numberWithInteger:RCTSharedApplication().applicationIconBadgeNumber]);
}

- (NSDictionary *)constantsToExport
{
  return @{ @"supported": @YES };
}

+ (BOOL)requiresMainQueueSetup
{
  return NO;  // only do this if your module initialization relies on calling UIKit!
}

@end
