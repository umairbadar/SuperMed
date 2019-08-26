package managment.protege.supermed.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Adapter.AlternativeListAdapter;
import managment.protege.supermed.Adapter.ProductAdapter;
import managment.protege.supermed.Adapter.SearchProductAdapter;
import managment.protege.supermed.Model.AlternativeModel;
import managment.protege.supermed.Model.GetProductsModel;
import managment.protege.supermed.Model.SearchModel;
import managment.protege.supermed.Model.SearchProductModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.CartResponse;
import managment.protege.supermed.Response.GetAllProductsResponse;
import managment.protege.supermed.Response.SearchResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static managment.protege.supermed.Activity.Main_Apps.getMainActivity;
import static managment.protege.supermed.Activity.Main_Apps.nobatch_products;
import static managment.protege.supermed.Fragment.Home.ProductDetailCartCounter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchProductFragment extends Fragment {

    RecyclerView recyclerView;
    View view;
    ImageView carts;
    LinearLayout LL_sp_rc, LL_sp_tv;
    Button searchbtn;
    private List<SearchModel> list = new ArrayList<>();
    TextView toolbar_text;
    private SearchProductAdapter pAdapter;
    EditText et_search;
    KProgressHUD hud;

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
        onSearchClick();

       /* et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_search.getText().toString().equals("")){
                    list.clear();
                    LL_sp_rc.setVisibility(View.INVISIBLE);
                } else {
                    LoadCatatApi(et_search.getText().toString(), GlobalHelper.getUserProfile(getContext()).getProfile().getId());
                }
            }
        });*/

        return view;
    }

    private void fillProcycle(List<SearchModel> getProductsModels) {
        list.clear();
        list = getProductsModels;
        pAdapter = new SearchProductAdapter(getProductsModels, getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pAdapter);

    }


    private void initWidget() {


        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false);
        searchbtn = (Button) view.findViewById(R.id.search);
        et_search = (EditText) view.findViewById(R.id.searchpro);
        LL_sp_rc = (LinearLayout) view.findViewById(R.id.LL_sp_rc);
        LL_sp_tv = (LinearLayout) view.findViewById(R.id.LL_sp_tv);
        toolbar_text = view.findViewById(R.id.toolbar_text);
        recyclerView = view.findViewById(R.id.recycler);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadCatatApi(et_search.getText().toString(), GlobalHelper.getUserProfile(getContext()).getProfile().getId());
            }
        });
        carts = (ImageView) view.findViewById(R.id.carts);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                et_search.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_search, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 500);
    }

    private void onClickFunction() {
        carts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main_Apps.getMainActivity().backfunction(new Cart());

            }
        });
    }

    private void onSearchClick() {
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    LoadCatatApi(et_search.getText().toString(), GlobalHelper.getUserProfile(getContext()).getProfile().getId());
                }
                return false;
            }
        });
    }

    public void LoadCatatApi(String search, String id) {
        hud.show();
        API api = RetrofitAdapter.createAPI();
        Call<SearchResponse> callBackCall = api.search(search, id);
        callBackCall.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, final Response<SearchResponse> response) {
                if (response != null) {
                    if (response.body().getStatus()) {
                        LL_sp_rc.setVisibility(View.VISIBLE);
                        LL_sp_tv.setVisibility(View.INVISIBLE);
                        fillProcycle(response.body().getSearch());

                    } else {
                        LL_sp_rc.setVisibility(View.INVISIBLE);
                        LL_sp_tv.setVisibility(View.VISIBLE);


                    }
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.e("Login", "Error is " + t.getMessage());
                hud.dismiss();
            }
        });
    }

}
