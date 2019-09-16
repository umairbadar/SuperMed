package managment.protege.supermed.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import managment.protege.supermed.Adapter.AdapterProduct;
import managment.protege.supermed.Adapter.SearchProductAdapter;
import managment.protege.supermed.Model.Model_PopularProducts;
import managment.protege.supermed.Model.SearchModel;
import managment.protege.supermed.R;

import managment.protege.supermed.Response.SearchResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import retrofit2.Call;
import retrofit2.Callback;

import static managment.protege.supermed.Activity.Main_Apps.nobatch_products;
import static managment.protege.supermed.Fragment.Home.ProductDetailCartCounter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchProductFragment extends Fragment {

    RecyclerView recyclerView;
    View view;
    ImageView carts;
    LinearLayout layout1, layout2;
    Button searchbtn;
    List<SearchModel> list = new ArrayList<>();
    TextView toolbar_text;
    SearchProductAdapter pAdapter;
    EditText et_search;
    KProgressHUD hud;
    Spinner spn_category;
    ArrayList<String> arr_category;
    ArrayList<String> arr_categorySlug;
    String cateSlug;

    RecyclerView recyclerViewProdcuts;
    SearchProductAdapter searchProductAdapter;
    List<SearchModel> productsList;

    public SearchProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_product, container, false);
        initWidget();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        Main_Apps.getMainActivity().addToolbar(getContext(), " Search Product", view);
        nobatch_products = (TextView) view.findViewById(R.id.nobatch_product);
        if (ProductDetailCartCounter > 0) {
            nobatch_products.setVisibility(View.VISIBLE);
            nobatch_products.setText(String.valueOf(ProductDetailCartCounter));
        } else {
            nobatch_products.setVisibility(View.INVISIBLE);
        }
        onClickFunction();
        //onSearchClick();

        arr_category = new ArrayList<>();
        arr_categorySlug = new ArrayList<>();
        getCategories();

        recyclerViewProdcuts = view.findViewById(R.id.recycler);
        recyclerViewProdcuts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productsList = new ArrayList<>();

        return view;
    }

    void initWidget() {


        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false);
        searchbtn = (Button) view.findViewById(R.id.search);
        et_search = (EditText) view.findViewById(R.id.searchpro);
        layout1 = (LinearLayout) view.findViewById(R.id.layout1);
        layout2 = (LinearLayout) view.findViewById(R.id.layout2);
        toolbar_text = view.findViewById(R.id.toolbar_text);
        recyclerView = view.findViewById(R.id.recycler);
        carts = (ImageView) view.findViewById(R.id.carts);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                et_search.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_search, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 500);

        spn_category = view.findViewById(R.id.spinner_category);

        spn_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cateSlug = arr_categorySlug.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getProducts(et_search.getText().toString(), cateSlug);
            }
        });
    }

    void getCategories(){

        hud.show();
        String URL = Register.Base_URL + "categories";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            hud.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                arr_category.add(object.getString("name"));
                                arr_categorySlug.add(object.getString("slug"));
                            }
                            spn_category.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_category));
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
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    void onClickFunction() {
        carts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main_Apps.getMainActivity().backfunction(new Cart());

            }
        });
    }

   /* void onSearchClick() {
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    LoadCatatApi(et_search.getText().toString(), GlobalHelper.getUserProfile(getContext()).getProfile().getId());
                }
                return false;
            }
        });
    }*/

    public void getProducts(final String keywords, final String cateSlug) {

        hud.show();
        String URL = Register.Base_URL + "search-products";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            if (status){
                                hud.dismiss();
                                layout1.setVisibility(View.VISIBLE);
                                layout2.setVisibility(View.GONE);

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String productId = object.getString("productId");
                                    String productName = object.getString("productName");
                                    String productSlug = object.getString("productSlug");
                                    String cateName = object.getString("cateName");
                                    String cateSlug = object.getString("cateSlug");
                                    String subcateName = object.getString("subcateName");
                                    String subcateSlug = object.getString("subcateSlug");
                                    String qty = object.getString("qty");
                                    String price = object.getString("price");
                                    String productDescription = object.getString("productDescription");
                                    String productImage = object.getString("productImage");
                                    String productTags = object.getString("productTags");

                                    SearchModel item = new SearchModel(
                                            productId,
                                            productName,
                                            productSlug,
                                            cateName,
                                            cateSlug,
                                            subcateName,
                                            subcateSlug,
                                            qty,
                                            price,
                                            productDescription,
                                            productImage,
                                            productTags
                                    );
                                    productsList.add(item);

                                }
                                searchProductAdapter = new SearchProductAdapter(productsList, getContext());
                                recyclerViewProdcuts.setAdapter(searchProductAdapter);

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
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("keywords", keywords);
                map.put("category", cateSlug);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

}
