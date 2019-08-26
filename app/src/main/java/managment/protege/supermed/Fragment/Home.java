package managment.protege.supermed.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Adapter.AdapterCategories;
import managment.protege.supermed.Adapter.AdapterProduct;
import managment.protege.supermed.Model.GetProductsModel;
import managment.protege.supermed.Model.ModelCategories;
import managment.protege.supermed.R;
import managment.protege.supermed.Tools.GlobalHelper;

public class Home extends Fragment {

    private ViewFlipper viewflip;
    public static int ProductDetailCartCounter, commonWishlistCounter;
    KProgressHUD hud;
    private Button btn_upload_prescription;
    Main_Apps main_apps;
    LinearLayout labTest, doctorsInformation, emergencyContacts, scheduledAppointments;
    View view;

    //Categories
    private RecyclerView recyclerViewCategories;
    private AdapterCategories adapter;
    private List<ModelCategories> list;

    //New Arrivals
    private RecyclerView recyclerViewNewArrivals;
    private AdapterProduct newArrivalsAdapter;
    private List<GetProductsModel> newArrivalsList;

    //Popular Products
    private RecyclerView recyclerViewPopularProducts;
    private AdapterProduct popularProductsAdapter;
    private List<GetProductsModel> popularProductsList;

    public Home() {
        Main_Apps.status = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        initWidget();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        Main_Apps.showToolBarItems("HOME");

        onClickFunctions();
        Main_Apps.nobatch.setVisibility(View.GONE);
        Main_Apps.wl_badge.setVisibility(View.GONE);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

        viewflip = view.findViewById(R.id.viewflip);
        int images[] = {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3};
        for (int image : images) {
            flipimage(image);
        }

        //Categories
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        list = new ArrayList<>();
        getCategories();

        //New Arrivals
        recyclerViewNewArrivals = view.findViewById(R.id.recyclerViewNewArrivals);
        recyclerViewNewArrivals.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        newArrivalsList = new ArrayList<>();
        newArrivalsAdapter = new AdapterProduct(newArrivalsList, getContext());
        getNewArrivals();

        //Popular Products
        recyclerViewPopularProducts = view.findViewById(R.id.recyclerViewPopularProducts);
        recyclerViewPopularProducts.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        popularProductsList = new ArrayList<>();
        getPopularProducts();

        return view;
    }

