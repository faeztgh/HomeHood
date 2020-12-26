package ir.faez.assignment2.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private static final String PREF_FILE_NAME = "ir.faez.assignment2.REGISTER_FORM";
    public static final String PREF_KEY_NAME = "name";
    public static final String PREF_KEY_LASTNAME = "lastname";
    public static final String PREF_KEY_EMAIL = "email";
    public static final String PREF_KEY_MOBILE = "mobile";
    public static final String PREF_KEY_USERNAME = "username";
    public static final String PREF_KEY_PASSWORD = "password";
    public static final String PREF_KEY_CONFIRM_PASSWORD = "confirm_password";
    public static final String PREF_KEY_ADDRESS = "address";
    public static final String PREF_KEY_NUMBER_OF_UNITS = "number_of_units";
    public static final String PREF_KEY_EMAIL_CHKBX = "email_checkbox";
    public static final String PREF_KEY_SMS_CHKBX = "sms_checkbox";


    private static PreferencesManager sInstance;
    private SharedPreferences preferences;
    private Context context;

    private PreferencesManager(Context context) {
        this.context = context;
        this.preferences = this.context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static PreferencesManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
        return sInstance;
    }

    public <T> void put(String key, T value) {

        if (value instanceof String) {

            preferences.edit().putString(key, (String) value).apply();
        }

        if (value instanceof Integer) {
            preferences.edit().putInt(key, (Integer) value).apply();
        }

        if (value instanceof Boolean) {
            preferences.edit().putBoolean(key, (Boolean) value).apply();
        }
    }

    public <T> T get(String key, T defaultValue) {
        if (defaultValue instanceof String) {
            return (T) preferences.getString(key, (String) defaultValue);
        }
        if (defaultValue instanceof Integer) {
            Integer res = preferences.getInt(key, (Integer) defaultValue);
            return (T) res;
        }

        if (defaultValue instanceof Boolean) {
            Boolean res = preferences.getBoolean(key, (Boolean) defaultValue);
            return (T) res;
        }

        return null;
    }


}
