package managment.protege.supermed.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SessionManager {
    Context context;

    static SessionManager sessionManager;

    public SessionManager(Context context) {
        this.context = context;
    }

    public static SessionManager getInSharedPreference(Context context) {
        if (sessionManager == null) {
            sessionManager = new SessionManager(context);
        }
        return sessionManager;
    }
    public void setUserObj(String userObj) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USER_OBJ", userObj);
        editor.apply();
    }
    public String getUserObj() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("USER_OBJ", null);
    }

}
