package managment.protege.supermed.Activity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Fragment.Home;
import managment.protege.supermed.Model.Banner;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.BannerResponse;
import managment.protege.supermed.Response.ForgotPassword;
import managment.protege.supermed.Response.LoginResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    String socialId, userEmail, userFirstName;
    String profilePicUrl;
    TextView forgotPassword;
    EditText login_email, login_password;
    Button login_btn, login_google;
    TextView btn_create_account, btn_guest_user;
    com.facebook.login.widget.LoginButton login_facebook;
    String email, password;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 007;
    CallbackManager mFacebookCallbackManager;
    List<Banner> listforbanner = new ArrayList<Banner>();

    public static KProgressHUD hud;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs3;
    private static boolean RUN_ONCE = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mFacebookCallbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        initializations();
        FacebookLogin();
        setGoogleIntegertion();
        banner();
        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        btn_guest_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //LoginByGuestUser();
                GuestLogin();
            }
        });

        CheckHomeOrLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckHomeOrLogin();
    }

    private void CheckHomeOrLogin() {
        prefs3 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        if (RUN_ONCE) {
            RUN_ONCE = false;

            Intent intent = new Intent(Login.this, Main_Apps.class);
            startActivity(intent);
        } else {
        }

        if (prefs3.getBoolean("ComingFromRegister", false)) {
            Intent intent = new Intent(Login.this, Main_Apps.class);
            startActivity(intent);
        } else {
        }

    }

    public void FacebookLogin() {
        login_facebook = (com.facebook.login.widget.LoginButton) findViewById(R.id.login_facebook);
        LoginManager.getInstance().logOut();
        login_facebook.setReadPermissions("email", "public_profile", "user_friends");
        login_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login_facebook.registerCallback(mFacebookCallbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(final LoginResult loginResult) {
                                AccessToken.getCurrentAccessToken().getToken();
                                loginResult.getAccessToken().getToken();
                                loginResult.getAccessToken().getUserId();

                                final GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());
                                        try {
                                            Log.v("LoginScreen Inside Try", response.toString());
                                            profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                            Log.e("profile", "picture " + profilePicUrl);
                                            socialId = object.getString("id");
                                            userFirstName = object.getString("name");
                                            userEmail = object.getString("email");

                                            SocialLogin(socialId, userEmail, userFirstName);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                String userid = loginResult.getAccessToken().getUserId();
                                Bundle parameters = new Bundle();

                                parameters.putString("fields", "id,name,email,picture.type(large)");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }

                            @Override
                            public void onCancel() {

                            }

                            @Override
                            public void onError(FacebookException error) {
                                Log.d(Login.class.getCanonicalName(), error.getMessage());
                            }


                        }
                );
            }
        });

    }

    public void initializations() {
        hud = KProgressHUD.create(Login.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false);

        login_email = (EditText) findViewById(R.id.login_email);
        login_password = (EditText) findViewById(R.id.login_password);
        forgotPassword = (TextView) findViewById(R.id.login_tv_forgot);
        forgotPassword.setOnClickListener(this);
        login_btn = (Button) findViewById(R.id.login_btn);
        login_google = (Button) findViewById(R.id.login_google);
        login_google.setOnClickListener(this);
        btn_create_account = (TextView) findViewById(R.id.btn_create_account);
        btn_guest_user = (TextView) findViewById(R.id.btn_guest_user);

        btn_create_account.setOnClickListener(this);
        btn_guest_user.setOnClickListener(this);
        login_btn.setOnClickListener(this);
    }

    public void validation() {
        //checking email empty or not
        if (login_email.length() != 0) {
            //checking password empty or notvcvcvc
            if (login_password.length() != 0) {
                //checking length of password should be more than 5
                if (login_password.length() > 0) {
                    //going to other activity by signing in
                    AssignValuesOnValidation();
                    //LoginApi(email,password);
                    LoginApi(login_email.getText().toString(), login_password.getText().toString());
                } else {
                    login_password.setError("Password Should be more than 5 characters");
                }
            } else {
                login_password.setError("Please Enter Password");
            }

        } else {
            login_email.setError("Please Enter Email");
        }
    }

    private void setGoogleIntegertion() {
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();//
    }

    private void AssignValuesOnValidation() {
        email = login_email.getText().toString();
        password = login_password.getText().toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                validation();
                break;
            case R.id.login_google:
                signIn();
                break;
            case R.id.login_tv_forgot:
                forgotPasswordDialog(this);
        }
    }


    void openWhatsappContact(String number) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(i, "sd"));
    }

    public void forgotPasswordDialog(final Context context) {
        final Dialog forgotPasswordPopup = new Dialog(context);
        forgotPasswordPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgotPasswordPopup.setContentView(R.layout.forgot_password_dialog);
        forgotPasswordPopup.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        forgotPasswordPopup.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        forgotPasswordPopup.setCancelable(false);
        final ImageView btn_close = (ImageView) forgotPasswordPopup.findViewById(R.id.iv_rp_close);
        final EditText email = (EditText) forgotPasswordPopup.findViewById(R.id.forgotPassword_email);
        Button sucess = (Button) forgotPasswordPopup.findViewById(R.id.btn_resetPassword);
        sucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Register.isValidEmail(email)) {
                    Toasty.success(context, "Reset Request Sent", Toast.LENGTH_SHORT, true).show();
                    forgotPass(email.getText().toString());
                    forgotPasswordPopup.dismiss();
                } else {
                    Toasty.error(context, "Email is not valid", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordPopup.dismiss();
            }
        });
        forgotPasswordPopup.show();
    }

    public void LoginApi(final String email, final String password) {

        hud.show();
        API api = RetrofitAdapter.createAPI();
        Call<LoginResponse> callBackCall = api.Login(email, password);
        callBackCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                hud.dismiss();
                if (response != null) {
                    if (response.body().getStatus()) {
                        GlobalHelper.saveUserInLocal(getApplicationContext(), response.body(), true);
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.putBoolean("CheckBox", true);
                        editor.apply();
                        Intent intent = new Intent(Login.this, Main_Apps.class);
                        intent.putExtra("mylist", (Serializable) listforbanner);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (response.body().getMessage().equals("Invalid Password Or Email Address.")) {
                        //login_password.setError("The password you have entered for this email is incorrect");
                        Toast.makeText(getApplicationContext(),"Please Enter valid Email and Password",
                                Toast.LENGTH_LONG).show();
                    }/* else if (response.body().getMessage().equals("email and password are both incorrect")) {
                        login_email.setError("The Email is not associated with any account");
                    }*/
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hud.dismiss();
                Log.e("Login", "Error is " + t.getMessage());
            }
        });
    }

    public void GuestLogin() {

        hud.show();
        API api = RetrofitAdapter.createAPI();
        Call<LoginResponse> callBackCall = api.LoginGuestUser();
        callBackCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                hud.dismiss();
                if (response != null) {
                    if (response.body().getStatus()) {
                        GlobalHelper.saveUserInLocal(getApplicationContext(), response.body(), true);
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.putBoolean("CheckBox", true);
                        editor.apply();
                        Intent intent = new Intent(Login.this, Main_Apps.class);
                        intent.putExtra("mylist", (Serializable) listforbanner);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (response.body().getMessage().equals("Invalid Password Or Email Address.")) {
                        //login_password.setError("The password you have entered for this email is incorrect");
                        Toast.makeText(getApplicationContext(),"Please Enter valid Email and Password",
                                Toast.LENGTH_LONG).show();
                    }/* else if (response.body().getMessage().equals("email and password are both incorrect")) {
                        login_email.setError("The Email is not associated with any account");
                    }*/
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hud.dismiss();
                Log.e("Login", "Error is " + t.getMessage());
            }
        });
    }

    public void SocialLogin(String socialId, String useremail, String username) {
        hud.show();
        API api = RetrofitAdapter.createAPI();
        Call<LoginResponse> callBackCall = api.SOCIAL_LOGIN_RESPONSE_CALL(socialId, useremail, username);
        callBackCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                hud.dismiss();
                if (response != null) {
                    if (response.body().getStatus()) {
                        GlobalHelper.saveUserInLocal(getApplicationContext(), response.body(), true);

                        Intent intent = new Intent(Login.this, Main_Apps.class);
                        intent.putExtra("mylist", (Serializable) listforbanner);
                        startActivity(intent);
                        //Toast.makeText(Login.this, "Successfully Logged In.", Toast.LENGTH_SHORT).show();
                    } else if (response.body().getMessage().equals("password incorrect")) {

                        login_password.setError("The password you have entered for this email is incorrect");
                        //Toast.makeText(Login.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else if (response.body().getMessage().equals("email and password are both incorrect")) {
                        login_email.setError("The Email is not associated with any account");
                        //Toast.makeText(Login.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hud.dismiss();
                Log.e("Login", "Error is " + t.getMessage());
            }
        });

    }

    public void LoginByGuestUser() {
        hud.show();
        API api = RetrofitAdapter.createAPI();
        Call<LoginResponse> callBackCall = api.GUEST_USER_RESPONSE_CALL();
        callBackCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                hud.dismiss();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        GlobalHelper.saveUserInLocal(getApplicationContext(), response.body(), true);

                        Intent intent = new Intent(Login.this, Main_Apps.class);
                        intent.putExtra("mylist", (Serializable) listforbanner);
                        startActivity(intent);
                    } else {
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hud.dismiss();
                Log.e("Logins", "Error is " + t.getMessage());
            }
        });
    }

    public void forgotPass(String email) {
        hud.show();
        API api = RetrofitAdapter.createAPI();
        Call<ForgotPassword> callBackCall = api.forgotPass(email);
        callBackCall.enqueue(new Callback<ForgotPassword>() {
            @Override
            public void onResponse(Call<ForgotPassword> call, final Response<ForgotPassword> response) {
                hud.dismiss();

                if (response != null) {
                    if (response.body().getStatus()) {
                    } else {
                    }
                }
            }

            @Override
            public void onFailure(Call<ForgotPassword> call, Throwable t) {
                hud.dismiss();
                Log.e("Logins", "Error is " + t.getMessage());
            }
        });
    }

    public void banner() {
        API api = RetrofitAdapter.createAPI();
        Call<BannerResponse> callBackCall = api.getBanners();
        callBackCall.enqueue(new Callback<BannerResponse>() {
            @Override
            public void onResponse(Call<BannerResponse> call, final Response<BannerResponse> response) {
                Log.e("tag", response.body().getBanners().toString());
                listforbanner = response.body().getBanners();

            }

            @Override
            public void onFailure(Call<BannerResponse> call, Throwable t) {
                Log.e("Login", "Error is " + t.getMessage());
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // (...)

        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Log.e("Google Request Code", String.valueOf(RC_SIGN_IN));
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.e("G+ Result", result.toString() + " ");
            Log.e("Request Code--", "" + result.getStatus().getStatusCode());
            Log.e("Request Code--", "" + result.getStatus().getStatusMessage());
            handleSignInResult(result);
            signOut();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        GoogleSignInAccount acct = result.getSignInAccount();
        Log.e("G+ Checking Success", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            socialId = acct.getId();
            userEmail = acct.getEmail();
            userFirstName = acct.getDisplayName();
            SocialLogin(socialId, userEmail, userFirstName);


        } else {
            Log.e("in else", "else data");
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
