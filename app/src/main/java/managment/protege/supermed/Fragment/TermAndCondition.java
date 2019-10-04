package managment.protege.supermed.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermAndCondition extends Fragment {
    View view;
    TextView textView;
    TextView toolbar_text;
    TextView addressOne;
    TextView addressTwo;
    TextView mobile;
    TextView whatsap;
    TextView email;

    public TermAndCondition() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_term_and_condition, container, false);

       /* toolbar_text = (TextView) view.findViewById(R.id.toolbar_text);
        textView = (TextView) view.findViewById(R.id.text);
        addressOne = view.findViewById(R.id.addressOne);
        addressTwo = view.findViewById(R.id.addressTwo);
        mobile = view.findViewById(R.id.mobileno);
        whatsap = view.findViewById(R.id.whatsap);
        email = view.findViewById(R.id.email);

        view.findViewById(R.id.addressHeading).setVisibility(View.GONE);
        view.findViewById(R.id.secondaryHeading).setVisibility(View.GONE);
        view.findViewById(R.id.mobHeading).setVisibility(View.GONE);
        view.findViewById(R.id.whatsapHeading).setVisibility(View.GONE);
        view.findViewById(R.id.emailHeading).setVisibility(View.GONE);*/


        /*addressOne.setVisibility(View.GONE);
        addressTwo.setVisibility(View.GONE);
        mobile.setVisibility(View.GONE);
        whatsap.setVisibility(View.GONE);
        email.setVisibility(View.GONE);*/

        //LoadApi();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.terms_conditions), view);
    }

    /*public void LoadApi() {
        API api = RetrofitAdapter.createAPI();
        Main_Apps.hud.show();

        Call<ContactUsResponse> callBackCall = api.getTermStatus();
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
                Log.e("Login", "Error is " + t.getMessage());
                Main_Apps.hud.dismiss();

            }
        });

    }*/

}
