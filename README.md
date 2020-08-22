# react-native-badge-number

Add badge number to application icon

## Getting started

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

## License

MIT
