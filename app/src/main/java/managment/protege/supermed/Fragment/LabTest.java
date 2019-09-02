package managment.protege.supermed.Fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.Activity.WebViewActivity;
import managment.protege.supermed.Adapter.LabtestCartAdapter;
import managment.protege.supermed.Constant.Constantapp;
import managment.protege.supermed.Model.LoadLabsModel;
import managment.protege.supermed.Model.LoadTestModel;
import managment.protege.supermed.Model.TestModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Tools.GlobalHelper;
import okhttp3.RequestBody;

public class LabTest extends Fragment {

    private TextView appointment, labTest_price;
    private Spinner lab, labTest;

    //Labs
    private ArrayList<String> arr_lab;
    private ArrayList<String> arr_lab_id;

    //Tests
    private ArrayList<String> arr_test;
    private ArrayList<String> arr_test_id;
    private ArrayList<String> arr_test_price;

    private EditText details, labTest_address, mr_id, patientName;
    RadioButton lt_rb_credit, plt_rb_cod;
    private Button submit;
    public static DatePickerDialog datePicker;
    public static JSONArray jsonArray = new JSONArray();
    String testName;
    JSONObject tests = new JSONObject();

    private DatePickerDialog datePickerDialog;
    String labId, TestId, testPrice, dates, time, problems, pay_type, address, labName;
    Button add_test;
    public static List<TestModel> cataList = new ArrayList<>();
    public static LabtestCartAdapter cAdapter;
    JSONObject jsonBody = null;
    RecyclerView recyclerViewCata;
    RequestBody body;
    private static KProgressHUD labHud;
    TextView toolbar_text;

