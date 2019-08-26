package managment.protege.supermed.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.ContactUsResponse;
import managment.protege.supermed.Response.GetAllProductsResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {

    View view;
    TextView textView;
    TextView toolbar_text;
    private EditText et_name, et_email, et_contact, et_inquiry;
    private Button btn_submit;
    public static KProgressHUD hud;

    public ContactUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.contact_us), view);

        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false);

        toolbar_text = (TextView) view.findViewById(R.id.toolbar_text);
        textView = (TextView) view.findViewById(R.id.text);

        LoadApi();

        et_name = view.findViewById(R.id.et_name);
        et_email = view.findViewById(R.id.et_email);
        et_contact = view.findViewById(R.id.et_contact);
        et_inquiry = view.findViewById(R.id.et_inquiry);
        btn_submit = view.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_name.getText().toString())) {
                    et_name.setError("Please Enter Name");
                    et_name.requestFocus();
                } else if (TextUtils.isEmpty(et_email.getText().toString())) {
                    et_email.setError("Please Enter Email");
                    et_email.requestFocus();
                } else if (TextUtils.isEmpty(et_contact.getText().toString())) {
                    et_contact.setError("Please Enter Contact Number");
                    et_contact.requestFocus();
                } else if (TextUtils.isEmpty(et_inquiry.getText().toString())) {
                    et_inquiry.setError("Please Enter Inquiry");
                    et_inquiry.requestFocus();
                } else {
                    submitInquiry();
                }
            }
        });

        return view;
    }


    public void LoadApi() {
        Main_Apps.hud.show();

        API api = RetrofitAdapter.createAPI();
        Call<ContactUsResponse> callBackCall = api.contactUs();
        callBackCall.enqueue(new Callback<ContactUsResponse>() {
            @Override
            public void onResponse(Call<ContactUsResponse> call, final Response<ContactUsResponse> response) {
                Main_Apps.hud.dismiss();

                if (response != null) {
                    if (response.body().getStatus()) {
                        textView.setText(Html.fromHtml(response.body().getContactUs().getPageContent()));
                    } else {
                    }
                }
            }

            @Override
            public void onFailure(Call<ContactUsResponse> call, Throwable t) {
                Main_Apps.hud.dismiss();

                Log.e("Login", "Error is " + t.getMessage());
            }
        });

    }

    private void submitInquiry() {

        hud.show();
        String URL = "https://www.supermed.pk/api/api/saveContactUs";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            hud.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean status = jsonObject.getBoolean("status");
                            if (status.equals(true)) {
                                startActivity(new Intent(getActivity(), Main_Apps.class));
                                Toast.makeText(getContext(), jsonObject.getString("message"),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), jsonObject.getString("message"),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
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
                map.put("name", et_name.getText().toString());
                map.put("email", et_email.getText().toString());
                map.put("contact", et_contact.getText().toString());
                map.put("inquiry", et_inquiry.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
