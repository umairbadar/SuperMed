package managment.protege.supermed.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.Adapter.cartAdapter;
import managment.protege.supermed.Model.CartActionResponse;
import managment.protege.supermed.Model.CartModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.CartResponse;
import managment.protege.supermed.Response.CouponResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import retrofit2.Call;
import retrofit2.Callback;

import static managment.protege.supermed.Fragment.Home.ProductDetailCartCounter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cart extends Fragment {

    private RecyclerView recyclerView;
    public static cartAdapter adapter;
    private List<CartModel> list;

    public static TextView tv_total, tv_price, tv_coupon_price;
    private EditText et_coupon;
    private Button btn_apply_coupon, btn_place_order;
    private LinearLayout layoutCoupon;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    Boolean applyCoupon;

    private LinearLayout layout_data, layout_empty;


    public Cart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.cart), v);

        sharedPreferences = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        applyCoupon = sharedPreferences.getBoolean("applyCoupon", false);

        layout_data = v.findViewById(R.id.layout_data);
        layout_empty = v.findViewById(R.id.layout_empty);

        //Coupon
        et_coupon = v.findViewById(R.id.et_coupon);
        btn_apply_coupon = v.findViewById(R.id.btn_apply_coupon);
        btn_place_order = v.findViewById(R.id.btn_place_order);
        layoutCoupon = v.findViewById(R.id.layoutCoupon);

        if (GlobalHelper.getUserProfile(getContext()).getProfile().getIsGuest().equals("No")) {
            layoutCoupon.setVisibility(View.VISIBLE);
        } else {
            layoutCoupon.setVisibility(View.GONE);
        }

        btn_apply_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_coupon.getText().toString().length() > 0) {

                    applyCoupon(et_coupon.getText().toString(), tv_price.getText().toString());
                } else {
                    et_coupon.setError("Please enter coupon code");
                    et_coupon.requestFocus();
                }
            }
        });

        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Bundle args = new Bundle();
                args.putString("price", tv_total.getText().toString());

                Main_Apps.getMainActivity().backfunction(new Fragment_Place_Order(), args);
            }
        });

        tv_total = v.findViewById(R.id.tv_total);
        tv_price = v.findViewById(R.id.tv_price);
        tv_coupon_price = v.findViewById(R.id.tv_coupon_price);

        recyclerView = v.findViewById(R.id.cartRecycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        list = new ArrayList<>();
        adapter = new cartAdapter(list, getContext());
        recyclerView.setAdapter(adapter);
        getCartDetails(getContext(), list);

        if (applyCoupon){
            tv_coupon_price.setText(sharedPreferences.getString("couponValue", "0") + "%");
        }

        return v;
    }

    public void getCartDetails(final Context context, final List<CartModel> list) {
        Main_Apps.hud.show();
        String URL = Register.Base_URL + "cart-list";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            if (status) {
                                Main_Apps.hud.dismiss();
                                layout_empty.setVisibility(View.GONE);
                                layout_data.setVisibility(View.VISIBLE);
                                btn_place_order.setVisibility(View.VISIBLE);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    String qty = object.getString("qty");
                                    String price = object.getString("price");
                                    String cartId = object.getString("cartId");
                                    JSONObject innerObj = object.getJSONObject("options");
                                    String image = innerObj.getString("productImage");

                                    CartModel item = new CartModel(
                                            id,
                                            name,
                                            price,
                                            qty,
                                            image,
                                            cartId
                                    );

                                    list.add(item);
                                }
                                adapter.notifyDataSetChanged();

                                /*if (applyCoupon) {
                                    tv_coupon_price.setText(sharedPreferences.getString("couponValue", "0") + "%");
                                    double discount = Double.parseDouble(sharedPreferences.getString("couponValue", "0"));
                                    *//*double price = Double.parseDouble(tv_price.getText().toString());
                                    double total = price * discount;
                                    tv_total.setText(String.format("%.2f", price - total));*//*
                                }*/

                            } else {
                                Main_Apps.hud.dismiss();
                                layout_empty.setVisibility(View.VISIBLE);
                                layout_data.setVisibility(View.GONE);
                                btn_place_order.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Main_Apps.hud.dismiss();
                        Toast.makeText(context, error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userId", GlobalHelper.getUserProfile(getContext()).getProfile().getId());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(req);

    }

    private void applyCoupon(final String coupon_code, final String total_price) {
        String URL = Register.Base_URL + "apply-for-coupon";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            String msg = jsonObject.getString("msg");
                            if (status) {
                                editor = sharedPreferences.edit();
                                JSONObject innerObj = jsonObject.getJSONObject("data");
                                JSONObject innerObj1 = jsonObject.getJSONObject("coupon_session");

                                editor.putBoolean("applyCoupon", true);
                                editor.putString("couponValue", innerObj.getString("discount"));
                                editor.putString("DiscountAmount", innerObj1.getString("amount"));
                                editor.apply();



                                tv_coupon_price.setText(innerObj.getString("discount") + "%");
                                tv_total.setText(innerObj.getString("discountedPrice"));
                                Toast.makeText(getContext(), msg,
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), msg,
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userId", GlobalHelper.getUserProfile(getContext()).getProfile().getId());
                map.put("coupon_code", coupon_code);
                map.put("total_cart_price", total_price);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);

    }
}
