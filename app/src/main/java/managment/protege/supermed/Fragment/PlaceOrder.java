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
import static managment.protege.supermed.Fragment.Cart.cartCounter_Cart;
import static managment.protege.supermed.Fragment.Cart.cartTotal;
import static managment.protege.supermed.Fragment.Cart.coupanCode;
import static managment.protege.supermed.Fragment.Home.ProductDetailCartCounter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceOrder extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    Button checkOut;
    List<CountryListModel> countryListModels;
    List<CityListModel> cityListModels;
    String st_country, st_city, countryId;
    public static Dialog downloadBillDialog;
    RadioButton placeOrder_rb_credit, placeOrder_rb_cod;
    EditText placeOrder_address, placeOrder_number;
    LinearLayout LL_pc_top, LL_pc_top2;
    Spinner guest_country, guest_city;
    TextView placeOrder_orderSummary, placeOrder_orderTotal, placeOrder_orderTotalPayable, placeOrder_UserName;
    EditText guest_fname, guest_lname, guest_email, guest_mobile, guest_address;
    String address, number, paytype, ordernote, guestFname, guestLname, guestEmail, guestMobile, guestAddress;
    public static String invoiceUrl;
    TextView toolbar_text, cartCouponDiscount;
    static private String disountAmountPercentage = "";

    private static final String TAG = "PlaceOrder";

    public PlaceOrder() {
    }

    @SuppressLint("ValidFragment")
    public PlaceOrder(String string) {
        disountAmountPercentage = string;
    }


    public static PlaceOrder newInstance(String string) {
        // Required empty public constructor
        disountAmountPercentage = string;
        return new PlaceOrder();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_place_order, container, false);

        initializations(v);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.place_order), v);
        if (GlobalHelper.getUserProfile(getContext()).getProfile().getFirstName().trim().toLowerCase().equals("guest")) {
            LL_pc_top.setVisibility(View.GONE);
            LL_pc_top2.setVisibility(View.VISIBLE);
            LoadCountries(getContext(), guest_country);
            SelectLabId(guest_country, guest_city);
            SelectCityId(guest_city);

        } else {
            LL_pc_top.setVisibility(View.VISIBLE);
            LL_pc_top2.setVisibility(View.GONE);
            placeOrder_address.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getAddress());
            placeOrder_number.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getContact());
            placeOrder_UserName.setText(GlobalHelper.getUserProfile(getContext()).getProfile().getFirstName());
        }

        placeOrder_orderSummary.setText(cartCounter_Cart + " Items");
        placeOrder_orderTotal.setText(cartTotal);
        placeOrder_orderTotalPayable.setText(cartTotal);
        cartCouponDiscount.setText(disountAmountPercentage + "");
        onClickFunctions();
        return v;
    }

    private void onClickFunctions() {
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalHelper.getUserProfile(getContext()).getProfile().getFirstName().toLowerCase().trim().equals("guest")) {
                    if (Register.etValidate(guest_fname) && Register.etValidate(guest_lname) && Register.etValidate(guest_email) && Register.etValidate(guest_mobile) && Register.etValidate(guest_address)) {
                        if (Register.isValidEmail(guest_email)) {
                            guestFname = guest_fname.getText().toString();
                            guestLname = guest_lname.getText().toString();
                            number = guest_mobile.getText().toString();
                            guestEmail = guest_email.getText().toString();
                            address = guest_address.getText().toString();
                            if (placeOrder_rb_credit.isChecked()) {
                                Log.e("paytype check", "credit " + paytype);
                                paytype = PAY_TYPE.KEENU.getValue();
                                SubmitOrder(GlobalHelper.getUserProfile(getContext()).getProfile().getId(), number, address, ordernote, paytype, coupanCode, guestFname, guestLname, st_country, st_city, guestEmail);
                            } else if (placeOrder_rb_cod.isChecked()) {
                                paytype = PAY_TYPE.CASH.getValue();
                                Log.e("paytype check", paytype);
                                SubmitOrder(GlobalHelper.getUserProfile(getContext()).getProfile().getId(), number, address, ordernote, paytype, coupanCode, guestFname, guestLname, st_country, st_city, guestEmail);

                            }
                        } else {

                        }
                    } else {

                    }
                } else {
                    if (Register.etValidate(placeOrder_address) && Register.etValidate(placeOrder_number)) {
                        address = placeOrder_address.getText().toString();
                        number = placeOrder_number.getText().toString();
                        if (placeOrder_rb_credit.isChecked()) {
                            paytype = PAY_TYPE.KEENU.getValue();

                            SubmitOrder(GlobalHelper.getUserProfile(getContext()).getProfile().getId(), number, address, ordernote, paytype, coupanCode, guestFname, guestLname, st_country, st_city, guestEmail);

                        } else if (placeOrder_rb_cod.isChecked()) {
                            paytype = PAY_TYPE.CASH.getValue();
                            Log.e("paytype check", paytype);
                            ;
                            SubmitOrder(GlobalHelper.getUserProfile(getContext()).getProfile().getId(), number, address, ordernote, paytype, coupanCode, guestFname, guestLname, st_country, st_city, guestEmail);
                        }

                    } else {
                    }
                }
            }
        });
    }

    private void initializations(View v) {
        toolbar_text = (TextView) v.findViewById(R.id.toolbar_text);
        cartCouponDiscount = (TextView) v.findViewById(R.id.cartCouponDiscount);
        checkOut = (Button) v.findViewById(R.id.placeOrder_btn_checkout);
        placeOrder_rb_cod = (RadioButton) v.findViewById(R.id.placeOrder_rb_cod);
        placeOrder_rb_cod.setChecked(true);
        placeOrder_rb_credit = (RadioButton) v.findViewById(R.id.placeOrder_rb_credit);
        placeOrder_address = (EditText) v.findViewById(R.id.placeOrder_address);
        placeOrder_number = (EditText) v.findViewById(R.id.placeOrder_number);
        guest_fname = (EditText) v.findViewById(R.id.guest_fname);
        guest_lname = (EditText) v.findViewById(R.id.guest_lname);
        guest_email = (EditText) v.findViewById(R.id.guest_email);
        guest_mobile = (EditText) v.findViewById(R.id.guest_mobile);
        guest_address = (EditText) v.findViewById(R.id.guest_address);
        placeOrder_orderSummary = (TextView) v.findViewById(R.id.placeOrder_orderSummary);
        placeOrder_orderTotal = (TextView) v.findViewById(R.id.placeOrder_orderTotal);
        placeOrder_orderTotalPayable = (TextView) v.findViewById(R.id.placeOrder_orderTotalPayable);
        placeOrder_UserName = (TextView) v.findViewById(R.id.placeOrder_UserName);
        LL_pc_top = (LinearLayout) v.findViewById(R.id.LL_pc_top);
        LL_pc_top2 = (LinearLayout) v.findViewById(R.id.LL_pc_top2);
        guest_country = (Spinner) v.findViewById(R.id.guest_country);
        guest_city = (Spinner) v.findViewById(R.id.guest_city);
    }

    public void DownloadBill(final Context context) {
        ProductDetailCartCounter = 0;
        Main_Apps.status = true;
        downloadBillDialog = new Dialog(context);
        downloadBillDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        downloadBillDialog.setContentView(R.layout.bill_dialog_box);
        downloadBillDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        downloadBillDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        downloadBillDialog.setCancelable(false);
        final ImageView btn_close = (ImageView) downloadBillDialog.findViewById(R.id.iv_rp_close);
        Button sucess = (Button) downloadBillDialog.findViewById(R.id.btn_downloadBill);
        sucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(invoiceUrl)));
                Main_Apps.getMainActivity().backfunction2(new Home());
                downloadBillDialog.dismiss();

            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadBillDialog.dismiss();

                Main_Apps.getMainActivity().backfunction2(new Home());


            }
        });
        downloadBillDialog.show();
    }

    private void SubmitOrder(String userid, final String contact, String address, String ordernote, final String paytype, String coupounCode, String first_name, String last_name, String country, String city, String email) {
        API api = RetrofitAdapter.createAPI();
        Main_Apps.hud.show();

        Call<CodCheckoutResponse> codCheckoutResponseCall = api.COD_CHECKOUT_RESPONSE_CALL(userid, contact, address, ordernote, paytype, coupounCode, first_name, last_name, country, city, email);


        codCheckoutResponseCall.enqueue(new Callback<CodCheckoutResponse>() {
            @Override
            public void onResponse(Call<CodCheckoutResponse> call, Response<CodCheckoutResponse> response) {
                Main_Apps.hud.dismiss();
                if (response != null) {
                    if (response.body().getStatus()) {

                        if (guestFname != null && guestLname != null && guestEmail != null) {
                            Main_Apps.navigationHeaderUsername = guestFname + " " + guestLname;
                            nav_username.setText(navigationHeaderUsername);
                            nav_email.setText(guestEmail);

                        }

                        if (paytype == PAY_TYPE.CASH.getValue()) {

                            invoiceUrl = response.body().getReceipt();
                            Log.e("invoice url", invoiceUrl);
                            coupanCode = "";
                            DownloadBill(getActivity());
                        } else {
                            Intent intent = new Intent(getContext(), WebViewActivity.class);
                            intent.putExtra("orderid", response.body().getData().getOrder_no());
                            intent.putExtra("orderamount", response.body().getData().getOrder_total_price());
                            intent.putExtra("Type", "Ordered");
                            startActivity(intent);

                        }
                    } else {
                        Log.e("place order", "error " + response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<CodCheckoutResponse> call, Throwable t) {
                Log.e("place order", "Error" + t.getMessage());
                Main_Apps.hud.dismiss();

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d("tag", "External storage2");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("tag", "Permission: " + permissions[0] + "was " + grantResults[0]);
                }
                break;

            case 3:
                Log.d("tag", "External storage1");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("red", "Permission: " + permissions[0] + "was " + grantResults[0]);
                }
                break;
        }
    }

    //COUNTRY AND CITY SPINNER WORKING STARTS HERE

    public void LoadCountries(final Context context, final Spinner sp_country) {
        API api = RetrofitAdapter.createAPI();
        Call<CountryListResponse> countryListResponseCall = api.COUNTRY_LIST_RESPONSE_CALL();
        countryListResponseCall.enqueue(new Callback<CountryListResponse>() {
            @Override
            public void onResponse(Call<CountryListResponse> call, Response<CountryListResponse> response) {
                if (response != null) {
                    if (response.body().getStatus()) {
                        countryListModels = response.body().getCountries();
                        setCountries(response.body().getCountries(), sp_country, context);

                    } else {
                        Log.e("Leads", "sales team spinner" + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryListResponse> call, Throwable t) {
                Log.e("Leads", "sales team spinner on failure" + t.getMessage());
            }
        });
    }

    static private void setCountries(List<CountryListModel> loadLabsModels, Spinner le_salesteam, Context context) {
        List<String> listSpinner = new ArrayList<String>();
        listSpinner.clear();
        for (int i = 0; i < loadLabsModels.size(); i++) {
            listSpinner.add(loadLabsModels.get(i).getName());
            ArrayAdapter<String> csAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listSpinner);
            csAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            le_salesteam.setAdapter(csAdapter);
        }
    }

    //selecting country id
    public void SelectLabId(final Spinner spinner, final Spinner spinner2) {
        spinner.setOnItemSelectedListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> obj, View view, int position, long id) {
                countryId = countryListModels.get(position).getId();
                st_country = countryListModels.get(position).getName();
                LoadCities(getContext(), countryId, spinner2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //set cities
    public void LoadCities(final Context context, String country_id, final Spinner sp_city) {
        API api = RetrofitAdapter.createAPI();
        Call<CityListResponse> cityListResponseCall = api.CITY_LIST_RESPONSE_CALL(country_id);
        cityListResponseCall.enqueue(new Callback<CityListResponse>() {
            @Override
            public void onResponse(Call<CityListResponse> call, Response<CityListResponse> response) {
                if (response != null) {
                    if (response.body().getStatus()) {
                        cityListModels = response.body().getCities();
                        setCities(response.body().getCities(), sp_city, context);

                    } else {
                        Log.e("city", " city spinner" + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CityListResponse> call, Throwable t) {
                Log.e("Leads", "sales team spinner on failure" + t.getMessage());
            }
        });
    }

    static private void setCities(List<CityListModel> cityListModels, Spinner le_salesteam, Context context) {
        List<String> listSpinner = new ArrayList<String>();
        listSpinner.clear();
        for (int i = 0; i < cityListModels.size(); i++) {
            listSpinner.add(cityListModels.get(i).getName());
            ArrayAdapter<String> csAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listSpinner);
            csAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            le_salesteam.setAdapter(csAdapter);
        }
    }

    // selecting City id
    public void SelectCityId(Spinner spinner) {
        spinner.setOnItemSelectedListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> obj, View view, int position, long id) {
                st_city = cityListModels.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //COUNTRY AND CITY SPINNER WORKING ENDS HERE

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public enum PAY_TYPE {
        CASH("cash"),
        KEENU("keenu");
        private String value;

        private PAY_TYPE(String str) {
            value = str;
        }

        String getValue() {
            return this.value;
        }
    }
}
