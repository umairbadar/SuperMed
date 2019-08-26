package managment.protege.supermed.Fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Model.CityListModel;
import managment.protege.supermed.Model.CountryListModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.CityListResponse;
import managment.protege.supermed.Response.CountryListResponse;
import managment.protege.supermed.Response.EditProfileResponse;
import managment.protege.supermed.Response.LoginResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

import static com.facebook.FacebookSdk.getApplicationContext;
import static managment.protege.supermed.Activity.Main_Apps.nav_email;
import static managment.protege.supermed.Activity.Main_Apps.nav_image;
import static managment.protege.supermed.Activity.Main_Apps.nav_username;
import static managment.protege.supermed.Activity.Main_Apps.navigationHeaderUsername;

/**
 * A simple {@link Fragment} subclass.
 */
public class Edit_Profile extends Fragment implements IPickResult, View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    Spinner sp_country, sp_city;
    RequestBody typedFile;
    List<CountryListModel> countryListModels;
    List<CityListModel> cityListModels;
    DatePickerDialog datePickerDialog;
    EditText firstname, lname, mobileno, address;
    TextView email;
    static String country, city;
    TextView date;
    MultipartBody.Part body;
    RequestBody name;
    File file;
    Button savechanges;
    String st_fname, st_lname, st_email, st_mobile, st_address, st_country, st_city, st_date, countryId;
    View v;
    // PickImageDialog.build(new PickSetup()).show(this);        AvatarImageView avatar;
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
        initailize();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.edit_profile), v);
        putDataOnLoad();
        String imageUrl = GlobalHelper.getUserProfile(getContext()).getProfile().getImage();
        Picasso.with(getContext()).load(imageUrl).placeholder(R.drawable.edit_profile_icon)
                .resize(80, 80)
                .into(CircularImage);
        onClickFunction();
        setonclick();
        return v;
    }

    private void putDataOnLoad() {

        address.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getAddress());
        firstname.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getFirstName());
        lname.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getLastName());
        email.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getEmail());
        mobileno.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getContact());
        date.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getDob());
        country = GlobalHelper.getUserProfile(getContext()).getProfile().getCountry();
        city = GlobalHelper.getUserProfile(getContext()).getProfile().getCity();

        Log.e("country is", country);
    }

    private void inputDate() {
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

    private void initailize() {
        toolbar_text = (TextView) v.findViewById(R.id.toolbar_text);
        CircularImage = (CircleImageView) v.findViewById(R.id.cir);
        uploadPhoto = (Button) v.findViewById(R.id.login_btn);
        sp_country = (Spinner) v.findViewById(R.id.editProfile_Country);
        sp_city = (Spinner) v.findViewById(R.id.editProfile_City);
        firstname = (EditText) v.findViewById(R.id.firstname);
        lname = (EditText) v.findViewById(R.id.lname);
        email = (TextView) v.findViewById(R.id.email);
        mobileno = (EditText) v.findViewById(R.id.mobileno);
        address = (EditText) v.findViewById(R.id.Address);
        date = (TextView) v.findViewById(R.id.date);
        savechanges = (Button) v.findViewById(R.id.savechanges);


    }

    private void setonclick() {
        CircularImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PickImageDialog.build(new PickSetup())
                                .setOnPickResult(new IPickResult() {
                                    @Override
                                    public void onPickResult(PickResult r) {
                                        CircularImage.setImageBitmap(r.getBitmap());
                                        String iv = r.getPath();
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
                }
        );

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                CircularImage.setImageBitmap(r.getBitmap());
                                String iv = r.getPath();
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
                inputDate();
                UpdateProfile(GlobalHelper.getUserProfile(getContext()).getProfile().getId(), st_fname, st_lname, st_mobile, st_date,
                        st_address, st_country, st_city, file);
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
    }


    //loading Countries api function
    public void LoadCountries(final Context context, final Spinner sp_country) {
        API api = RetrofitAdapter.createAPI();
        Call<CountryListResponse> countryListResponseCall = api.COUNTRY_LIST_RESPONSE_CALL();
        countryListResponseCall.enqueue(new Callback<CountryListResponse>() {
            @Override
            public void onResponse(Call<CountryListResponse> call, Response<CountryListResponse> response) {
                if (response != null) {
                    if (response.body().getStatus()) {
                        countryListModels = response.body().getCountries();
                        setCountries(response.body().getCountries(), sp_country, context);

                    } else {
                        Log.e("Leads", "sales team spinner" + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryListResponse> call, Throwable t) {
                Log.e("Leads", "sales team spinner on failure" + t.getMessage());
            }
        });
    }

    static private void setCountries(List<CountryListModel> loadLabsModels, Spinner le_salesteam, Context context) {
        List<String> listSpinner = new ArrayList<String>();
        listSpinner.clear();
        for (int i = 0; i < loadLabsModels.size(); i++) {
            if (loadLabsModels.get(i).getName().equals(country)) {
                loadLabsModels.add(0, loadLabsModels.get(i));
                loadLabsModels.remove(i + 1);
            }
        }
        for (int i = 0; i < loadLabsModels.size(); i++) {
            listSpinner.add(loadLabsModels.get(i).getName());
            ArrayAdapter<String> csAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listSpinner);
            csAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            le_salesteam.setAdapter(csAdapter);
        }
    }

    //selecting country id
    public void SelectLabId(final Spinner spinner, final Spinner spinner2) {
        spinner.setOnItemSelectedListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> obj, View view, int position, long id) {
                countryId = countryListModels.get(position).getId();
                st_country = countryListModels.get(position).getName();
                LoadCities(getContext(), countryId, spinner2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //set cities
    public void LoadCities(final Context context, String country_id, final Spinner sp_city) {
        API api = RetrofitAdapter.createAPI();
        Call<CityListResponse> cityListResponseCall = api.CITY_LIST_RESPONSE_CALL(country_id);
        cityListResponseCall.enqueue(new Callback<CityListResponse>() {
            @Override
            public void onResponse(Call<CityListResponse> call, Response<CityListResponse> response) {
                if (response != null) {
                    if (response.body().getStatus()) {
                        cityListModels = response.body().getCities();
                        setCities(response.body().getCities(), sp_city, context);

                    } else {
                        Log.e("city", " city spinner" + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CityListResponse> call, Throwable t) {
                Log.e("Leads", "sales team spinner on failure" + t.getMessage());
            }
        });
    }

    static private void setCities(List<CityListModel> cityListModels, Spinner le_salesteam, Context context) {
        List<String> listSpinner = new ArrayList<String>();
        listSpinner.clear();
        for (int i = 0; i < cityListModels.size(); i++) {
            if (cityListModels.get(i).getName().equals(city)) {
                cityListModels.add(0, cityListModels.get(i));
                cityListModels.remove(i + 1);
            }
        }
        for (int i = 0; i < cityListModels.size(); i++) {

            listSpinner.add(cityListModels.get(i).getName());
            ArrayAdapter<String> csAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listSpinner);
            csAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            le_salesteam.setAdapter(csAdapter);
        }
    }

    // selecting City id
    public void SelectCityId(Spinner spinner) {
        spinner.setOnItemSelectedListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> obj, View view, int position, long id) {
                st_city = cityListModels.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onPickResult(PickResult pickResult) {
        if (pickResult.getError() == null) {
            CircularImage.setImageBitmap(pickResult.getBitmap());
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
