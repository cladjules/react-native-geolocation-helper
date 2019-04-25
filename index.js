import { NativeModules, Platform } from 'react-native';

const { RNGeolocationHelper } = NativeModules;

export const LocationMode = {
  HIGH_ACCURACY: 'HIGH_ACCURACY',
  SENSORS_ONLY: 'SENSORS_ONLY',
  BATTERY_SAVING: 'BATTERY_SAVING',
  OFF: 'OFF',
};

export default {
  getLocationMode: () => {
    if (Platform.OS === 'android') {
      return RNGeolocationHelper.getLocationMode();
    }

    // TODO: Detect iOS Accuracy?
    return LocationMode.HIGH_ACCURACY;
  },
  requestLocationSettings: (options = {}) => {
    if (Platform.OS === 'android') {
      return RNGeolocationHelper.requestLocationSettings(options.title, options.message);
    }

    // TODO: Detect iOS Accuracy?
    return LocationMode.HIGH_ACCURACY;
  },
};
