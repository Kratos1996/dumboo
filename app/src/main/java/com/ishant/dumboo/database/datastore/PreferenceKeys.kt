package com.ishant.dumboo.database.datastore

import androidx.datastore.preferences.core.*

object PreferenceKeys {
    val BOOLEAN_KEY : Preferences.Key<Boolean> = booleanPreferencesKey("boolean_key")
    val STRING_KEY : Preferences.Key<String> = stringPreferencesKey("string_key")
    val INTEGER_KEY : Preferences.Key<Int> = intPreferencesKey("integer_key")
    val DOUBLE_KEY : Preferences.Key<Double> = doublePreferencesKey("double_key")
    val LONG_KEY : Preferences.Key<Long> = longPreferencesKey("long_key")
    val APP_NAME : Preferences.Key<String> = stringPreferencesKey("Dumboo")
    val IS_APP_LOGGED_IN : Preferences.Key<Boolean> = booleanPreferencesKey("app_logged_in")
    val IS_HELLO_SCREEN_VISIBLE : Preferences.Key<Boolean> = booleanPreferencesKey("helloScreenVisible")
    val IS_LOCATION_SHARE : Preferences.Key<Boolean> = booleanPreferencesKey("isLocationShared")
    val IS_PRIVATED : Preferences.Key<Boolean> = booleanPreferencesKey("isYourInformationPrivate")
    val NAME : Preferences.Key<String> = stringPreferencesKey("name")
    val USERID : Preferences.Key<String> = stringPreferencesKey("userId")
    val EMAIL : Preferences.Key<String> = stringPreferencesKey("email")
    val MOBILE : Preferences.Key<String> = stringPreferencesKey("phone")
    val COUNTRY_CODE : Preferences.Key<String> = stringPreferencesKey("countryCode")
    //val LIST_MODEL_KEY : Preferences.Key<List<CustomModel>> = preferencesKey<List<CustomModel>>("list_model_key")
}