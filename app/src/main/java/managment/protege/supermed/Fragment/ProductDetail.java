package managment.protege.supermed.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.Adapter.AdapterProduct;
import managment.protege.supermed.Adapter.ImageAdapter;
import managment.protege.supermed.Adapter.ProductAdapter;
import managment.protege.supermed.Adapter.TopCatagorieAdapter;
import managment.protege.supermed.Model.CartActionResponse;
import managment.protege.supermed.Model.GetProductsModel;
import managment.protege.supermed.Model.ImageData;
import managment.protege.supermed.Model.Model_PopularProducts;
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
    public String checkValue, product_ID, wishlistProductId = "";
    ImageView product_Image;
    ImageButton btn_add_to_wishlist;
    boolean isPlay;
    String cateSlug;

    //Related Products
    private RecyclerView recyclerViewRelatedProducts;
    private AdapterProduct relatedProductsAdapter;
    private List<Model_PopularProducts> relatedProductsList;

    private String userid;

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

        userid = GlobalHelper.getUserProfile(getContext()).getProfile().getId();

        cart_toolbarapps = (ImageView) view.findViewById(R.id.carts);
        nobatch_products = (TextView) view.findViewById(R.id.nobatch_product);
        if (ProductDetailCartCounter > 0) {
            nobatch_products.setVisibility(View.VISIBLE);
            nobatch_products.setText(String.valueOf(ProductDetailCartCounter));
        } else {
            nobatch_products.setVisibility(View.INVISIBLE);
        }

        if (getArguments() != null) {
            product_ID = getArguments().getString("ProductID");
            cateSlug = getArguments().getString("cateslug");
        }

        initViews();
        getProductDetail(product_ID);

        //Categories
        recyclerViewRelatedProducts = view.findViewById(R.id.recyclerViewRelatedProducts);
        recyclerViewRelatedProducts.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        relatedProductsList = new ArrayList<>();
        getRelatedProducts();

        return view;
    }

    public void initViews() {

        pname = view.findViewById(R.id.pname);
        price = view.findViewById(R.id.price);
        productStock = view.findViewById(R.id.productStock);
        detail = view.findViewById(R.id.detail);
        product_Image = view.findViewById(R.id.product_Image);
        btn_add_to_wishlist = view.findViewById(R.id.btn_add_to_wishlist);

        btn_add_to_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddtoWishList();
                view.setBackgroundResource(R.drawable.wishlist_product_green);
                btn_add_to_wishlist.setEnabled(false);
            }
        });

        if (isPlay){
            btn_add_to_wishlist.setBackgroundResource(R.drawable.wishlist_product_green);
        } else {
            btn_add_to_wishlist.setBackgroundResource(R.drawable.wishlist_product);
        }


        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setWindowColor(Color.parseColor("#5D910B"))
                .show();

    }

    public void getProductDetail(String product_ID) {

        String URL = Register.Base_URL + "single-products/" + product_ID;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject object = jsonObject.getJSONObject("data");
                            String productId = object.getString("productId");
                            String ImageLink = object.getString("productImage");
                            if (!ImageLink.equals("")) {
                                ImageLink = ImageLink.replaceAll(" ", "%20");
                                Picasso.get()
                                        .load(ImageLink)
                                        .resize(80, 80)
                                        .centerCrop()
                                        .placeholder(R.drawable.tab_miss)
                                        .into(product_Image);
                            }
                            pname.setText(object.getString("productName"));
                            price.setText("Rs. " + object.getString("price"));
                            productStock.setText("Quantity Left: " + object.getString("qty") + " pcs");
                            detail.setText(object.getString("productDescription"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hud.dismiss();
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    public void AddtoWishList() {

        hud.show();
        String URL = Register.Base_URL + "add-to-wishlist";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            String message = jsonObject.getString("message");
                            if (status) {
                                hud.dismiss();
                                JSONObject object = jsonObject.getJSONObject("data");
                                wishlistProductId = object.getString("wishlistId");
                                isPlay = true;
                                Toast.makeText(getContext(), message,
                                        Toast.LENGTH_LONG).show();
                            } else {
                                hud.dismiss();
                                Toast.makeText(getContext(), message,
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
                        hud.dismiss();
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userId", userid);
                map.put("productId", product_ID);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    public void getRelatedProducts(){

        String URL = Register.Base_URL + "related-products/" + cateSlug;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            hud.dismiss();
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
                                relatedProductsList.add(item);
                            }
                            relatedProductsAdapter = new AdapterProduct(relatedProductsList, getContext());
                            recyclerViewRelatedProducts.setAdapter(relatedProductsAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hud.dismiss();
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);

    }


}
