package managment.protege.supermed.Fragment;


import android.content.Context;
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
    private Button btn_apply_coupon;
    private LinearLayout layoutCoupon;


    public Cart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.cart), v);

        //Coupon
        et_coupon = v.findViewById(R.id.et_coupon);
        btn_apply_coupon = v.findViewById(R.id.btn_apply_coupon);
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
        getCartDetails();

        return v;
    }

    private void getCartDetails() {
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
                            } else {
                                Main_Apps.hud.dismiss();
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
                        Toast.makeText(getContext(), error.getMessage(),
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                            if (status) {
                                JSONObject innerObj = jsonObject.getJSONObject("data");
                                tv_coupon_price.setText(innerObj.getString("discount"));
                                tv_total.setText(innerObj.getString("discountedPrice"));
                                Toast.makeText(getContext(), "Congratulations! Your Coupon Code is applied",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "Invalid Coupon Code",
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
