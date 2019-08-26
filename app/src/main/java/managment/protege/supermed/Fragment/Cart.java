package managment.protege.supermed.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
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

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
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
import retrofit2.Response;

import static managment.protege.supermed.Fragment.Home.ProductDetailCartCounter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cart extends Fragment {
    private List<CartModel> movieList = new ArrayList<>();
    private cartAdapter mAdapter;
    private RecyclerView recyclerView;
    EditText cartApplyPromo;
    TextView cartApplyCoupon, cartCouponDiscount, cartTotalPayable, cartDelivery;
    Button placeOrder;
    TextView cartPrice;
    public static String coupanCode;
    LinearLayout LL_cart_bottom, LL_cart_top, pricelayout;
    String cCode = "";
    int discountPercent;
    KProgressHUD hud;
    public static String cartCounter_Cart, cartTotal;
    public static int pprice = 0;
    Main_Apps main_apps;
    View view;
    TextView toolbar_text;
    int i = 0;
    Double total = 0.0;
    int totalWithQty = 0;

    public Cart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.cartRecycler);
        initialzations(v);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.cart), v);

        OnClickFunctions();
        LoadApi(GlobalHelper.getUserProfile(getContext()).getProfile().getId().toString());

        return v;
    }

    private void initialzations(View v) {
        toolbar_text = (TextView) v.findViewById(R.id.toolbar_text);
        placeOrder = (Button) v.findViewById(R.id.btn_cartPlaceOrder);
        cartApplyPromo = (EditText) v.findViewById(R.id.cartApplyPromo);
        cartApplyCoupon = (TextView) v.findViewById(R.id.cartApplyCoupon);
        cartTotalPayable = (TextView) v.findViewById(R.id.total);
        cartDelivery = (TextView) v.findViewById(R.id.cartDelivery);
        cartCouponDiscount = (TextView) v.findViewById(R.id.cartCouponDiscount);
        LL_cart_bottom = (LinearLayout) v.findViewById(R.id.LL_cart_bottom);
        LL_cart_top = (LinearLayout) v.findViewById(R.id.LL_cart_top);
        pricelayout = v.findViewById(R.id.pricelayout);
        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false);
        cartPrice = (TextView) v.findViewById(R.id.cartPrice);
    }


    private void fillProcycle(List<CartModel> Cart) {
        movieList = Cart;
        mAdapter = new cartAdapter(movieList, getContext());
        mAdapter.setOnItemClickListener(new cartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, CartModel obj, String text) {


                if (Integer.parseInt(text) == 0) {
                    cartAction(obj.getProductID(), GlobalHelper.getUserProfile(getContext()).getProfile().getId().toString(), text, "2");
                } else {
                    cartAction(obj.getProductID(), GlobalHelper.getUserProfile(getContext()).getProfile().getId().toString(), text, "1");
                }
            }
        });

        mAdapter.setOnItemClickListenerplus(new cartAdapter.OnItemClickListenerplus() {
            @Override
            public void onItemClick(View view, CartModel obj, String text) {
                cartAction(obj.getProductID(), GlobalHelper.getUserProfile(getContext()).getProfile().getId().toString(), text, "1");

            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareMovieData();
    }


    public void LoadApi(String Userid) {
        hud.show();
        API api = RetrofitAdapter.createAPI();
        Call<CartResponse> callBackCall = api.get_cart(Userid);
        callBackCall.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, final Response<CartResponse> response) {

                if (response != null) {
                    if (response.body().getStatus()) {
                        ProductDetailCartCounter = response.body().getCartCounter();
                        LL_cart_bottom.setVisibility(View.VISIBLE);
                        pricelayout.setVisibility(View.VISIBLE);
                        LL_cart_top.setVisibility(View.INVISIBLE);

                        placeOrder.setVisibility(View.VISIBLE);
                        total = Double.valueOf(0);
                        totalWithQty = 0;
                        pprice = 0;

                        for (i = 0; i < response.body().getCart().size(); i++) {
                            total = Double.valueOf(response.body().getCart().get(i).getProductPrice());
                            totalWithQty = total.intValue() * Integer.valueOf(response.body().getCart().get(i).getUserQty());
                            pprice = pprice + totalWithQty;
                        }

                        cartPrice.setText("RS " + pprice);
                        cartTotalPayable.setText("RS " + String.valueOf(pprice));
                        cartTotal = String.valueOf(pprice);
                        cartCouponDiscount.setText("RS 0");


                        cartDelivery.setText("Free");

                        cartCounter_Cart = response.body().getCartCounter().toString();
                        fillProcycle(response.body().getCart());
                        Toasty.success(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                    } else {
                        ProductDetailCartCounter = response.body().getCartCounter();
                        LL_cart_bottom.setVisibility(View.INVISIBLE);
                        LL_cart_top.setVisibility(View.VISIBLE);
                        placeOrder.setVisibility(View.INVISIBLE);
                        //Toasty.success(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT, false).show();
                    }
                    hud.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                //Toast.makeText(getContext(), "incorrect", Toast.LENGTH_SHORT).show();
                Log.e("Login", "Error is " + t.getMessage());
                hud.dismiss();
            }
        });
    }

    private void prepareMovieData() {
        mAdapter.notifyDataSetChanged();
    }

    public void cartAction(String pid, String uid, String qty, String action) {
        hud.show();
        API api = RetrofitAdapter.createAPI();
        Call<CartActionResponse> callBackCall = api.cart_action(pid, uid, qty, action);
        callBackCall.enqueue(new Callback<CartActionResponse>() {
            @Override
            public void onResponse(Call<CartActionResponse> call, final Response<CartActionResponse> response) {
                if (response != null) {
                    if (response.body().getStatus()) {
                        Toasty.success(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                        LoadApi(GlobalHelper.getUserProfile(getContext()).getProfile().getId().toString());


                    } else {
                        Toasty.success(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT, false).show();
                    }
                    hud.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CartActionResponse> call, Throwable t) {
                Log.e("cart", "Error is " + t.getMessage());
                hud.dismiss();
            }
        });

    }

    public void OnClickFunctions() {
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                cCode = cartApplyPromo.getText().toString();
                if (cCode.length() > 0) {
                    CheckCoupon(cCode, GlobalHelper.getUserProfile(getContext()).getProfile().getId());
                } else {
                    Main_Apps.getMainActivity().backfunction(new PlaceOrder(cartCouponDiscount.getText().toString()));
                }
            }
        });

        cartApplyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cCode = cartApplyPromo.getText().toString();


                if (cCode.length() > 0) {
                    CheckCoupon(cCode, GlobalHelper.getUserProfile(getContext()).getProfile().getId());
                } else {
                    cartApplyPromo.setError("Please Enter Promo Code First");
                }
            }
        });
    }

    public void CheckCoupon(final String code, final String userid) {
        API api = RetrofitAdapter.createAPI();
        Call<CouponResponse> couponResponseCall = api.COUPON_RESPONSE_CALL(code, userid);
        couponResponseCall.enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {
                if (response != null) {
                    if (response.body().getStatus()) {
                        Toasty.success(getContext(), "Coupon Applied Successfully", Toast.LENGTH_SHORT, true).show();
                        coupanCode = code;
                        Log.e("coupon", coupanCode);
                        discountPercent = Integer.parseInt(response.body().getCoupon().get(0).getDiscount());
                        String discountAmount = String.valueOf((pprice * discountPercent) / 100);
                        cartCouponDiscount.setText(discountAmount);
                        cartTotalPayable.setText(String.valueOf(pprice - (Integer.parseInt(discountAmount))));
                        cartTotal = String.valueOf(pprice - (Integer.parseInt(discountAmount)));
                        Main_Apps.getMainActivity().backfunction(new PlaceOrder(cartCouponDiscount.getText().toString()));
                    } else {
                        cartApplyPromo.setError("Coupon Code not valid or is already used.");
                    }
                }
            }

            @Override
            public void onFailure(Call<CouponResponse> call, Throwable t) {
                Log.e("Coupon", "error" + t.getMessage());
            }
        });
    }
}