    public LabTest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lab_test, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), "LAB & TEST", v);
        initializations(v);

        labTest_address.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getAddress());
        spinnerFunctions();
        lt_rb_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        onClickFunctions();
        return v;
    }

    private void initializations(View v) {
        toolbar_text = (TextView) v.findViewById(R.id.toolbar_text);

        labHud = KProgressHUD.create(Main_Apps.getMainActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false);
        add_test = (Button) v.findViewById(R.id.add_test);
        recyclerViewCata = (RecyclerView) v.findViewById(R.id.recycler);
        cAdapter = new LabtestCartAdapter(cataList);
        patientName = (EditText) v.findViewById(R.id.labTest_patientName);
        mr_id = (EditText) v.findViewById(R.id.mr_id);
        appointment = (TextView) v.findViewById(R.id.labTest_Appointment);
        labTest_price = (TextView) v.findViewById(R.id.labTest_price);

        //Test
        labTest = (Spinner) v.findViewById(R.id.labTest_Test);
        arr_test_id = new ArrayList<>();
        arr_test = new ArrayList<>();
        arr_test.add("Select Test");
        arr_test_price = new ArrayList<>();
        labTest.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, arr_test));
        labTest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (labTest.getSelectedItemPosition() == 0) {
                    labTest_price.setText("");
                } else {
                    TestId = arr_test_id.get(i);
                    testPrice = arr_test_price.get(i);
                    labTest_price.setText(arr_test_price.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Labs
        lab = (Spinner) v.findViewById(R.id.labTest_lab);
        arr_lab = new ArrayList<>();
        arr_lab_id = new ArrayList<>();
        getLabs();
        lab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                labId = arr_lab_id.get(i);
                labName = arr_lab.get(i);
                if (lab.getSelectedItemPosition() != 0) {
                    getTests(labId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        details = (EditText) v.findViewById(R.id.labTest_Details);
        labTest_address = (EditText) v.findViewById(R.id.labTest_address);
        submit = (Button) v.findViewById(R.id.labTest_btn_submit);
        lt_rb_credit = (RadioButton) v.findViewById(R.id.lt_rb_credit);
        plt_rb_cod = (RadioButton) v.findViewById(R.id.plt_rb_cod);
        plt_rb_cod.setChecked(true);
    }

    public void DownloadBill(final Context context, final String recipt) {
        final Dialog downloadBillDialog = new Dialog(context);
        downloadBillDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        downloadBillDialog.setContentView(R.layout.bill_dialog_box);
        downloadBillDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        downloadBillDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        downloadBillDialog.setCancelable(false);
        final ImageView btn_close = (ImageView) downloadBillDialog.findViewById(R.id.iv_rp_close);
        Button sucess = (Button) downloadBillDialog.findViewById(R.id.btn_downloadBill);
        sucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(recipt)));
                Main_Apps.getMainActivity().backfunction2(new Home());
                downloadBillDialog.dismiss();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main_Apps.getMainActivity().backfunction2(new Home());
                downloadBillDialog.dismiss();
            }
        });
        downloadBillDialog.show();
    }


    private void fillCatacycle() {
        recyclerViewCata.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewCata.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCata.setAdapter(cAdapter);
        prepareCataData();
    }

    public static void delete(List<TestModel> data) {
        cataList = data;
        cAdapter.notifyDataSetChanged();
    }

    private void prepareCataData() {
        try {
            tests.put("date", dates);
            tests.put("lab_id", labId);
            tests.put("test_id", TestId);
            tests.put("price", testPrice);
            tests.put("time", dates);
            tests.put("problem", details.getText().toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        jsonArray.put(tests);
        // add rec
        TestModel movie = new TestModel(testName, TestId, labId, labName, testPrice);

        cataList.add(movie);

        cAdapter.notifyDataSetChanged();
    }

    private void dialogDatePicker() {
        final Calendar cur_calender = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getContext(), R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int _day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month + 1);
                calendar.set(Calendar.DAY_OF_MONTH, _day);
                dates = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(_day);
                datePickerDialog.dismiss();

                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // startingtime,ending;
                        time = hourOfDay + ":" + minute;

                        //   if(startingtime)
                        appointment.setText(dates + " " + time);


                    }
                }, cur_calender.get(Calendar.HOUR_OF_DAY), cur_calender.get(Calendar.MINUTE), false).show();
            }
        }, cur_calender.get(Calendar.YEAR), cur_calender.get(Calendar.MONTH), cur_calender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setCancelable(true);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void spinnerFunctions() {


        //Appointment Date and Time
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDatePicker();
            }

        });

    }

    private void onClickFunctions() {
        add_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillCatacycle();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                labHud.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (lt_rb_credit.isChecked()) {
                            pay_type = PlaceOrder.PAY_TYPE.KEENU.getValue();
                        } else {
                            pay_type = PlaceOrder.PAY_TYPE.CASH.getValue();
                        }
                        address = labTest_address.getText().toString();
                        problems = details.getText().toString();

                        if (jsonArray.length() != 0) {
                            BookAppointment();
                        } else {
                            labHud.dismiss();
                            Toasty.error(getContext(), "Please add test to proceed.", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                }, 1000);
            }
        });
    }

    public void getLabs() {

        labHud.show();
        String URL = "https://www.supermed.pk/api/api/list_lab";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            labHud.dismiss();
                            arr_lab.add("Select Lab");
                            arr_lab_id.add("0");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("labs");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String labId = object.getString("labId");
                                String labName = object.getString("labName");
                                arr_lab_id.add(labId);
                                arr_lab.add(labName);
                            }
                            lab.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_lab));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        labHud.dismiss();
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    public void getTests(final String LabID) {
        labHud.show();
        String URL = "https://www.supermed.pk/api/api/list_test";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            labHud.dismiss();
                            arr_test.clear();
                            arr_test_id.add("0");
                            arr_test.add("Select Test");
                            arr_test_price.add("0");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("tests");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String testId = object.getString("testId");
                                String testName = object.getString("testName");
                                String testPrice = object.getString("testPrice");
                                arr_test_id.add(testId);
                                arr_test.add(testName);
                                arr_test_price.add(testPrice);
                            }
                            labTest.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_test));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        labHud.dismiss();
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("lab_id", LabID);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }


    // payment by card popup
    public static void cardDetailDialog(final Context context, final RadioButton rb1) {
        final Dialog cardDetailsSubmit = new Dialog(context);
        cardDetailsSubmit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cardDetailsSubmit.setContentView(R.layout.credit_card_detail_dialog);
        cardDetailsSubmit.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        cardDetailsSubmit.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        cardDetailsSubmit.setCancelable(false);
        final EditText cardNumber = (EditText) cardDetailsSubmit.findViewById(R.id.checkoutCardNumber);
        final TextView checkoutExpiryDate = (TextView) cardDetailsSubmit.findViewById(R.id.checkoutExpiryDate);
        final EditText checkoutCvv = (EditText) cardDetailsSubmit.findViewById(R.id.checkoutCvv);
        final ImageView btn_close = (ImageView) cardDetailsSubmit.findViewById(R.id.iv_rp_close);
        Button sucess = (Button) cardDetailsSubmit.findViewById(R.id.btn_submitCard);
        checkoutExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate(context, checkoutExpiryDate);
            }
        });
        cardNumber.addTextChangedListener(new FourDigitCardFormatWatcher());
        sucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Register.etValidate(cardNumber) && Register.etValidateTv(checkoutExpiryDate) && Register.etValidate(checkoutCvv)) {
                    Toasty.success(context, "Card Details Added Successfully", Toast.LENGTH_SHORT, true).show();
                    cardDetailsSubmit.dismiss();
                } else {
                    Toasty.error(context, "Enter Details!", Toast.LENGTH_SHORT, true).show();

                }

            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb1.setChecked(true);


                cardDetailsSubmit.dismiss();
            }
        });
        cardDetailsSubmit.show();
    }

    public static void showDate(Context context, final TextView editText) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current date
        // dateEditText picker dialog
        datePicker = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set date of month , month and year value in the edit text
                        editText.setText((monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePicker.show();
    }

    public static class FourDigitCardFormatWatcher implements TextWatcher {

        // Change this to what you want... ' ', '-' etc..
        private static final char space = ' ';

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Remove spacing char
            if (s.length() > 0 && (s.length() % 5) == 0) {
                final char c = s.charAt(s.length() - 1);
                if (space == c) {
                    s.delete(s.length() - 1, s.length());
                }
            }
            // Insert char where needed.
            if (s.length() > 0 && (s.length() % 5) == 0) {
                char c = s.charAt(s.length() - 1);
                // Only if its a digit where there should be a space we insert a space
                if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                    s.insert(s.length() - 1, String.valueOf(space));
                }
            }
        }

    }

    public void BookAppointment() {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection conn = null;

        jsonBody = new JSONObject();
        try {
            jsonBody.put("patient_name", patientName.getText().toString());
            jsonBody.put("gender", GlobalHelper.getUserProfile(getContext()).getProfile().getGender());
            jsonBody.put("patient_address", GlobalHelper.getUserProfile(getContext()).getProfile().getAddress());
            jsonBody.put("pay_type", pay_type);
            jsonBody.put("patient_id", GlobalHelper.getUserProfile(getContext()).getProfile().getPatientId());
            jsonBody.put("tests", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            URL url = new URL(Constantapp.WEB_URL + "book_appointment");

            String message = jsonBody.toString();
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /*milliseconds*/);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(message.getBytes().length);
            //make some HTTP header nicety
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            //open
            conn.connect();
            //setup send
            os = new BufferedOutputStream(conn.getOutputStream());
            os.write(message.getBytes());
            //clean up
            os.flush();
            StringBuffer response;
            //do somehting with response
            is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            response = new StringBuffer();
            //Expecting answer of type JSON single line {"json_items":[{"status":"OK","message":"<Message>"}]}
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();
            String a = response.toString();
            try {
                JSONObject jsonObject = new JSONObject(a);
                message = jsonObject.getString("status");
                final String recipt = jsonObject.getString("receipt");
                final String orderId = jsonObject.getJSONObject("data").getString("appointment_no");
                final String orderAmount = jsonObject.getJSONObject("data").getString("total_price");
                Log.e("message", message);
                cataList.clear();
                labHud.dismiss();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pay_type == PlaceOrder.PAY_TYPE.CASH.getValue()) {
                            DownloadBill(getContext(), recipt);
                        } else {
                            Intent intent = new Intent(getContext(), WebViewActivity.class);
                            intent.putExtra("orderid", orderId);
                            intent.putExtra("orderamount", orderAmount);
                            intent.putExtra("Type", "Appointment");
                            startActivity(intent);
                        }
                    }
                }, 1000);
            } catch (JSONException e) {

                e.printStackTrace();
            }
            labHud.dismiss();
            System.out.println(response.toString() + "\n");
            conn.disconnect(); // close the connection a
            //String contentAsString = readIt(is,len);
            labHud.dismiss();
        } catch (IOException e) {
            e.printStackTrace();
            labHud.dismiss();
        }
        labHud.dismiss();
    }
}