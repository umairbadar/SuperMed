package managment.protege.supermed.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.R;
import managment.protege.supermed.Tools.GlobalHelper;

public class Fragment_Place_Order extends Fragment {

    private EditText et_address, et_contact, et_district, et_company, et_email, et_order_note, et_fname, et_lname, et_postal_code;
    private TextView tv_total_price;
    private Spinner spn_payment_method, spn_city;
    private Button btn_checkout;
    private String total, city = null, guest_email = "", payment_method;
    private List<String> arr_city;
    private List<String> arr_payment_method;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_order, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            total = getArguments().getString("price");
        }

        initWidgets(view);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), "PLACE ORDER", view);


        et_address.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getAddress());
        et_contact.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getContact());
        et_email.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getEmail());
        et_fname.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getFirstName());
        et_lname.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getLastName());
        tv_total_price.setText("Total Price: " + total);
    }

    private void initWidgets(View view) {

        if (GlobalHelper.getUserProfile(getContext()).getProfile().getIsGuest().equals("Yes")) {
            guest_email = GlobalHelper.getUserProfile(getContext()).getProfile().getEmail();
        }

        et_fname = view.findViewById(R.id.et_fname);
        et_lname = view.findViewById(R.id.et_lname);
        et_postal_code = view.findViewById(R.id.et_postal_code);
        et_address = view.findViewById(R.id.et_address);
        et_contact = view.findViewById(R.id.et_contact);
        et_district = view.findViewById(R.id.et_district);
        et_company = view.findViewById(R.id.et_company);
        et_email = view.findViewById(R.id.et_email);
        et_order_note = view.findViewById(R.id.et_order_note);

        tv_total_price = view.findViewById(R.id.tv_total_price);

        spn_city = view.findViewById(R.id.spn_city);
        arr_city = new ArrayList<>();
        getCities();
        city = GlobalHelper.getUserProfile(getContext()).getProfile().getCity();

        spn_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spn_city.getSelectedItemPosition() != 0) {
                    city = arr_city.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spn_payment_method = view.findViewById(R.id.spn_payment_method);
        arr_payment_method = new ArrayList<>();
        arr_payment_method.add("Select Payment Method");
        arr_payment_method.add("KEENU");
        arr_payment_method.add("Cash On Delivery");
        spn_payment_method.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, arr_payment_method));

        spn_payment_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spn_payment_method.getSelectedItemPosition() != 0) {
                    payment_method = arr_payment_method.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_checkout = view.findViewById(R.id.btn_checkout);

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validation();
            }
        });
    }

    private void getCities() {

        String URL = Register.Base_URL + "city-list";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            arr_city.add("Select City");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                arr_city.add(jsonObject1.getString("name"));
                            }
                            spn_city.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_city));
                            if (GlobalHelper.getUserProfile(getContext()).getProfile().getIsGuest().equals("No")) {
                                if (!city.equals(null)) {
                                    spn_city.setSelection(Integer.parseInt(city));
                                }
                            }
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

    private void Validation() {

        if (TextUtils.isEmpty(et_fname.getText().toString())) {
            et_fname.setError("Please enter first name");
            et_fname.requestFocus();
        }else if (TextUtils.isEmpty(et_lname.getText().toString())) {
            et_lname.setError("Please enter last name");
            et_lname.requestFocus();
        } else if (TextUtils.isEmpty(et_address.getText().toString())) {
            et_address.setError("Please enter address");
            et_address.requestFocus();
        } else if (TextUtils.isEmpty(et_contact.getText().toString())) {
            et_contact.setError("Please enter contact");
            et_contact.requestFocus();
        } else if (TextUtils.isEmpty(et_district.getText().toString())) {
            et_district.setError("Please enter district");
            et_district.requestFocus();
        } else if (TextUtils.isEmpty(et_postal_code.getText().toString())) {
            et_postal_code.setError("Please enter postal code");
            et_postal_code.requestFocus();
        } else if (TextUtils.isEmpty(et_company.getText().toString())) {
            et_company.setError("Please enter company");
            et_company.requestFocus();
        } else if (TextUtils.isEmpty(et_email.getText().toString())) {
            et_email.setError("Please enter email");
            et_email.requestFocus();
        } else if (TextUtils.isEmpty(et_order_note.getText().toString())) {
            et_order_note.setError("Please enter order note");
            et_order_note.requestFocus();
        } else if (spn_payment_method.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Please Select Payment Method",
                    Toast.LENGTH_LONG).show();
        } else if (spn_city.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Please Select City",
                    Toast.LENGTH_LONG).show();
        } else {
            sendPostReq();
        }
    }

    private void sendPostReq() {

        Main_Apps.hud.show();
        String URL = Register.Base_URL + "place-order-now";
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
                                Main_Apps.getMainActivity().backfunction(new Home());

                                Toast.makeText(getContext(), msg,
                                        Toast.LENGTH_LONG).show();
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
                map.put("userId", GlobalHelper.getUserProfile(getContext()).getProfile().getId());
                map.put("first_name", et_fname.getText().toString());
                map.put("last_name", et_lname.getText().toString());
                map.put("zipcode", et_postal_code.getText().toString());
                map.put("payment_method", payment_method);
                map.put("address", et_address.getText().toString());
                map.put("contact", et_contact.getText().toString());
                map.put("order_note", et_order_note.getText().toString());
                map.put("order_total_price", tv_total_price.getText().toString());
                map.put("district", et_district.getText().toString());
                map.put("company", et_company.getText().toString());
                map.put("city", city);
                map.put("email", et_email.getText().toString());
                map.put("isGuest", GlobalHelper.getUserProfile(getContext()).getProfile().getIsGuest());
                map.put("guest_email", guest_email);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
