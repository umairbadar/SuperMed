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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Adapter.AppointmentAdapter;
import managment.protege.supermed.Adapter.OrderHistoryAdapter;
import managment.protege.supermed.Model.AppoinmentModel;
import managment.protege.supermed.Model.AppointmentHistoryModel;
import managment.protege.supermed.Model.OrderHistoryList;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.AppointmentHistoryResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Appointment_Fragment extends Fragment {

    private List<AppointmentHistoryModel> movieList = new ArrayList<>();
    private AppointmentAdapter mAdapter;
    private RecyclerView recyclerView;
    private String patient_id;
    TextView toolbar_text;

    public Appointment_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_appointment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.appointment_history), v);
        initializations(v);
        LoadData();
        return v;

    }

    private void initializations(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);
    }

    private void LoadData() {
        mAdapter = new AppointmentAdapter(getAppointmentHistory(GlobalHelper.getUserProfile(getContext()).getProfile().getPatientId()), getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private List<AppointmentHistoryModel> getAppointmentHistory(String patient_id) {
        API api = RetrofitAdapter.createAPI();
        Main_Apps.hud.show();
        Call<AppointmentHistoryResponse> appointmentHistoryResponseCall = api.APPOINTMENT_HISTORY_RESPONSE_CALL(patient_id);


        appointmentHistoryResponseCall.enqueue(new Callback<AppointmentHistoryResponse>() {
            @Override
            public void onResponse(Call<AppointmentHistoryResponse> call, Response<AppointmentHistoryResponse> response) {
                movieList.clear();
                Main_Apps.hud.dismiss();

                if (response != null) {
                    if (response.body().getStatus()) {
                        Log.e("date", "is " + response.body().getStatus());

                        movieList = response.body().getHistory();
                        if (movieList == null) {
                            movieList.clear();
                            mAdapter.notifyDataSetChanged();
                        } else {
                            if (movieList.get(0).getAppointmentId() == null) {
                                movieList.clear();
                                mAdapter.notifyDataSetChanged();
                            } else {
                                setAppointmentHistory(response.body().getHistory());
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        movieList.clear();
                        mAdapter.notifyDataSetChanged();
                        Log.e("Toast", "show" + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<AppointmentHistoryResponse> call, Throwable t) {
                Main_Apps.hud.dismiss();
            }
        });
        return movieList;
    }

    private void setAppointmentHistory(List<AppointmentHistoryModel> movieList) {

        mAdapter = new AppointmentAdapter(movieList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}