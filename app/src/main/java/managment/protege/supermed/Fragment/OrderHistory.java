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

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Adapter.OrderHistoryAdapter;
import managment.protege.supermed.Adapter.cartAdapter;
import managment.protege.supermed.Model.CartModel;
import managment.protege.supermed.Model.OrderHistoryList;
import managment.protege.supermed.Model.OrderhistoryModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.CartResponse;
import managment.protege.supermed.Response.ResponseOrderHistroy;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderHistory extends Fragment {
    private List<OrderhistoryModel> movieList = new ArrayList<>();
    private OrderHistoryAdapter mAdapter;
    private RecyclerView recyclerView;
    TextView tv_orderHistory;
    TextView toolbar_text;

    public OrderHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order_history, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.order_history), v);
        initializations(v);
        LoadApi(GlobalHelper.getUserProfile(getContext()).getProfile().getId().toString());

        return v;
    }

    private void initializations(View v) {
        toolbar_text = (TextView) v.findViewById(R.id.toolbar_text);
        recyclerView = (RecyclerView) v.findViewById(R.id.orderHistoryRecycler);
        tv_orderHistory = (TextView) v.findViewById(R.id.tv_orderHistory);
    }

    public void LoadApi(String Userid) {
        Main_Apps.hud.show();
        API api = RetrofitAdapter.createAPI();
        Call<ResponseOrderHistroy> callBackCall = api.list_of_order(Userid);
        callBackCall.enqueue(new Callback<ResponseOrderHistroy>() {
            @Override
            public void onResponse(Call<ResponseOrderHistroy> call, final Response<ResponseOrderHistroy> response) {
                Main_Apps.hud.dismiss();

                if (response != null) {
                    if (response.body().getStatus()) {
                        recyclerView.setVisibility(View.VISIBLE);
                        tv_orderHistory.setVisibility(View.INVISIBLE);
                        fillProcycle(response.body().getOrders());
                    } else {
                        recyclerView.setVisibility(View.INVISIBLE);
                        tv_orderHistory.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseOrderHistroy> call, Throwable t) {
                Log.e("Login", "Error is " + t.getMessage());
                Main_Apps.hud.dismiss();

            }
        });

    }

    private void fillProcycle(List<OrderhistoryModel> Cart) {
        movieList = Cart;
        mAdapter = new OrderHistoryAdapter(movieList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareMovieData();
    }

    private void prepareMovieData() {
        mAdapter.notifyDataSetChanged();
    }

}
