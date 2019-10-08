package managment.protege.supermed.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.Adapter.Adapter_Category;
import managment.protege.supermed.Adapter.AppointmentAdapter;
import managment.protege.supermed.Model.AppoinmentModel;
import managment.protege.supermed.Model.AppointmentHistoryModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.AppointmentHistoryResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import retrofit2.Call;
import retrofit2.Callback;

public class Appointment_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private List<AppoinmentModel> list;
    private AppointmentAdapter adapter;
    private TextView tv_error;

    public Appointment_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_appointment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.appointment_history), v);

        recyclerView = v.findViewById(R.id.recycler);
        tv_error = v.findViewById(R.id.tv_error);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        list = new ArrayList<>();
        adapter = new AppointmentAdapter(list, getContext());
        recyclerView.setAdapter(adapter);
        getAppointments(GlobalHelper.getUserProfile(getContext()).getProfile().getId(), "1");

        return v;

    }

    public void getAppointments(String userid, String limit){
        Main_Apps.hud.show();
        String URL = Register.Base_URL + "/user-appointment-list/" + userid + "/" + limit;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            if (status){
                                Main_Apps.hud.dismiss();
                                tv_error.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String appointment_id = object.getString("appointment_id");
                                    String patient_id = object.getString("patient_id");
                                    String day = object.getString("day");
                                    String date = object.getString("date");
                                    String time = object.getString("time");
                                    String lab = object.getString("lab_name");
                                    String status1 = object.getString("payment_status");
                                    String payment_method = object.getString("pay_type");
                                    String desc = object.getString("problem");
                                    String price = object.getString("price");

                                    AppoinmentModel item = new AppoinmentModel(
                                            appointment_id,
                                            day,
                                            date,
                                            time,
                                            patient_id,
                                            lab,
                                            status1,
                                            payment_method,
                                            desc,
                                            price
                                    );

                                    list.add(item);
                                }
                                adapter.notifyDataSetChanged();

                            } else {
                                Main_Apps.hud.dismiss();
                                tv_error.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
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
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);

    }
}