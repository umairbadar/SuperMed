package managment.protege.supermed.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Adapter.HelpCenterAdapter;
import managment.protege.supermed.Adapter.HelpCenterSearchAdapter;
import managment.protege.supermed.Model.HelpCenterModel;
import managment.protege.supermed.Model.HelpCenterSearchModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.HelpCenterResponse;
import managment.protege.supermed.Response.HelpCenterSearchResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static managment.protege.supermed.Activity.Main_Apps.getMainActivity;
import static managment.protege.supermed.Activity.Main_Apps.search_toolbarapps;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpCenterFragment extends Fragment {

    private List<HelpCenterModel> helpList = new ArrayList<>();
    private static List<HelpCenterSearchModel> helpListSearch = new ArrayList<>();
    private HelpCenterAdapter cAdapter;
    private static HelpCenterSearchAdapter SearchcAdapter;
    View view;
    public String search;
    static RecyclerView recyclerViewCata;

    public HelpCenterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_help_center, container, false);


        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbar(getContext(), " HELP CENTER", view);
        search_toolbarapps = (ImageView) view.findViewById(R.id.search_toolbarapps);
        search_toolbarapps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main_Apps.getMainActivity().SearchDialog(getContext());
            }
        });

        initWidget();
        return view;
    }

    private void initWidget() {

        recyclerViewCata = view.findViewById(R.id.recycler);
        cAdapter = new HelpCenterAdapter(helpList);
        LoadHelpCenterData();
    }

    private void LoadHelpCenterData() {
        cAdapter = new HelpCenterAdapter(getHelpCenterData());
        recyclerViewCata.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewCata.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCata.setAdapter(cAdapter);

    }

    private List<HelpCenterModel> getHelpCenterData() {
        Main_Apps.hud.show();

        API api = RetrofitAdapter.createAPI();
        Call<HelpCenterResponse> helpCenterResponseCall = api.getHelpCenter();


        helpCenterResponseCall.enqueue(new Callback<HelpCenterResponse>() {
            @Override
            public void onResponse(Call<HelpCenterResponse> call, Response<HelpCenterResponse> response) {
                helpList.clear();
                Main_Apps.hud.dismiss();

                if (response != null) {
                    if (response.body().getStatus()) {
                        Log.e("date", "is " + response.body().getStatus());

                        helpList = response.body().getTopics();
                        if (helpList == null) {
                            helpList.clear();
                            cAdapter.notifyDataSetChanged();

                        } else {


                            if (helpList.get(0).getTopic() == null) {
                                helpList.clear();
                                cAdapter.notifyDataSetChanged();
                            } else {
                                setHelpCenterData(response.body().getTopics());
                                cAdapter.notifyDataSetChanged();
                            }

                        }
                    } else {
                        helpList.clear();
                        cAdapter.notifyDataSetChanged();
                        Log.e("Toast", "show" + response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<HelpCenterResponse> call, Throwable t) {
                Main_Apps.hud.dismiss();

            }
        });
        return helpList;

    }

    private void setHelpCenterData(List<HelpCenterModel> emergencyModels) {
        cAdapter = new HelpCenterAdapter(emergencyModels);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewCata.setLayoutManager(mLayoutManager);
        recyclerViewCata.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCata.setAdapter(cAdapter);
    }

    //search function
    public static List<HelpCenterSearchModel> getSearchHelp(String search) {
        API api = RetrofitAdapter.createAPI();
        Call<HelpCenterSearchResponse> helpCenterSearchResponseCall = api.HELP_CENTER_SEARCH_RESPONSE_CALL(search);


        helpCenterSearchResponseCall.enqueue(new Callback<HelpCenterSearchResponse>() {
            @Override
            public void onResponse(Call<HelpCenterSearchResponse> call, Response<HelpCenterSearchResponse> response) {
                helpListSearch.clear();
                if (response != null) {
                    if (response.body().getStatus()) {
                        Log.e("date", "is " + response.body().getStatus());

                        helpListSearch = response.body().getTopics();
                        if (helpListSearch == null) {
                            helpListSearch.clear();
                            SearchcAdapter.notifyDataSetChanged();

                        } else {


                            if (helpListSearch.get(0).getTopic() == null) {
                                helpListSearch.clear();
                                SearchcAdapter.notifyDataSetChanged();
                            } else {
                                setHelpCenterSearchData(response.body().getTopics());
                                SearchcAdapter.notifyDataSetChanged();
                            }

                        }
                    } else {
                        helpListSearch.clear();
                        SearchcAdapter.notifyDataSetChanged();
                        Log.e("Toast", "show" + response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<HelpCenterSearchResponse> call, Throwable t) {
//                Toast.makeText(getMainActivity(), "Error"+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        return helpListSearch;

    }

    public static void setHelpCenterSearchData(List<HelpCenterSearchModel> emergencyModels) {
        SearchcAdapter = new HelpCenterSearchAdapter(emergencyModels);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getMainActivity());
        recyclerViewCata.setLayoutManager(mLayoutManager);
        recyclerViewCata.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCata.setAdapter(SearchcAdapter);
    }

    public static void LoadSearchHelpCenterData(String search) {
        SearchcAdapter = new HelpCenterSearchAdapter(getSearchHelp(search));
        recyclerViewCata.setLayoutManager(new LinearLayoutManager(getMainActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewCata.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCata.setAdapter(SearchcAdapter);

    }


}
