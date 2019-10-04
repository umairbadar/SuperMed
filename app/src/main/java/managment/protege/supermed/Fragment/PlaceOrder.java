package managment.protege.supermed.Fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.Activity.WebViewActivity;
import managment.protege.supermed.Model.CityListModel;
import managment.protege.supermed.Model.CountryListModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.CityListResponse;
import managment.protege.supermed.Response.CodCheckoutResponse;
import managment.protege.supermed.Response.CodCheckoutResponse;
import managment.protege.supermed.Response.CountryListResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static managment.protege.supermed.Activity.Main_Apps.nav_email;
import static managment.protege.supermed.Activity.Main_Apps.nav_username;
import static managment.protege.supermed.Activity.Main_Apps.navigationHeaderUsername;
import static managment.protege.supermed.Fragment.Home.ProductDetailCartCounter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceOrder extends Fragment{

    private static final String TAG = "PlaceOrder";

    public PlaceOrder() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_place_order, container, false);
        return v;
    }

}
