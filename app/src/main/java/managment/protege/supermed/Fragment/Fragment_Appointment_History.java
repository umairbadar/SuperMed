package managment.protege.supermed.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import managment.protege.supermed.Adapter.Adapter_Appointment_History;
import managment.protege.supermed.Model.Model_Appointment_History;
import managment.protege.supermed.R;

public class Fragment_Appointment_History extends Fragment {

    private String appointment_id;
    private RecyclerView recyclerView;
    private List<Model_Appointment_History> list;
    private Adapter_Appointment_History adapter;
    public static TextView tv_total_price;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_appointment_history, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), "APPOINTMENT DETAIL", v);

        if (getArguments() != null){
            appointment_id = getArguments().getString("appointment_id");
        }

        recyclerView = v.findViewById(R.id.recycler);
        tv_total_price = v.findViewById(R.id.tv_total_price);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        list = new ArrayList<>();
        adapter = new Adapter_Appointment_History(list, getContext());
        recyclerView.setAdapter(adapter);
        getAppointmentDetails(appointment_id);

        return v;

    }

    private void getAppointmentDetails(String appointment_id){
        Main_Apps.hud.show();
        String URL = Register.Base_URL + "user-appointment-details/" + appointment_id;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Main_Apps.hud.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                String name = object.getString("first_name") + " " + object.getString("last_name");
                                String status = object.getString("payment_status");
                                String appointment_id = object.getString("appointment_id");
                                String dob = object.getString("dob");
                                String gender = object.getString("gender");
                                String datetime = object.getString("datetime");
                                String pay_type = object.getString("pay_type");
                                String patient_address = object.getString("patient_address");
                                String problem = object.getString("problem");
                                String price = object.getString("price");

                                Model_Appointment_History item = new Model_Appointment_History(
                                        name,
                                        status,
                                        appointment_id,
                                        dob,
                                        gender,
                                        datetime,
                                        pay_type,
                                        patient_address,
                                        problem,
                                        price
                                );

                                list.add(item);

                            }

                            adapter.notifyDataSetChanged();
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
