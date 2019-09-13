package managment.protege.supermed.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.ChangePasswordResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePassword extends Fragment {
    Button changePassword;
    EditText oldPassword, newPassword, againNewPassword;
    String old_pass, new_pass, con_pass;
    TextView toolbar_text;

    public ChangePassword() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change_password, container, false);

        initializations(v);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.change_password), v);

        ProceedingFunctions();
        return v;
    }

    private void initializations(View v) {
        toolbar_text = (TextView) v.findViewById(R.id.toolbar_text);
        changePassword = (Button) v.findViewById(R.id.btn_changepassword);
        oldPassword = (EditText) v.findViewById(R.id.oldpassword);
        newPassword = (EditText) v.findViewById(R.id.newpassword);
        againNewPassword = (EditText) v.findViewById(R.id.confirm_newpassword);
    }

    public void ProceedingFunctions() {

        //change password button on click
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                old_pass = oldPassword.getText().toString();
                new_pass = newPassword.getText().toString();
                con_pass = againNewPassword.getText().toString();
                if (Register.etValidate(oldPassword) && Register.etValidate(newPassword) && Register.etValidate(againNewPassword)) {
                    if (Register.passwordMatch(newPassword, againNewPassword)) {
                        UpdatePassword(GlobalHelper.getUserProfile(getContext()).getProfile().getId(), old_pass, new_pass, con_pass);
                    }
                }
            }
        });
    }

    public void UpdatePassword(final String userid, final String old_pass, final String new_pass, final String con_pass) {
        Main_Apps.hud.show();
        String URL = Register.Base_URL + "user-update-password/" + userid;
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            String message = jsonObject.getString("message");
                            if (status){
                                Main_Apps.hud.dismiss();
                                Toast.makeText(getContext(), message,
                                        Toast.LENGTH_LONG).show();
                                oldPassword.setText("");
                                newPassword.setText("");
                                againNewPassword.setText("");
                            } else {
                                Main_Apps.hud.dismiss();
                                Toast.makeText(getContext(), message,
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Main_Apps.hud.dismiss();
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("old_password", old_pass);
                map.put("password", new_pass);
                map.put("confirm_password", con_pass);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
