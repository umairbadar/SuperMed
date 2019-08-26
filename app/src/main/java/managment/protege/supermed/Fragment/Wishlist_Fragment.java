package managment.protege.supermed.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Adapter.AlternativeListAdapter;
import managment.protege.supermed.Adapter.ProductAdapter;
import managment.protege.supermed.Adapter.WishListAdapter;
import managment.protege.supermed.Model.AlternativeModel;
import managment.protege.supermed.Model.GetProductsModel;
import managment.protege.supermed.Model.WishlistModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.AddProductWishlistResponse;
import managment.protege.supermed.Response.DeleteWishlist;
import managment.protege.supermed.Response.WishlistResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static managment.protege.supermed.Activity.Main_Apps.getMainActivity;
import static managment.protege.supermed.Activity.Main_Apps.nobatch_products;
import static managment.protege.supermed.Fragment.Home.ProductDetailCartCounter;


public class Wishlist_Fragment extends Fragment {


    RecyclerView recyclerView;
    View view;
    private List<WishlistModel> list = new ArrayList<>();
    TextView toolbar_text;
    private WishListAdapter pAdapter;
    ImageView carts;
    LinearLayout LL_wl_top, LL_wl_bottom;
    String userid;

    public Wishlist_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_wishlist_, container, false);
        initWidget();
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

        pAdapter = new WishListAdapter(list, getContext());
        fillProcycle();
        return view;
    }

    private void fillProcycle() {
        pAdapter = new WishListAdapter(getWishListItems(userid), getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pAdapter);

        onclick();
    }

    private void onclick() {
        carts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main_Apps.getMainActivity().backfunction(new Cart());
            }
        });

    }

    private void initWidget() {
        carts = (ImageView) view.findViewById(R.id.carts);
        LL_wl_top = (LinearLayout) view.findViewById(R.id.LL_wl_top);
        LL_wl_bottom = (LinearLayout) view.findViewById(R.id.LL_wl_bottom);

        toolbar_text = view.findViewById(R.id.toolbar_text);
        recyclerView = view.findViewById(R.id.recycler);
    }

    private List<WishlistModel> getWishListItems(String userid) {
        Main_Apps.hud.show();

        API api = RetrofitAdapter.createAPI();
        Call<WishlistResponse> wishlistResponseCall = api.WISHLIST_RESPONSE_CALL(userid);

        wishlistResponseCall.enqueue(new Callback<WishlistResponse>() {
            @Override
            public void onResponse(Call<WishlistResponse> call, Response<WishlistResponse> response) {
                Main_Apps.hud.dismiss();

                list.clear();
                if (response != null) {
                    if (response.body().getStatus()) {
                        Log.e("date", "is " + response.body().getStatus());

                        list = response.body().getWishlist();
                        if (list == null) {
                            LL_wl_bottom.setVisibility(View.VISIBLE);
                            LL_wl_top.setVisibility(View.INVISIBLE);
                            list.clear();
                            pAdapter.notifyDataSetChanged();

                        } else {


                            if (list.get(0).getProductName() == null) {
                                LL_wl_bottom.setVisibility(View.VISIBLE);
                                LL_wl_top.setVisibility(View.INVISIBLE);
                                list.clear();
                                pAdapter.notifyDataSetChanged();
                            } else {
                                LL_wl_bottom.setVisibility(View.INVISIBLE);
                                LL_wl_top.setVisibility(View.VISIBLE);
                                setTeacherList(response.body().getWishlist());
                                pAdapter.notifyDataSetChanged();
                            }

                        }
                    } else {
                        LL_wl_bottom.setVisibility(View.VISIBLE);
                        LL_wl_top.setVisibility(View.INVISIBLE);
                        list.clear();
                        pAdapter.notifyDataSetChanged();
                        Log.e("Toast", "show" + response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<WishlistResponse> call, Throwable t) {
                Main_Apps.hud.dismiss();
            }
        });
        return list;

    }

    private void setTeacherList(List<WishlistModel> wishlistModels) {
        pAdapter = new WishListAdapter(wishlistModels, getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        pAdapter.setOnItemClickListener(new WishListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, WishlistModel obj) {
                DeleteProduct(obj.getProductID(), GlobalHelper.getUserProfile(getContext()).getProfile().getId().toString(), getContext());

            }
        });

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pAdapter);
    }

    public void DeleteProduct(String pid, String uid, final Context context) {

        API api = RetrofitAdapter.createAPI();
        Call<DeleteWishlist> deleteWishlistCall = api.DELETE_WISHLIST_CALL(pid, uid);
        deleteWishlistCall.enqueue(new Callback<DeleteWishlist>() {
            @Override
            public void onResponse(Call<DeleteWishlist> call, final Response<DeleteWishlist> response) {
                if (response.body().getStatus().equals(true)) {
                    fillProcycle();
                    Log.e("log", response.body().getMessage());
                    Log.e("log", response.body().getStatus().toString());
                    Toasty.success(context, response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                } else {
                    Toasty.error(context, response.body().getMessage(), Toast.LENGTH_SHORT, false).show();
                }

            }

            @Override
            public void onFailure(Call<DeleteWishlist> call, Throwable t) {
                Log.e("wishlist product", "Error is " + t.getMessage());

            }
        });

    }


}
