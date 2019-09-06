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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.Adapter.ExpandableListAdapter1;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.ContactUsResponse;
import managment.protege.supermed.Response.GetAllProductsResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsFragment extends Fragment {

    View view;
    TextView textView;
    TextView toolbar_text;
    private EditText et_name, et_email, et_contact, et_inquiry;
    private Button btn_submit;
    public static KProgressHUD hud;

    ExpandableListAdapter1 listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private int lastPosition = -1;

    //Spinner
    private Spinner spn_subject;
    private ArrayList<String> arr_subject;

    public ContactUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.contact_us), view);

        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false);

        toolbar_text = (TextView) view.findViewById(R.id.toolbar_text);
        textView = (TextView) view.findViewById(R.id.text);

        //LoadApi();

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

        spn_subject = view.findViewById(R.id.spn_subject);
        arr_subject = new ArrayList<>();
        arr_subject.add("Please select your topic");
        arr_subject.add("Capture the information you need");
        arr_subject.add("Add or remove any fields");
        arr_subject.add("Your own custom criteria");
        arr_subject.add("Make any field required or not");
        spn_subject.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, arr_subject));

        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter1(getContext(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.expandGroup(1);

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastPosition != -1
                        && groupPosition != lastPosition) {
                    expListView.collapseGroup(lastPosition);
                }
                lastPosition = groupPosition;
            }
        });

        return view;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("DHA Branch:");
        listDataHeader.add("Bahadurabad Branch:");

        // Adding child data
        List<String> dha_branch = new ArrayList<String>();
        dha_branch.add("Ground floor, Plot # 6-C, Main Khayaban-e-Shamsheer, Stadium Commercial Area, Phase V, DHA, Karachi, Pakistan.");
        dha_branch.add("+92 3111 123911");
        dha_branch.add("+92 310 2911911");
        dha_branch.add("info@supermed.pk");
        dha_branch.add("11:00 AM to 12:00 AM");

        List<String> bahadurabad_branch = new ArrayList<String>();
        bahadurabad_branch.add("8 Adam Arcade, Shaheed-e-Millat Road, Karachi-74800, Pakistan.");
        bahadurabad_branch.add("+92 3111 123911");
        bahadurabad_branch.add("+92 310 2911911");
        bahadurabad_branch.add("info@supermed.pk");
        bahadurabad_branch.add("Coming Soon");

        listDataChild.put(listDataHeader.get(0), dha_branch); // Header, Child data
        listDataChild.put(listDataHeader.get(1), bahadurabad_branch);
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
