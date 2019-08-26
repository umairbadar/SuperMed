package managment.protege.supermed.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import managment.protege.supermed.Activity.Login;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.R;
import managment.protege.supermed.Tools.GlobalHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {
    Button EditProfile, changePassword, CreditCardDetails;
    TextView toolbar_text;

    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.settings), v);
        initializations(v);
        ProceedFunctions();
        return v;
    }

    public void initializations(View v) {
        toolbar_text = (TextView) v.findViewById(R.id.toolbar_text);
        EditProfile = (Button) v.findViewById(R.id.btn_settingsEditProfile);
        changePassword = (Button) v.findViewById(R.id.btn_settingsChangePassword);
        CreditCardDetails = (Button) v.findViewById(R.id.btn_settingsCardDetails);
    }

    public void ProceedFunctions() {
        //Edit Profile Button function
        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (GlobalHelper.getUserProfile(getContext()).getProfile().getFirstName().trim().toLowerCase().equals("guest")) {
                    noAcessDialog(getContext());
                } else {
                    Main_Apps.getMainActivity().backfunction(new Edit_Profile());
                }

            }
        });
        //Change password Button Function
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (GlobalHelper.getUserProfile(getContext()).getProfile().getFirstName().trim().toLowerCase().equals("guest")) {
                    noAcessDialog(getContext());
                } else {
                    Main_Apps.getMainActivity().backfunction(new ChangePassword());
                }
            }
        });
        //Credit Card Button function
        CreditCardDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main_Apps.getMainActivity().backfunction(new CreditCardDetails());
            }
        });
    }


    public void noAcessDialog(final Context context) {
        final Dialog forgotPasswordPopup = new Dialog(context);
        forgotPasswordPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgotPasswordPopup.setContentView(R.layout.guest_user_dialog);
        forgotPasswordPopup.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        forgotPasswordPopup.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        forgotPasswordPopup.setCancelable(false);
        final ImageView btn_close = (ImageView) forgotPasswordPopup.findViewById(R.id.iv_rp_close);
        Button btn_guest_register = (Button) forgotPasswordPopup.findViewById(R.id.btn_guest_register);
        Button btn_guest_login = (Button) forgotPasswordPopup.findViewById(R.id.btn_guest_login);
        TextView text = forgotPasswordPopup.findViewById(R.id.forgotPassword_email);
        text.setText("You have logged in as Guest Member, in order to access this feature. you need to Login OR Register on App");
        btn_guest_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Login.class);
                startActivity(i);

            }
        });
        btn_guest_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Register.class);
                startActivity(i);
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

}
