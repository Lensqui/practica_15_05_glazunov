package com.example.project_practics_3_week;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER = "user";

    private static final String LAST_LOGIN_PREF_NAME = "LastLoginPrefs";
    private static final String KEY_LAST_EMAIL = "last_email";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private SharedPreferences lastLoginPref;
    private SharedPreferences.Editor lastLoginEditor;
    private Context context;
    private Gson gson = new Gson();

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        lastLoginPref = context.getSharedPreferences(LAST_LOGIN_PREF_NAME, Context.MODE_PRIVATE);
        lastLoginEditor = lastLoginPref.edit();
    }

    public void saveUserData(String email, String password) {
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public String getPassword() {
        return pref.getString(KEY_PASSWORD, null);
    }

    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return pref.getString(KEY_TOKEN, null);
    }

    public void saveUser(User user) {
        if (user != null) {
            String userJson = gson.toJson(user);
            editor.putString(KEY_USER, userJson);
            editor.apply();
        }
    }

    public User getUser() {
        String userJson = pref.getString(KEY_USER, null);
        if (userJson != null) {
            return gson.fromJson(userJson, User.class);
        }
        return null;
    }

    public void saveLastEmail(String email) {
        lastLoginEditor.putString(KEY_LAST_EMAIL, email);
        lastLoginEditor.apply();
    }

    public String getLastEmail() {
        return lastLoginPref.getString(KEY_LAST_EMAIL, null);
    }

    public void clearSession() {
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_PASSWORD);
        editor.remove(KEY_TOKEN);
        editor.remove(KEY_USER);
        editor.apply();

    }

    public void setToken(String token) {
        saveToken(token);
    }
}