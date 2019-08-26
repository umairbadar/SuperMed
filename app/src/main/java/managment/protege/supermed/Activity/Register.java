package managment.protege.supermed.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import managment.protege.supermed.R;

public class Register extends AppCompatActivity {

    private EditText et_fname, et_lname, et_email, et_phone, et_password, et_cpassword;
    private Button btn_signup;
    private TextView tv_dob;
    private Spinner spn_gender, spn_city;
    private ArrayList<String> arr_city;
    private ArrayList<String> arr_gender;
    private DatePickerDialog datePicker;
    public static KProgressHUD hud;
    private TextView tv_already_account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initializations
        initViews();

        arr_city = new ArrayList<>();
        getCities();


        //Gender
        arr_gender = new ArrayList<>();
        arr_gender.add("Select Gender");
        arr_gender.add("Male");
        arr_gender.add("Female");
        spn_gender.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, arr_gender));

        tv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate();
            }
        });

        tv_already_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_fname.getText().toString())) {
                    et_fname.setError("Please Enter First Name");
                    et_fname.requestFocus();
                } else if (TextUtils.isEmpty(et_lname.getText().toString())) {
                    et_lname.setError("Please Enter Last Name");
                    et_lname.requestFocus();
                } else if (TextUtils.isEmpty(et_email.getText().toString())) {
                    et_email.setError("Please Enter Email");
                    et_email.requestFocus();
                } else if (!isValidEmail(et_email)) {
                    et_email.setError("Please Enter Valid Email Address");
                    et_email.requestFocus();
                } else if (TextUtils.isEmpty(et_phone.getText().toString())) {
                    et_phone.setError("Please Enter Phone Number");
                    et_phone.requestFocus();
                } else if (TextUtils.isEmpty(tv_dob.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please Select Date of Birth",
                            Toast.LENGTH_LONG).show();
                } else if (spn_gender.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "Plase Select Gender",
                            Toast.LENGTH_LONG).show();
                } else if (spn_city.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "Plase Select City",
                            Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(et_password.getText().toString())) {
                    et_password.setError("Please Enter Password");
                    et_password.requestFocus();
                } else if (TextUtils.isEmpty(et_cpassword.getText().toString())) {
                    et_cpassword.setError("Please Enter confirm Password");
                    et_cpassword.requestFocus();
                } else if (!et_password.getText().toString().equals(et_cpassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Password not match",
                            Toast.LENGTH_LONG).show();
                } else {
                    hud.show();
                    RegisterUser();
                }
            }
        });
    }

    private void initViews() {

        hud = KProgressHUD.create(Register.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false);
        et_fname = findViewById(R.id.et_fname);
        et_lname = findViewById(R.id.et_lname);
        et_email = findViewById(R.id.et_email);
        et_phone = findViewById(R.id.et_phone);
        et_password = findViewById(R.id.et_password);
        et_cpassword = findViewById(R.id.et_cpassword);
        btn_signup = findViewById(R.id.btn_signup);

        tv_dob = findViewById(R.id.tv_dob);
        tv_already_account = findViewById(R.id.tv_already_account);

        spn_gender = findViewById(R.id.spinner_gender);
        spn_city = findViewById(R.id.spinner_city);
    }

    //validation function
    public static boolean etValidate(EditText edittext) {
        String validate = edittext.getText().toString().trim();
        validate = validate.replaceAll("\\s+", " ").trim();
        if (validate.isEmpty()) {
            edittext.setError("Please Enter Values");
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(EditText email) {
        boolean v = !TextUtils.isEmpty(email.getText().toString().trim()) && android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches();
        if (!v) {
            email.setError("Please Enter Valid Email");
        }
        return !TextUtils.isEmpty(email.getText().toString().trim()) && android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches();
    }

    //password matching function
    public static boolean passwordMatch(EditText editText1, EditText editText2) {
        String password1 = editText1.getText().toString();
        String password2 = editText2.getText().toString();
        if (!password1.equals(password2)) {
            editText1.setError("Passwords don't match");
            editText2.setError("Passwords don't match");
            return false;
        }
        return true;
    }

    public static boolean etValidateTv(TextView edittext) {
        String validate = edittext.getText().toString().trim();
        validate = validate.replaceAll("\\s+", " ").trim();
        if (validate.isEmpty()) {
            edittext.setError("Please Enter Values");
            return false;
        }
        return true;
    }

    private void getCities() {

        String URL = "https://www.supermed.pk/api/api/getCities";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            arr_city.add("Select City");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cities");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                arr_city.add(jsonObject1.getString("name"));
                            }
                            spn_city.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_city));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
    }

    public void showDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current date
        // dateEditText picker dialog
        datePicker = new DatePickerDialog(Register.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set date of month , month and year value in the edit text
                        tv_dob.setText(dayOfMonth + "-"
                                + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePicker.show();
    }

    private void RegisterUser() {
        String URL = "https://www.supermed.pk/api/api/signupUser";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean status = jsonObject.getBoolean("status");
                            if (status.equals(true)) {
                                hud.dismiss();
                                finish();
                                startActivity(new Intent(getApplicationContext(), Login.class));
                                Toast.makeText(getApplicationContext(), "Please Login to Continue",
                                        Toast.LENGTH_LONG).show();

                            } else {
                                hud.dismiss();
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"),
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
                        Toast.makeText(getApplicationContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("first_name", et_fname.getText().toString());
                map.put("last_name", et_lname.getText().toString());
                map.put("contact", et_phone.getText().toString());
                map.put("dob", tv_dob.getText().toString());
                map.put("city", spn_city.getSelectedItem().toString());
                map.put("email", et_email.getText().toString());
                map.put("password", et_password.getText().toString());
                map.put("gender", spn_gender.getSelectedItem().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
    }
}
