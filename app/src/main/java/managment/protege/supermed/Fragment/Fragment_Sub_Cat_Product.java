package managment.protege.supermed.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.RecyclerScrollChangeListener;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.Adapter.Adapter_Cat_Product;
import managment.protege.supermed.Model.Model_Cat_Product;
import managment.protege.supermed.R;

public class Fragment_Sub_Cat_Product extends Fragment {

    View view;
    private String slug, sub_slug;
    private RecyclerView recyclerViewProdcuts;
    private List<Model_Cat_Product> productsList;
    private Adapter_Cat_Product adapter;
    private int currentPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sub_cat_products, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), "PRODUCTS", view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null){
            slug = getArguments().getString("slug");
            sub_slug = getArguments().getString("sub_slug");
        }
        recyclerViewProdcuts = view.findViewById(R.id.recyclerView);
        recyclerViewProdcuts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productsList = new ArrayList<>();
        adapter = new Adapter_Cat_Product(productsList, getContext());
        recyclerViewProdcuts.setAdapter(adapter);
        getProducts(slug, sub_slug, currentPage);

        recyclerViewProdcuts.addOnScrollListener(new RecyclerScrollChangeListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                currentPage = page;
                getProducts(slug, sub_slug,currentPage);
            }
        });

    }
    private void getProducts(final String cateslug, final String sub_slug, final int limit){
        Main_Apps.hud.show();
        String URL = Register.Base_URL + "product-list";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            if (status){
                                Main_Apps.hud.dismiss();
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject innerObj = jsonArray.getJSONObject(i);
                                    String productID = innerObj.getString("productId");
                                    String productName = innerObj.getString("productName");
                                    String productDescription = innerObj.getString("productDescription");
                                    String productImage = innerObj.getString("productImage");
                                    String productTags = innerObj.getString("productTags");
                                    String price = innerObj.getString("price");
                                    String qty = innerObj.getString("qty");
                                    String cateSlug = innerObj.getString("cateSlug");
                                    String cateName = innerObj.getString("cateName");
                                    String subcateSlug = innerObj.getString("subcateSlug");
                                    String subcateName = innerObj.getString("subcateName");

                                    Model_Cat_Product item = new Model_Cat_Product(
                                            productID,
                                            productName,
                                            productDescription,
                                            productImage,
                                            productTags,
                                            price,
                                            qty,
                                            cateSlug,
                                            cateName,
                                            subcateSlug,
                                            subcateName
                                    );

                                    productsList.add(item);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                Main_Apps.hud.dismiss();
                                /*Toast.makeText(getContext(), "No Products Available",
                                        Toast.LENGTH_LONG).show();*/
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
                map.put("cateSlug", cateslug);
                map.put("subcateSlug", sub_slug);
                map.put("startLimit", String.valueOf(limit));
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);

    }
}
