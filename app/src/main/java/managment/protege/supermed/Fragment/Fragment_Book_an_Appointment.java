package managment.protege.supermed.Fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.R;
import managment.protege.supermed.Tools.GlobalHelper;

public class Fragment_Book_an_Appointment extends Fragment {

    private EditText et_patient_name, et_problem_detail, et_address;
    private Button btn_add_test, btn_submit, btn_remove;
    private TextView tv_dob, tv_head, tv_test_price, tv_show_date, tv_show_time, tv_country;
    private DatePickerDialog datePicker;
    private LinearLayout mainLayout, childLayout;

    //Storing data for post request
    private String[] lab_id = new String[6];
    private String[] test_id = new String[6];
    private String[] price = new String[6];
    private String[] date = new String[6];
    private String[] time = new String[6];
    private String[] problem = new String[6];
    private String gender, city, payment_method;

    //Lab
    private Spinner spn_lab;
    private ArrayList<String> arr_lab;
    private ArrayList<String> arr_lab_id;

    //Tests
    private Spinner spn_test;
    private ArrayList<String> arr_test;
    private ArrayList<String> arr_test_id;
    private ArrayList<String> arr_test_price;

    //Gender
    private Spinner spn_gender;
    private ArrayList<String> arr_gender;

    //Payment Method
    private Spinner spn_payment_method;
    private ArrayList<String> arr_payment_method;

    //City
    private Spinner spn_city;
    private ArrayList<String> arr_city;


