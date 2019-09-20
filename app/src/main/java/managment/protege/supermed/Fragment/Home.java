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
import com.android.volley.Response;
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
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.Adapter.AdapterCategories;
import managment.protege.supermed.Adapter.AdapterProduct;
import managment.protege.supermed.Adapter.Adapter_Brand;
import managment.protege.supermed.Model.ModelCategories;
import managment.protege.supermed.Model.Model_Brand;
import managment.protege.supermed.Model.Model_PopularProducts;
import managment.protege.supermed.R;
import managment.protege.supermed.Tools.GlobalHelper;

public class Home extends Fragment {

    private ViewFlipper viewflip;
    public static int ProductDetailCartCounter, commonWishlistCounter;
    private KProgressHUD hud;
    private Button btn_upload_prescription;
    private LinearLayout labTest, doctorsInformation, emergencyContacts, scheduledAppointments;
    View view;

    //Categories
    private RecyclerView recyclerViewCategories;
    private AdapterCategories adapter;
    private List<ModelCategories> list;

    //New Arrivals
    private RecyclerView recyclerViewNewArrivals;
    private AdapterProduct newArrivalsAdapter;
    private List<Model_PopularProducts> newArrivalsList;

    //Popular Products
    private RecyclerView recyclerViewPopularProducts;
    private AdapterProduct popularProductsAdapter;
    private List<Model_PopularProducts> popularProductsList;

    //Brands
    private RecyclerView recyclerViewBrands;
    private Adapter_Brand adapterBrand;
    private List<Model_Brand> brandList;

    //RecentlyViewed Products
    private RecyclerView recyclerViewRecentProducts;
    private AdapterProduct recentProductsAdapter;
    private List<Model_PopularProducts> recentProductsList;

    private String userid;

    public Home() {
        Main_Apps.status = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        initWidget();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        Main_Apps.showToolBarItems("HOME");

        userid = GlobalHelper.getUserProfile(getContext()).getProfile().getId();

        onClickFunctions();
        Main_Apps.nobatch.setVisibility(View.GONE);
        Main_Apps.wl_badge.setVisibility(View.GONE);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

        //Slider
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

        //Brands
        recyclerViewBrands = view.findViewById(R.id.recyclerViewBrands);
        recyclerViewBrands.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        brandList = new ArrayList<>();
        getBrands();

        //Recently Viewed Products
        recyclerViewRecentProducts = view.findViewById(R.id.recyclerViewRecentProducts);
        recyclerViewRecentProducts.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        recentProductsList = new ArrayList<>();
        getRecentProducts();

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
                Fragment fragment = new Fragment_Labs();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
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
                    Main_Apps.getMainActivity().backfunction(new Fragment_Book_an_Appointment());
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

    public void getRecentProducts(){

        String URL = Register.Base_URL + "recently-viewed-products";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                String cateSlug = object.getString("cateSlug");
                                String CatName = object.getString("cateName");
                                String subcateSlug = object.getString("subcateSlug");
                                String SubCatName = object.getString("subcateName");
                                String productId = object.getString("productId");
                                String productName = object.getString("productName");
                                String productDescription = object.getString("productDescription");
                                String productImage = object.getString("productImage").trim();
                                String price = object.getString("price");
                                String qty = object.getString("qty");
                                String productTags = object.getString("productTags");
                                Model_PopularProducts item = new Model_PopularProducts(
                                        cateSlug,
                                        CatName,
                                        subcateSlug,
                                        SubCatName,
                                        productId,
                                        productName,
                                        productDescription,
                                        productImage,
                                        price,
                                        qty,
                                        productTags
                                );
                                recentProductsList.add(item);
                            }
                            recentProductsAdapter = new AdapterProduct(recentProductsList, getContext());
                            recyclerViewRecentProducts.setAdapter(recentProductsAdapter);
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

    public void getNewArrivals() {
        String URL = Register.Base_URL + "latest-products";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            hud.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String cateSlug = object.getString("cateSlug");
                                String CatName = object.getString("cateName");
                                String subcateSlug = object.getString("subcateSlug");
                                String SubCatName = object.getString("subcateName");
                                String productId = object.getString("productId");
                                String productName = object.getString("productName");
                                String productDescription = object.getString("productDescription");
                                String productImage = object.getString("productImage").trim();
                                String price = object.getString("price");
                                String qty = object.getString("qty");
                                String productTags = object.getString("productTags");
                                Model_PopularProducts item = new Model_PopularProducts(
                                        cateSlug,
                                        CatName,
                                        subcateSlug,
                                        SubCatName,
                                        productId,
                                        productName,
                                        productDescription,
                                        productImage,
                                        price,
                                        qty,
                                        productTags
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
                map.put("userid", userid);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);

    }

    public void getPopularProducts() {
        String URL = Register.Base_URL + "popular-products";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            hud.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String cateSlug = object.getString("cateSlug");
                                String CatName = object.getString("cateName");
                                String subcateSlug = object.getString("subcateSlug");
                                String SubCatName = object.getString("subcateName");
                                String productId = object.getString("productId");
                                String productName = object.getString("productName");
                                String productDescription = object.getString("productDescription");
                                String productImage = object.getString("productImage").trim();
                                String price = object.getString("price");
                                String qty = object.getString("qty");
                                String productTags = object.getString("productTags");
                                Model_PopularProducts item = new Model_PopularProducts(
                                        cateSlug,
                                        CatName,
                                        subcateSlug,
                                        SubCatName,
                                        productId,
                                        productName,
                                        productDescription,
                                        productImage,
                                        price,
                                        qty,
                                        productTags
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
                        hud.dismiss();
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userid", userid);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);

    }

    public void getBrands(){

        String URL = Register.Base_URL + "brands";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String id = jsonObject1.getString("id");
                                String name = jsonObject1.getString("name");
                                String slug = jsonObject1.getString("slug");
                                String image = jsonObject1.getString("image");

                                Model_Brand item = new Model_Brand(
                                        id,
                                        name,
                                        slug,
                                        image
                                );

                                brandList.add(item);
                            }

                            adapterBrand = new Adapter_Brand(brandList, getContext());
                            recyclerViewBrands.setAdapter(adapterBrand);
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
