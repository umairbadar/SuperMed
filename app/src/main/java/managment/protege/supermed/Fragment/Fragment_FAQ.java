package managment.protege.supermed.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Adapter.Adapter_FAQ;
import managment.protege.supermed.R;

public class Fragment_FAQ extends Fragment {

    View view;
    private RecyclerView recyclerView;
    private Adapter_FAQ adapter;
    private List<String> list;

    public Fragment_FAQ() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_faq, container, false);


        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), "FAQ", view);

        list = new ArrayList<>();
        list.add("Order");
        list.add("Payment");
        list.add("Product");
        list.add("Delivery");
        list.add("Return");
        list.add("Technical");
        list.add("General");

        recyclerView = view.findViewById(R.id.recyclerView_faq);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        adapter = new Adapter_FAQ(list, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }


}
