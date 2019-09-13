package managment.protege.supermed.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.Adapter.WishListAdapter;
import managment.protege.supermed.Model.WishlistModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Tools.GlobalHelper;

import static managment.protege.supermed.Activity.Main_Apps.nobatch_products;
import static managment.protege.supermed.Fragment.Home.ProductDetailCartCounter;


public class Wishlist_Fragment extends Fragment {


    View view;
    String userid;

    private RecyclerView wishlist_recyclerView;
    private WishListAdapter adapter_wishlist;
    private List<WishlistModel> arr_wishlist;
    private LinearLayout layout1, layout2;
    private KProgressHUD hud;

    public Wishlist_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_wishlist_, container, false);

        userid = GlobalHelper.getUserProfile(getContext()).getProfile().getId();
        nobatch_products = (TextView) view.findViewById(R.id.nobatch_product);
        if (ProductDetailCartCounter > 0) {
            nobatch_products.setVisibility(View.VISIBLE);
            nobatch_products.setText(String.valueOf(ProductDetailCartCounter));
        } else {
            nobatch_products.setVisibility(View.INVISIBLE);
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbar(getContext(), "WISH LIST", view);

        layout1 = view.findViewById(R.id.layout1);
        layout2 = view.findViewById(R.id.layout2);

        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setWindowColor(Color.parseColor("#5D910B"))
                .show();

        wishlist_recyclerView = view.findViewById(R.id.wishlist_recyclerView);
        wishlist_recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        wishlist_recyclerView.setItemAnimator(new DefaultItemAnimator());
        wishlist_recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        arr_wishlist = new ArrayList<>();
        getWishlist();

        return view;
    }

    public void getWishlist(){

        hud.show();
        String URL = Register.Base_URL + "wishlist/" + userid;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            ProductDetailCartCounter = jsonObject.getInt("row_count");
                            if (status){
                                hud.dismiss();
                                layout1.setVisibility(View.VISIBLE);
                                layout2.setVisibility(View.GONE);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String productId = object.getString("productId");
                                    String productName = object.getString("productName");
                                    String productImage = object.getString("productImage");
                                    String productDescription = object.getString("productDescription");
                                    String wishlistId = object.getString("wishlistId");
                                    String qty = object.getString("qty");
                                    String price = object.getString("price");
                                    String subcateName = object.getString("subcateName");

                                    WishlistModel item = new WishlistModel(
                                            productId,
                                            productName,
                                            productImage,
                                            productDescription,
                                            wishlistId,
                                            qty,
                                            price,
                                            subcateName
                                    );
                                    arr_wishlist.add(item);
                                }
                                adapter_wishlist = new WishListAdapter(arr_wishlist, getContext());
                                wishlist_recyclerView.setAdapter(adapter_wishlist);
                            } else {
                                hud.dismiss();
                                layout1.setVisibility(View.GONE);
                                layout2.setVisibility(View.VISIBLE);
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
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

}
