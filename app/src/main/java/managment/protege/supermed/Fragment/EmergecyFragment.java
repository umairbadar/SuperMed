package managment.protege.supermed.Fragment;


import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Adapter.EmergencyAdapter;
import managment.protege.supermed.Model.EmergencyModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.EmergencyResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmergecyFragment extends Fragment {

    View view;
    KProgressHUD hud;
    EmergencyAdapter emergencyAdapter;
    private List<EmergencyModel> list = new ArrayList<>();
    TextView toolbar_text;
    RecyclerView recyclerView;

    public EmergecyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_emergecy, container, false);
        initlize(view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.emergency_contacts), view);
        emergencyAdapter = new EmergencyAdapter(list, getContext());
        LoadEmergencyContacts();
        // Inflate the layout for this fragment
        return view;
    }

    private void initlize(View view) {
        toolbar_text = (TextView) view.findViewById(R.id.toolbar_text);
        recyclerView = (RecyclerView) view.findViewById(R.id.emergency_recycler);
    }

    private void LoadEmergencyContacts() {
        emergencyAdapter = new EmergencyAdapter(getEmergencyList(), getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(emergencyAdapter);
    }

    private List<EmergencyModel> getEmergencyList() {
        Main_Apps.hud.show();

        API api = RetrofitAdapter.createAPI();
        Call<EmergencyResponse> emergencyResponseCall = api.getEmergency();


        emergencyResponseCall.enqueue(new Callback<EmergencyResponse>() {
            @Override
            public void onResponse(Call<EmergencyResponse> call, Response<EmergencyResponse> response) {
                Main_Apps.hud.dismiss();

                list.clear();
                if (response != null) {
                    if (response.body().getStatus()) {
                        Log.e("date", "is " + response.body().getStatus());

                        list = response.body().getEmergency();
                        if (list == null) {
                            list.clear();
                            emergencyAdapter.notifyDataSetChanged();
                        } else {
                            if (list.get(0).getContact() == null) {
                                list.clear();
                                emergencyAdapter.notifyDataSetChanged();
                            } else {
                                setEmergencyContacts(response.body().getEmergency());
                                emergencyAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        list.clear();
                        emergencyAdapter.notifyDataSetChanged();
                        Log.e("Toast", "show" + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<EmergencyResponse> call, Throwable t) {
                Main_Apps.hud.dismiss();
            }
        });
        return list;
    }

    private void setEmergencyContacts(List<EmergencyModel> emergencyModels) {
        emergencyAdapter = new EmergencyAdapter(emergencyModels, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        emergencyAdapter.setOnItemClickListener(new EmergencyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, EmergencyModel obj) {
                Uri number = Uri.parse(obj.getContact());
                Log.e("Contact: ", obj.getContact());
                try {
                    String contact_number = String.valueOf(number);
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + contact_number));
                    startActivity(callIntent);
                } catch (Exception e) {
                }

            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(emergencyAdapter);
    }

}
