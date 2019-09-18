package managment.protege.supermed.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.Adapter.AdapterSpecialityDoctor;
import managment.protege.supermed.Adapter.CategoryAdapter;
import managment.protege.supermed.Adapter.DocInfoAdapter;
import managment.protege.supermed.Adapter.EmergencyAdapter;
import managment.protege.supermed.Adapter.ProductAdapter;
import managment.protege.supermed.Model.CategoryModel;
import managment.protege.supermed.Model.DocModel;
import managment.protege.supermed.Model.DoctorsModel;
import managment.protege.supermed.Model.GetProductsModel;
import managment.protege.supermed.Model.HospitalModel;
import managment.protege.supermed.Model.ModelSpecialityDoctor;
import managment.protege.supermed.Model.Specialization;
import managment.protege.supermed.Model.SpecializationModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.DocResponse;
import managment.protege.supermed.Response.DoctorDetailsREsponse;
import managment.protege.supermed.Response.GetAllProductsResponse;
import managment.protege.supermed.Response.GetDoctorServiceResponse;
import managment.protege.supermed.Response.GetHospitalServiceResponse;
import managment.protege.supermed.Response.GetSpecializationServiceResponse;
import managment.protege.supermed.Response.SpecilizationResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorInformation extends Fragment {

    private LinearLayout layout_hsptl, layout_spec, layout_hospitalbased, layout_specs;
    private LinearLayout layout1, layout2, layout3, layout4;
    private Spinner spn_type, spn_hospital, spn_speclization, spn_speclization1, spn_hospital1;
    private ArrayList<String> arr_type;
    private ArrayList<String> arr_specialites;
    private ArrayList<String> arr_specialites_slug;
    private ArrayList<String> arr_specialites_id;
    private ArrayList<String> arr_hospitals;
    private ArrayList<String> arr_hospitals_slug;
    private ArrayList<String> arr_hospitals_id;
    private ArrayList<String> arr_hospitals1;
    private ArrayList<String> arr_hospitals1_id;
    private ArrayList<String> arr_specialites1;
    private ArrayList<String> arr_specialites1_id;
    private String hospital_id, hospital1_id, specialization_id, specialization1_id;
    public static KProgressHUD hud;
    private TextView tv_error;

    //Speciality Doctor
    private RecyclerView doctor_list1;
    private RecyclerView.Adapter adapter;
    private List<ModelSpecialityDoctor> arr_list_speciality_doctor;

    //All Doctors
    private RecyclerView doctor_list4;
    private RecyclerView.Adapter adapter4;
    private List<ModelSpecialityDoctor> arr_list_all_doctors;

    //get All Doctors Hospital wise
    private RecyclerView doctor_list3;
    private RecyclerView.Adapter adapter3;
    private List<ModelSpecialityDoctor> arr_list_doctor_hospital_wise;

    //get All Doctors with Hospital and Speciality Wise
    private RecyclerView doctor_list2;
    private RecyclerView.Adapter adapter2;
    private List<ModelSpecialityDoctor> arr_list_doctor_hospital_and_speciality_wise;

    public DoctorInformation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doctor_information, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.doc_info), v);

        /*getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);*/

        //Initializations
        initViews(v);

        return v;
    }

    private void initViews(View view) {

        //Error TextView
        tv_error = view.findViewById(R.id.tv_error);

        //Progress Dialog
        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false);

        //Layouts
        layout_hsptl = view.findViewById(R.id.layout_hospital);
        layout_spec = view.findViewById(R.id.layout_speclization);
        layout_specs = view.findViewById(R.id.layout_specs);
        layout_hospitalbased = view.findViewById(R.id.layout_hospitalbased);

        layout1 = view.findViewById(R.id.layout1);
        layout2 = view.findViewById(R.id.layout2);
        layout3 = view.findViewById(R.id.layout3);
        layout4 = view.findViewById(R.id.layout4);

        doctor_list4 = view.findViewById(R.id.doctor_list4);
        doctor_list4.setLayoutManager(new GridLayoutManager(getContext(), 2));
        arr_list_all_doctors = new ArrayList<>();

        //Spinners
        spn_type = view.findViewById(R.id.spn_type);
        spn_hospital = view.findViewById(R.id.spn_hospital);
        spn_hospital1 = view.findViewById(R.id.spn_hospital1);
        spn_speclization = view.findViewById(R.id.spn_speclization);
        spn_speclization1 = view.findViewById(R.id.spn_speclization1);

        arr_type = new ArrayList<>();
        arr_type.add("Select Filter");
        arr_type.add("Speciality-wise");
        arr_type.add("Hospital Based Speciality-wise");
        arr_type.add("Hospital wise");
        arr_type.add("All Doctors");
        spn_type.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, arr_type));

        arr_specialites = new ArrayList<>();
        arr_specialites_slug = new ArrayList<>();
        arr_specialites_id = new ArrayList<>();

        arr_hospitals = new ArrayList<>();
        arr_hospitals_slug = new ArrayList<>();
        arr_hospitals_id = new ArrayList<>();

        arr_hospitals1 = new ArrayList<>();
        arr_hospitals1_id = new ArrayList<>();

        spn_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spn_type.getSelectedItemPosition() == 1) {
                    getAllSpecialites();
                    if (tv_error.getVisibility() == View.VISIBLE) {
                        tv_error.setVisibility(View.GONE);
                    }
                    layout1.setVisibility(View.VISIBLE);
                    layout2.setVisibility(View.GONE);
                    layout3.setVisibility(View.GONE);
                    layout4.setVisibility(View.GONE);
                    layout_hsptl.setVisibility(View.GONE);
                    layout_spec.setVisibility(View.VISIBLE);
                    layout_hospitalbased.setVisibility(View.GONE);
                } else if (spn_type.getSelectedItemPosition() == 2) {
                    getAllHospitals1();
                    if (tv_error.getVisibility() == View.VISIBLE) {
                        tv_error.setVisibility(View.GONE);
                    }
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    layout3.setVisibility(View.GONE);
                    layout4.setVisibility(View.GONE);
                    layout_hsptl.setVisibility(View.GONE);
                    layout_spec.setVisibility(View.GONE);
                    layout_hospitalbased.setVisibility(View.VISIBLE);
                } else if (spn_type.getSelectedItemPosition() == 3) {
                    getAllHospitals();
                    if (tv_error.getVisibility() == View.VISIBLE) {
                        tv_error.setVisibility(View.GONE);
                    }
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.GONE);
                    layout3.setVisibility(View.VISIBLE);
                    layout4.setVisibility(View.GONE);
                    layout_hsptl.setVisibility(View.VISIBLE);
                    layout_spec.setVisibility(View.GONE);
                    layout_hospitalbased.setVisibility(View.GONE);
                } else if (spn_type.getSelectedItemPosition() == 4) {
                    getAllDoctors();
                    if (tv_error.getVisibility() == View.VISIBLE) {
                        tv_error.setVisibility(View.GONE);
                    }
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.GONE);
                    layout3.setVisibility(View.GONE);
                    layout4.setVisibility(View.VISIBLE);
                    layout_hsptl.setVisibility(View.GONE);
                    layout_spec.setVisibility(View.GONE);
                    layout_hospitalbased.setVisibility(View.GONE);
                    arr_list_all_doctors.clear();
                } else if (spn_type.getSelectedItemPosition() == 0) {
                    if (tv_error.getVisibility() == View.VISIBLE) {
                        tv_error.setVisibility(View.GONE);
                    }
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.GONE);
                    layout3.setVisibility(View.GONE);
                    layout4.setVisibility(View.GONE);
                    layout_spec.setVisibility(View.GONE);
                    layout_hsptl.setVisibility(View.GONE);
                    layout_spec.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        doctor_list1 = view.findViewById(R.id.doctor_list1);
        doctor_list1.setLayoutManager(new GridLayoutManager(getContext(), 2));
        arr_list_speciality_doctor = new ArrayList<>();

        spn_speclization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spn_speclization.getSelectedItemPosition() == 0) {
                    tv_error.setVisibility(View.GONE);
                } else {
                    specialization_id = arr_specialites_id.get(i);
                    arr_list_speciality_doctor.clear();
                    getSpecialityDoctor();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        doctor_list3 = view.findViewById(R.id.doctor_list3);
        doctor_list3.setLayoutManager(new GridLayoutManager(getContext(), 2));
        arr_list_doctor_hospital_wise = new ArrayList<>();

        spn_hospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (spn_hospital.getSelectedItemPosition() == 0) {
                    tv_error.setVisibility(View.GONE);
                } else {
                    hospital_id = arr_hospitals_id.get(i);
                    arr_list_doctor_hospital_wise.clear();
                    getAllDoctorsHospitalWise();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        arr_specialites1 = new ArrayList<>();
        arr_specialites1_id = new ArrayList<>();

        doctor_list2 = view.findViewById(R.id.doctor_list2);
        doctor_list2.setLayoutManager(new GridLayoutManager(getContext(), 2));
        arr_list_doctor_hospital_and_speciality_wise = new ArrayList<>();

        spn_hospital1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spn_hospital1.getSelectedItemPosition() == 0) {
                    layout_specs.setVisibility(View.GONE);
                } else {
                    layout_specs.setVisibility(View.VISIBLE);
                    hospital1_id = arr_hospitals1_id.get(i);
                    arr_specialites1.clear();
                    arr_specialites1_id.clear();
                    getAllSpecialitesWithHospWise();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_speclization1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (spn_speclization1.getSelectedItemPosition() == 0) {
                    tv_error.setVisibility(View.GONE);
                } else {
                    specialization1_id = arr_specialites1_id.get(i);
                    arr_list_doctor_hospital_and_speciality_wise.clear();
                    getAllDoctorsWithHospitalAndSpecialityWise();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getAllSpecialites() {

        hud.show();
        arr_specialites.clear();
        arr_specialites_id.clear();
        arr_specialites_slug.clear();

        String URL = Register.Base_URL + "listing-for/specialization-list";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            hud.dismiss();
                            arr_specialites.add("Select Specialization");
                            arr_specialites_id.add("0");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject innerObj = jsonArray.getJSONObject(i);
                                arr_specialites.add(innerObj.getString("name"));
                                arr_specialites_id.add(innerObj.getString("id"));
                                arr_specialites_slug.add(innerObj.getString("slug"));
                            }
                            spn_speclization.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_specialites));
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
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    private void getAllHospitals() {

        hud.show();

        arr_hospitals.clear();
        arr_hospitals_id.clear();
        arr_hospitals_slug.clear();

        String URL = Register.Base_URL + "listing-for/hospital-list";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            hud.dismiss();
                            arr_hospitals.add("Select Hospital");
                            arr_hospitals_id.add("0");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject innerObj = jsonArray.getJSONObject(i);
                                arr_hospitals.add(innerObj.getString("name"));
                                arr_hospitals_id.add(innerObj.getString("id"));
                                arr_hospitals_slug.add(innerObj.getString("slug"));
                            }
                            spn_hospital.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_hospitals));
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
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    private void getAllHospitals1() {

        hud.show();

        arr_hospitals1.clear();
        arr_hospitals1_id.clear();

        String URL = Register.Base_URL + "listing-for/hospital-list";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            hud.dismiss();
                            arr_hospitals1.add("Select Hospital");
                            arr_hospitals1_id.add("0");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject innerObj = jsonArray.getJSONObject(i);
                                arr_hospitals1.add(innerObj.getString("name"));
                                arr_hospitals1_id.add(innerObj.getString("id"));
                            }
                            spn_hospital1.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_hospitals1));
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
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    private void getAllSpecialitesWithHospWise() {

        hud.show();

        arr_specialites1.clear();
        arr_specialites1_id.clear();

        String URL = Register.Base_URL + "specialities";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            hud.dismiss();
                            arr_specialites1.add("Select Specialization");
                            arr_specialites1_id.add("0");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject innerObj = jsonArray.getJSONObject(i);
                                arr_specialites1.add(innerObj.getString("s_name"));
                                arr_specialites1_id.add(innerObj.getString("s_id"));
                            }
                            spn_speclization1.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_specialites1));
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("hospitalId", hospital1_id);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    private void getSpecialityDoctor() {

        hud.show();
        String URL = Register.Base_URL + "doctor-info";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            hud.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean status = jsonObject.getBoolean("status");
                            if (status.equals(true)) {
                                tv_error.setVisibility(View.GONE);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject innerObj = jsonArray.getJSONObject(i);
                                    String doc_name = innerObj.getString("d_name");
                                    String doc_designation = innerObj.getString("d_qualification");
                                    String doc_time = innerObj.getString("d_daysAndTime");
                                    String gender = innerObj.getString("d_gender");
                                    ModelSpecialityDoctor item = new ModelSpecialityDoctor(
                                            doc_name,
                                            doc_designation,
                                            doc_time,
                                            gender
                                    );
                                    arr_list_speciality_doctor.add(item);
                                }
                                adapter = new AdapterSpecialityDoctor(arr_list_speciality_doctor, getContext());
                                doctor_list1.setAdapter(adapter);
                            } else {
                                tv_error.setVisibility(View.VISIBLE);
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("global_id", specialization_id);
                map.put("type", "speciality-wise");
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    private void getAllDoctors() {

        hud.show();
        String URL = Register.Base_URL + "listing-for/doctor-list";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            hud.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean status = jsonObject.getBoolean("status");
                            if (status.equals(true)) {
                                tv_error.setVisibility(View.GONE);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject innerObj = jsonArray.getJSONObject(i);
                                    String doc_name = innerObj.getString("name");
                                    String doc_designation = innerObj.getString("qualification");
                                    String doc_time = innerObj.getString("daysAndTime");
                                    String gender = innerObj.getString("gender");
                                    ModelSpecialityDoctor item = new ModelSpecialityDoctor(
                                            doc_name,
                                            doc_designation,
                                            doc_time,
                                            gender
                                    );
                                    arr_list_all_doctors.add(item);
                                }
                                adapter4 = new AdapterSpecialityDoctor(arr_list_all_doctors, getContext());
                                doctor_list4.setAdapter(adapter4);
                            } else {
                                tv_error.setVisibility(View.VISIBLE);
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
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    private void getAllDoctorsHospitalWise(){

        hud.show();
        String URL = Register.Base_URL + "doctor-info";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            hud.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean status = jsonObject.getBoolean("status");
                            if (status.equals(true)) {
                                tv_error.setVisibility(View.GONE);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject innerObj = jsonArray.getJSONObject(i);
                                    String doc_name = innerObj.getString("d_name");
                                    String doc_designation = innerObj.getString("d_qualification");
                                    String doc_time = innerObj.getString("d_daysAndTime");
                                    String gender = innerObj.getString("d_gender");
                                    ModelSpecialityDoctor item = new ModelSpecialityDoctor(
                                            doc_name,
                                            doc_designation,
                                            doc_time,
                                            gender
                                    );
                                    arr_list_doctor_hospital_wise.add(item);
                                }
                                adapter3 = new AdapterSpecialityDoctor(arr_list_doctor_hospital_wise, getContext());
                                doctor_list3.setAdapter(adapter3);
                            } else {
                                tv_error.setVisibility(View.VISIBLE);
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
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("global_id", hospital_id);
                map.put("type", "hospital-wise");
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    private void getAllDoctorsWithHospitalAndSpecialityWise() {

        hud.show();
        String URL = Register.Base_URL + "doctor-info";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            hud.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean status = jsonObject.getBoolean("status");
                            if (status.equals(true)) {
                                tv_error.setVisibility(View.GONE);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject innerObj = jsonArray.getJSONObject(i);
                                    String doc_name = innerObj.getString("d_name");
                                    String doc_designation = innerObj.getString("d_qualification");
                                    String doc_time = innerObj.getString("d_daysAndTime");
                                    String gender = innerObj.getString("d_gender");
                                    ModelSpecialityDoctor item = new ModelSpecialityDoctor(
                                            doc_name,
                                            doc_designation,
                                            doc_time,
                                            gender
                                    );
                                    arr_list_doctor_hospital_and_speciality_wise.add(item);
                                }
                                adapter2 = new AdapterSpecialityDoctor(arr_list_doctor_hospital_and_speciality_wise, getContext());
                                doctor_list2.setAdapter(adapter2);
                            } else {
                                tv_error.setVisibility(View.VISIBLE);
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("getHospitalId", hospital1_id);
                map.put("global_id", specialization1_id);
                map.put("type", "hospital-and-speciality-wise");
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}

