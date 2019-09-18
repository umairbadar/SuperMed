package managment.protege.supermed.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.util.List;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.RecyclerScrollChangeListener;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.Adapter.Adapter_Tests;
import managment.protege.supermed.Model.Model_Tests;
import managment.protege.supermed.R;

public class Fragment_Tests extends Fragment {

    private String labID;

    private RecyclerView recyclerViewTests;
    private Adapter_Tests testsAdapter;
    private List<Model_Tests> testsList;

    private int currentPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tests, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), "Tests", v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null){
            labID = getArguments().getString("LabID");
        }

        recyclerViewTests = view.findViewById(R.id.recyclerViewTests);
        recyclerViewTests.setLayoutManager(new GridLayoutManager(getContext(), 1));
        testsList = new ArrayList<>();
        getTests(currentPage);

        recyclerViewTests.addOnScrollListener(new RecyclerScrollChangeListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                currentPage = page;
                getTests(currentPage);
            }
        });
    }

    private void getTests(int currentPage){
        String URL = Register.Base_URL + "test-list/" + labID + "/" + currentPage;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            String message = jsonObject.getString("message");
                            if (status){
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    String price = object.getString("price");

                                    Model_Tests item = new Model_Tests(
                                            id,
                                            name,
                                            price
                                    );

                                    testsList.add(item);
                                }
                                testsAdapter = new Adapter_Tests(testsList, getContext());
                                recyclerViewTests.setAdapter(testsAdapter);
                            } else {
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
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
