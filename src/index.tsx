import { Platform } from 'react-native';
import ShortcutBadge from 'react-native-shortcut-badge';
import { NativeModules } from 'react-native';

const setBadge = async (number: number) => {
  if (Platform.OS === 'ios') {
    await NativeModules.BadgeNumber.setBadgeNumber(number);
  } else {
    await ShortcutBadge.setCount(number);
  }
};

const getBadge: () => Promise<number> = () => {
  if (Platform.OS === 'ios') {
    return new Promise<number>((resolve, reject) => {
      try {
        NativeModules.BadgeNumber.getBadgeNumber(resolve);
      } catch (error) {
        reject(error);
      }
    });
  } else {
    return ShortcutBadge.getCount();
  }
};

/**
 * Requests notification permissions from iOS, prompting the user's
 * dialog box. By default, it will request all notification permissions, but
 * a subset of these can be requested by passing a map of requested
 * permissions.
 *
 * See https://reactnative.dev/docs/pushnotificationios.html#requestpermissions
 */
const requestPermissions = (permissions?: {
  alert?: boolean;
  badge?: boolean;
  sound?: boolean;
}): Promise<{
  alert: boolean;
  badge: boolean;
  sound: boolean;
}> => {
  let requestedPermissions = {
    alert: true,
    badge: true,
    sound: true,
  };
  if (permissions) {
    requestedPermissions = {
      alert: !!permissions.alert,
      badge: !!permissions.badge,
      sound: !!permissions.sound,
    };
  }
  if (Platform.OS == 'android') {
    return Promise.resolve({ alert: true, badge: true, sound: true });
  }
  return NativeModules.BadgeNumber.requestPermissions(requestedPermissions);
};

/**
 * Unregister for all remote notifications received via Apple Push Notification service.
 *
 * See https://reactnative.dev/docs/pushnotificationios.html#abandonpermissions
 */
const abandonPermissions = () => {
  if (Platform.OS == 'android') {
    return;
  }
  NativeModules.BadgeNumber.abandonPermissions();
};

/**
 * See what push permissions are currently enabled. `callback` will be
 * invoked with a `permissions` object.
 *
 * See https://reactnative.dev/docs/pushnotificationios.html#checkpermissions
 */
const checkPermissions = (callback: Function) => {
  if (Platform.OS == 'android') {
    callback && callback();
  }
  NativeModules.BadgeNumber.checkPermissions(callback);
};

const decrement = async (i = 1) => {
  const current = await getBadge();
  await setBadge(current - i);
  return current - i;
};

const increment = async (i = 1) => {
  const current = await getBadge();
  await setBadge(current + i);
  return current + i;
};

const supported: boolean = Platform.OS === 'ios' ? true : ShortcutBadge.supported;

export default {
  setBadge,
  getBadge,
  increment,
  decrement,
  requestPermissions,
  checkPermissions,
  abandonPermissions,
  supported,
};
