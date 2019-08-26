package managment.protege.supermed.Fragment;


import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Adapter.ImageAdapter;
import managment.protege.supermed.Adapter.ProductAdapter;
import managment.protege.supermed.Adapter.TopCatagorieAdapter;
import managment.protege.supermed.Model.CartActionResponse;
import managment.protege.supermed.Model.GetProductsModel;
import managment.protege.supermed.Model.ImageData;
import managment.protege.supermed.Model.Search;
import managment.protege.supermed.Model.SearchModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.AddProductWishlistResponse;
import managment.protege.supermed.Response.DeleteWishlist;
import managment.protege.supermed.Response.GetAllProductsResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static managment.protege.supermed.Activity.Main_Apps.cart_toolbarapps;
import static managment.protege.supermed.Activity.Main_Apps.getMainActivity;
import static managment.protege.supermed.Activity.Main_Apps.nobatch;
import static managment.protege.supermed.Activity.Main_Apps.nobatch_products;
import static managment.protege.supermed.Fragment.Home.ProductDetailCartCounter;
import static managment.protege.supermed.Fragment.Home.commonWishlistCounter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetail extends Fragment {

    Button addToCart;
    View view;
    ViewPager viewPager;
    static KProgressHUD hud;
    ImageView carts, plus, min;
    static ImageView wishlistProduct;
    String checkWishlist = "0";
    GetProductsModel obj;
    SearchModel objSearch;
    Search objNativeSearch;
    TextView pname, oldrate, price, detail, qty, productStock;
    public String checkValue;

    public ProductDetail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        Main_Apps.status = false;
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbar(getContext(), "Product Detail", view);
        cart_toolbarapps = (ImageView) view.findViewById(R.id.carts);
        nobatch_products = (TextView) view.findViewById(R.id.nobatch_product);
        if (ProductDetailCartCounter > 0) {
            nobatch_products.setVisibility(View.VISIBLE);
            nobatch_products.setText(String.valueOf(ProductDetailCartCounter));
        } else {
            nobatch_products.setVisibility(View.INVISIBLE);
        }
        initWidget();
        onclick();
        return view;
    }

    private void onclick() {
        carts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Main_Apps.getMainActivity().backfunction(new Cart());

            }
        });
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("ds", GlobalHelper.getUserProfile(getContext()).getProfile().getId().toString());

                if (checkValue == "1") {
                    cartActionProductDetail(obj.getProductID(), GlobalHelper.getUserProfile(getContext()).getProfile().getId().toString(), qty.getText().toString(), "0", getContext(), addToCart);

                } else if (checkValue == "2") {
                    cartActionProductDetail(objSearch.getProductID(), GlobalHelper.getUserProfile(getContext()).getProfile().getId().toString(), qty.getText().toString(), "0", getContext(), addToCart);

                } else if (checkValue == "3") {
                    cartActionProductDetail(objNativeSearch.getProductID(), GlobalHelper.getUserProfile(getContext()).getProfile().getId().toString(), qty.getText().toString(), "0", getContext(), addToCart);
                }


            }
        });
        wishlistProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkWishlist.equals("1")) {
                    if (checkValue == "1") {
                        DeleteProduct(obj.getProductID(), GlobalHelper.getUserProfile(getContext()).getProfile().getId().toString(), getContext());

                    } else if (checkValue == "2") {
                        DeleteProduct(objSearch.getProductID(), GlobalHelper.getUserProfile(getContext()).getProfile().getId().toString(), getContext());

                    } else if (checkValue == "3") {
                        DeleteProduct(objNativeSearch.getProductID(), GlobalHelper.getUserProfile(getContext()).getProfile().getId().toString(), getContext());

                    }

                    checkWishlist = "0";
                } else {
                    if (checkValue == "1") {
                        ProductWishList(obj.getProductID(), GlobalHelper.getUserProfile(getContext()).getProfile().getId().toString(), "1", getContext());

                    } else if (checkValue == "2") {
                        ProductWishList(objSearch.getProductID(), GlobalHelper.getUserProfile(getContext()).getProfile().getId().toString(), "1", getContext());

                    } else if (checkValue == "3") {
                        ProductWishList(objNativeSearch.getProductID(), GlobalHelper.getUserProfile(getContext()).getProfile().getId().toString(), "1", getContext());

                    }
                    checkWishlist = "1";
                }

            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty.setText(String.valueOf(Integer.parseInt(qty.getText().toString()) + Integer.parseInt("1")));
            }
        });
        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!qty.getText().equals("1")) {
                    qty.setText(String.valueOf(Integer.parseInt(qty.getText().toString()) - Integer.parseInt("1")));
                } else {
                }
            }
        });


    }

    private void initWidget() {
        carts = (ImageView) view.findViewById(R.id.carts);
        plus = (ImageView) view.findViewById(R.id.plus);
        min = (ImageView) view.findViewById(R.id.min);
        pname = (TextView) view.findViewById(R.id.pname);
        qty = (TextView) view.findViewById(R.id.qty);
        addToCart = (Button) view.findViewById(R.id.add_cart);
        detail = (TextView) view.findViewById(R.id.detail);
        oldrate = (TextView) view.findViewById(R.id.oldrate);
        price = (TextView) view.findViewById(R.id.price);
        productStock = (TextView) view.findViewById(R.id.productStock);
        ArrayList<String> articleList = getArticleData();
        Bundle bundle = getArguments();
        if (bundle.containsKey("your_obj")) {
            checkValue = "1"; // if check value is 1 then perfom task for simple product view
            obj = (GetProductsModel) bundle.getSerializable("your_obj");
            checkWishlist = obj.getIsWish();
            oldrate.setText("Rs" + obj.getProductOldPrice());
            productStock.setText("Quantity Left: " + obj.getProductQty());
            if (obj.getProductQty().equals("0")) {
                addToCart.setText("Out Of Stock");
                addToCart.setEnabled(false);
                addToCart.setBackgroundColor(getResources().getColor(R.color.sale));
            }
            if (obj.getProductPrice().equals("0")) {
                addToCart.setEnabled(false);
                addToCart.setBackgroundColor(getResources().getColor(R.color.cartCouponText));
            }
            oldrate.setPaintFlags(oldrate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            pname.setText(obj.getProductName());
            price.setText("Rs" + obj.getProductPrice());
            detail.setText(Html.fromHtml(obj.getProductDescription()));
            articleList.clear();
            articleList.add(obj.getProductImage());
        } else if (bundle.containsKey("your_obj_2")) {
            checkValue = "2"; // if check value is 1 then perfom task for simple search product view
            objSearch = (SearchModel) bundle.getSerializable("your_obj_2");
            checkWishlist = objSearch.getIsWish();
            oldrate.setText("Rs" + objSearch.getProductOldPrice());
            productStock.setText("Quantity Left: " + objSearch.getProductQty());
            if (objSearch.getProductQty().equals("0")) {
                addToCart.setText("Out Of Stock");
                addToCart.setEnabled(false);
                addToCart.setBackgroundColor(getResources().getColor(R.color.sale));
            }
            if (objSearch.getProductPrice().equals("0")) {
                addToCart.setEnabled(false);
                addToCart.setBackgroundColor(getResources().getColor(R.color.cartCouponText));
            }
            oldrate.setPaintFlags(oldrate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            pname.setText(objSearch.getProductName());
            price.setText("Rs" + objSearch.getProductPrice());
            detail.setText(Html.fromHtml(objSearch.getProductDescription()));
            articleList.clear();
            articleList.add(objSearch.getProductImage());
        } else if (bundle.containsKey("your_obj_3")) {
            checkValue = "3"; // if check value is 1 then perfom task for simple search product view
            objNativeSearch = (Search) bundle.getSerializable("your_obj_3");
            checkWishlist = objNativeSearch.getIsWish();
            oldrate.setText("Rs" + objNativeSearch.getProductOldPrice());

            oldrate.setPaintFlags(oldrate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            pname.setText(objNativeSearch.getProductName());
            price.setText("Rs" + objNativeSearch.getProductPrice());
            detail.setText(Html.fromHtml(objNativeSearch.getProductDescription()));
            productStock.setText("Quantity Left: " + objNativeSearch.getProductQty());
            if (objNativeSearch.getProductQty().equals("0")) {
                addToCart.setText("Out Of Stock");
                addToCart.setEnabled(false);
                addToCart.setBackgroundColor(getResources().getColor(R.color.sale));
            }
            if (objNativeSearch.getProductPrice().equals("0")) {
                addToCart.setEnabled(false);
                addToCart.setBackgroundColor(getResources().getColor(R.color.cartCouponText));

            }
            articleList.clear();
            articleList.add(objNativeSearch.getProductImage());
        }


        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        wishlistProduct = (ImageView) view.findViewById(R.id.wishlist_product);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);
        if (checkWishlist.equals("1")) {
            wishlistProduct.setBackgroundResource(R.drawable.wishlist_product_green);
        } else {
            wishlistProduct.setBackgroundResource(R.drawable.wishlist_product);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        ImageAdapter adapter = new ImageAdapter(getContext(), articleList);
        viewPager.setAdapter(adapter);
    }


    private ArrayList<String> getArticleData() {
        ArrayList<String> articleList = new ArrayList<String>(Arrays.asList(ImageData.articles));


        return articleList;
    }


    public static void cartAction(String pid, String uid, String qty, String action, final Context context, final Button button) {
        Main_Apps.hud.show();
        API api = RetrofitAdapter.createAPI();
        Call<CartActionResponse> callBackCall = api.cart_action(pid, uid, qty, action);
        callBackCall.enqueue(new Callback<CartActionResponse>() {
            @Override
            public void onResponse(Call<CartActionResponse> call, final Response<CartActionResponse> response) {
                if (response != null) {
                    if (response.body().getStatus()) {
                        ProductDetailCartCounter = ProductDetailCartCounter + 1;
                        Main_Apps.nobatch.setVisibility(View.VISIBLE);
                        Main_Apps.nobatch.setText(String.valueOf(ProductDetailCartCounter));
                    } else {
                    }
                }
                Main_Apps.hud.dismiss();
            }

            @Override
            public void onFailure(Call<CartActionResponse> call, Throwable t) {
                Log.e("cart", "Error is " + t.getMessage());
                Main_Apps.hud.dismiss();
            }
        });

    }

    public static void cartActionProductDetail(String pid, String uid, String qty, String action, final Context context, final Button button) {
        Main_Apps.hud.show();
        API api = RetrofitAdapter.createAPI();
        Call<CartActionResponse> callBackCall = api.cart_action(pid, uid, qty, action);
        callBackCall.enqueue(new Callback<CartActionResponse>() {
            @Override
            public void onResponse(Call<CartActionResponse> call, final Response<CartActionResponse> response) {
                if (response != null) {
                    if (response.body().getStatus() == true) {
                        ProductDetailCartCounter = ProductDetailCartCounter + 1;
                        Main_Apps.nobatch_products.setText(String.valueOf(ProductDetailCartCounter));
                        Main_Apps.getMainActivity().backfunction(new Cart());
                    } else {
                    }
                }
                Main_Apps.hud.dismiss();
            }

            @Override
            public void onFailure(Call<CartActionResponse> call, Throwable t) {
                Log.e("cart", "Error is " + t.getMessage());
                Main_Apps.hud.dismiss();
            }
        });

    }

    public static void ProductWishList(String pid, String uid, String qty, final Context context) {

        API api = RetrofitAdapter.createAPI();
        Call<AddProductWishlistResponse> addProductWishlistResponseCall = api.ADD_PRODUCT_WISHLIST_RESPONSE_CALL(pid, uid, qty);
        addProductWishlistResponseCall.enqueue(new Callback<AddProductWishlistResponse>() {
            @Override
            public void onResponse(Call<AddProductWishlistResponse> call, final Response<AddProductWishlistResponse> response) {
                if (response != null) {
                    if (response.body().getStatus()) {
                        wishlistProduct.setBackgroundResource(R.drawable.wishlist_product_green);
                        Log.e("log", response.body().getMessage());
                        Log.e("log", response.body().getStatus().toString());
                    } else {
                    }

                }
            }

            @Override
            public void onFailure(Call<AddProductWishlistResponse> call, Throwable t) {
                Log.e("wishlist product add", "Error is " + t.getMessage());

            }
        });

    }

    public void DeleteProduct(String pid, String uid, final Context context) {

        API api = RetrofitAdapter.createAPI();
        Call<DeleteWishlist> deleteWishlistCall = api.DELETE_WISHLIST_CALL(pid, uid);
        deleteWishlistCall.enqueue(new Callback<DeleteWishlist>() {
            @Override
            public void onResponse(Call<DeleteWishlist> call, final Response<DeleteWishlist> response) {
                if (response != null) {
                    if (response.body().getStatus().equals(true)) {
                        wishlistProduct.setBackgroundResource(R.drawable.wishlist_product);
                    } else {
                    }

                }
            }

            @Override
            public void onFailure(Call<DeleteWishlist> call, Throwable t) {
//                Toast.makeText(context, "incorrect", Toast.LENGTH_SHORT).show();
                Log.e("wishlist product del", "Error is " + t.getMessage());

            }
        });

    }


}
