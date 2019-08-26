package managment.protege.supermed.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Fragment.AlternativeListFragment;
import managment.protege.supermed.Fragment.ProductDetail;
import managment.protege.supermed.Model.AlternativeModel;
import managment.protege.supermed.Model.GetProductsModel;
import managment.protege.supermed.Model.Product;
import managment.protege.supermed.Model.Search;
import managment.protege.supermed.R;
import managment.protege.supermed.Tools.GlobalHelper;

/**
 * Created by wahaj on 6/12/2018.
 */

public class AlternativeListAdapter extends RecyclerView.Adapter<AlternativeListAdapter.MyViewHolder> {

    public AlternativeListAdapter(List<Search> Prolist, Context context) {
        this.Prolist = Prolist;
        this.context = context;
    }

    private List<Search> Prolist;
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alternative_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Search Pro = Prolist.get(position);
        holder.title.setText(Pro.getProductName());
        holder.price.setText("Rs" + Pro.getProductPrice());
        holder.priceoff.setText("Rs" + Pro.getProductOldPrice());
        Picasso.with(context).load(Pro.getProductImage().replaceAll(" ", "%20"))
                .placeholder(R.drawable.tab_miss)
                .into(holder.iv);
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlternativeListFragment.cartActionSearch(Pro.getProductID(), GlobalHelper.getUserProfile(context).getProfile().getId(), "1", "0", context, holder.detail);

            }
        });
        holder.LL_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                Search obj = Prolist.get(position);
                args.putSerializable("your_obj_3", obj);


                Main_Apps.getMainActivity().backfunctions(new ProductDetail(), args);

            }
        });
    }

    @Override
    public int getItemCount() {
        return Prolist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, genre, price, priceoff;
        public Button detail;
        CardView cardView;
        LinearLayout LL_search;
        ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.click);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);
            priceoff = (TextView) view.findViewById(R.id.priceoff);
            detail = (Button) view.findViewById(R.id.alt_detail);
            iv = (ImageView) view.findViewById(R.id.iv);
            LL_search = (LinearLayout) view.findViewById(R.id.LL_search);

        }
    }
}
