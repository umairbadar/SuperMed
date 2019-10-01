package managment.protege.supermed.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.R;

public class DoctorDetailFragment extends DialogFragment {

    private TextView tv_dctr_name, tv_hsptl_name, tv_qualification, tv_desc, tv_time;
    private Button btn_close;
    private String Id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog, container, false);

        tv_dctr_name = view.findViewById(R.id.tv_dctr_name);
        tv_hsptl_name = view.findViewById(R.id.tv_hsptl_name);
        tv_qualification = view.findViewById(R.id.tv_qualification);
        tv_desc = view.findViewById(R.id.tv_desc);
        tv_time = view.findViewById(R.id.tv_time);
        btn_close = view.findViewById(R.id.btn_close);

        if (getArguments() != null){
            Id = getArguments().getString("Id");
        }

        getDoctorInfo(Id);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    public void getDoctorInfo(final String doctorId){
        String URL = Register.Base_URL + "doctor-single-info";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject object = jsonObject.getJSONObject("data");
                            tv_dctr_name.setText(object.getString("d_name"));
                            tv_hsptl_name.setText(object.getString("h_name"));
                            tv_qualification.setText(object.getString("d_qualification"));
                            tv_desc.setText(object.getString("d_description"));
                            tv_time.setText(object.getString("d_daysAndTime"));
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
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("doctorId", doctorId);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
