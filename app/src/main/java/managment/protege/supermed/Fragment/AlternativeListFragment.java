package managment.protege.supermed.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Adapter.AlternativeListAdapter;
import managment.protege.supermed.Adapter.ProductAdapter;
import managment.protege.supermed.Adapter.SearchProductAdapter;
import managment.protege.supermed.Adapter.TopCatagorieAdapter;
import managment.protege.supermed.Model.AlternativeModel;
import managment.protege.supermed.Model.CartActionResponse;
import managment.protege.supermed.Model.Product;
import managment.protege.supermed.Model.Search;
import managment.protege.supermed.Model.SearchModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.AulterSearchResponse;
import managment.protege.supermed.Response.SearchResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static managment.protege.supermed.Activity.Main_Apps.cart_toolbarapps;
import static managment.protege.supermed.Activity.Main_Apps.getMainActivity;
import static managment.protege.supermed.Activity.Main_Apps.nobatch_products;
import static managment.protege.supermed.Fragment.Home.ProductDetailCartCounter;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlternativeListFragment extends Fragment {

    RecyclerView recyclerView;
    View view;
    private List<Search> list = new ArrayList<>();
    TextView toolbar_text;
    LinearLayout LL_am_tv, LL_am_rc;
    private AlternativeListAdapter pAdapter;
    Button searchbtn;
    EditText et_search;
    TextView nobatch_product;

    public AlternativeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_alternative_list, container, false);
        initWidget();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.alternative_list), view);
        pAdapter = new AlternativeListAdapter(list, getContext());

        onSearchClick();
        return view;
    }

    private void fillProcycle(List<Search> getProductsModels) {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        list.clear();
        list = getProductsModels;
        pAdapter = new AlternativeListAdapter(getProductsModels, getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pAdapter);
        pAdapter.notifyDataSetChanged();

    }


    private void initWidget() {
        toolbar_text = view.findViewById(R.id.toolbar_text);
        recyclerView = view.findViewById(R.id.recycler);
        searchbtn = (Button) view.findViewById(R.id.search);
        LL_am_tv = (LinearLayout) view.findViewById(R.id.LL_am_tv);
        LL_am_rc = (LinearLayout) view.findViewById(R.id.LL_am_rc);
        et_search = (EditText) view.findViewById(R.id.searchpro);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_search.getText().equals("")) {
                    LoadCatatApi(et_search.getText().toString(), GlobalHelper.getUserProfile(getContext()).getProfile().getId());
                } else {
                    Toasty.success(getContext(), "Plz Insert data ", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    public void LoadCatatApi(String search, String id) {
        Main_Apps.hud.show();
        API api = RetrofitAdapter.createAPI();
        Call<AulterSearchResponse> callBackCall = api.searchAlternateMed(search, id);
        callBackCall.enqueue(new Callback<AulterSearchResponse>() {
            @Override
            public void onResponse(Call<AulterSearchResponse> call, final Response<AulterSearchResponse> response) {
                if (response != null) {
                    if (response.body().getStatus()) {
                        et_search.getText().clear();
                        LL_am_rc.setVisibility(View.VISIBLE);
                        LL_am_tv.setVisibility(View.INVISIBLE);
                        fillProcycle(response.body().getSearch());

                    } else {
                        LL_am_rc.setVisibility(View.INVISIBLE);
                        LL_am_tv.setVisibility(View.VISIBLE);
                    }
                    Main_Apps.hud.dismiss();
                }
            }

            @Override
            public void onFailure(Call<AulterSearchResponse> call, Throwable t) {
                Log.e("Login", "Error is " + t.getMessage());
                Main_Apps.hud.dismiss();
            }
        });

    }

    private void onSearchClick() {
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (!et_search.getText().equals("")) {
                    LoadCatatApi(et_search.getText().toString(), GlobalHelper.getUserProfile(getContext()).getProfile().getId());
                } else {
                    Toasty.success(getContext(), "Please Insert data ", Toast.LENGTH_SHORT, true).show();
                }
                return false;
            }
        });
    }

    public static void cartActionSearch(String pid, String uid, String qty, String action, final Context context, final Button button) {
        Main_Apps.hud.show();
        API api = RetrofitAdapter.createAPI();
        Call<CartActionResponse> callBackCall = api.cart_action(pid, uid, qty, action);
        callBackCall.enqueue(new Callback<CartActionResponse>() {
            @Override
            public void onResponse(Call<CartActionResponse> call, final Response<CartActionResponse> response) {
                if (response != null) {
                    if (response.body().getStatus()) {
                        ProductDetailCartCounter = ProductDetailCartCounter + 1;
                        nobatch_products.setVisibility(View.VISIBLE);
                        nobatch_products.setText(String.valueOf(ProductDetailCartCounter));


                        Toasty.success(context, response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                    } else {
                        Toasty.success(context, response.body().getMessage(), Toast.LENGTH_SHORT, false).show();
                    }
                    Main_Apps.hud.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CartActionResponse> call, Throwable t) {
                Log.e("cart", "Error is " + t.getMessage());
                Main_Apps.hud.dismiss();
            }
        });

    }

}