    public Fragment_Book_an_Appointment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_an_appointment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), "BOOK AN APPOINTMENT", v);

        //Initializing Widgets
        initWidgets(v);

        return v;
    }

    private void initWidgets(View view) {

        btn_remove = view.findViewById(R.id.btn_remove);
        mainLayout = view.findViewById(R.id.mainLayout);
        addTestViews();

        //Gender
        spn_gender = view.findViewById(R.id.spn_gender);
        arr_gender = new ArrayList<>();
        arr_gender.add("Gender");
        arr_gender.add("Male");
        arr_gender.add("Female");
        spn_gender.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, arr_gender));

        spn_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = arr_gender.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Payment Method
        spn_payment_method = view.findViewById(R.id.spn_payment_method);
        arr_payment_method = new ArrayList<>();
        arr_payment_method.add("Select Payment Method");
        arr_payment_method.add("Cash on Delivery");
        arr_payment_method.add("Pay with Keenu");
        spn_payment_method.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, arr_payment_method));

        spn_payment_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                payment_method = arr_payment_method.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //City
        spn_city = view.findViewById(R.id.spn_city);
        arr_city = new ArrayList<>();
        getCities();

        spn_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                city = arr_city.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        et_patient_name = view.findViewById(R.id.et_patient_name);
        btn_add_test = view.findViewById(R.id.btn_add_test);
        btn_submit = view.findViewById(R.id.btn_submit);
        tv_dob = view.findViewById(R.id.tv_dob);
        tv_country = view.findViewById(R.id.tv_country);
        et_address = view.findViewById(R.id.et_address);

        tv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate();
            }
        });

        btn_add_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tv_test_price.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Please select test first",
                            Toast.LENGTH_LONG).show();
                } else if (tv_show_date.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Please select date first",
                            Toast.LENGTH_LONG).show();
                } else if (tv_show_time.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Please select time first",
                            Toast.LENGTH_LONG).show();
                } else if (et_problem_detail.getText().toString().length() == 0) {
                    et_problem_detail.setError("Please enter problem detail");
                    et_problem_detail.requestFocus();
                } else {
                    addTestViews();
                }

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(et_patient_name.getText().toString())) {
                    et_patient_name.setError("Enter Patient Name");
                    et_patient_name.requestFocus();
                } else if (spn_gender.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please select gender",
                            Toast.LENGTH_LONG).show();
                    spn_gender.requestFocus();
                } else if (tv_dob.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Please enter DOB",
                            Toast.LENGTH_LONG).show();
                    tv_dob.requestFocus();
                } else if (spn_city.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please select City",
                            Toast.LENGTH_LONG).show();
                    spn_city.requestFocus();
                } else if (TextUtils.isEmpty(et_address.getText().toString())) {
                    et_address.setError("Please enter Address");
                    et_address.requestFocus();
                } else if (spn_lab.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please select Lab",
                            Toast.LENGTH_LONG).show();
                    spn_lab.requestFocus();
                } else if (spn_test.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please select Test",
                            Toast.LENGTH_LONG).show();
                    spn_test.requestFocus();
                } else if (tv_show_date.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Please enter Date",
                            Toast.LENGTH_LONG).show();
                    tv_show_date.requestFocus();
                } else if (tv_show_time.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Please enter Time",
                            Toast.LENGTH_LONG).show();
                    tv_show_time.requestFocus();
                } else if (TextUtils.isEmpty(et_problem_detail.getText().toString())) {
                    et_problem_detail.setError("Please enter problem");
                    et_problem_detail.requestFocus();
                } else if (spn_payment_method.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please select payment method",
                            Toast.LENGTH_LONG).show();
                    spn_payment_method.requestFocus();
                } else {
                    sendRequest();
                }

            }
        });

        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainLayout.getChildCount() == 2) {
                    mainLayout.removeViewAt(mainLayout.getChildCount() - 1);
                    btn_remove.setVisibility(View.GONE);
                } else {
                    mainLayout.removeViewAt(mainLayout.getChildCount() - 1);
                }

            }
        });
    }

    private void addTestViews() {

        childLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.childlayout,
                null, false);

        if (mainLayout.getChildCount() >= 1) {
            btn_remove.setVisibility(View.VISIBLE);
        } else {
            btn_remove.setVisibility(View.GONE);
        }

        tv_test_price = childLayout.findViewById(R.id.tv_test_price);
        tv_show_date = childLayout.findViewById(R.id.tv_show_date);
        tv_show_time = childLayout.findViewById(R.id.tv_show_time);
        et_problem_detail = childLayout.findViewById(R.id.et_problem_detail);

        et_problem_detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                problem[mainLayout.getChildCount() - 1] = cs.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tv_show_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate1();
            }
        });

        tv_show_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTime();
            }
        });

        //Getting Labs
        spn_lab = childLayout.findViewById(R.id.spn_lab);
        arr_lab = new ArrayList<>();
        arr_lab_id = new ArrayList<>();
        getLabs();

        //Initializing Test Spinner
        spn_test = childLayout.findViewById(R.id.spn_test);
        arr_test_id = new ArrayList<>();
        arr_test = new ArrayList<>();
        arr_test_price = new ArrayList<>();

        spn_lab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String id = arr_lab_id.get(i);
                if (!id.equals("0")) {
                    getTest(id);
                    lab_id[mainLayout.getChildCount() - 1] = arr_lab_id.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!arr_test_price.get(i).equals("price")) {
                    tv_test_price.setText(arr_test_price.get(i));
                    test_id[mainLayout.getChildCount() - 1] = arr_test_id.get(i);
                    price[mainLayout.getChildCount() - 1] = arr_test_price.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tv_head = childLayout.findViewById(R.id.tv_head);
        if (mainLayout.getChildCount() < 5) {
            childLayout.setTag(mainLayout.getChildCount() + 1);
            mainLayout.removeView(childLayout);
            mainLayout.addView(childLayout);
            tv_head.setText("Lab Test No." + String.valueOf(mainLayout.getChildCount()));
        } else {
            Toast.makeText(getContext(), "You can add five test at a time.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void showDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current date
        // dateEditText picker dialog
        datePicker = new DatePickerDialog(getContext(),
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

    public void showDate1() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current date
        // dateEditText picker dialog
        datePicker = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set date of month , month and year value in the edit text
                        tv_show_date.setText(dayOfMonth + "-"
                                + (monthOfYear + 1) + "-" + year);
                        date[mainLayout.getChildCount() - 1] = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    }
                }, mYear, mMonth, mDay);
        datePicker.show();
    }

    private void showTime() {
        // TODO Auto-generated method stub
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                tv_show_time.setText(selectedHour + ":" + selectedMinute);
                time[mainLayout.getChildCount() - 1] = selectedHour + ":" + selectedMinute;
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void getCities() {
        String URL = Register.Base_URL + "city-list";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            arr_city.add("City");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                arr_city.add(jsonObject1.getString("name"));
                            }
                            spn_city.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_city));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    private void getLabs() {

        String URL = Register.Base_URL + "listing-for/lab-list";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            arr_lab_id.add("0");
                            arr_lab.add("Select Lab");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id");
                                String name = object.getString("name");
                                arr_lab_id.add(id);
                                arr_lab.add(name);
                            }
                            spn_lab.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_lab));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    private void getTest(String labID) {
        Main_Apps.hud.show();
        String URL = Register.Base_URL + "test-list-by-lab/" + labID;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Main_Apps.hud.dismiss();
                            arr_test_id.add("0");
                            arr_test.add("Select Test");
                            arr_test_price.add("price");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id");
                                String name = object.getString("name");
                                String price = object.getString("price");

                                arr_test_id.add(id);
                                arr_test.add(name);
                                arr_test_price.add(price);
                            }

                            spn_test.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_test));


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
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    private void sendRequest() {
        Main_Apps.hud.show();
        String URL = Register.Base_URL + "add-appointment";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            String msg = jsonObject.getString("message");
                            if (status) {
                                Main_Apps.hud.dismiss();
                                Toast.makeText(getContext(), msg,
                                        Toast.LENGTH_LONG).show();
                                Main_Apps.getMainActivity().backfunction(new Home());
                            } else {
                                Main_Apps.hud.dismiss();
                                Toast.makeText(getContext(), msg,
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("patient_id", GlobalHelper.getUserProfile(getContext()).getProfile().getPatientId());
                map.put("patient_name", et_patient_name.getText().toString());
                map.put("gender", gender);
                map.put("dob", tv_dob.getText().toString());
                map.put("country", tv_country.getText().toString());
                map.put("city", city);
                map.put("address", et_address.getText().toString());
                for (int i = 0; i < mainLayout.getChildCount(); i++) {
                    map.put("lab_id[" + i + "]", lab_id[i]);
                    map.put("test_id[" + i + "]", test_id[i]);
                    map.put("price[" + i + "]", price[i]);
                    map.put("preferred_date[" + i + "]", date[i]);
                    map.put("preferred_time[" + i + "]", time[i]);
                    map.put("problem_details[" + i + "]", problem[i]);
                }
                map.put("payment_method", payment_method);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

}