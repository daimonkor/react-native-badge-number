import { Platform } from 'react-native';
import { NativeModules } from 'react-native';

const setBadge = async (number: number) => {
  await NativeModules.BadgeNumber.setBadgeNumber(number);
};

const getBadge: () => Promise<number> = () => {
  return NativeModules.BadgeNumber.getBadgeNumber();
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
  if (Platform.OS === 'android') {
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
  NativeModules.BadgeNumber.abandonPermissions();
};

/**
 * See what push permissions are currently enabled. `callback` will be
 * invoked with a `permissions` object.
 *
 * See https://reactnative.dev/docs/pushnotificationios.html#checkpermissions
 */
const checkPermissions = (callback: Function) => {
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

const supported: boolean = NativeModules.BadgeNumber.supported;

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