    public void flipimage(int image) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);
        viewflip.addView(imageView);
        viewflip.setFlipInterval(4000);
        viewflip.setAutoStart(true);
        viewflip.setInAnimation(getContext(), android.R.anim.slide_in_left);
        viewflip.setOutAnimation(getContext(), android.R.anim.slide_out_right);
    }

    private void initWidget() {

        labTest = (LinearLayout) view.findViewById(R.id.lab_test);
        doctorsInformation = (LinearLayout) view.findViewById(R.id.doctors_appointment);
        emergencyContacts = (LinearLayout) view.findViewById(R.id.emergency_contacts);
        scheduledAppointments = (LinearLayout) view.findViewById(R.id.scheduled_appointment);

        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setWindowColor(Color.parseColor("#5D910B"))
                .show();
        btn_upload_prescription = view.findViewById(R.id.btn_upload_prescription);
        btn_upload_prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new UploadPriscribtionFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    public void onClickFunctions() {

        labTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalHelper.getUserProfile(getContext()).getProfile().getFirstName().trim().toLowerCase().equals("guest")) {
                    Main_Apps.getMainActivity().forgotPasswordDialog(getContext());
                } else {
                    Main_Apps.getMainActivity().backfunction(new LabTest());
                }
            }
        });
        doctorsInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new DoctorInformation();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        scheduledAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalHelper.getUserProfile(getContext()).getProfile().getFirstName().trim().toLowerCase().equals("guest")) {
                    Main_Apps.getMainActivity().forgotPasswordDialog(getContext());
                } else {
                    Main_Apps.getMainActivity().backfunction(new Appointment_Fragment());
                }
            }
        });

        emergencyContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main_Apps.getMainActivity().backfunction(new EmergecyFragment());

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getView() == null) {
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().moveTaskToBack(true);
                    return true;
                }
                return false;
            }
        });

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        Main_Apps.showToolBarItems("HOME");
        Main_Apps.nobatch.setVisibility(View.GONE);
        Main_Apps.wl_badge.setVisibility(View.GONE);
    }

    public void getCategories() {
        String URL = "https://www.supermed.pk/api/api/getCategories";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("categories");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("cat_id");
                                String name = object.getString("name");
                                ModelCategories item = new ModelCategories(
                                        id,
                                        name
                                );
                                list.add(item);
                            }
                            adapter = new AdapterCategories(getContext(), list);
                            recyclerViewCategories.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);

    }

    public void getNewArrivals() {
        String URL = "https://www.supermed.pk/api/api/getProducts";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("products");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String CatId = object.getString("CatId");
                                String CatName = object.getString("CatName");
                                String CatBriefIntro = object.getString("CatBriefIntro");
                                String SubCatId = object.getString("SubCatId");
                                String SubCatName = object.getString("SubCatName");
                                String SubCatBriefIntro = object.getString("SubCatBriefIntro");
                                String ProductID = object.getString("ProductID");
                                String ProductName = object.getString("ProductName");
                                String ProductDescription = object.getString("ProductDescription");
                                String ProductSku = object.getString("ProductSku");
                                String ProductImage = object.getString("ProductImage");
                                String ProductPrice = object.getString("ProductPrice");
                                String ProductQty = object.getString("ProductQty");
                                String ProductTags = object.getString("ProductTags");
                                String is_wish = object.getString("is_wish");
                                String is_cart = object.getString("is_cart");
                                GetProductsModel item = new GetProductsModel(
                                        CatId,
                                        CatName,
                                        CatBriefIntro,
                                        SubCatId,
                                        SubCatName,
                                        SubCatBriefIntro,
                                        ProductID,
                                        ProductName,
                                        ProductImage,
                                        ProductDescription,
                                        ProductSku,
                                        ProductPrice,
                                        ProductQty,
                                        ProductTags,
                                        is_wish,
                                        is_cart
                                );
                                newArrivalsList.add(item);
                            }
                            newArrivalsAdapter = new AdapterProduct(newArrivalsList, getContext());
                            recyclerViewNewArrivals.setAdapter(newArrivalsAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userid", "12");
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);

    }

    public void getPopularProducts() {
        String URL = "https://www.supermed.pk/api/api/getProducts";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            hud.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("products");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String CatId = object.getString("CatId");
                                String CatName = object.getString("CatName");
                                String CatBriefIntro = object.getString("CatBriefIntro");
                                String SubCatId = object.getString("SubCatId");
                                String SubCatName = object.getString("SubCatName");
                                String SubCatBriefIntro = object.getString("SubCatBriefIntro");
                                String ProductID = object.getString("ProductID");
                                String ProductName = object.getString("ProductName");
                                String ProductDescription = object.getString("ProductDescription");
                                String ProductSku = object.getString("ProductSku");
                                String ProductImage = object.getString("ProductImage");
                                String ProductPrice = object.getString("ProductPrice");
                                String ProductQty = object.getString("ProductQty");
                                String ProductTags = object.getString("ProductTags");
                                String is_wish = object.getString("is_wish");
                                String is_cart = object.getString("is_cart");
                                GetProductsModel item = new GetProductsModel(
                                        CatId,
                                        CatName,
                                        CatBriefIntro,
                                        SubCatId,
                                        SubCatName,
                                        SubCatBriefIntro,
                                        ProductID,
                                        ProductName,
                                        ProductImage,
                                        ProductDescription,
                                        ProductSku,
                                        ProductPrice,
                                        ProductQty,
                                        ProductTags,
                                        is_wish,
                                        is_cart
                                );
                                popularProductsList.add(item);
                            }
                            popularProductsAdapter = new AdapterProduct(popularProductsList, getContext());
                            recyclerViewPopularProducts.setAdapter(popularProductsAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userid", "12");
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);

    }
}
