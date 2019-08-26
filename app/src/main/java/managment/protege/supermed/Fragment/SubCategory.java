package managment.protege.supermed.Fragment;


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
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Adapter.CategoryAdapter;
import managment.protege.supermed.Adapter.SubCategoryAdapter;
import managment.protege.supermed.Model.CategoryModel;
import managment.protege.supermed.Model.Subcategory;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.CategoryResponse;
import managment.protege.supermed.Response.subcategoriesResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubCategory extends Fragment {
    private List<CategoryModel> cataList = new ArrayList<>();
    private SubCategoryAdapter cAdapter;
    View view;
    KProgressHUD hud;

    RecyclerView recyclerViewCata;
    TextView tv_sc_empty;
    TextView toolbar_text;

    public SubCategory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.sub_categories), view);

        initWidget();
        Bundle b = getArguments();
        String s = b.getString("id");
        LoadCatatApi(s);
        // Inflate the layout for this fragment
        return view;
    }

    private void initWidget() {
        toolbar_text = (TextView) view.findViewById(R.id.toolbar_text);
        recyclerViewCata = view.findViewById(R.id.recycler);
        tv_sc_empty = view.findViewById(R.id.tv_sc_empty);
        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false);
    }

    private void fillCatacycle(List<Subcategory> cm) {
        cAdapter = new SubCategoryAdapter(cm);
        recyclerViewCata.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewCata.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCata.setAdapter(cAdapter);
        cAdapter.notifyDataSetChanged();

    }

    public void LoadCatatApi(String id) {
        hud.show();
        API api = RetrofitAdapter.createAPI();
        Call<subcategoriesResponse> callBackCall = api.getSubCategoriesById(id);
        callBackCall.enqueue(new Callback<subcategoriesResponse>() {
            @Override
            public void onResponse(Call<subcategoriesResponse> call, final Response<subcategoriesResponse> response) {
                if (response != null) {
                    if (response.body().getStatus()) {
                        tv_sc_empty.setVisibility(View.INVISIBLE);
                        recyclerViewCata.setVisibility(View.VISIBLE);
                        fillCatacycle(response.body().getSubcategories());

                    } else {
                        tv_sc_empty.setVisibility(View.VISIBLE);
                        recyclerViewCata.setVisibility(View.INVISIBLE);

                    }
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<subcategoriesResponse> call, Throwable t) {
                Log.e("Login", "Error is " + t.getMessage());
                hud.dismiss();
            }
        });

    }


}
