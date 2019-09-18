package managment.protege.supermed.Fragment;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.R;
import managment.protege.supermed.Tools.GlobalHelper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.facebook.FacebookSdk.getApplicationContext;
import static managment.protege.supermed.Activity.Main_Apps.hud;

public class Edit_Profile extends Fragment {

    private Spinner spn_city;
    private ArrayList<String> arr_city;
    private DatePickerDialog datePickerDialog;
    private String imagePath = "";
    RequestBody typedFile;
    EditText firstname, lname, mobileno, address;
    TextView email;
    String city;
    TextView date;
    MultipartBody.Part body;
    RequestBody name;
    File file;
    Button savechanges;
    String st_fname, st_lname, st_email, st_mobile, st_address, st_country, st_city, st_date, countryId;
    View v;
    CircleImageView CircularImage;
    Button uploadPhoto;
    TextView toolbar_text;

    public Edit_Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_edit__profile, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.edit_profile), v);

        //Initializing widgets
        initailize();

        //Getting data from local storage
        loadData();

        //Getting cities from api
        arr_city = new ArrayList<>();
        getCities();

       /* Toast.makeText(getApplicationContext(), city,
                Toast.LENGTH_LONG).show();*/

        /*onClickFunction();
        setonclick();*/

        setonclick();
        return v;
    }

    private void initailize() {

        toolbar_text = (TextView) v.findViewById(R.id.toolbar_text);
        CircularImage = (CircleImageView) v.findViewById(R.id.cir);
        uploadPhoto = (Button) v.findViewById(R.id.login_btn);
        spn_city = (Spinner) v.findViewById(R.id.editProfile_City);
        firstname = (EditText) v.findViewById(R.id.firstname);
        lname = (EditText) v.findViewById(R.id.lname);
        email = (TextView) v.findViewById(R.id.email);
        mobileno = (EditText) v.findViewById(R.id.mobileno);
        address = (EditText) v.findViewById(R.id.Address);
        date = (TextView) v.findViewById(R.id.date);
        savechanges = (Button) v.findViewById(R.id.savechanges);

        String imageUrl = GlobalHelper.getUserProfile(getContext()).getProfile().getImage();
        Picasso.get()
                .load(imageUrl)
                .resize(80, 80)
                .centerCrop()
                .placeholder(R.drawable.edit_profile_icon)
                .into(CircularImage);

    }

    private void loadData() {

        address.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getAddress());
        firstname.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getFirstName());
        lname.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getLastName());
        email.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getEmail());
        mobileno.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getContact());
        date.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getDob());
        city = GlobalHelper.getUserProfile(getContext()).getProfile().getCity();
    }

    private void getCities() {

        hud.show();
        String URL = Register.Base_URL + "city-list";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            hud.dismiss();
                            arr_city.add("Select City");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                arr_city.add(jsonObject1.getString("name"));
                            }
                            spn_city.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_city));
                            if (!city.equals("")){
                                spn_city.setSelection(Integer.parseInt(city));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hud.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
    }

    private void setonclick() {

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                CircularImage.setImageBitmap(r.getBitmap());
                                imagePath = r.getPath();
                                file = new File(r.getPath());

                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                                //TODO: do what you have to if user clicked cancel
                            }
                        }).show(getActivity().getSupportFragmentManager());
            }
        });

        //save changes
        savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //inputDate();
                /*UpdateProfile(GlobalHelper.getUserProfile(getContext()).getProfile().getId(), st_fname, st_lname, st_mobile, st_date,
                        st_address, st_country, st_city, file);*/

                updateProfile(GlobalHelper.getUserProfile(getContext()).getProfile().getId());
            }
        });

        //date picker m

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current dateEditText , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current date
                // dateEditText picker dialog
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set date of month , month and year value in the edit text
                                date.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                st_date = date.getText().toString();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    private void updateProfile(String userid){

        hud.show();
        String URL = Register.Base_URL + "user-update-profile/" + userid;
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            String message = jsonObject.getString("message");
                            if (status){
                                hud.dismiss();
                                Toast.makeText(getContext(), message,
                                        Toast.LENGTH_LONG).show();
                            } else {
                                hud.dismiss();
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
                        hud.dismiss();
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("first_name", firstname.getText().toString());
                map.put("last_name", lname.getText().toString());
                map.put("contact", mobileno.getText().toString());
                map.put("dob", date.getText().toString());
                map.put("city", String.valueOf(spn_city.getSelectedItemPosition()));
                map.put("email", email.getText().toString());
                map.put("image", imagePath);
                map.put("address", address.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);

    }

    /*@Override
    public void onPickResult(PickResult pickResult) {
        if (pickResult.getError() == null) {
            CircularImage.setImageBitmap(pickResult.getBitmap());
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
        }
    }*/

    /*private void inputDate() {
        st_date = date.getText().toString();
        Log.e("date", st_date);
        st_fname = firstname.getText().toString();
        Log.e("first name", st_fname);
        st_lname = lname.getText().toString();
        Log.e("st_lname", st_lname);
        st_email = email.getText().toString();
        st_mobile = mobileno.getText().toString();
        st_address = address.getText().toString();

    }

    private void onClickFunction() {
        LoadCountries(getContext(), sp_country);
        SelectLabId(sp_country, sp_city);
        SelectCityId(sp_city);
    }

    }

    public void UpdateProfile(final String userid, final String first_name, final String last_name, String contact, String dob, String address, String country, String city, File file) {
        Main_Apps.hud.show();
        if (file != null) {
            file = new File(file.getPath());
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            Log.e("FILE ", file.getPath());
            Log.e("filename", file.getName());
            Log.e("request body:", String.valueOf(reqFile));
            body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
            Log.e("body", String.valueOf(body));

        } else {
        }

        API api = RetrofitAdapter.createAPI();
        Call<LoginResponse> editProfileResponseCall = api.EDIT_PROFILE_RESPONSE_CALL(userid, first_name, last_name, contact, dob, address, country, city, body);
        editProfileResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Main_Apps.hud.dismiss();

                if (response != null) {
                    if (response.body().getStatus()) {

                        GlobalHelper.saveUserInLocal(getApplicationContext(), response.body(), true);
                        ((Main_Apps) getActivity()).NavigationFunction();
                        Main_Apps.getMainActivity().backfunction2(new Home());

                    } else {
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Main_Apps.hud.dismiss();
                Log.e("Coupon", "error" + t.getMessage());
            }
        });
    }*/
}
