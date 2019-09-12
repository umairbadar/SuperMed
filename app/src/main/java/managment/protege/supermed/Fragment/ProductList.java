package managment.protege.supermed.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Adapter.ProductAdapter;
import managment.protege.supermed.Adapter.TopCatagorieAdapter;
import managment.protege.supermed.Model.GetProductsModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.GetAllProductsResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductList extends Fragment {

    RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    private List<GetProductsModel> proList = new ArrayList<>();
    private ProductAdapter pAdapter;
    KProgressHUD hud;
    RecyclerView recyclerViewPro;
    TextView tv_pl_empty;
    View view;
    TextView toolbar_text;

    public ProductList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_screen, container, false);
        initWidget();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.products), view);
        return view;
    }

    private void initWidget() {
        toolbar_text = (TextView) view.findViewById(R.id.toolbar_text);
        recyclerViewPro = view.findViewById(R.id.rv);
        tv_pl_empty = view.findViewById(R.id.tv_pl_empty);
        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false);

        Bundle b = getArguments();
        String s = b.getString("sub_id");
        LoadCatatApi(s);
    }


    private void fillProcycle(List<GetProductsModel> getProductsModels) {
        pAdapter = new ProductAdapter(getProductsModels, getContext());
        recyclerViewPro.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewPro.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPro.setAdapter(pAdapter);

    }

    public void LoadCatatApi(String id) {
        hud.show();
        API api = RetrofitAdapter.createAPI();

        Call<GetAllProductsResponse> callBackCall = api.GetAllProducts(id, GlobalHelper.getUserProfile(getContext()).getProfile().getId().toString());
        callBackCall.enqueue(new Callback<GetAllProductsResponse>() {
            @Override
            public void onResponse(Call<GetAllProductsResponse> call, final Response<GetAllProductsResponse> response) {
                if (response != null) {
                    if (response.body().getStatus()) {
                        tv_pl_empty.setVisibility(View.INVISIBLE);
                        recyclerViewPro.setVisibility(View.VISIBLE);
                        fillProcycle(response.body().getProducts());

                    } else {
                        tv_pl_empty.setVisibility(View.VISIBLE);
                        recyclerViewPro.setVisibility(View.INVISIBLE);
                        Toasty.success(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT, false).show();


                    }
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<GetAllProductsResponse> call, Throwable t) {
                Log.e("Login", "Error is " + t.getMessage());
                hud.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
