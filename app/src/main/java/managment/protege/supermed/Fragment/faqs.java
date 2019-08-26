package managment.protege.supermed.Fragment;


import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Adapter.HelpCenterAdapter;
import managment.protege.supermed.Model.HelpCenterModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.FaqsResponse;
import managment.protege.supermed.Response.ForgotPassword;
import managment.protege.supermed.Response.HelpCenterResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class faqs extends Fragment {
    public managment.protege.supermed.Adapter.ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    TextView toolbar_text;

    public faqs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_faqs, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.faq), v);
        getHelpCenterData(HelpCenterAdapter.data);

        initialization(v);
        return v;
    }

    private void initialization(View v) {

        toolbar_text = (TextView) v.findViewById(R.id.toolbar_text);
        expListView = (ExpandableListView) v.findViewById(R.id.lvExp);

        expListView.setChildDivider(getResources().getDrawable(R.color.white));
        expListView.setDivider(getResources().getDrawable(R.color.divider_green));
        expListView.setDividerHeight(2);
        prepareListData();


        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.e("Width", "" + width);
        Log.e("height", "" + height);
        expListView.setIndicatorBounds(width - GetDipsFromPixel(35), width - GetDipsFromPixel(5));
    }

    public int GetDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        expListView.onWindowFocusChanged(true);

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
    }

    private void getHelpCenterData(String id) {
        Main_Apps.hud.show();

        API api = RetrofitAdapter.createAPI();
        Call<FaqsResponse> helpCenterResponseCall = api.getHelpCenterByTopic(id);


        helpCenterResponseCall.enqueue(new Callback<FaqsResponse>() {
            @Override
            public void onResponse(Call<FaqsResponse> call, Response<FaqsResponse> response) {
                Main_Apps.hud.dismiss();

                if (response != null) {
                    List<String> top250 = new ArrayList<String>();
                    int size = response.body() != null ? (response.body().getTopics() != null ? response.body().getTopics().size() : 0) : 0;
                    for (int i = 0; i < size; i++) {
                        if (response.body() != null) {
                            if (response.body().getTopics() != null) {
                                if (response.body().getTopics().get(i) != null) {
                                    if (response.body().getTopics().get(i).getQuestion() != null) {
                                        if (response.body().getTopics().get(i).getAnswer() != null) {
                                            listDataHeader.add(response.body().getTopics().get(i).getQuestion());

                                            String htmlExtracted = String.valueOf(Html.fromHtml(response.body().getTopics().get(i).getAnswer()));
                                            top250.add(htmlExtracted);

                                            listDataChild.put(listDataHeader.get(i), top250); // Header, Child data
                                        }
                                    }
                                }

                            }
                        }
                    }

                    listAdapter = new managment.protege.supermed.Adapter.ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
                    expListView.setAdapter(listAdapter);

                }
            }

            @Override
            public void onFailure(Call<FaqsResponse> call, Throwable t) {
                Main_Apps.hud.dismiss();
            }
        });

    }


}
