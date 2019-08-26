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
import retrofit2.Response;

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

    public void UpdatePassword(final String userid, String old_pass, String new_pass, String con_pass) {
        Main_Apps.hud.show();
        API api = RetrofitAdapter.createAPI();
        Call<ChangePasswordResponse> changePasswordResponseCall = api.CHANGE_PASSWORD_RESPONSE_CALL(userid, old_pass, new_pass, con_pass);
        changePasswordResponseCall.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                Main_Apps.hud.dismiss();
                if (response != null) {
                    if (response.body().getStatus()) {

                        Toasty.success(getContext(), "Password Changed successfully", Toast.LENGTH_SHORT, true).show();

                    } else {
                        Toasty.error(getContext(), "Password cant be changed because " + response.body().getMessage(), Toast.LENGTH_SHORT, true).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                Main_Apps.hud.dismiss();
                Toasty.error(getContext(), "Error " + t.getMessage(), Toast.LENGTH_SHORT, true).show();

                Log.e("Change Password", "error" + t.getMessage());
            }
        });
    }
}
