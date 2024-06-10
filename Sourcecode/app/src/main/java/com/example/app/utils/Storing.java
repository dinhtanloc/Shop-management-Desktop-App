package com.example.app.utils;

import com.example.app.Main;

import java.util.prefs.Preferences;

public class Storing {
    private static final String USERNAME_PREF_KEY = "username";
    private static final String PASSWORD_PREF_KEY = "password";
    static public void putValueToPreferences(String key, String value){
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        prefs.put(key, value);
    }
    static public String getValueToPreferences(String key){
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        return prefs.get(key, "");
    }

    public String getUsername() {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        return prefs.get(USERNAME_PREF_KEY, ""); // Trả về giá trị mặc định là chuỗi rỗng nếu không có giá trị đã lưu
    }

    public String getEncryptedPassword(String username) {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        return prefs.get(username, ""); // Trả về giá trị mặc định là chuỗi rỗng nếu không có giá trị đã lưu
    }
    public void saveLoginDetails(String username, String encryptedPassword) {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        prefs.put(USERNAME_PREF_KEY, username);
        prefs.put(PASSWORD_PREF_KEY, encryptedPassword);
    }
}
