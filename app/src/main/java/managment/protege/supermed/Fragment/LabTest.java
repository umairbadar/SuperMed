package managment.protege.supermed.Fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.KeyEvent;
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


import com.kaopiz.kprogresshud.KProgressHUD;
import com.rw.loadingdialog.LoadingView;

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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Login;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.Activity.WebViewActivity;
import managment.protege.supermed.Adapter.HelpCenterAdapter;
import managment.protege.supermed.Adapter.LabtestCartAdapter;
import managment.protege.supermed.Constant.Constantapp;
import managment.protege.supermed.Model.HelpCenterModel;
import managment.protege.supermed.Model.LoadLabsModel;
import managment.protege.supermed.Model.LoadTestModel;
import managment.protege.supermed.Model.TestModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.BookAppointmentResponse;
import managment.protege.supermed.Response.LoadLabsResponse;
import managment.protege.supermed.Response.LoadTestResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LabTest extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private TextView patientId, appointment, labTest_price;
    private Spinner lab, labTest;
    private EditText details, labTest_address, mr_id, patientName;
    RadioButton lt_rb_credit, plt_rb_cod;
    List<LoadLabsModel> loadLabsModels;
    List<LoadTestModel> loadTestModels;
    private Button submit;
    public static DatePickerDialog datePicker;
    public static JSONArray jsonArray = new JSONArray();
    String testName;
    //    String paytype;
    JSONObject tests = new JSONObject();

    private DatePickerDialog datePickerDialog;
    String labId, TestId, testPrice, dates, time, problems, pay_type, address, labName;
    public DatePickerDialog datePickers;
    TimePickerDialog tp;
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
        lab = (Spinner) v.findViewById(R.id.labTest_lab);
        labTest = (Spinner) v.findViewById(R.id.labTest_Test);
        details = (EditText) v.findViewById(R.id.labTest_Details);
        labTest_address = (EditText) v.findViewById(R.id.labTest_address);
        submit = (Button) v.findViewById(R.id.labTest_btn_submit);
        labTest_price = (TextView) v.findViewById(R.id.labTest_price);
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

        loadLabsSpinner(lab, getContext());
        SelectLabId(lab);
        SelectTestId(labTest);


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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    //loading labs api function
    public void loadLabsSpinner(final Spinner le_salesperson, final Context context) {
        Main_Apps.hud.show();

        API api = RetrofitAdapter.createAPI();
        Call<LoadLabsResponse> loadLabsResponseCall = api.LOAD_LABS_RESPONSE_CALL();
        loadLabsResponseCall.enqueue(new Callback<LoadLabsResponse>() {
            @Override
            public void onResponse(Call<LoadLabsResponse> call, Response<LoadLabsResponse> response) {
                Main_Apps.hud.dismiss();
                if (response != null) {
                    if (response.body().getStatus()) {
                        loadLabsModels = response.body().getLabs();
                        setLabs(response.body().getLabs(), le_salesperson, context);

                    } else {
                        Log.e("Leads", "sales team spinner" + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoadLabsResponse> call, Throwable t) {
                Main_Apps.hud.dismiss();
                Log.e("Leads", "sales team spinner on failure" + t.getMessage());
            }
        });
    }

    static private void setLabs(List<LoadLabsModel> loadLabsModels, Spinner le_salesteam, Context context) {
        List<String> listSpinner = new ArrayList<String>();
        listSpinner.clear();
        for (int i = 0; i < loadLabsModels.size(); i++) {
            listSpinner.add(loadLabsModels.get(i).getLabName());
            ArrayAdapter<String> csAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listSpinner);
            csAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            le_salesteam.setAdapter(csAdapter);
        }
    }

    // selecting lab id
    public void SelectLabId(Spinner spinner) {
        spinner.setOnItemSelectedListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> obj, View view, int position, long id) {
                labId = loadLabsModels.get(position).getLabId();
                labName = loadLabsModels.get(position).getLabName();
                LoadTests(labId, labTest, getContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Loading test Api
    public void LoadTests(String lab_id, final Spinner le_salesperson, final Context context) {
        Main_Apps.hud.show();
        API api = RetrofitAdapter.createAPI();
        Call<LoadTestResponse> loadTestResponseCall = api.LOAD_TEST_RESPONSE_CALL(lab_id);
        loadTestResponseCall.enqueue(new Callback<LoadTestResponse>() {
            @Override
            public void onResponse(Call<LoadTestResponse> call, Response<LoadTestResponse> response) {
                Main_Apps.hud.dismiss();
                if (response != null) {
                    if (response.body().getStatus()) {
                        loadTestModels = response.body().getTests();
                        setTests(response.body().getTests(), le_salesperson, context);
                    } else {
                        Log.e("Leads", "sales team spinner" + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoadTestResponse> call, Throwable t) {
                Main_Apps.hud.dismiss();

                Log.e("Leads", "sales team spinner on failure" + t.getMessage());
            }
        });
    }

    static private void setTests(List<LoadTestModel> loadTestModels, Spinner le_salesteam, Context context) {
        List<String> listSpinner = new ArrayList<String>();
        listSpinner.clear();
        //listSpinner.add("Select Test");
        for (int i = 0; i < loadTestModels.size(); i++) {
            listSpinner.add(loadTestModels.get(i).getTestName());
            ArrayAdapter<String> csAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listSpinner);
            csAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            le_salesteam.setAdapter(csAdapter);
        }
    }

    // selecting test id
    public void SelectTestId(Spinner spinner) {
        spinner.setOnItemSelectedListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> obj, View view, int position, long id) {
                TestId = loadTestModels.get(position).getTestId();
                labTest_price.setText(loadTestModels.get(position).getTestPrice());
                testPrice = loadTestModels.get(position).getTestPrice();
                testName = loadTestModels.get(position).getTestName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
//                        date = dateEditText.getText().toString();
//                        LoadAttendance();

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