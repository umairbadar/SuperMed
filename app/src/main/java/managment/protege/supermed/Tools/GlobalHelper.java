package managment.protege.supermed.Tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.facebook.Profile;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.gson.Gson;

import managment.protege.supermed.Activity.Login;
import managment.protege.supermed.Response.LoginResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GlobalHelper {
    public static Gson gson = new Gson();

    static public LoginResponse getUserProfile(Context context) {
        LoginResponse userProfile = new LoginResponse();
        userProfile = new Gson().fromJson(SessionManager.getInSharedPreference(context).getUserObj(), LoginResponse.class);
        return userProfile;
    }

    static public void saveUserInLocal(Context context, LoginResponse userProfile, boolean isLogedIn) {
        SessionManager.getInSharedPreference(context);
        SessionManager.sessionManager.setUserObj(gson.toJson(userProfile));
    }

    static public void saveUserInLocalFb(Context context, Profile userProfile, boolean isLogedIn) {
        SessionManager.getInSharedPreference(context);
        SessionManager.sessionManager.setUserObj(gson.toJson(userProfile));
    }
}
