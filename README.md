# react-native-geolocation-helper
Geolocation Helpers for React Native
Currently only works on Android, there are 2 methods:
- Retrieve device's location mode
- Prompt an `Enable high accuracy` modal to the user.

# Installation
```bash
npm i react-native-geolocation-helper
react-native link react-native-geolocation-helper
```

# Compatibility
| RN Version | Package Version |
| ---------- | --------------- |
| 0.57+      | 1.0.0           |
| <0.57      | Not tested      |

## Usage

```js
import GeolocationHelper, { LocationMode } from 'react-native-geolocation-helper';

GeolocationHelper.requestLocationSettings({ title: 'Your title', message: 'Your message' })
  .then(() => // User has accepted modal or high accuracy is already enabled })
  .catch(() => // User has canceled high accuracy modal });

GeolocationHelper.getLocationMode().then(locationMode => {
  // locationMode === LocationMode.HIGH_ACCURACY
  // locationMode === LocationMode.SENSORS_ONLY
  // locationMode === LocationMode.BATTERY_SAVING
  // locationMode === LocationMode.OFF
});
```

# API
#### `requestLocationSettings(?title, ?message)`
Display a modal to prompt the user to enable High accuracy,
redirects the user to the Location Settings Page `ACTION_LOCATION_SOURCE_SETTINGS` when clicking `OK`.
**resolve**: resolve if highAccuracy is enabled or users clicks OK on the modal.
**reject**: reject if highAccuracy not enabled and users clicks Cancel on the modal.

| Param | Default |
| -- | -- |
| title | `Enable High Accuracy`|
| message | `Choose High Accuracy as the Location method`|

#### `getLocationMode()`
Return the current location mode enabled on the device.
**resolve**: return a value from `LocationMode`

| LocationMode |
| -- |
| HIGH_ACCURACY |
| SENSORS_ONLY |
| BATTERY_SAVING |
| OFF |
